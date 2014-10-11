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
import at.diamonddogs.net.SSLHelper;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ServiceProcessorMessageUtil;


public class MainActivity extends Activity implements PostingsListFragment.OnPostingsListFragmentInteractionListener, RSSListFragment.OnRSSListFragmentInteractionListener, SubscribeToRSSFragment.OnSubscribeToRSSFragmentInteractionListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(RssProcessor.class.getSimpleName());

    private List<RssFeed> feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feeds = new ArrayList<RssFeed>();

        SSLHelper.getInstance().initSSLFactoryJava(null, -1, null);

        initTestFeed();

        FragmentUtility.changeToListFragment(this, new RSSListFragment(), feeds);
    }

    private void initTestFeed() {
        RssFeed testfeed = new RssFeed();
        testfeed.setSource("http://heise.de.feedsportal.com/c/35207/f/653902/index.rss");
        testfeed.setTitle("Heise");

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
    }

    @Override
    public void onPause()
    {
        super.onPause();
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
                FragmentUtility.changeToListFragment(this, new SubscribeToRSSFragment(), feeds);
                return true;

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

}
