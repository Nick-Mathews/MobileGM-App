package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;

import java.util.List;

public class RoomList extends AppCompatActivity {
    //CREATE REPOSITORY AND ROOM LIST
    Repository repository;
    List<Room> allRooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY, AND POPULATE ROOMS LIST
        repository = new Repository(getApplication());
        allRooms = repository.getmAllRooms();

        //SET ROOM LIST ON ADAPTER; SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
        RecyclerView recyclerView = findViewById(R.id.roomListRecyclerView);
        final EditRoomAdapter editRoomAdapter = new EditRoomAdapter(this);
        editRoomAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editRoomAdapter);

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoomList.this, MainActivity.class);
            startActivity(intent);
        });
    }

    //ON RESUME FUNCTION THAT REPOPULATES AND REFRESHES THE ADAPTER
    @Override
    protected void onResume(){
        super.onResume();
        List<Room> allRooms=repository.getmAllRooms();
        RecyclerView recyclerView=findViewById(R.id.roomListRecyclerView);
        final EditRoomAdapter editRoomAdapter = new EditRoomAdapter(this);
        editRoomAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editRoomAdapter);

    }
}