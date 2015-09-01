package android.com.reznikalexey.model;

import android.util.Log;

import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alexeyreznik on 01/09/15.
 */
public class ArticleXMLParser {
    public static final String LOG_TAG = "ArticleXMLParser";
    private static final String ns = null;


    public static List<ArticleEntry> parse(InputStream inputStream, String encoding) {
        ArrayList<ArticleEntry> entries = new ArrayList<ArticleEntry>();

        try {
            Reader reader = new InputStreamReader(inputStream, encoding);
            InputSource inputSource = new InputSource(reader);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputSource);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");

            for (int i=0; i < nodeList.getLength(); i++) {
                entries.add(parseItem(nodeList.item(i)));
            }

            Log.d(LOG_TAG, "Number of items found in XML: " + nodeList.getLength());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

    private static ArticleEntry parseItem(Node item) {
        String date = null;
        String title = null;
        String description = null;
        String imageUrl = null;
        String link = null;
        ArticleEntry.ArticleSource source;

        NodeList itemElements = item.getChildNodes();

        for (int i=0; i < itemElements.getLength(); i++) {
            Node node = itemElements.item(i);

            if (node.getNodeName().equals("pubDate")) {
                Node value = node.getFirstChild();
                date = value.getNodeValue();
            }

            if (node.getNodeName().equals("title")) {
                Node value = node.getFirstChild();
                title = value.getNodeValue();
            }

            if (node.getNodeName().equals("description")) {
                Node value = node.getFirstChild();
                description = value.getNodeValue();
            }

            if (node.getNodeName().equals("link")) {
                Node value = node.getFirstChild();
                link = value.getNodeValue();
            }

            if (node.getNodeName().equals("enclosure")) {
                imageUrl = node.getAttributes().getNamedItem("url").getNodeValue();
            }
        }

        source = ArticleEntry.ArticleSource.UNKNOWN;

        if (link != null) {
            if (link.contains("lenta")) {
                source = ArticleEntry.ArticleSource.LENTA;
            }

            if (link.contains("gazeta")) {
                source = ArticleEntry.ArticleSource.GAZETA;
            }
        }

        Log.d(LOG_TAG, "New item has been parsed");
        Log.d(LOG_TAG, "Date: " + date);
        Log.d(LOG_TAG, "Title: " + title);
        Log.d(LOG_TAG, "Description: " + description);
        Log.d(LOG_TAG, "Source: " + source.toString());
        Log.d(LOG_TAG, "ImageUrl: " + imageUrl);

        return new ArticleEntry(date, source, title, description, imageUrl);
    }
}
