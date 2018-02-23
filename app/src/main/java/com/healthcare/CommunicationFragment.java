package com.healthcare;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 20/06/2017.
 */

public class CommunicationFragment extends Fragment {
    private View communicationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        communicationView = inflater.inflate(R.layout.communication_layout, container, false);
        return communicationView;
    }
}
