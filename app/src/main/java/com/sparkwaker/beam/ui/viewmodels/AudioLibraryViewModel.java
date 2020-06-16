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
import com.sparkwaker.beam.models.MediaStoreAudio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioLibraryViewModel extends AndroidViewModel {

    private MutableLiveData<List<MediaStoreAudio>> mSounds = new MutableLiveData<>();
    private ObserverWrapper observerWrapper = null;

    public AudioLibraryViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadSounds(){

       new LoadSoundsTask(new AudioStore(), sounds -> {
            mSounds.setValue(sounds);
       }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

       if (observerWrapper == null) {
           observerWrapper = new ObserverWrapper(getApplication(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                selfChange -> {
                loadSounds();
            });
       }

    }

    public LiveData<List<MediaStoreAudio>> getSounds(){
        return mSounds;
    }

    public class AudioStore{

        public List<MediaStoreAudio> querySounds() {

            List<MediaStoreAudio> sounds = new ArrayList<>();

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

                     MediaStoreAudio audio = new MediaStoreAudio(id,contentUri,displayName,size,null ,dateModified);
                     sounds.add(audio);
                }
                cursor.close();
             }
            return sounds;
        }

    }

    private static class LoadSoundsTask extends AsyncTask<Void, List<MediaStoreAudio>, List<MediaStoreAudio>> {

        private interface FinishedListener{
            void onFinished(List<MediaStoreAudio> sounds);
        }

        private AudioStore mAudioStore;
        private FinishedListener mCallback;

        LoadSoundsTask(AudioStore audioStore, FinishedListener callback){
            mAudioStore = audioStore;
            mCallback = callback;
        }

        @Override
        protected List<MediaStoreAudio> doInBackground(Void... voids) {
            return mAudioStore.querySounds();
        }

        @Override
        protected void onPostExecute(List<MediaStoreAudio> sounds) {
            mCallback.onFinished(sounds);
        }
    }

    private static class ObserverWrapper extends ContentObserver {

        private changeListener mCallback;

        interface changeListener {
            void onChange(boolean selfChange);
        }

        ObserverWrapper(Application application, Uri uri, changeListener callback ) {
           super(new Handler());
           mCallback = callback;
           application.getContentResolver().registerContentObserver(uri,true, this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mCallback.onChange(selfChange);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        getApplication().getContentResolver().unregisterContentObserver(observerWrapper);
    }
}
