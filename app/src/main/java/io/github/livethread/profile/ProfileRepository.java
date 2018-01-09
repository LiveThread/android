package io.github.livethread.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import net.dean.jraw.models.Account;

/**
 * Created by williamreed on 1/9/18.
 * Repository for getting profile information.
 */
public class ProfileRepository {

    LiveData<Account> getUser(String username) {
        final MutableLiveData<Account> data = new MutableLiveData<>();

        // TODO: call jraw...

        return data;
    }
}
