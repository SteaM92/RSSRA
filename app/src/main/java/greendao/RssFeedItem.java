package greendao;

import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RSS_FEED_ITEM.
 */
public class RssFeedItem {

    private Long id;
    private Long feedId;
    private String title;
    private String description;
    private String link;
    private String author;
    private java.util.Date pubDate;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RssFeedItemDao myDao;

    private RssFeed feed;
    private Long feed__resolvedKey;


    public RssFeedItem() {
    }

    public RssFeedItem(Long id) {
        this.id = id;
    }

    public RssFeedItem(Long id, Long feedId, String title, String description, String link, String author, java.util.Date pubDate) {
        this.id = id;
        this.feedId = feedId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRssFeedItemDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public java.util.Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(java.util.Date pubDate) {
        this.pubDate = pubDate;
    }

    /** To-one relationship, resolved on first access. */
    public RssFeed getFeed() {
        Long __key = this.feedId;
        if (feed__resolvedKey == null || !feed__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RssFeedDao targetDao = daoSession.getRssFeedDao();
            RssFeed feedNew = targetDao.load(__key);
            synchronized (this) {
                feed = feedNew;
            	feed__resolvedKey = __key;
            }
        }
        return feed;
    }

    public void setFeed(RssFeed feed) {
        synchronized (this) {
            this.feed = feed;
            feedId = feed == null ? null : feed.getId();
            feed__resolvedKey = feedId;
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s", title);
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
