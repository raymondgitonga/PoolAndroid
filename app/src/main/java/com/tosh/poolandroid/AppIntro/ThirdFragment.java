package com.tosh.poolandroid.AppIntro;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tosh.poolandroid.LoginRegistration.LoginActivity;
import com.tosh.poolandroid.LoginRegistration.RegisterActivity;
import com.tosh.poolandroid.MainActivity;
import com.tosh.poolandroid.R;


public class ThirdFragment extends Fragment {
    private Button getStartedBtn;

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        Button btnStarted = view.findViewById(R.id.btn_getStarted);

        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
