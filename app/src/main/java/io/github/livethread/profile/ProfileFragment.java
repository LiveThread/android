package io.github.livethread.profile;

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
import io.github.livethread.R;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by williamreed on 1/9/18.
 * Fragment for a profile. Expects argument with name 'username'.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "lt_ProfileFragment";

    private ProfileRepository profileRepository;
    private String username;
    private Account account;

    @BindView(R.id.tv_username)      TextView tvUsername;
    @BindView(R.id.tv_commentKarma)  TextView tvCommentKarma;
    @BindView(R.id.tv_linkKarma)     TextView tvLinkKarma;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        profileRepository = new ProfileRepository();
        username = getArguments().getString("username");
        Log.d(TAG, "Starting Profile Fragment. Given username: '" + username + "'");
        ButterKnife.bind(view);
        fetchUser(username);
    }

    /**
     * Get the user and update the UI.
     *
     * @param username the user to display.
     */
    private void fetchUser(String username) {
        Log.d(TAG, "fetching user '" + username + "'");
        profileRepository.getUser(username)
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
}
