package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;
import com.example.mobilegamemaster.Entities.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //CREATE REPOSITORY, ROOM LIST
    Repository repository;
    List<Room> allRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //POPULATE REPOSITORY, SET ROOM LIST, AND ADD ROOM LIST TO THE ADAPTER
        repository = new Repository(getApplication());
        allRooms = repository.getmAllRooms();
        final RoomAdapter roomAdapter = new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);


        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    //ADMIN MENU OPTIONS FOR ADDING, EDITING OR DELETING ROOMS AND PUZZLES AND ACCESS TO THE LOG TABLES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_room) {
            Intent intent = new Intent(MainActivity.this, AddRoom.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.edit_delete_room) {
            Intent intent = new Intent(MainActivity.this, RoomList.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.report_logs) {
            Intent intent = new Intent(MainActivity.this, RoomLogs.class);
            startActivity(intent);
        }

        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }

        return true;
    }

    //UPDATES LIST OF ROOMS AND RECYCLERVIEW WHEN RETURNING FROM ADMIN FUNCTIONS
    @Override
    protected void onResume(){
        super.onResume();
        //SET ROOM LIST AND ADD ROOM LIST TO THE ADAPTER
        allRooms = repository.getmAllRooms();
        final RoomAdapter roomAdapter = new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);

    }
}