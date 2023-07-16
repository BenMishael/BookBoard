package com.example.bookboard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookboard.Model.Room;
import com.example.bookboard.R;
import com.example.bookboard.Utilities.FireBaseOperations;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private ArrayList<Room> rooms;
    private FireBaseOperations fireBaseOperations;
    private RoomClickListener listener;

    public RoomAdapter(Context context) {
        this.rooms = rooms;
        rooms = new ArrayList<>();
        fireBaseOperations = FireBaseOperations.getInstance();
    }

    public void updateRooms(final ArrayList<Room> rooms){
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        RoomViewHolder roomViewHolder = new RoomViewHolder(view);
        return roomViewHolder;
    }

    public RoomAdapter setRoomClickListener(RoomAdapter.RoomClickListener roomClickListener){
        this.listener = roomClickListener;
        return this;
    }

    public interface RoomClickListener{
        void openRoomWeekView(Room room,int position);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = getItem(position);
        holder.room_LBL_title.setText(room.getTitle());
        holder.room_LBL_max.setText("Max People: " + room.getMaxPeople());
        holder.room_LBL_describe.setText(room.getDescribe());
        holder.room_LBL_available.setText(room.getAvailable());
        holder.room_LTV_poster.setAnimation(room.getPosterLottieID());
    }

    @Override
    public int getItemCount() {
        return rooms == null ? 0 : rooms.size();
    }

    private Room getItem(int position) {
        return rooms.get(position);
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private LottieAnimationView room_LTV_poster;
        private LottieAnimationView room_LTV_button;
        private MaterialTextView room_LBL_available;
        private MaterialTextView room_LBL_describe;
        private MaterialTextView room_LBL_max;
        private MaterialTextView room_LBL_title;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            room_LTV_poster = itemView.findViewById(R.id.room_LTV_poster);
            room_LTV_button = itemView.findViewById(R.id.room_LTV_button);
            room_LBL_available = itemView.findViewById(R.id.room_LBL_available);
            room_LBL_describe = itemView.findViewById(R.id.room_LBL_describe);
            room_LBL_max = itemView.findViewById(R.id.room_LBL_max);
            room_LBL_title = itemView.findViewById(R.id.room_LBL_title);
        }
    }
}
