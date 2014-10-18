package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RSS_FEED_ITEM.
 */
public class RssFeedItem {

    private Long id;
    private String title;
    private String description;
    private String link;
    private String author;
    private java.util.Date pubDate;

    public RssFeedItem() {
    }

    public RssFeedItem(Long id) {
        this.id = id;
    }

    public RssFeedItem(Long id, String title, String description, String link, String author, java.util.Date pubDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
