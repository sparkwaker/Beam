package com.sparkwaker.beam.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sparkwaker.beam.R;


public class SettingsFragment extends Fragment {
    private TextView RateAppOption;
    private TextView SoundQualityOption;
    private TextView AudioFormatOption;
    private TextView ExtractTimeOption;
    private  View mView;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        configUiGeneral();
    }

    private void configUiGeneral (){
        RateAppOption = mView.findViewById(R.id.txtRateApp);
        SoundQualityOption =mView.findViewById(R.id.txtSoundQuality);
        AudioFormatOption = mView.findViewById(R.id.txtAudioFormat);
        ExtractTimeOption = mView.findViewById(R.id.txtExtractTime);

        RateAppOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Rate option",Toast.LENGTH_LONG).show();
            }
        });
        SoundQualityOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Sound Quality Option",Toast.LENGTH_LONG).show();
            }
        });

        AudioFormatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Audio Format Option",Toast.LENGTH_LONG).show();
            }
        });

        ExtractTimeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Extract time option",Toast.LENGTH_LONG).show();
            }
        });

    }
}
