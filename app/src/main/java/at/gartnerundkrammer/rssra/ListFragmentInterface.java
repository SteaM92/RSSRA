package at.gartnerundkrammer.rssra;

import android.app.Fragment;

import java.util.List;

/**
 * Created by Markus on 10.10.2014.
 */
public interface ListFragmentInterface {

    public void setListData(List list);
    public Fragment getFragment();
}
