package io.github.livethread.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by williamreed on 1/9/18.
 * Fragment for a profile.
 */

public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO: not sure where I need to init the subreddit view model ?

        super.onActivityCreated(savedInstanceState);
        viewModel.getUser().observe(this, user -> {
            // this will get triggered by the changing of LiveData
            // update UI
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.user_profile, container, false);
        return null;
    }
}
