package android.com.reznikalexey.ui.adapters;

import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.content.Context;
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

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticlesAdapter extends ArrayAdapter<ArticleEntry> {
    public ArticlesAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ArticlesAdapter(Context context, int resource, int textViewResourceId, List<ArticleEntry> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.article_entry_layout, null);
        }

        ArticleEntry entry = getItem(position);

        if (entry != null) {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            TextView tvDescription = (TextView) view.findViewById(R.id.tv_description);
            ProgressBar rlLoadingPanel = (ProgressBar) view.findViewById(R.id.pb_loadingPanel);
            TextView tvSource = (TextView) view.findViewById(R.id.tv_source);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date);

            if (tvTitle != null) {
                tvTitle.setText(entry.getTitle());
            }

            if (ivImage != null) {
                if (entry.getImageBitmap() != null) {
                    ivImage.setImageBitmap(entry.getImageBitmap());
                    rlLoadingPanel.setVisibility(View.GONE);
                } else {
                    //ImageUrl is not null => picture must be loading at the moment
                    if (entry.getImageUrl() != null) {
                        ivImage.setImageResource(R.drawable.img_placeholder);
                        rlLoadingPanel.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (tvDescription != null) {
                tvDescription.setText(entry.getDescription());
                if (entry.isDetailedView()) {
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }
            }

            if (tvSource != null) {
                tvSource.setText(entry.getSource().toString());
            }

            if (tvDate != null) {
                try {
                    String dateString = entry.getDate();
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
                    Date date = format.parse(dateString);

                    SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
                    tvDate.setText(newFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

        return view;
    }
}
