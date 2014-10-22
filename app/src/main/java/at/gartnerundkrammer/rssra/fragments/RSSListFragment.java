package at.gartnerundkrammer.rssra.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ServiceProcessorMessageUtil;
import at.gartnerundkrammer.rssra.FragmentUtility;
import at.gartnerundkrammer.rssra.R;
import at.gartnerundkrammer.rssra.RssProcessor;
import greendao.RssFeedContentProvider;
import greendao.RssFeedDao;

/**
 * RSSListFragment shows a list of your current RSS Feeds
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class RSSListFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSSListFragment.class.getSimpleName());

    private HttpServiceAssister assister;

    private Cursor cursor;

    private OnRSSListFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CursorAdapter mAdapter;

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
    }

    @Override
    public void onResume()
    {
        assister.bindService();

        bindData();

        super.onResume();
    }

    @Override
    public void onPause()
    {
        if (cursor != null)
            cursor.close();

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

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(this);
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
                FragmentUtility.changeFragment(getActivity(), new SubscribeToRSSFragment());
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
        long feedId = ((Cursor)mAdapter.getItem(position)).getLong(0);
        fragment.setFeed(feedId);
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
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
        actionMode.setTitle(mListView.getCheckedItemCount() + " selected");
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.contextual_rsslist_longclick, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
        ArrayList<Long> feedsToDelete = new ArrayList<Long>();
        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                mAdapter.getCursor().moveToPosition(checkedItems.keyAt(i));
                Long idFeed = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedDao.Properties.Id.columnName));

                feedsToDelete.add(idFeed);
            }
        }

        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_LONG).show();

        //callback in activity
        mListener.onFeedsDeleted(feedsToDelete);
        actionMode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

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

        void onFeedsDeleted(ArrayList<Long> feedsToDelete);

    }

    private void bindData()
    {
        Uri uri = RssFeedContentProvider.CONTENT_URI;
        cursor = getActivity().getContentResolver().query(uri,
                new String[]{"_id", "title"}, null, null, null);
        if (mAdapter == null) {
            mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,
                    cursor, new String[]{"TITLE"}, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mListView.setAdapter(mAdapter);
        }
        else {
            mAdapter.changeCursor(cursor);
        }
    }

    public void startSync()
    {
        //for(greendao.RssFeed feed : list) {
        for (int i=0; i<mAdapter.getCount(); i++) {
            long id = ((Cursor)mAdapter.getItem(i)).getLong(0);
            Cursor c = getActivity().getContentResolver().query(RssFeedContentProvider.CONTENT_URI,
                    new String[]{"source"}, "_id = ?", new String[]{Long.toString(id)}, null);
            c.moveToFirst();
            String url = c.getString(0);
            c.close();

            WebRequest asyncRequest = new WebRequest();
            asyncRequest.setUrl(url);
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
                    greendao.RssFeed feed = (greendao.RssFeed)message.obj;
                    feed.setSource(ServiceProcessorMessageUtil.getWebRequest(message).getUrl().toString());
                    feed.update();
                    feed.resetItems();

                    getActivity().getContentResolver().delete(RssFeedContentProvider.CONTENT_URI,
                            "source = ? and _id != ?", new String[]{feed.getSource(), Long.toString(feed.getId())});
                    bindData();

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
