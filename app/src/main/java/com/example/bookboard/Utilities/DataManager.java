package com.example.bookboard.Utilities;

import com.example.bookboard.Model.Room;
import com.example.bookboard.R;

import java.util.ArrayList;

public class DataManager {

        public static ArrayList<Room> getRooms() {
            ArrayList<Room> rooms = new ArrayList<>();

            Room room1 = new Room("Room 1", R.raw.lottie_workspace_1, 3, "Room 1 Description", "Available");
            rooms.add(room1);

            Room room2 = new Room("Room 2", R.raw.lottie_workspace_2, 3, "Room 2 Description", "Available");
            rooms.add(room2);

            Room room3 = new Room("Room 3", R.raw.lottie_workspace_3, 3, "Room 3 Description", "Available");
            rooms.add(room3);

            Room room4 = new Room("Room 4", R.raw.lottie_workspace_4, 3, "Room 4 Description", "Available");
            rooms.add(room4);

            Room room5 = new Room("Room 5", R.raw.lottie_workspace_5, 3, "Room 5 Description", "Available");
            rooms.add(room5);

            Room room6 = new Room("Room 6", R.raw.lottie_workspace_6, 3, "Room 6 Description", "Available");
            rooms.add(room6);

            return rooms;
        }





    /*
    lottie_bookboard_logo.json = IfJheVpRs4CQbtcmMYGB
    lottie_businessman.json = G4AnB3YhTcdCXSsI6WDu
    lottie_loading.json = RyLJUX57hI6kQNaZY8KG
    lottie_loadingcircle_end.json = bD7QqvaTR2c4Lr5dwMVh
    lottie_loadingcircle_start.json = drsVQwaLWE36xKh7MTJA
    lottie_nfc.json = dFTtDwaNrB3LzXp9kuxf
    lottie_reserve_button.json = hZRtTNXvYux9z5kCKqJI
    lottie_workspace_1.json = 38MpDSH2QFnkjfmZhzPb
    lottie_workspace_2.json = Gkg8xZXLNcyu2FEaRAHS
    lottie_workspace_3.json = a5BVdhMgSeQ3qIRfJLsj
    lottie_workspace_4.json = ZbrxDwshPpz5gSQtmRMj
    lottie_workspace_5.json = NhBLjz5WwetTGdfcEPZp
    lottie_workspace_6.json = Z9x4zRB2Vbcs7trumIw5
    Room 1 38MpDSH2QFnkjfmZhzPb
    Room 2 Gkg8xZXLNcyu2FEaRAHS
    Room 3 a5BVdhMgSeQ3qIRfJLsj
    Room 4 ZbrxDwshPpz5gSQtmRMj
    Room 5 NhBLjz5WwetTGdfcEPZp
    Room 6 Z9x4zRB2Vbcs7trumIw5
     */
}
