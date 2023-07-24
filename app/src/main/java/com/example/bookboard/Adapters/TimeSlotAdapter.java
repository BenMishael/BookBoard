package com.example.bookboard.Adapters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookboard.Model.TimeSlot;
import com.example.bookboard.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.SlotViewHolder> {

    private List<TimeSlot> timeSlots;
    private SlotClickListener slotClickListener;

    public TimeSlotAdapter(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void setSlotClickListener(SlotClickListener slotClickListener) {
        this.slotClickListener = slotClickListener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false);
        return new SlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);
        holder.bind(timeSlot);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void updateSlots(List<TimeSlot> updatedTimeSlots) {
        this.timeSlots = updatedTimeSlots;
        notifyDataSetChanged();
    }

    class SlotViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView slotTimeTextView;
        private MaterialTextView slotTitleTextView;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            slotTimeTextView = itemView.findViewById(R.id.slotTimeTextView);
            slotTitleTextView = itemView.findViewById(R.id.slotTitleTextView);
        }

        public void bind(TimeSlot timeSlot) {
            slotTimeTextView.setText(timeSlot.getStartTime() + " - " + timeSlot.getEndTime());
            String slotTitle = timeSlot.getTitle();

            if (TextUtils.isEmpty(slotTitle)) {
                // If there is no event, set the background color to white
                itemView.setBackgroundColor(Color.WHITE);
                slotTitleTextView.setText("");
                slotTimeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                slotTitleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                itemView.setOnClickListener(null); // Disable click on empty slots
            } else {
                slotTitleTextView.setText(slotTitle);

                if (slotTitle.equals("Available")) {
                    // If it's an available slot, set the background color to white
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    slotTimeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                    slotTitleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                    itemView.setOnClickListener(v -> {
                        if (slotClickListener != null) {
                            slotClickListener.onSlotClick(timeSlots.get(getAdapterPosition()));
                        }
                    });
                } else if (slotTitle.equals(getUserEmail())) {
                    // If it's the user's booking, set the background color to blue
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                    slotTimeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    slotTitleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    itemView.setOnClickListener(null); // Disable click on user's booking
                } else {
                    // If it's a reserved slot (not booked by the user), set the background color to gray
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray_500));
                    slotTimeTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    slotTitleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                    itemView.setOnClickListener(v -> {
                        // Handle click on event not booked by the user if needed
                        // For example, show more details or perform an action
                        if (slotClickListener != null) {
                            slotClickListener.onSlotClick(timeSlots.get(getAdapterPosition()));
                        }
                    });
                }
            }
        }

        private String getUserEmail() {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                return currentUser.getEmail();
            }
            return "";
        }
    }

    public interface SlotClickListener {
        void onSlotClick(TimeSlot timeSlot);
    }
}
