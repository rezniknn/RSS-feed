package android.com.reznikalexey.ui.adapters;

import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticlesAdapter extends ArrayAdapter<ArticleEntry> {
    private List<ArticleEntry> articles;
    private final LayoutInflater inflator;

    public ArticlesAdapter(Context context, int resource, List<ArticleEntry> articles) {
        super(context, resource, articles);
        this.articles = articles;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
        public TextView tvSource;
        public TextView tvDate;
        public ImageView ivImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.article_entry_layout, null);

            TextView tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            final TextView tvDescription = (TextView) convertView
                    .findViewById(R.id.tv_description);
            TextView tvSource = (TextView) convertView
                    .findViewById(R.id.tv_source);
            final TextView tvDate = (TextView) convertView
                    .findViewById(R.id.tv_date);
            ImageView ivImage = (ImageView) convertView
                    .findViewById(R.id.iv_image);

            holder = new ViewHolder();
            holder.tvTitle = tvTitle;
            holder.tvDescription = tvDescription;
            holder.tvSource = tvSource;
            holder.tvDate = tvDate;
            holder.ivImage = ivImage;

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

            //TODO Temporary
            ivImage.setVisibility(View.GONE);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArticleEntry article = articles.get(position);

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
                String dateString = article.getDate();
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
                Date date = format.parse(dateString);

                SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
                holder.tvDate.setText(newFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (holder.ivImage != null) {
            holder.ivImage.setTag(article.getImageUrl());
            //BitmapManager.getInstance().loadBitmap(article.getImageUrl(), holder.ivImage, Const.IMG_WIDTH, Const.IMG_HEIGHT);
        }

        return convertView;
    }
}
