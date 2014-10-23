package at.gartnerundkrammer.rssra.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import at.gartnerundkrammer.rssra.FragmentUtility;
import at.gartnerundkrammer.rssra.R;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.RssFeedContentProvider;
import greendao.RssFeedDao;


/**
 * SubscribeToRSSFragment shows the GUI to subscribe to a RSS Feed
 * Activities that contain this fragment must implement the
 * {@link SubscribeToRSSFragment.OnSubscribeToRSSFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscribeToRSSFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SubscribeToRSSFragment extends Fragment implements View.OnClickListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeToRSSFragment.class.getSimpleName());

    private Button cancelButton;
    private Button addButton;
    private EditText addRSSEditText;

    private OnSubscribeToRSSFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe_to_rss, container, false);

        cancelButton = (Button) view.findViewById(R.id.SubscribeToRSS_cancelButton);
        cancelButton.setOnClickListener(this);

        addButton = (Button) view.findViewById(R.id.SubscribeToRSS_addButton);
        addButton.setOnClickListener(this);

        addRSSEditText = (EditText) view.findViewById(R.id.SubscribeToRSS_addRSSTextfield);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSubscribeToRSSFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //mainActivity = (MainActivity)activity;
        try {
            mListener = (OnSubscribeToRSSFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubscribeToRSSFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPause()
    {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().getActionBar().setHomeButtonEnabled(false);
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SubscribeToRSS_addButton:
                if (addRSSEditText.getText().length()>0) {
                    ContentValues values = new ContentValues();
                    values.put(RssFeedDao.Properties.Source.columnName, addRSSEditText.getText().toString());
                    values.put(RssFeedDao.Properties.Title.columnName, addRSSEditText.getText().toString());
                    getActivity().getContentResolver().insert(RssFeedContentProvider.CONTENT_URI, values);
                    Toast.makeText(getActivity(), "RSS added", Toast.LENGTH_SHORT).show();
                    addRSSEditText.setText("");
                    FragmentUtility.changeToLastFragment(getActivity());
                }else {
                    Toast.makeText(getActivity(), "Please fill out the field.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.SubscribeToRSS_cancelButton:
                FragmentUtility.changeToLastFragment(getActivity());
                break;
        }
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
    public interface OnSubscribeToRSSFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSubscribeToRSSFragmentInteraction(Uri uri);
    }

}
