package com.healthcare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 25/02/2018.
 */

public class RegisterFragment extends Fragment {

    private View registerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registerLayout = inflater.inflate(R.layout.register_layout, container, false);


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
