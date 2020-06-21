package com.sparkwaker.beam.ui.viewmodels;

import android.app.Application;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.sparkwaker.beam.models.AudioContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioLibraryViewModel extends AndroidViewModel {

    private MutableLiveData<List<AudioContent>> mSounds = new MutableLiveData<>();
    private ObserverWrapper observerWrapper = null;

    public AudioLibraryViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadSounds(){

         LoadSoundsTask soundTask = new LoadSoundsTask(new AudioStore(), sounds -> {
             mSounds.postValue(sounds);
         });

         soundTask.execute();

         if (observerWrapper == null) {
           observerWrapper = new ObserverWrapper(getApplication(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new ObserverWrapper.changeListener() {
               @Override
               public void onChange(boolean selfChange, Uri uri) {
                   loadSounds();
               }
           });
         }
    }

    public LiveData<List<AudioContent>> getSounds(){
        return mSounds;
    }

    public class AudioStore{

        public List<AudioContent> querySounds() {

            List<AudioContent> sounds = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED
            };

            String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

            Cursor cursor = getApplication().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
            );

            if (cursor != null){

                int  idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int  dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
                int  displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int  sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);

                while (cursor.moveToNext()) {

                     long id =  cursor.getLong(idColumn);
                     String dateModified =  new Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn))).toString();
                     String displayName =    cursor.getString(displayNameColumn);
                     String size = cursor.getString(sizeColumn);

                     Uri contentUri = ContentUris.withAppendedId(
                             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                     );

                     AudioContent audio = new AudioContent(id,contentUri,displayName,size,null ,dateModified);
                     sounds.add(audio);
                }
                cursor.close();
             }
            return sounds;
        }

    }

    private static class LoadSoundsTask extends AsyncTask<Void, List<AudioContent>, List<AudioContent>> {

        private interface FinishedListener{
            void onFinished(List<AudioContent> sounds);
        }

        private AudioStore mAudioStore;
        private FinishedListener mCallback;

        LoadSoundsTask(AudioStore audioStore, FinishedListener callback){
            mAudioStore = audioStore;
            mCallback = callback;
        }

        @Override
        protected List<AudioContent> doInBackground(Void... voids) {
            return mAudioStore.querySounds();
        }

        @Override
        protected void onPostExecute(List<AudioContent> sounds) {
            mCallback.onFinished(sounds);
        }
    }

    private static class ObserverWrapper extends ContentObserver {

        private changeListener mCallback;

        interface changeListener {
            void onChange(boolean selfChange, Uri uri);
        }

        ObserverWrapper(Application application, Uri uri, changeListener callback ) {
           super(new Handler());
           mCallback = callback;
           application.getContentResolver().registerContentObserver(uri,true, this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            mCallback.onChange(selfChange,uri);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        getApplication().getContentResolver().unregisterContentObserver(observerWrapper);
    }
}
