package guru.nickthompson.livethread.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import guru.nickthompson.livethread.R;
import guru.nickthompson.livethread.SortedHashedArrayList;
import guru.nickthompson.redditapi.Comment;

/**
 * Created by williamreed on 9/29/17.
 * <p>
 * Adapter for intermediary between data and recycler view
 * thanks code path
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 1;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public TextView tvDate;
        public TextView tvContent;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_post_username);
            tvDate = itemView.findViewById(R.id.tv_post_date);
            tvContent = itemView.findViewById(R.id.tv_post_content);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private SortedHashedArrayList<Comment> comments;
    private View header;
    private Context context;


    //https://stackoverflow.com/questions/26448717/android-5-0-add-header-footer-to-a-recyclerview
    /// TODO :https://gist.github.com/willblaschko/1113be1eaff048a6ed14
    public CommentsAdapter(Context context, SortedHashedArrayList<Comment> comments, View header) {
        this.context = context;
        this.comments = comments;
        this.header = header;
    }

    private Context getContext() {
        return context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == TYPE_HEADER) {
            View headerView = inflater.inflate(R.layout.item_post_header, parent, false);
            HeaderViewHolder viewHolder = new HeaderViewHolder(headerView);
            return viewHolder;
        }

        // Inflate the custom layout
        View commentView = inflater.inflate(R.layout.item_comment, parent, false);

        // Return a new holder instance
        ItemViewHolder viewHolder = new ItemViewHolder(commentView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

            // Get the data model based on position
            Comment comment = comments.get(position);

            // Set item views based on your views and data model
            TextView username = itemViewHolder.tvUsername;
            username.setText("/u/" + comment.getUsername());

            TextView date = itemViewHolder.tvDate;
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, h:mm a"); //Or whatever format fits best your needs.
            date.setText(sdf.format(comment.getTimeStamp()));


            TextView content = itemViewHolder.tvContent;

            // TODO: update to respect api and deprecation
//        if (Build.VERSION.SDK_INT >= 24) {
//            content.setText(Html.fromHtml(comment.getBody(), Html.FROM_HTML_MODE_LEGACY)); // for 24 api and more
//        } else {
//            content.setText(Html.fromHtml(comment.getBody())) ;// or for older api
//        }

            content.setText(Html.fromHtml(comment.getBody()).toString().trim());
        } else if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

            // TODO: adapt and overcome ralphy
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        // adding one because of the expected singular header
        // TODO: maybe make this more dynamic for the header
        // or possibly use a list of headers
        // maybe an extendable class that implementes headers for you?
        return comments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return super.getItemViewType(position);

    }
}
