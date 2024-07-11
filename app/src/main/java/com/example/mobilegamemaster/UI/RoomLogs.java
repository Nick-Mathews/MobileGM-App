package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.Entities.Timer;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RoomLogs extends AppCompatActivity {
    //CREATE REPOSITORY AND LIST OF TIMERS
    Repository repository;
    List<Timer> allTimers, roomTimers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_logs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //POPULATE REPOSITORY AND TIMER LIST; SET TIMER LIST ON ADAPTER
        repository = new Repository(getApplication());
        allTimers = repository.getmAllTimers();
        final LogAdapter logAdapter = new LogAdapter(this);
        logAdapter.setmTimers(allTimers);

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
        RecyclerView recyclerView = findViewById(R.id.gameLogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(logAdapter);

        //CREATE SEARCH BUTTON AND SET ONCLICK LISTENER
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            int roomID = -1;
            EditText searchEditText = findViewById(R.id.searchEditText);
            String searchTerm = searchEditText.getText().toString();
            for (Room current: repository.getmAllRooms()) {
                if (current.getRoomName().toLowerCase().contains(searchTerm.toLowerCase()) && !searchTerm.isEmpty()){
                    roomID = current.getRoomID();
                }
            }
            if (roomID != -1) {
                roomTimers = repository.getmTimers(roomID);
                logAdapter.setmTimers(roomTimers);
                recyclerView.setAdapter(logAdapter);
            }
            else {
                logAdapter.setmTimers(allTimers);
                recyclerView.setAdapter(logAdapter);
            }
        });

        FloatingActionButton backButton = findViewById(R.id.floating_back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoomLogs.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_logs, menu);
        return true;
    }

    //ADMIN MENU FOR DELETING ALL LOGS
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.delete_logs) {
            for (Timer timer: allTimers) {
                try {
                    repository.delete(timer);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
            //REFRESH RECYCLERVIEW AFTER DELETING LOGS
            allTimers = repository.getmAllTimers();
            final LogAdapter logAdapter = new LogAdapter(this);
            logAdapter.setmTimers(allTimers);

            //SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
            RecyclerView recyclerView = findViewById(R.id.gameLogRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(logAdapter);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }
}