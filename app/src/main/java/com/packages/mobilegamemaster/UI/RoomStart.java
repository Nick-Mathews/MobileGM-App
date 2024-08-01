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

        //POPULATE ROOM ID AND ROOM NAME; SET ROOM NAME TO TEXT VIEW
        int roomID = getIntent().getIntExtra("id", -1);
        String roomName = getIntent().getStringExtra("name");
        TextView nameView = findViewById(R.id.roomNameView);
        nameView.setText(roomName);

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
                intent.putExtra("puzzle_num", 0);
                startActivity(intent);
            }
        });

        FloatingActionButton backButton = findViewById(R.id.floating_back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}