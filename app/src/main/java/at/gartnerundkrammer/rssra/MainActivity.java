package at.gartnerundkrammer.rssra;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.diamonddogs.net.SSLHelper;
import at.gartnerundkrammer.rssra.fragments.PostingsListFragment;
import at.gartnerundkrammer.rssra.fragments.RSSListFragment;
import at.gartnerundkrammer.rssra.fragments.SubscribeToRSSFragment;

import at.gartnerundkrammer.rssra.models.RssFeed;
import at.gartnerundkrammer.rssra.models.RssFeedItem;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.RssFeedContentProvider;
import greendao.RssFeedDao;
import greendao.RssFeedItemContentProvider;
import greendao.RssFeedItemDao;


public class MainActivity extends Activity implements PostingsListFragment.OnPostingsListFragmentInteractionListener, RSSListFragment.OnRSSListFragmentInteractionListener, SubscribeToRSSFragment.OnSubscribeToRSSFragmentInteractionListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(RssProcessor.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SSLHelper.getInstance().initSSLFactoryJava(null, -1, null);

        FragmentUtility.changeFragment(this, new RSSListFragment());

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "rssra", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        RssFeedContentProvider.setDaoSession(daoSession);
        RssFeedItemContentProvider.setDaoSession(daoSession);
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
            case android.R.id.home:
                FragmentUtility.changeToLastFragment(this);
                return true;
            case R.id.action_settings:
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
    public void onFeedReaded(ArrayList<Long> ids, Long feedId) {
        for(Long id : ids) {
            ContentValues valuesFeedItem = new ContentValues();
            valuesFeedItem.put(RssFeedItemDao.Properties.State.columnName, "read");

            getApplicationContext().getContentResolver().update(RssFeedItemContentProvider.CONTENT_URI,valuesFeedItem, RssFeedItemDao.Properties.Id.columnName + "=?",new String[]{Long.toString(id)});
        }

        FragmentUtility.changeFragment(this, new PostingsListFragment());

    }


    @Override
    public void onFeedUnreaded(ArrayList<Long> ids, Long feedId) {
        for(Long id : ids) {
            ContentValues valuesFeedItem = new ContentValues();
            valuesFeedItem.put(RssFeedItemDao.Properties.State.columnName, "unread");

            getApplicationContext().getContentResolver().update(RssFeedItemContentProvider.CONTENT_URI,valuesFeedItem, RssFeedItemDao.Properties.Id.columnName + "=?",new String[]{Long.toString(id)});
        }

        FragmentUtility.changeFragment(this, new PostingsListFragment());

    }


    @Override
    public void onFeedStarred(ArrayList<Long> ids, Long feedId) {
        for(Long id : ids) {
            ContentValues valuesFeedItem = new ContentValues();
            valuesFeedItem.put(RssFeedItemDao.Properties.State.columnName, "starred");

            getApplicationContext().getContentResolver().update(RssFeedItemContentProvider.CONTENT_URI,valuesFeedItem, RssFeedItemDao.Properties.Id.columnName + "=?",new String[]{Long.toString(id)});
        }

        FragmentUtility.changeFragment(this, new PostingsListFragment());

    }


    @Override
    public void onRSSListFragmentInteraction(String id) {

    }

    @Override
    public void onFeedsDeleted(ArrayList<Long> feedsToDelete) {
        for(Long id : feedsToDelete) {
            getApplicationContext().getContentResolver().delete(RssFeedItemContentProvider.CONTENT_URI, RssFeedItemDao.Properties.FeedId.columnName + "=?",new String[]{Long.toString(id)});
            getApplicationContext().getContentResolver().delete(RssFeedContentProvider.CONTENT_URI, RssFeedDao.Properties.Id.columnName + "=?",new String[]{Long.toString(id)});
        }

        FragmentUtility.changeFragment(this, new RSSListFragment());

    }

    @Override
    public void onSubscribeToRSSFragmentInteraction(Uri uri) {

    }

}
