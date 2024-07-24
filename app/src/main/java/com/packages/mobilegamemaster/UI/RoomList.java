package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.util.List;

public class RoomList extends AppCompatActivity {
    //CREATE REPOSITORY AND ROOM LIST
    Repository repository;
    List<Room> allRooms;
    RecyclerView recyclerView;
    Button finishButton;
    ProgressBar pgBar;
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
        recyclerView = findViewById(R.id.roomListRecyclerView);
        final EditRoomAdapter editRoomAdapter = new EditRoomAdapter(this);
        editRoomAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editRoomAdapter);

        //SET PROGRESS BAR VIEW
        pgBar = findViewById(R.id.progressBar);

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(RoomList.this, AdminMenu.class);
            startActivity(intent);
        });
        }
    }

    //ON RESUME FUNCTION THAT REPOPULATES AND REFRESHES THE ADAPTER
   /* @Override
    protected void onResume(){
        super.onResume();
        allRooms = repository.getmAllRooms();
        recyclerView = findViewById(R.id.roomListRecyclerView);
        final EditRoomAdapter editRoomAdapter = new EditRoomAdapter(this);
        editRoomAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editRoomAdapter);

    }*/
