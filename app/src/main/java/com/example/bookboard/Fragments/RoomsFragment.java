package com.example.bookboard.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Adapters.RoomAdapter;
import com.example.bookboard.Model.Room;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.Constants;
import com.example.bookboard.Utilities.DataManager;
import com.example.bookboard.databinding.FragmentRoomsBinding;

public class RoomsFragment extends Fragment {

    private RoomAdapter roomAdapter;
    private FragmentRoomsBinding binding;
    private RecyclerView roomsRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        roomsRV = binding.mainLSTRooms;
        roomAdapter = new RoomAdapter(getContext());
        roomAdapter.updateRooms(DataManager.getRooms());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        roomsRV.setLayoutManager(linearLayoutManager);
        roomsRV.setAdapter(roomAdapter);
        roomAdapter.setRoomClickListener(new RoomAdapter.RoomClickListener() {
            @Override
            public void openRoomWeekView(Room room, int position) {
                String roomID = room.getTitle();
                Bundle args = new Bundle();
                args.putString(Constants.ARGS_ROOMID, roomID);

                // Create an instance of the WeekViewFragment
                WeekViewFragment weekViewFragment = new WeekViewFragment();
                weekViewFragment.setArguments(args);

                // Replace the current fragment with the WeekViewFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, weekViewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

