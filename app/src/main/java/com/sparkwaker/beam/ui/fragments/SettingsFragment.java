package com.sparkwaker.beam.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sparkwaker.beam.R;
import com.sparkwaker.beam.helpers.BeamUtils;
import com.sparkwaker.beam.ui.fragments.dialogs.AppRateDialog;
import com.sparkwaker.beam.ui.fragments.dialogs.AudioFormatDialog;
import com.sparkwaker.beam.ui.fragments.dialogs.ExtractTimeDialog;
import com.sparkwaker.beam.ui.fragments.dialogs.SoundQualityDialog;


public class SettingsFragment extends Fragment {
    private LinearLayout RateAppOption;
    private LinearLayout SoundQualityOption;
    private LinearLayout AudioFormatOption;
    private LinearLayout ExtractTimeOption;
    private TextView AppVersionNumber;
    private LinearLayout UpdateApp;
    private LinearLayout ClearAppCache;
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
    private  void showDialog(Object dialogObject, String tag){

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = (DialogFragment) dialogObject;
        newFragment.show(ft, tag);
    }
    private void configUiGeneral (){
        RateAppOption = mView.findViewById(R.id.layoutRateApp);
        SoundQualityOption =mView.findViewById(R.id.layoutSoundQuality);
        AudioFormatOption = mView.findViewById(R.id.layoutAudioFormat);
        ExtractTimeOption = mView.findViewById(R.id.layoutExtractTime);
        AppVersionNumber = mView.findViewById(R.id.txtAppVersion);
        UpdateApp = mView.findViewById(R.id.layoutUpdateApp);
        ClearAppCache = mView.findViewById(R.id.layoutClearCache);

        AppVersionNumber.setText(BeamUtils.getAppVersion(requireActivity()));



        RateAppOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog(new AppRateDialog(), "AppRateDialog");

            }
        });
        SoundQualityOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(new SoundQualityDialog(), "SoundQualityDialog");

            }
        });

        AudioFormatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(new AudioFormatDialog(), "AudioFormatDialog");
            }
        });

        ExtractTimeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(new ExtractTimeDialog(), "ExtractTimeDialog");
            }
        });

        UpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeamUtils.redirectToAppPlayStore(requireActivity());
            }
        });

        ClearAppCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeamUtils.clearAppCache(requireActivity());
            }
        });

    }
}
