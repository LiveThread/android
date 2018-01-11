package io.github.livethread.profile;

import net.dean.jraw.models.Account;

import rx.Observable;

import static io.github.livethread.LiveThreadApplication.getAccountHelper;

/**
 * Created by williamreed on 1/9/18.
 * Repository for getting profile information.
 */
public class ProfileRepository {

    /**
     * Get the given user.
     *
     * @param username
     * @return the Account for the given user
     */
    public Observable<Account> getUser(String username) {
        // need to defer this to subscription to avoid main thread networking
        return Observable.defer(() -> Observable.just(getAccountHelper().getReddit().user(username).about()));
    }

    /**
     * @return the Account of the logged in user.
     */
    public Observable<Account> getMe() {
        return Observable.defer(() -> Observable.just(getAccountHelper().getReddit().me().about()));
    }
}
