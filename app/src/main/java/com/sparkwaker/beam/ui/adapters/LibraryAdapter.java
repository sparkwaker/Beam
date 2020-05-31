package com.sparkwaker.beam.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sparkwaker.beam.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<String> mSounds;

    public LibraryAdapter(Context mContext, ArrayList<String> sounds){
        this.mContext = mContext;
        this.mSounds = new ArrayList<>();
        this.mSounds.add("Patchouli le parte la madre a Marisa");
        this.mSounds.add("Cato se la Come");
        this.mSounds.add("Gatos Galacticos");
        this.mSounds.add("Perros SAlvajes 100% real no feik");

    }



    class ItemSoundViewHolder extends RecyclerView.ViewHolder{
        ImageView mImgSoundItem;
        TextView mTxtTittle;
        TextView mTxtSize;

        public ItemSoundViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgSoundItem = itemView.findViewById(R.id.imgSoundItem);
            mTxtTittle = itemView.findViewById(R.id.txtSoundItemTitle);
            mTxtSize = itemView.findViewById(R.id.txtSoundItemSize);

        }


        public void onBind(int position) {
            mTxtTittle.setText(mSounds.get(position));
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sound_item,parent, false);



        return new ItemSoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemSoundViewHolder)holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return mSounds.size();
    }
}
