package com.sparkwaker.beam.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.sparkwaker.beam.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class HomeFragment extends Fragment {

    private MaterialCardView mCardMyLibrary;
    private MaterialCardView mCardTrimSound;
    private MaterialCardView mCardSettings;
    private MaterialCardView mCardSetRingtone;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configUI(view);
    }

    private void configUI(View view){

        // Retrieve user interface controls
        mCardTrimSound = view.findViewById(R.id.cardTrimSound);
        mCardSetRingtone = view.findViewById(R.id.cardSetRingtone);
        mCardMyLibrary = view.findViewById(R.id.cardMyLibrary);
        mCardSettings = view.findViewById(R.id.cardSettings);

        // Set listeners
        mCardTrimSound.setOnClickListener(v -> Toast.makeText(requireContext(), "Trim sound option", Toast.LENGTH_SHORT).show());
        mCardSetRingtone.setOnClickListener(v -> Toast.makeText(requireContext(), "Set ringtone option", Toast.LENGTH_SHORT).show());
        mCardMyLibrary.setOnClickListener(v -> findNavController(this).navigate(R.id.navigate_to_library));
        mCardSettings.setOnClickListener(v -> findNavController(this).navigate(R.id.navigate_to_settings));

    }

   /* void showDialog() {

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = new AppRateDialog();
        newFragment.show(ft, "dialog");
    }*/

}
