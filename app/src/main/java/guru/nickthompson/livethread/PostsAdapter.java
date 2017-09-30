package guru.nickthompson.livethread;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import guru.nickthompson.livethread.PostListFragment.OnListFragmentInteractionListener;
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
        // TODO: change to post.getTitle();
        tvPostTitle.setText(post.getID());
        // TODO: add more fields

        holder.mItem = post;

        //TODO: on click load comments
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
        public Post mItem;
        // TODO: add more fields

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPostTitleView = view.findViewById(R.id.tv_item_post_title);
        }
    }
}
