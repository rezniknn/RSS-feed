package android.com.reznikalexey.ui.adapters;

import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.utils.BitmapUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
            RelativeLayout rlLoadingPanel = (RelativeLayout)view.findViewById(R.id.rl_loadingPanel);

            if (tvTitle != null) {
                tvTitle.setText(entry.getTitle());
            }

            if (ivImage != null) {
                ivImage.setImageBitmap(entry.getImageBitmap());
                rlLoadingPanel.setVisibility(View.GONE);
            } else {
                ivImage.setImageResource(R.drawable.img_placeholder);
                rlLoadingPanel.setVisibility(View.VISIBLE);
            }

            if (tvDescription != null) {
                tvDescription.setText(entry.getDescription());
            }
        }

        return view;
    }
}
