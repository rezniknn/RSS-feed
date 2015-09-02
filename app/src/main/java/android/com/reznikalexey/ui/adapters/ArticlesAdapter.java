package android.com.reznikalexey.ui.adapters;

import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.utils.BitmapManager;
import android.com.reznikalexey.utils.Const;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticlesAdapter extends ArrayAdapter<ArticleEntry> {
    private List<ArticleEntry> articles;
    private final LayoutInflater inflater;
    private Bitmap placeholder;

    public ArticlesAdapter(Context context, int resource, List<ArticleEntry> articles) {
        super(context, resource, articles);
        this.articles = articles;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Init BitmapManager with placeholder image
        placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_placeholder);
        BitmapManager.getInstance().setPlaceholder(Bitmap.createScaledBitmap(placeholder, Const.IMG_WIDTH, Const.IMG_HEIGHT, false));
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
        public TextView tvSource;
        public TextView tvDate;
        public ImageView ivImage;
        public ProgressBar pbProgress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final ArticleEntry article = articles.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.article_entry_layout, null);

            TextView tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            final TextView tvDescription = (TextView) convertView
                    .findViewById(R.id.tv_description);
            TextView tvSource = (TextView) convertView
                    .findViewById(R.id.tv_source);
            TextView tvDate = (TextView) convertView
                    .findViewById(R.id.tv_date);
            ImageView ivImage = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            ProgressBar pbProgress = (ProgressBar) convertView
                    .findViewById(R.id.pb_loadingPanel);

            holder = new ViewHolder();
            holder.tvTitle = tvTitle;
            holder.tvDescription = tvDescription;
            holder.tvSource = tvSource;
            holder.tvDate = tvDate;
            holder.ivImage = ivImage;
            holder.pbProgress = pbProgress;

            convertView.setTag(holder);

            //Set item click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvDescription != null) {
                        if (tvDescription.getVisibility() == View.GONE) {
                            tvDescription.setVisibility(View.VISIBLE);
                        } else {
                            tvDescription.setVisibility(View.GONE);
                        }
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Hide article's description by default
        holder.tvDescription.setVisibility(View.GONE);

        if (article.getTitle() != null) {
            holder.tvTitle.setText(article.getTitle());
        }

        if (article.getDescription() != null) {
            holder.tvDescription.setText(article.getDescription());
        }

        if (article.getSource() != null) {
            holder.tvSource.setText(article.getSource().toString());
        }

        if (article.getDate() != null) {
            try {
                //Reformat Date to a more human readable format
                String dateString = article.getDate();
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Date date = format.parse(dateString);

                SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                holder.tvDate.setText(newFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (article.getImageUrl() != null) {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivImage.setTag(article.getImageUrl());
            //Instantiate image loading task
            BitmapManager.getInstance().loadBitmap(article.getImageUrl(), holder.ivImage, holder.pbProgress, Const.IMG_WIDTH, Const.IMG_HEIGHT);
        } else {
            //Hide ImageView if url is null.
            holder.ivImage.setVisibility(View.GONE);
        }
        return convertView;
    }
}
