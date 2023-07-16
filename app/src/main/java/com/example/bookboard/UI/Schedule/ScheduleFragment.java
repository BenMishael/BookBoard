package com.example.bookboard.UI.Schedule;

import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookboard.Fragments.RoomsFragment;
import com.example.bookboard.R;
import com.example.bookboard.UI.Reservations.ReservationsViewModel;
import com.example.bookboard.databinding.FragmentScheduleBinding;


public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReservationsViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationsViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        textView.setVisibility(INVISIBLE);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Replace the current fragment with RoomsFragment
        RoomsFragment roomsFragment = new RoomsFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, roomsFragment)
                .addToBackStack(null)
                .commit();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}