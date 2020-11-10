package com.example.smartwarehouse;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    public static final String TAG = SecondFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);



        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        View drawView = view.findViewById(R.id.drawView);
//        drawView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String[] target_positions=new String[]{"A1001","C5010","E8010"};
//
//                drawView.setTargets(target_positions);
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.drawView).bringToFront();
    }
}
