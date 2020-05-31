package com.sparkwaker.beam.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparkwaker.beam.R;
import com.sparkwaker.beam.ui.adapters.LibraryAdapter;

public class LibraryFragment extends Fragment {
    RecyclerView mRcSounds;

    public LibraryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcSounds = view.findViewById(R.id.rcSounds);

        LibraryAdapter libraryAdapter = new LibraryAdapter(getActivity(), null);
        mRcSounds.setAdapter(libraryAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRcSounds.setLayoutManager(layoutManager);
        mRcSounds.setHasFixedSize(true);
    }
}
