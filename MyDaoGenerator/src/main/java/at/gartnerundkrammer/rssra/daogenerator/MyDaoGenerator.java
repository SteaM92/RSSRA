package at.gartnerundkrammer.rssra.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;
import de.greenrobot.daogenerator.ToOne;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {

        Schema schema = new Schema(4, "greendao"); //use version 4

        Entity rssFeed = schema.addEntity("RssFeed");
        rssFeed.addIdProperty();
        rssFeed.addStringProperty("source")
            ;
        rssFeed.addStringProperty("title");
        rssFeed.addStringProperty("description");
        rssFeed.addStringProperty("link");
        rssFeed.addStringProperty("language");
        rssFeed.addStringProperty("copyright");
        rssFeed.addDateProperty("pubDate");
        rssFeed.addContentProvider();


        Entity rssFeedItem = schema.addEntity("RssFeedItem");
        Property rssFeedItemID = rssFeedItem.addIdProperty().getProperty();
        Property rssFeedID = rssFeedItem.addLongProperty("feedId").getProperty();
        rssFeedItem.addStringProperty("title");
        rssFeedItem.addStringProperty("description");
        rssFeedItem.addStringProperty("link");
        rssFeedItem.addStringProperty("author");
        rssFeedItem.addDateProperty("pubDate");
        rssFeedItem.addStringProperty("state");
        rssFeedItem.addContentProvider();

        ToMany items = rssFeed.addToMany(rssFeedItem, rssFeedID);
        items.setName("items"); // Optional
        //items.orderAsc(orderDate); // Optional

        ToOne feed = rssFeedItem.addToOne(rssFeed, rssFeedID);
        feed.setName("feed");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
