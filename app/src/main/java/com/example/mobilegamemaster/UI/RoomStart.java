package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilegamemaster.R;

public class RoomStart extends AppCompatActivity {
    //CREATE ROOM ID AND ROOM NAME VARIABLES, AND ROOM NAME TEXT VIEWS
    int roomID;
    String roomName;
    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE ROOM ID AND ROOM NAME; SET ROOM NAME TO TEXT VIEW
        roomID = getIntent().getIntExtra("id", -1);
        roomName = getIntent().getStringExtra("name");
        nameView = findViewById(R.id.roomNameView);
        nameView.setText(roomName);

        //CREATE START BUTTON AND SET ONCLICK LISTENER
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoomStart.this, RoomPuzzles.class);
            intent.putExtra("name", roomName);
            intent.putExtra("id", roomID);
            intent.putExtra("puzzle_num", 0);
            startActivity(intent);
        });
    }
}