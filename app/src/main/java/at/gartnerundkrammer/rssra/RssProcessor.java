package at.gartnerundkrammer.rssra;

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

/**
 * RSS processor called by android-http
 */

public class RssProcessor extends XMLProcessor<RssFeed>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RssProcessor.class.getSimpleName());

    @Override
    protected RssFeed parse(Document document) {
        NodeList list = document.getElementsByTagName("channel");
        if (list.getLength() != 1)
            return null;

        RssFeed feed = new RssFeed();
        Node channel = list.item(0);

        // Date format: http://validator.w3.org/feed/docs/warning/ProblematicalRFC822Date.html
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z", Locale.getDefault());

        for (int i=0; i<channel.getChildNodes().getLength(); i++)
        {
            Node n = channel.getChildNodes().item(i);
            switch (n.getNodeName())
            {
                case "title":
                    feed.setTitle(n.getNodeValue());
                    break;
                case "description":
                    feed.setDescription(n.getNodeValue());
                    break;
                case "link":
                    feed.setLink(n.getNodeValue());
                    break;
                case "language":
                    feed.setLanguage((n.getNodeValue()));
                    break;
                case "copyright":
                    feed.setCopyright(n.getNodeValue());
                    break;
                case "pubDate":
                    try {
                        feed.setPubDate(df.parse(n.getNodeValue()));
                    } catch (ParseException e) {
                        LOGGER.warn("Unable to parse pubDate", e);
                    }
                    break;
            }
        }

        NodeList items = document.getElementsByTagName("item");
        for (int i=0; i<items.getLength(); i++)
        {
            for (int j=0; j<items.item(i).getChildNodes().getLength(); j++)
            {
                Node n = items.item(i).getChildNodes().item(j);
                RssFeedItem item = new RssFeedItem();
                switch (n.getNodeName())
                {
                    case "title":
                        item.setTitle(n.getNodeValue());
                        break;
                    case "description":
                        item.setDescription(n.getNodeValue());
                        break;
                    case "link":
                        item.setLink(n.getNodeValue());
                        break;
                    case "author":
                        item.setAuthor(n.getNodeValue());
                        break;
                    case "pubDate":
                        try {
                            item.setPubDate(df.parse(n.getNodeValue()));
                        } catch (ParseException e) {
                            LOGGER.warn("Unable to parse pubDate", e);
                        }
                        break;
                }
                feed.addItem(item);
            }
        }

        return feed;
    }

    @Override
    public int getProcessorID() {
        return 554498;
    }
}