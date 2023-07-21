package com.example.bookboard.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bookboard.Model.Reservation;
import com.example.bookboard.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private Context context;
    private ArrayList<Reservation> reservations;
    private ReservationClickListener listener;

    public ReservationAdapter(Context context) {
        this.context = context;
        this.reservations = new ArrayList<>(); // Initialize the reservations ArrayList
    }


    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        ReservationViewHolder reservationViewHolder = new ReservationViewHolder(view);
        return reservationViewHolder;
    }

    public ReservationAdapter setReservationClickListener(ReservationClickListener reservationClickListener) {
        this.listener = reservationClickListener;
        return this;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = getReservation(position);
        holder.reservation_LBL_date.setText("Date: "+ reservation.getDate());
        holder.reservation_LBL_start.setText("Start: "+reservation.getStartTime());
        holder.reservation_LBL_end.setText("End: "+reservation.getEndTime());
        holder.reservation_LBL_roomname.setText(reservation.getRoomName());
    }

    @Override
    public int getItemCount() {
        return reservations == null ? 0 : reservations.size();
    }

    private Reservation getReservation(int position) {
        return reservations.get(position);
    }
    public void updateReservations(final ArrayList<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    public interface ReservationClickListener{
        void changeScreen(Reservation reservation);
    }


    class ReservationViewHolder extends RecyclerView.ViewHolder {
        private LottieAnimationView reservation_LTV_poster;
        private LottieAnimationView reservation_LTV_button;
        private MaterialTextView reservation_LBL_roomname;
        private MaterialTextView reservation_LBL_date;
        private MaterialTextView reservation_LBL_end;
        private MaterialTextView reservation_LBL_start;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            reservation_LTV_poster = itemView.findViewById(R.id.reservation_LTV_poster);
            reservation_LTV_button = itemView.findViewById(R.id.reservation_LTV_button);
            reservation_LBL_roomname = itemView.findViewById(R.id.reservation_LBL_roomname);
            reservation_LBL_date = itemView.findViewById(R.id.reservation_LBL_date);
            reservation_LBL_start = itemView.findViewById(R.id.reservation_LBL_start);
            reservation_LBL_end = itemView.findViewById(R.id.reservation_LBL_end);
            reservation_LTV_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.changeScreen(reservations.get(position));
                }
            });
        }
    }
}
