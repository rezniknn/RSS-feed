package android.com.reznikalexey.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticleEntry implements Comparable {
    public static final String LOG_TAG = "ArticleEntry";

    private ArticleSource source;
    private String date;
    private String title;
    private String description;
    private String imageUrl;

    private boolean detailedView;

    //Defines how articles are sorted.
    @Override
    public int compareTo(Object another) {
        if (this.getDate() != null && ((ArticleEntry) another).getDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
            try {
                //Convert String to Date object
                Date thisDate = format.parse(this.date);
                Date thatDate = format.parse(((ArticleEntry) another).getDate());

                //Compare dates of 2 articles. Articles with fresher date should go first
                if (thisDate.before(thatDate)) {
                    return 1;
                } else if (thatDate.before(thisDate)) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public enum ArticleSource {
        LENTA("lenta.ru"),
        GAZETA("gazeta.ru"),
        UNKNOWN("source is unknown");

        private final String textDescription;

        private ArticleSource(String s) {
            textDescription = s;
        }

        public String toString() {
            return this.textDescription;
        }
    }

    public ArticleEntry(String date, ArticleSource source, String title, String description, String imageUrl) {
        this.date = date;
        this.source = source;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArticleSource getSource() {
        return source;
    }

    public void setSource(ArticleSource source) {
        this.source = source;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDetailedView() {
        return detailedView;
    }

    public void setDetailedView(boolean detailedView) {
        this.detailedView = detailedView;
    }
}
