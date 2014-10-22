package at.gartnerundkrammer.rssra.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

import at.gartnerundkrammer.rssra.FragmentUtility;
import at.gartnerundkrammer.rssra.R;
import greendao.RssFeed;
import greendao.RssFeedContentProvider;
import greendao.RssFeedDao;
import greendao.RssFeedItemContentProvider;
import greendao.RssFeedItemDao;

/**
 * PostingsListFragment shows all postings from one RSS Feed
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * interface.
 */
public class PostingsListFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    // TODO: Rename parameter arguments, choose names that match

    private long feedId;
    private Cursor cursor;

    private OnPostingsListFragmentInteractionListener mListener;

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
        try {
            mListener = (OnPostingsListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPostingsListFragmentInteractionListener");
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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        bindData();
    }

    @Override
    public void onPause()
    {
        cursor.close();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postingslist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(this);

        setEmptyText(getString(R.string.emptyFeed));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onPostingsListFragmentInteraction(list.get(position).getLink());
        }

        String link = ((Cursor)mAdapter.getItem(position)).getString(
                mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.Link.columnName));

        if (link != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        }
    }

    private void bindData()
    {
        cursor = getActivity().getContentResolver().query(RssFeedItemContentProvider.CONTENT_URI,
            new String[]{RssFeedItemDao.Properties.Id.columnName, RssFeedItemDao.Properties.Title.columnName,
                    RssFeedItemDao.Properties.FeedId.columnName, RssFeedItemDao.Properties.State.columnName,
                    RssFeedItemDao.Properties.Link.columnName},
            RssFeedItemDao.Properties.FeedId.columnName + "=?", new String[]{Long.toString(feedId)}, null);



        if (mAdapter == null) {
            mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.postinglist_item_row, cursor,
                    new String[]{RssFeedItemDao.Properties.Title.columnName, RssFeedItemDao.Properties.State.columnName},
                    new int[]{R.id.feeditem_title, R.id.feeditem_state},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mListView.setAdapter(mAdapter);
        }
        else {
            mAdapter.changeCursor(cursor);
        }
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

    public void setFeed(long feedId)
    {
        this.feedId = feedId;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
        actionMode.setTitle(mListView.getCheckedItemCount() + " selected");
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.contextual_postingslist_longclick, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        Long feedId = null;

        switch (menuItem.getItemId()) {
            case R.id.menu_read:

                ArrayList<Long> feedsToRead = new ArrayList<Long>();
                SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();

                for (int i = 0; i < checkedItems.size(); i++) {
                    if (checkedItems.valueAt(i)) {
                        mAdapter.getCursor().moveToPosition(checkedItems.keyAt(i));
                        Long feedID = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.Id.columnName));

                        if(feedId == null)
                            feedId = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.FeedId.columnName));

                        feedsToRead.add(feedID);
                    }
                }
                mListener.onFeedReaded(feedsToRead, feedId);

                Toast.makeText(getActivity(), "Read", Toast.LENGTH_SHORT).show();

                actionMode.finish();
                break;
            case R.id.menu_unread:


                SparseBooleanArray checkedItemsUnRead = mListView.getCheckedItemPositions();
                ArrayList<Long> feedsToUnReaded = new ArrayList<Long>();
                for (int i = 0; i < checkedItemsUnRead.size(); i++) {
                    if (checkedItemsUnRead.valueAt(i)) {
                        mAdapter.getCursor().moveToPosition(checkedItemsUnRead.keyAt(i));
                        Long idFeed = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.Id.columnName));

                        if(feedId == null)
                            feedId = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.FeedId.columnName));


                        feedsToUnReaded.add(idFeed);
                    }
                }
                //callback in activity
                mListener.onFeedUnreaded(feedsToUnReaded,feedId);

                Toast.makeText(getActivity(), "Unread", Toast.LENGTH_SHORT).show();

                actionMode.finish();
                break;
            case R.id.menu_star:
                Toast.makeText(getActivity(), "Starred", Toast.LENGTH_SHORT).show();

                SparseBooleanArray checkedItemsStarred = mListView.getCheckedItemPositions();
                ArrayList<Long> feedsToStarr = new ArrayList<Long>();
                for (int i = 0; i < checkedItemsStarred.size(); i++) {
                    if (checkedItemsStarred.valueAt(i)) {
                        mAdapter.getCursor().moveToPosition(checkedItemsStarred.keyAt(i));
                        Long idFeed = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.Id.columnName));

                        if(feedId == null)
                            feedId = mAdapter.getCursor().getLong(mAdapter.getCursor().getColumnIndex(RssFeedItemDao.Properties.FeedId.columnName));


                        feedsToStarr.add(idFeed);
                    }
                }
                //callback in activity
                mListener.onFeedStarred(feedsToStarr,feedId);

                actionMode.finish();
                break;

        }
        FragmentUtility.changeToLastFragment(getActivity());
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
    public interface OnPostingsListFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPostingsListFragmentInteraction(String id);

        void onFeedReaded(ArrayList<Long> feedsToRead, Long feedId);

        void onFeedUnreaded(ArrayList<Long> feedsToUnReaded, Long feedId);

        void onFeedStarred(ArrayList<Long> feedsToStarr, Long feedId);
    }

}
