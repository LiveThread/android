package io.github.livethread.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.dean.jraw.models.Account;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.livethread.LiveThreadApplication;
import io.github.livethread.R;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by williamreed on 1/9/18.
 * Fragment for a profile. Expects argument with name 'username'.
 * Use {@link ProfileFragment#newInstance} to construct.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "lt_ProfileFragment";
    private static final String ARG_USERNAME = "ARG_USERNAME";

    private ProfileRepository profileRepository;
    private String username;
    private Account account;
    private Subscription userSubscription;

    @BindView(R.id.tv_username)      TextView tvUsername;
    @BindView(R.id.tv_commentKarma)  TextView tvCommentKarma;
    @BindView(R.id.tv_linkKarma)     TextView tvLinkKarma;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username the username to show the profile of.
     * @return A new instance of fragment HomeFragment.
     */
    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        } else {
            Log.e(TAG, "username not provided to ProfileFragment");
            throw new RuntimeException("username not provided to ProfileFragment");
        }

        Log.d(TAG, "Starting Profile Fragment. Given username: '" + username + "'");
        profileRepository = new ProfileRepository();
        ButterKnife.bind(this, getView());
        fetchUser(username);
    }

    /**
     * Get the user and update the UI.
     *
     * @param username the user to display.
     */
    private void fetchUser(String username) {
        Log.d(TAG, "fetching user '" + username + "'");
        userSubscription = profileRepository.getUser(username)
                // retrieve on non main thread
                .subscribeOn(Schedulers.io())
                // take action on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "updating UI with account");
                        try {
                            // update UI
                            tvUsername.setText(account.getName());
                            tvCommentKarma.setText(String.valueOf(account.getCommentKarma()) + " comment karma");
                            tvLinkKarma.setText(String.valueOf(account.getLinkKarma()) + " link karma");
                        } catch (Exception e) {
                            Log.e(TAG, "error fetching user");
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error fetching user");
                        Log.d(TAG, e.getMessage());
                        // tell user there was an error
                        Toast.makeText(getContext(), "Error retrieving profile", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(Account a) {
                        Log.d(TAG, "account info retrieved - updating local copy");
                        // give the fragment a reference to the account
                        account = a;
                    }
                });
    }

    /**
     * Log the current user out.
     */
    @OnClick(R.id.b_logout)
    public void logout() {
        // logout from client
        Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
            LiveThreadApplication.getAccountHelper().switchToUserless();
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io());

        // remove username cache
        SharedPreferences settings = getActivity().getSharedPreferences(LiveThreadApplication.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("username");
        editor.commit();
        // remove tokens
        LiveThreadApplication.getTokenStore().deleteLatest(username);


        Log.d(TAG, "User logged out");
    }

    @Override
    public void onStop() {
        super.onStop();

        userSubscription.unsubscribe();
    }
}
