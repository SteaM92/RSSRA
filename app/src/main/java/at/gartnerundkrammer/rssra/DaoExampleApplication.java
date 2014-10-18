package at.gartnerundkrammer.rssra;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.RssFeedDao;

/**
 * Created by surecase on 19/03/14.
 */
public class DaoExampleApplication extends Application {

    public DaoSession daoSession;

    private static DaoExampleApplication ourInstance;

    private static Activity activity;

    /*public static DaoExampleApplication getInstance(Activity a) {
        activity = a;
        if (ourInstance == null) {
            ourInstance = new DaoExampleApplication();
        }
        return ourInstance;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        // As we are in development we will use the DevOpenHelper which drops the database on a schema update
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "rssra", null);
        // Access the database using the helper
        SQLiteDatabase db = helper.getWritableDatabase();
        // Construct the DaoMaster which brokers DAOs for the Domain Objects
        DaoMaster daoMaster = new DaoMaster(db);
        // Create the session which is a container for the DAO layer and has a cache which will return handles to the same object across multiple queries
        daoSession = daoMaster.newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
