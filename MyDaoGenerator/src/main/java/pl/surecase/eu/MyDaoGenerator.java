package pl.surecase.eu;

import java.util.Date;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao"); //use version 3

        Entity rssFeed = schema.addEntity("RssFeed");
        rssFeed.addIdProperty();
        rssFeed.addStringProperty("source");
        rssFeed.addStringProperty("title");
        rssFeed.addStringProperty("description");
        rssFeed.addStringProperty("link");
        rssFeed.addStringProperty("language");
        rssFeed.addStringProperty("copyright");
        rssFeed.addDateProperty("pubDate");

        Entity rssFeedItem = schema.addEntity("RssFeedItem");
        Property rssFeedItemID = rssFeedItem.addIdProperty().getProperty();
        rssFeedItem.addStringProperty("title");
        rssFeedItem.addStringProperty("description");
        rssFeedItem.addStringProperty("link");
        rssFeedItem.addStringProperty("author");
        rssFeedItem.addDateProperty("pubDate");

        ToMany items = rssFeed.addToMany(rssFeedItem, rssFeedItemID);
        items.setName("items"); // Optional
        //items.orderAsc(orderDate); // Optional

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
