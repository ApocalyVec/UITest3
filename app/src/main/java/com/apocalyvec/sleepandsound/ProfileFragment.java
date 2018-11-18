package com.apocalyvec.sleepandsound;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    TextView user_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user_email = getActivity().findViewById(R.id.user_email);
        return inflater.inflate(com.apocalyvec.sleepandsound.R.layout.fragment_profile, container, false);
    }
}
