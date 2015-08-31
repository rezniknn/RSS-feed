package android.com.reznikalexey.model;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticleEntry {
    private ArticleSource source;
    private String title;
    private String description;
    private String imageUrl;

    public enum ArticleSource {
        LENTA ("lenta.ru"),
        GAZETA ("gazeta.ru");

        private final String textDescription;

        private ArticleSource(String s) {
            textDescription = s;
        }

        public String toString() {
            return this.textDescription;
        }
    }

    public ArticleEntry(ArticleSource source, String title, String description, String imageUrl) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
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
}
