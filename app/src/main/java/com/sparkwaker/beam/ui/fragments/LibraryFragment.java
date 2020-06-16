package com.sparkwaker.beam.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sparkwaker.beam.R;
import com.sparkwaker.beam.models.MediaStoreAudio;
import com.sparkwaker.beam.ui.adapters.AudioGalleryAdapter;
import com.sparkwaker.beam.ui.viewmodels.AudioLibraryViewModel;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LibraryFragment extends Fragment {

    private RecyclerView mRcSounds;
    private AudioLibraryViewModel mAudioLibraryViewModel;
    private AudioGalleryAdapter mLibraryAdapter;
    private  static final int READ_EXTERNAL_STORAGE_REQUEST = 0x1045;
    private View mView;

    private boolean haveStoragePermission(){
        return   ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
    }

    public void requestPermission() {
        if (!haveStoragePermission()) {
            String[] permissions = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(requireActivity(), permissions, READ_EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean showRationale;
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST:

                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      configUI();
                }  else {
                    showRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                    if(showRationale){
                        Toast.makeText(requireActivity(),"Habilitar permisos" , Toast.LENGTH_LONG).show();
                    }
                }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        if(haveStoragePermission()){
            configUI();
        }else {
            requestPermission();
        }
    }

    private void configUI(){

        mRcSounds = mView.findViewById(R.id.rcSounds);
        mAudioLibraryViewModel = new ViewModelProvider(requireActivity()).get(AudioLibraryViewModel.class);
        mAudioLibraryViewModel.loadSounds();

        mLibraryAdapter = new AudioGalleryAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),((MediaStoreAudio)v.getTag()).getTitle(),Toast.LENGTH_LONG).show();
            }
        });
        mAudioLibraryViewModel.getSounds().observe(getViewLifecycleOwner(), (Observer<? super List<MediaStoreAudio>>) v -> {
             Toast.makeText(getContext(),"Hay nuevos sonidos en la lista",Toast.LENGTH_LONG).show();
             mLibraryAdapter.submitList(v);
        });

        mRcSounds.setAdapter(mLibraryAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRcSounds.setLayoutManager(layoutManager);
        mRcSounds.setHasFixedSize(true);

    }

}
