package guru.nickthompson.livethread.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import guru.nickthompson.livethread.AsyncCACRunner;
import guru.nickthompson.livethread.AsyncCommandAndCallback;
import guru.nickthompson.livethread.R;
import guru.nickthompson.livethread.adapters.PostsAdapter;
import guru.nickthompson.redditapi.Post;
import guru.nickthompson.redditapi.Subreddit;

/**
 * A fragment representing a list of Subreddit posts.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PostListFragment extends Fragment {
    private static final String ARG_SUBREDDIT_NAME = "SUBREDDIT_NAME";
    private String mSubredditName = "frontpage";
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostListFragment() {
    }

    public static PostListFragment newInstance(String subredditName) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBREDDIT_NAME, subredditName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSubredditName = getArguments().getString(ARG_SUBREDDIT_NAME);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.t_subreddit);
        toolbar.setTitle("/r/" + mSubredditName);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            new AsyncCACRunner<>(new PostFetcher(mSubredditName)).execute();
            // add a horizontal line between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Post item);
    }

    /**
     * Function object responsible for fetching all posts and adding to RecyclerView.
     */
    public class PostFetcher implements AsyncCommandAndCallback<ArrayList<Post>> {

        String subredditName;

        public PostFetcher(String subredditName) {
            this.subredditName = subredditName;
        }

        @Override
        public ArrayList<Post> command() {
            return Subreddit.getAllPosts(subredditName);
        }

        @Override
        public void callback(ArrayList<Post> result) {
            recyclerView.setAdapter(new PostsAdapter(result, mListener));
        }
    }
}
