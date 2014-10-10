package at.gartnerundkrammer.rssra;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ServiceProcessorMessageUtil;


public class MainActivity extends Activity implements PostingsListFragment.OnPostingsListFragmentInteractionListener, RSSListFragment.OnRSSListFragmentInteractionListener, SubscribeToRSSFragment.OnSubscribeToRSSFragmentInteractionListener{

    private HttpServiceAssister assister;
    private List<RssFeed> feeds;
    private static final Logger LOGGER = LoggerFactory.getLogger(RssProcessor.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feeds = new ArrayList<RssFeed>();

        assister = new HttpServiceAssister(this);

        initTestFeed();

        changeToListFragment(new RSSListFragment(), feeds);
    }

    private void initTestFeed() {
        RssFeed testfeed = new RssFeed();
        testfeed.setSource("http://heise.de.feedsportal.com/c/35207/f/653902/index.rss");

        RssFeedItem item = new RssFeedItem("title", "des", "asd", "title", new Date(), "asd");
        RssFeedItem item2 = new RssFeedItem("title2", "des2", "asd2", "title2", new Date(), "asd2");
        List<RssFeedItem> list = new ArrayList<>();
        list.add(item);
        list.add(item2);
        testfeed.setItems(list);
        feeds.add(testfeed);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        assister.bindService();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        assister.unbindService();
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_place, fragment);
        transaction.addToBackStack(((Object) fragment).getClass().getName());
        transaction.commit();
    }

    public void changeToListFragment(ListFragementInterface fr, List list) {
        fr.setListData(list);
        changeFragment(fr.getFragment());
    }

    public void changeToLastFragment() {
        getFragmentManager().popBackStack();

    }

    public void startSync()
    {
        for(RssFeed feed : feeds) {
            WebRequest asyncRequest = new WebRequest();
            asyncRequest.setUrl(feed.getSource());
            asyncRequest.setProcessorId(RssProcessor.ID);
            assister.runWebRequest(new RssHandler(), asyncRequest, new RssProcessor());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;

            case R.id.action_addRSS:
                SubscribeToRSSFragment fragment = new SubscribeToRSSFragment();
                changeFragment(fragment);
                return true;

            case R.id.action_sync:
                startSync();

            case R.id.action_about:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostingsListFragmentInteraction(String id) {

    }

    @Override
    public void onRSSListFragmentInteraction(String id) {

    }

    @Override
    public void onSubscribeToRSSFragmentInteraction(Uri uri) {

    }

    public void addRSSToList(String source) {
        RssFeed f = new RssFeed();
        f.setSource(source);
        Log.v("v", "source added:" + source);
        feeds.add(f);
    }

    private class RssHandler extends Handler
    {
        @Override
        public void handleMessage(Message message)
        {
            super.handleMessage(message);
            if (ServiceProcessorMessageUtil.isFromProcessor(message, RssProcessor.ID))
            {
                if (ServiceProcessorMessageUtil.isSuccessful(message))
                {
                    LOGGER.trace("feed fetched.");
                    int insertIndex = -1;
                    RssFeed feed = (RssFeed)message.obj;
                    for (int i=0; i<feeds.size(); i++)
                    {
                        if (feeds.get(i).getSource() == feed.getSource())
                        {
                            insertIndex = i;
                            feeds.remove(i);
                            break;
                        }
                    }

                    if (insertIndex >= 0)
                        feeds.add(insertIndex, feed);
                    else
                        feeds.add(feed);
                }
                else
                {
                    String host = ServiceProcessorMessageUtil.getWebRequest(message).getUrl().getHost();
                    Toast.makeText(MainActivity.this, String.format("Failed to fetch feed from %s", host), Toast.LENGTH_LONG).show();
                    LOGGER.error(String.format("android.http HTTP Request to %s failed", host));
                }
            }
        }
    }
}
