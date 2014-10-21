package at.gartnerundkrammer.rssra;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import at.diamonddogs.service.processor.XMLProcessor;
import at.gartnerundkrammer.rssra.models.RssFeed;
import at.gartnerundkrammer.rssra.models.RssFeedItem;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.RssFeedDao;
import greendao.RssFeedItemDao;

/**
 * RSS processor called by android-http
 */

public class RssProcessor extends XMLProcessor<greendao.RssFeed>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RssProcessor.class.getSimpleName());

    public static final int ID = 554498;

    private Context applicationContext;

    public RssProcessor(Context context)
    {
        applicationContext = context.getApplicationContext();
    }

    @Override
    protected greendao.RssFeed parse(Document document) {
        LOGGER.trace("Start processing feed");

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(applicationContext, "rssra", null);
        // Access the database using the helper
        SQLiteDatabase db = helper.getWritableDatabase();
        // Construct the DaoMaster which brokers DAOs for the Domain Objects
        DaoMaster daoMaster = new DaoMaster(db);
        // Create the session which is a container for the DAO layer and has a cache which will return handles to the same object across multiple queries
        DaoSession daoSession = daoMaster.newSession();

        RssFeedDao rssFeedDao = daoSession.getRssFeedDao();
        RssFeedItemDao rssFeedItemDao = daoSession.getRssFeedItemDao();

        NodeList list = document.getElementsByTagName("channel");
        if (list.getLength() != 1)
            return null;

        greendao.RssFeed feed = new greendao.RssFeed();
        Node channel = list.item(0);

        // Date format: http://validator.w3.org/feed/docs/warning/ProblematicalRFC822Date.html
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z", Locale.getDefault());

        for (int i=0; i<channel.getChildNodes().getLength(); i++)
        {
            Node n = channel.getChildNodes().item(i);
            if (n.getChildNodes().getLength() < 1)
                continue;

            switch (n.getNodeName())
            {
                case "title":
                    feed.setTitle(n.getChildNodes().item(0).getNodeValue());
                    break;
                case "description":
                    feed.setDescription(n.getChildNodes().item(0).getNodeValue());
                    break;
                case "link":
                    feed.setLink(n.getChildNodes().item(0).getNodeValue());
                    break;
                case "language":
                    feed.setLanguage((n.getChildNodes().item(0).getNodeValue()));
                    break;
                case "copyright":
                    feed.setCopyright(n.getChildNodes().item(0).getNodeValue());
                    break;
                case "pubDate":
                    try {
                        feed.setPubDate(df.parse(n.getChildNodes().item(0).getNodeValue()));
                    } catch (ParseException e) {
                        LOGGER.warn("Unable to parse pubDate", e);
                    }
                    break;
            }
        }

        rssFeedDao.insert(feed);

        NodeList items = document.getElementsByTagName("item");
        for (int i=0; i<items.getLength(); i++)
        {
            greendao.RssFeedItem item = new greendao.RssFeedItem();

            for (int j=0; j<items.item(i).getChildNodes().getLength(); j++)
            {
                Node n = items.item(i).getChildNodes().item(j);
                if (n.getChildNodes().getLength() < 1)
                    continue;

                switch (n.getNodeName())
                {
                    case "title":
                        item.setTitle(n.getChildNodes().item(0).getNodeValue());
                        break;
                    case "description":
                        item.setDescription(n.getChildNodes().item(0).getNodeValue());
                        break;
                    case "link":
                        item.setLink(n.getChildNodes().item(0).getNodeValue());
                        break;
                    case "author":
                        item.setAuthor(n.getChildNodes().item(0).getNodeValue());
                        break;
                    case "pubDate":
                        try {
                            item.setPubDate(df.parse(n.getChildNodes().item(0).getNodeValue()));
                        } catch (ParseException e) {
                            LOGGER.warn("Unable to parse pubDate", e);
                        }
                        break;
                }
            }

            if (item.getTitle() != null && item.getLink() != null) {
                //feed.addItem(item); // automaticly via relation
                item.setFeed(feed);
                rssFeedItemDao.insert(item);
            }
        }

        LOGGER.trace("Processing finished");
        feed.update();
        return feed;
    }

    @Override
    public int getProcessorID() {
        return ID;
    }
}