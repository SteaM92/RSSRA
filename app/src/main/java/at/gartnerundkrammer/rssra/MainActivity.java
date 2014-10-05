package at.gartnerundkrammer.rssra;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements PostingsListFragment.OnPostingsListFragmentInteractionListener, RSSListFragment.OnRSSListFragmentInteractionListener, SubscribeToRSSFragment.OnSubscribeToRSSFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new PostingsListFragment());
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_place, fragment);
        transaction.addToBackStack(((Object) fragment).getClass().getName());
        transaction.commit();
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
                changeFragment(new RSSListFragment());
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
