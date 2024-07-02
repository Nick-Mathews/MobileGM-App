package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilegamemaster.Entities.Timer;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;

import java.util.List;

public class RoomLoss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_loss);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //CREATE REPOSITORY AND GET ALL TIMERS
        Repository repository = new Repository(getApplication());
        List<Timer> allTimers = repository.getmAllTimers();

        //GET INTENT EXTRAS AND SETUP VARIABLES TO CREATE A TIMER
        String timeLeft = getIntent().getStringExtra("time_left");
        String endDate = getIntent().getStringExtra("end_date");
        String startTime = getIntent().getStringExtra("start_time");
        String endTime = getIntent().getStringExtra("end_time");
        int roomID = getIntent().getIntExtra("id", -1);
        int timerID;

        //DISPLAY TIME LEFT ON TEXTVIEW
        TextView timerText = findViewById(R.id.countdownTimerView);
        timerText.setText(timeLeft);

        //DISPLAY LOSS MESSAGE ON TEXTVIEW
        TextView lossText = findViewById(R.id.lossText);
        String name = getIntent().getStringExtra("name");
        String msg = "You've Lost " + name + "...";
        lossText.setText(msg);

        //DETERMINE TIMER ID, CREATE TIMER OBJECT AND INSERT INTO REPOSITORY
        if (allTimers.isEmpty()) {
            timerID = 1;
        }
        else {
            timerID = allTimers.get(allTimers.size()-1).getTimerID() + 1;
        }
        Timer timer = new Timer(timerID, roomID, name, startTime, endTime, endDate, timeLeft);
        try{
            repository.insert(timer);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        //FINISH BUTTON RETURNS YOU TO MAIN ACTIVITY
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomLoss.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}