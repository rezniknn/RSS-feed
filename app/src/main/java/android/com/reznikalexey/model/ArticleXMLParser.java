package android.com.reznikalexey.model;

import android.util.Log;

import org.w3c.dom.CharacterData;
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
    public static final String LOG_TAG = "ArticleXMLParser";
    private static final String ns = null;


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

        if (link != null) {
            if (link.contains("lenta")) {
                source = ArticleEntry.ArticleSource.LENTA;
            }

            if (link.contains("gazeta")) {
                source = ArticleEntry.ArticleSource.GAZETA;
            }
        }

        return new ArticleEntry(date, source, title, description, imageUrl);
    }

    static public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return getElementValue(n.item(0));
    }

    static public final String getElementValue( Node elem ) {
        try {
            Node child;
            if( elem != null){
                if (elem.hasChildNodes()){
                    for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                        if( child.getNodeType() == Node.CDATA_SECTION_NODE
                                || child.getNodeType() == Node.TEXT_NODE )
                        {
                            return child.getNodeValue().trim();
                        }
                    }
                }
            }
            return null;
        } catch (DOMException e) {
            //Logger.logError(e);
            return null;
        }
    }
}
