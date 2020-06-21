package com.sparkwaker.beam.ui.fragments;

import android.Manifest;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.sparkwaker.beam.PermissionManager;
import com.sparkwaker.beam.R;
import com.sparkwaker.beam.models.AudioContent;
import com.sparkwaker.beam.ui.adapters.LibraryAdapter;
import com.sparkwaker.beam.ui.viewmodels.AudioLibraryViewModel;
import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class LibraryFragment extends Fragment {

    //region private methods
    private RecyclerView mRcSounds;
    private AudioLibraryViewModel mAudioLibraryViewModel;
    private LibraryAdapter mLibraryAdapter;
    private  static final int READ_EXTERNAL_STORAGE_REQUEST = 0x1045;
    private View mView;
    private TextView mTxtSoundName;
    private MaterialCardView mCardSoundPlaying;
    private ProgressBar mPBarLoading;
    private  MediaPlayer mPlayer;
    //endregion

    //region global listeners
    private PermissionManager.RequestPermissionListener  mRequestPermissionListener = new PermissionManager.RequestPermissionListener() {
        @Override
        public void onPermissionGranted() {
            configUI();
        }

        @Override
        public void onPermissionNotGranted() {
            Toast.makeText(requireActivity(), "Permiso 'WRITE_EXTERNAL_STORAGE' denegado", Toast.LENGTH_SHORT ).show();
            findNavController(LibraryFragment.this).popBackStack();
        }
    };

    private LibraryAdapter.SelectedAudioListener mSoundSelectedListener = soundSelected -> {
        playSound(soundSelected);
        updatePlayerBar(soundSelected);
    };
    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        PermissionManager.requestPermission(this,new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, READ_EXTERNAL_STORAGE_REQUEST, mRequestPermissionListener);

    }

    private void configUI(){

        mRcSounds = mView.findViewById(R.id.rcSounds);
        mTxtSoundName = mView.findViewById(R.id.txtSoundName);
        mPBarLoading = mView.findViewById(R.id.pBarLoading);
        mCardSoundPlaying = mView.findViewById(R.id.cardSoundPlaying);
        mCardSoundPlaying.setVisibility(View.GONE);
        mAudioLibraryViewModel = new ViewModelProvider(this).get(AudioLibraryViewModel.class);
        mPBarLoading.setVisibility(View.VISIBLE);
        mAudioLibraryViewModel.loadSounds();
        mLibraryAdapter = new LibraryAdapter(mSoundSelectedListener);
        mRcSounds.setAdapter(mLibraryAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRcSounds.setLayoutManager(layoutManager);
        registerObservers();

    }

    private void registerObservers(){

        mAudioLibraryViewModel.getSounds().observe(getViewLifecycleOwner(), sounds -> {
            mLibraryAdapter.submitList(sounds);
            mPBarLoading.setVisibility(View.GONE);
        });
    }

    private void updatePlayerBar(AudioContent audioItem){
         mTxtSoundName.setText(audioItem.getTitle());
         if(mCardSoundPlaying.getVisibility() == View.GONE)
             mCardSoundPlaying.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        if(mPlayer != null){
            if(mPlayer.isPlaying())
                mPlayer.stop();

            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void playSound(AudioContent currentSound){


        try{

           if(mPlayer !=null)
               mPlayer.reset();

            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
           /* mPlayer.setOnCompletionListener(SoundService.this);
            mPlayer.setOnPreparedListener(SoundService.this);
            mPlayer.setOnErrorListener(SoundService.this);
            mPlayer.setOnInfoListener(SoundService.this);
            mPlayer.setOnBufferingUpdateListener(SoundService.this);*/
            //mPlayer.setWakeMode(requireActivity(), PowerManager.PARTIAL_WAKE_LOCK);

            try{
                long currentSoundId = currentSound.getId();
                Uri trackUri = ContentUris.withAppendedId( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSoundId);
                //mSessionId = (mSessionId != -1)? mSessionId :mPlayer.getAudioSessionId();
               // mPlayer.setAudioSessionId(mSessionId);
                mPlayer.setDataSource(requireActivity(), trackUri);
                mPlayer.prepare();
                //Log.e("GETSESSIONID",mPlayer.getAudioSessionId() + "");

                mPlayer.start();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ERROR", e.getMessage() +"");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
