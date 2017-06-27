package com.healthcareandroidapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hungnguyen on 27/06/2017.
 */

public class MessagesFragment extends Fragment {

    private View messagesView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        messagesView = inflater.inflate(R.layout.messages_layout, container, false);
        messagesHandle();
        return messagesView;
    }

    private void messagesHandle()
    {

    }
}
