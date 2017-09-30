package guru.nickthompson.livethread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by williamreed on 9/30/17.
 * <p>
 * Simple array adapter to populate ListView with Subreddit.
 */

public class SubredditAdapter extends ArrayAdapter<Subreddit> {

    public SubredditAdapter(Context context, ArrayList<Subreddit> subreddits) {
        super(context, 0, subreddits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subreddit subreddit = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_subreddit, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_item_subreddit_name);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_item_subreddit_description);

        tvName.setText("/r/" + subreddit.getName());
        tvDescription.setText(subreddit.getDescription());

        return convertView;
    }
}
