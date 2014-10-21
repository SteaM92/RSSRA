package greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import greendao.RssFeedItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RSS_FEED_ITEM.
*/
public class RssFeedItemDao extends AbstractDao<RssFeedItem, Long> {

    public static final String TABLENAME = "RSS_FEED_ITEM";

    /**
     * Properties of entity RssFeedItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FeedId = new Property(1, Long.class, "feedId", false, "FEED_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Description = new Property(3, String.class, "description", false, "DESCRIPTION");
        public final static Property Link = new Property(4, String.class, "link", false, "LINK");
        public final static Property Author = new Property(5, String.class, "author", false, "AUTHOR");
        public final static Property PubDate = new Property(6, java.util.Date.class, "pubDate", false, "PUB_DATE");
    };

    private DaoSession daoSession;

    private Query<RssFeedItem> rssFeed_ItemsQuery;

    public RssFeedItemDao(DaoConfig config) {
        super(config);
    }
    
    public RssFeedItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RSS_FEED_ITEM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'FEED_ID' INTEGER," + // 1: feedId
                "'TITLE' TEXT," + // 2: title
                "'DESCRIPTION' TEXT," + // 3: description
                "'LINK' TEXT," + // 4: link
                "'AUTHOR' TEXT," + // 5: author
                "'PUB_DATE' INTEGER);"); // 6: pubDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RSS_FEED_ITEM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RssFeedItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long feedId = entity.getFeedId();
        if (feedId != null) {
            stmt.bindLong(2, feedId);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(4, description);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(5, link);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(6, author);
        }
 
        java.util.Date pubDate = entity.getPubDate();
        if (pubDate != null) {
            stmt.bindLong(7, pubDate.getTime());
        }
    }

    @Override
    protected void attachEntity(RssFeedItem entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RssFeedItem readEntity(Cursor cursor, int offset) {
        RssFeedItem entity = new RssFeedItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // feedId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // description
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // link
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // author
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // pubDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RssFeedItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFeedId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLink(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAuthor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPubDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RssFeedItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RssFeedItem entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "items" to-many relationship of RssFeed. */
    public List<RssFeedItem> _queryRssFeed_Items(Long feedId) {
        synchronized (this) {
            if (rssFeed_ItemsQuery == null) {
                QueryBuilder<RssFeedItem> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.FeedId.eq(null));
                rssFeed_ItemsQuery = queryBuilder.build();
            }
        }
        Query<RssFeedItem> query = rssFeed_ItemsQuery.forCurrentThread();
        query.setParameter(0, feedId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRssFeedDao().getAllColumns());
            builder.append(" FROM RSS_FEED_ITEM T");
            builder.append(" LEFT JOIN RSS_FEED T0 ON T.'FEED_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected RssFeedItem loadCurrentDeep(Cursor cursor, boolean lock) {
        RssFeedItem entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        RssFeed feed = loadCurrentOther(daoSession.getRssFeedDao(), cursor, offset);
        entity.setFeed(feed);

        return entity;    
    }

    public RssFeedItem loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RssFeedItem> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RssFeedItem> list = new ArrayList<RssFeedItem>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<RssFeedItem> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RssFeedItem> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
