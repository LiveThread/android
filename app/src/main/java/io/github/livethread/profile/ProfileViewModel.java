package io.github.livethread.profile;

import android.arch.lifecycle.LiveData;

import net.dean.jraw.models.Account;

/**
 * Created by williamreed on 1/9/18.
 * ViewModel for Profile. Containing information about the profile. Probably an abridged
 * version of the profile containing karma counts, profile creation date...
 */
public class ProfileViewModel {
    private String username;
    private LiveData<Account> user;

    public void init(String username) {
        this.username = username;
    }

    public LiveData<Account> getUser() {
        return user;
    }
}
