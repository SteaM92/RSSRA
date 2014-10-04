package at.gartnerundkrammer.rssra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Primary data class for RSS feeds
 */
public class RssFeed {

    private String source;
    private String title;
    private String description;
    private String link;
    private String language;
    private String copyright;

    private Date pubDate;
    private List<RssFeedItem> items;

    public RssFeed()
    {
        items = new ArrayList<RssFeedItem>();
    }

    public RssFeed(String source, String title, String description, String link, Date pubDate, List<RssFeedItem> items) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.items = items;
        if (this.items == null)
            this.items = new ArrayList<RssFeedItem>();
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void addItem(RssFeedItem item) {
        items.add(item);
    }

    public List<RssFeedItem> getItems() {
        return items;
    }

    public void setItems(List<RssFeedItem> items) {
        this.items = items;
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
}
