package android.com.reznikalexey.model;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

    //Parse XML from InputStream given a particular encoding
    public static List<ArticleEntry> parse(InputStream inputStream, String encoding) {
        ArrayList<ArticleEntry> entries = new ArrayList<ArticleEntry>();

        try {
            Reader reader = new InputStreamReader(inputStream, encoding);
            InputSource inputSource = new InputSource(reader);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //Convert CDATA nodes to text nodes
            dbf.setCoalescing(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputSource);
            doc.getDocumentElement().normalize();

            //Locate all <item> elements
            NodeList nodeList = doc.getElementsByTagName("item");

            //Parse each <item> element separately. Add ArticleEntry's to the list
            for (int i = 0; i < nodeList.getLength(); i++) {
                entries.add(parseItem(nodeList.item(i)));
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Return a list of ArticleEntries
        return entries;
    }

    //Parse <item> element
    private static ArticleEntry parseItem(Node item) {
        String date;
        String title;
        String description;
        String link;
        ArticleEntry.ArticleSource source = ArticleEntry.ArticleSource.UNKNOWN;
        String imageUrl = null;

        Element itemElement = (Element) item;

        title = getValue(itemElement, "title");
        description = getValue(itemElement, "description");
        date = getValue(itemElement, "pubDate");
        link = getValue(itemElement, "link");

        //Special parsing for imageUrl
        NodeList nodes = ((Element) item).getElementsByTagName("enclosure");
        if (nodes.getLength() != 0) {
            Node imageUrlElement = nodes.item(0);
            if (imageUrlElement != null) {
                imageUrl = imageUrlElement.getAttributes().getNamedItem("url").getNodeValue();
            }
        }

        //Define Source of the article based on the source Link
        if (link != null) {
            if (link.contains("lenta")) {
                source = ArticleEntry.ArticleSource.LENTA;
            }

            if (link.contains("gazeta")) {
                source = ArticleEntry.ArticleSource.GAZETA;
            }
        }

        //Create and return a new instance of ArticleEntry
        return new ArticleEntry(date, source, title, description, imageUrl);
    }

    //Get text value within a particular XML element
    static public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

    static public final String getElementValue(Node elem) {
        try {
            Node child;
            if (elem != null) {
                if (elem.hasChildNodes()) {
                    for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                        if (child.getNodeType() == Node.CDATA_SECTION_NODE
                                || child.getNodeType() == Node.TEXT_NODE) {
                            return child.getNodeValue().trim();
                        }
                    }
                }
            }
            return null;
        } catch (DOMException e) {
            return null;
        }
    }
}
