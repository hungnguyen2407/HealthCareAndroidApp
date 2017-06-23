package com.healthcareandroidapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 23/06/2017.
 */

public class SettingFragment extends Fragment {
    private View settingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        settingView = inflater.inflate(R.layout.setting_layout, container, false);

        return settingView;
    }
}
