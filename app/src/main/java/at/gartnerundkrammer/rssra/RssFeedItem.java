package at.gartnerundkrammer.rssra;

import java.util.Date;
import java.util.UUID;

/**
 * Data class for a item in a feed
 */
public class RssFeedItem {

    private String title, description, link, author;
    private Date pubDate;

    public RssFeedItem()
    {}

    public RssFeedItem(String title, String description, String link, String author, Date pubDate, String guid) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
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

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString()
    {
        return String.format("%s", title);
    }
}
