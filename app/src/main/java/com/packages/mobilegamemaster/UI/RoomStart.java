package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.packages.mobilegamemaster.database.Repository;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RoomStart extends AppCompatActivity {
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
        //SET REPOSITORY AND PROGRESS BAR
        Repository repository = new Repository(getApplication());
        ProgressBar pgBar = findViewById(R.id.progressBar);
        NumberFormat f = new DecimalFormat("00");

        //POPULATE ROOM ID AND ROOM NAME; SET ROOM NAME AND TIMER TO TEXT VIEW
        int roomID = getIntent().getIntExtra("id", -1);
        String roomName = getIntent().getStringExtra("name");
        int roomTimer = getIntent().getIntExtra("timer", -1);
        TextView nameView = findViewById(R.id.roomNameView);
        nameView.setText(roomName);
        TextView timerView = findViewById(R.id.countdownTimerView);
        long min = (roomTimer / 60000);
        long sec = (roomTimer / 1000) % 60;
        String timerText = f.format(min) + ":" + f.format(sec);
        timerView.setText(timerText);

        //CREATE START BUTTON AND SET ONCLICK LISTENER
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            if(repository.getmRoomPuzzles(roomID).isEmpty()){
                Toast msg = Toast.makeText(this, "Room must contain at least 1 puzzle", Toast.LENGTH_LONG);
                msg.show();
                pgBar.setVisibility(View.INVISIBLE);
            }
            else {
                Intent intent = new Intent(RoomStart.this, RoomPuzzles.class);
                intent.putExtra("name", roomName);
                intent.putExtra("id", roomID);
                intent.putExtra("timer", roomTimer);
                intent.putExtra("puzzle_num", 0);
                startActivity(intent);
            }
        });

        FloatingActionButton backButton = findViewById(R.id.floating_back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}