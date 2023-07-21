package com.example.bookboard.UI.Rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Adapters.RoomAdapter;
import com.example.bookboard.Model.Room;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.Constants;
import com.example.bookboard.Utilities.DataManager;
import com.example.bookboard.databinding.FragmentRoomsBinding;

public class RoomsFragment extends Fragment {

    private RoomAdapter roomsAdapter;
    private FragmentRoomsBinding binding;
    private RecyclerView roomsRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        RoomsViewModel reservationViewModel =
                new ViewModelProvider(this).get(RoomsViewModel.class);
        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        roomsRV = binding.roomsLSTAllrooms;
        roomsAdapter = new RoomAdapter(getContext());
        roomsAdapter.updateRooms(DataManager.getRooms());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        roomsRV.setLayoutManager(linearLayoutManager);
        roomsRV.setAdapter(roomsAdapter);
        roomsAdapter.setRoomClickListener(new RoomAdapter.RoomClickListener() {
            @Override
            public void changeScreen(Room room) {
                String roomID = room.getRoomID();
                Bundle args = new Bundle();
                args.putString(Constants.ARGS_CATEGORYID, roomID);
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_calendar,args);

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

