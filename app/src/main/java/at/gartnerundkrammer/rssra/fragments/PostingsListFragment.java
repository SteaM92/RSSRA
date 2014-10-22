package at.gartnerundkrammer.rssra.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import at.gartnerundkrammer.rssra.R;
import greendao.RssFeed;
import greendao.RssFeedItemContentProvider;

/**
 * PostingsListFragment shows all postings from one RSS Feed
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * interface.
 */
public class PostingsListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match

    private long feedId;

    private OnPostingsListFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

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

        Cursor c = getActivity().getContentResolver().query(RssFeedItemContentProvider.CONTENT_URI,
                new String[]{"_id", "title"}, "feed_id = ?", new String[]{Long.toString(feedId)}, null);
        mAdapter = new SimpleCursorAdapter(getActivity(),  android.R.layout.simple_list_item_1,
                c, new String[]{"TITLE"}, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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

        mListView.setAdapter(mAdapter);

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

        String link = ((RssFeed)mAdapter.getItem(position)).getLink();
        if (link != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
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
    }

}
