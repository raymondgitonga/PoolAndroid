package com.tosh.poolandroid.view.appIntro;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tosh.poolandroid.R;


public class SecondFragment extends Fragment {

    TextView next;
    ViewPager viewPager;


    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        viewPager = getActivity().findViewById(R.id.viewPager);
        next = view.findViewById(R.id.intro_next2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        return view;

    }

}
