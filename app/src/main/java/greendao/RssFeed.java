package greendao;

import java.util.List;
import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RSS_FEED.
 */
public class RssFeed {

    private Long id;
    private String source;
    private String title;
    private String description;
    private String link;
    private String language;
    private String copyright;
    private java.util.Date pubDate;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RssFeedDao myDao;

    private List<RssFeedItem> items;

    public RssFeed() {
    }

    public RssFeed(Long id) {
        this.id = id;
    }

    public RssFeed(Long id, String source, String title, String description, String link, String language, String copyright, java.util.Date pubDate) {
        this.id = id;
        this.source = source;
        this.title = title;
        this.description = description;
        this.link = link;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRssFeedDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public java.util.Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(java.util.Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString()
    {
        if (title == null)
            return source;
        else
            return String.format("%s (%s)", title, source);
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<RssFeedItem> getItems() {
        if (items == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RssFeedItemDao targetDao = daoSession.getRssFeedItemDao();
            List<RssFeedItem> itemsNew = targetDao._queryRssFeed_Items(id);
            synchronized (this) {
                if(items == null) {
                    items = itemsNew;
                }
            }
        }
        return items;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetItems() {
        items = null;
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
