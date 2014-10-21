package at.gartnerundkrammer.rssra.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ServiceProcessorMessageUtil;
import at.gartnerundkrammer.rssra.FragmentUtility;
import at.gartnerundkrammer.rssra.ListFragmentInterface;
import at.gartnerundkrammer.rssra.R;
import at.gartnerundkrammer.rssra.models.RssFeed;
import at.gartnerundkrammer.rssra.RssProcessor;
import greendao.RssFeedItem;

/**
 * RSSListFragment shows a list of your current RSS Feeds
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class RSSListFragment extends Fragment implements AbsListView.OnItemClickListener, ListFragmentInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSSListFragment.class.getSimpleName());

    private List<greendao.RssFeed> list = null;
    private HttpServiceAssister assister;

    private OnRSSListFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<greendao.RssFeed> mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        assister = new HttpServiceAssister(activity);

        try {
            mListener = (OnRSSListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRSSListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (list == null)
            throw new IllegalStateException("setList() was not called");
        
        mAdapter = new ArrayAdapter<greendao.RssFeed>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, list);
    }

    @Override
    public void onResume()
    {
        assister.bindService();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        assister.unbindService();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rsslist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.rsslist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_addRSS:
                FragmentUtility.changeToListFragment(getActivity(), new SubscribeToRSSFragment(), list);
                return true;
            case R.id.action_sync:
                startSync();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
           // mListener.onRSSListFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }

        //Log.v("ja", "pos:"+position+", id"+id);
        PostingsListFragment fragment = new PostingsListFragment();
        fragment.setListData(list.get(position).getItems());
        FragmentUtility.changeFragment(getActivity(), fragment);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void setListData(List list) {
        if (list == null)
            throw new IllegalArgumentException("list must not be null");

        this.list = list;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnRSSListFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onRSSListFragmentInteraction(String id);
    }


    public void startSync()
    {
        for(greendao.RssFeed feed : list) {
            WebRequest asyncRequest = new WebRequest();
            asyncRequest.setUrl(feed.getSource());
            asyncRequest.setProcessorId(RssProcessor.ID);
            assister.runWebRequest(new RssHandler(), asyncRequest, new RssProcessor(getActivity()));
        }
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
                    greendao.RssFeed feed = (greendao.RssFeed)message.obj;
                    feed.setSource(ServiceProcessorMessageUtil.getWebRequest(message).getUrl().toString());
                    feed.update();
                    feed.resetItems();
                    for (int i=0; i<list.size(); i++)
                    {
                        if (list.get(i).getSource().equals(feed.getSource()))
                        {
                            insertIndex = i;
                            greendao.RssFeed f = list.get(i);
                            list.remove(i);
                            for (RssFeedItem item : f.getItems())
                                item.delete();
                            f.delete();
                            break;
                        }
                    }

                    if (insertIndex >= 0)
                        list.add(insertIndex, feed);
                    else
                        list.add(feed);

                    mAdapter.notifyDataSetChanged();

                    String host = ServiceProcessorMessageUtil.getWebRequest(message).getUrl().getHost();
                    Toast.makeText(getActivity(), String.format("Feed %s updated", host), Toast.LENGTH_LONG).show();
                    LOGGER.info(String.format("Feed %s updated", host));
                }
                else
                {
                    String host = ServiceProcessorMessageUtil.getWebRequest(message).getUrl().getHost();
                    Toast.makeText(getActivity(), String.format("Failed to fetch feed from %s", host), Toast.LENGTH_LONG).show();
                    LOGGER.error(String.format("android.http HTTP Request to %s failed", host));
                }
            }
        }
    }
}
