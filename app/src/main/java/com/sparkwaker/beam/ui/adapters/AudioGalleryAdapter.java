package com.sparkwaker.beam.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sparkwaker.beam.R;
import com.sparkwaker.beam.models.MediaStoreAudio;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class  AudioGalleryAdapter extends ListAdapter<MediaStoreAudio, AudioGalleryAdapter.ItemSoundViewHolder> {

    private View.OnClickListener mOnClickListener;

    public AudioGalleryAdapter(View.OnClickListener onClickListener){
        super(DIFF_CALLBACK);
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sound_item,parent, false);
        return new ItemSoundViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSoundViewHolder holder, int position) {
        holder.getRootView().setTag(getItem(position));
        holder.onBind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<MediaStoreAudio> DIFF_CALLBACK =

            new DiffUtil.ItemCallback<MediaStoreAudio>() {
                @Override
                public boolean areItemsTheSame(@NonNull MediaStoreAudio oldSound, @NonNull MediaStoreAudio newSound) {
                    return oldSound.getId() == newSound.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull MediaStoreAudio oldSound, @NonNull MediaStoreAudio newSound) {
                    return oldSound.equals(newSound);
                }
            };

    static class ItemSoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImgSoundItem;
        private TextView mTxtTittle;
        private TextView mTxtSize;
        private View rootView;
        private View.OnClickListener mOnClickListener;

        View getRootView() {
            return rootView;
        }

        ItemSoundViewHolder(@NonNull View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            rootView = itemView;
            mImgSoundItem = itemView.findViewById(R.id.imgSoundItem);
            mTxtTittle = itemView.findViewById(R.id.txtSoundItemTitle);
            mTxtSize = itemView.findViewById(R.id.txtSoundItemSize);
            rootView.setOnClickListener(this);
            mOnClickListener = onClickListener;
        }

        void onBind(MediaStoreAudio sound) {
            mTxtTittle.setText(sound.getTitle());
            mTxtSize.setText(String.valueOf(sound.getSize()));
        }

        @Override
        public void onClick(View v) {
            if (rootView.getTag() instanceof MediaStoreAudio)
                 mOnClickListener.onClick(rootView);
        }
    }
}
