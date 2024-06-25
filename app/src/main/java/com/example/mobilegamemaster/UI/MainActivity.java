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
    Repository repository;

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

        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        repository = new Repository(getApplication());

        List<Room> allRooms = repository.getmAllRooms();

        final RoomAdapter roomAdapter = new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addroom) {
            Intent intent = new Intent(MainActivity.this, AddRoom.class);
            startActivity(intent);
        }
//TODO: complete start screen menu options
        if (item.getItemId() == R.id.editdeleteroom) {
            return true;
        }

        if (item.getItemId() == R.id.reportlogs) {
            return true;
        }

        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }

        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        List<Room> allRooms=repository.getmAllRooms();
        RecyclerView recyclerView=findViewById(R.id.chooseGameRecyclerView);

        final RoomAdapter roomAdapter=new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);

    }
}