package guru.nickthompson.livethread.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import guru.nickthompson.livethread.R;
import guru.nickthompson.livethread.fragments.PostListFragment.OnListFragmentInteractionListener;
import guru.nickthompson.redditapi.Post;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final List<Post> mPosts;
    private final OnListFragmentInteractionListener mListener;

    public PostsAdapter(List<Post> items, OnListFragmentInteractionListener listener) {
        mPosts = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Post post = mPosts.get(position);

        TextView tvPostTitle = holder.mPostTitleView;
        tvPostTitle.setText(post.getTitle());

        TextView tvScore = holder.mScoreView;
        tvScore.setText(String.valueOf(post.getScore() + " votes"));

        TextView tvNumComments = holder.mNumComments;
        tvNumComments.setText(" • " + String.valueOf(post.getNumComments()) + " comments");

        TextView tvAuthor = holder.mAuthor;
        tvAuthor.setText("/u/" + post.getAuthor());

        TextView tvPostDate = holder.mPostDate;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, h:mm a"); //Or whatever format fits best your needs.
        tvPostDate.setText(" on " + sdf.format(post.getCreated()));

        holder.mItem = post;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPostTitleView;
        public final TextView mScoreView;
        public final TextView mNumComments;
        public final TextView mAuthor;
        public final TextView mPostDate;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPostTitleView = view.findViewById(R.id.tv_item_post_title);
            mScoreView = view.findViewById(R.id.tv_item_post_score);
            mNumComments = view.findViewById(R.id.tv_item_post_num_comments);
            mPostDate = view.findViewById(R.id.tv_item_post_date);
            mAuthor = view.findViewById(R.id.tv_item_post_author);
        }
    }
}
