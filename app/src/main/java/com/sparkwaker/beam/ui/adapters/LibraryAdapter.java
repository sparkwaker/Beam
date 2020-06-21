package com.sparkwaker.beam.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sparkwaker.beam.R;
import com.sparkwaker.beam.models.AudioContent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryAdapter extends ListAdapter<AudioContent, LibraryAdapter.ItemSoundViewHolder> {

    private SelectedAudioListener mSelectedAudioListener;

    public interface SelectedAudioListener{
        void onAudioSelected(AudioContent audioSelected);
    }

    public LibraryAdapter(SelectedAudioListener selectedAudioListener){
        super(DIFF_CALLBACK);
        mSelectedAudioListener = selectedAudioListener;
    }

    @NonNull
    @Override
    public ItemSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sound_item,parent, false);
        return new ItemSoundViewHolder(view, mSelectedAudioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSoundViewHolder holder, int position) {
        holder.getRootView().setTag(getItem(position));
        holder.onBind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<AudioContent> DIFF_CALLBACK =

            new DiffUtil.ItemCallback<AudioContent>() {
                @Override
                public boolean areItemsTheSame(@NonNull AudioContent oldSound, @NonNull AudioContent newSound) {
                    return oldSound.getId() == newSound.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull AudioContent oldSound, @NonNull AudioContent newSound) {
                    return oldSound.equals(newSound);
                }
            };

    static class ItemSoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTxtTittle;
        private TextView mTxtSize;
        private View rootView;
        private SelectedAudioListener mSelectedAudioListener;

        View getRootView() {
            return rootView;
        }

        ItemSoundViewHolder(@NonNull View itemView, SelectedAudioListener selectedAudioListener) {
            super(itemView);
            rootView = itemView;
            mTxtTittle = itemView.findViewById(R.id.txtSoundItemTitle);
            mTxtSize = itemView.findViewById(R.id.txtSoundItemSize);
            rootView.setOnClickListener(this);
            mSelectedAudioListener = selectedAudioListener;
        }

        void onBind(AudioContent sound) {
            mTxtTittle.setText(sound.getTitle());
            mTxtSize.setText(String.valueOf(sound.getSize()));
        }

        @Override
        public void onClick(View v) {
            if (rootView.getTag() instanceof AudioContent)
                mSelectedAudioListener.onAudioSelected(((AudioContent)rootView.getTag()));
        }
    }
}
