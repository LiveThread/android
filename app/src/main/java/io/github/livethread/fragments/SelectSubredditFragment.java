package io.github.livethread.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;

import java.util.ArrayList;
import java.util.List;

import io.github.livethread.AndroidTokenStore;
import io.github.livethread.LiveThreadApplication;
import io.github.livethread.R;
import io.github.livethread.activities.LoginActivity;
import io.github.livethread.adapters.SubredditAdapter;
import io.github.livethread.cac.AsyncCACRunner;
import io.github.livethread.cac.Authenticate;
import io.github.livethread.cac.AuthenticateRefresh;
import io.github.livethread.redditapi.Subreddit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectSubredditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelectSubredditFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private List<Subreddit> subreddits;
    private ListView mListView;

    public SelectSubredditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReddit();
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.t_subreddit);
        toolbar.setTitle("LiveThread");
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_circle);
    }

    // TODO: what is this supposed to be?
    public static final int REQUEST_CODE = 1;

    // gets called on success from the login activity.
    // TODO: i think this needs to be reworked now that it is a part of a fragment thing
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("rq " + requestCode + " rc " + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            String url = data.getStringExtra("RESULT_URL");

            new AsyncCACRunner<>(new Authenticate(
                    (LiveThreadApplication) getActivity().getApplication(), url)).execute();
        }
    }

    /**
     * Init the reddit stuff.
     */
    private void initReddit() {
        RedditClient redditClient = ((LiveThreadApplication) getActivity().getApplication()).getRedditClient();
        TokenStore store = new AndroidTokenStore(
                PreferenceManager.getDefaultSharedPreferences(getActivity()));
        RefreshTokenHandler refreshTokenHandler = new RefreshTokenHandler(store, redditClient);

        AuthenticationManager manager = AuthenticationManager.get();
        manager.init(redditClient, refreshTokenHandler);

        // check auth state and display it
        AuthenticationState authState = AuthenticationManager.get().checkAuthState();

        if (authState == AuthenticationState.NEED_REFRESH) {
            new AsyncCACRunner<>(new AuthenticateRefresh((LiveThreadApplication) getActivity()
                    .getApplication())).execute();
        } else if (authState == AuthenticationState.NONE) {
            // TODO: what should the actual request code be?
            // TODO: should login be a fragment?
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
        } else if (authState == AuthenticationState.READY) {
            // do nothing
            // maybe fetch some user info or something i dunno.
        }
    }

    /**
     * Setup the suggested subreddits.
     */
    private void setupSuggestedSubreddits() {
        subreddits = new ArrayList<Subreddit>();
        /// TODO: populate
        subreddits.add(new Subreddit("Soccer", "The back page of the internet"));
        subreddits.add(new Subreddit("NFL", "National Football League Discussion"));
        subreddits.add(new Subreddit("NBA", "National Basketball Association"));
        subreddits.add(new Subreddit("Hockey", "the best game on earth"));
        subreddits.add(new Subreddit("Tennis", "Tennis News & Discussion"));
        subreddits.add(new Subreddit("CFB", "The Internet's Tailgate"));
        subreddits.add(new Subreddit("News", "All news, US and international"));
        subreddits.add(new Subreddit("WorldNews", "The Worlds News"));
        subreddits.add(new Subreddit("Politics", "Political Discussion"));

        SubredditAdapter adapter = new SubredditAdapter(getContext(), subreddits);
        mListView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_subreddit, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.b_select_subreddit).setOnClickListener(this);

        // create list view of suggested subs
        mListView = (ListView) getView().findViewById(R.id.lv_select_subreddit_popular);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Subreddit subreddit = subreddits.get(position);
                mListener.onSubredditClick(subreddit.getName());
            }
        });
        setupSuggestedSubreddits();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_select_subreddit:
                EditText etSubreddit = (EditText) getView().findViewById(R.id.et_select_subreddit);
                String subredditName = etSubreddit.getText().toString();

                if (subredditName.equals("")) {
                    Toast.makeText(getContext(), "Enter a valid subreddit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onSubredditClick(subredditName);

                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**
         * Open the subreddit.
         *
         * @param subredditName name of the subreddit.
         */
        void onSubredditClick(String subredditName);
    }
}
