package com.healthcareandroidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 26/06/2017.
 */

public class InfoFragment extends Fragment {

    private View infoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        infoView = inflater.inflate(R.layout.info_layout, container, false);
        return infoView;
    }
}
