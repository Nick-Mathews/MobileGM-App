package com.example.mobilegamemaster.UI;

import android.os.Bundle;
import android.os.CountDownTimer;
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

public class RoomPuzzles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_puzzles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String currentName = getIntent().getStringExtra("name");
        int currentID = getIntent().getIntExtra("id", -1);
        TextView nameView = findViewById(R.id.roomNameView);
        nameView.setText(currentName);

        Timer timer = new Timer(currentID);
        TextView countDownTimerView = findViewById(R.id.countdownTimerView);
        CountDownTimer countDownTimer = timer.startCountdownTimer(countDownTimerView);
        countDownTimer.start();

        String currentHint;
        Button nudgeButton = findViewById(R.id.nudgebutton);
        Button hintButton = findViewById(R.id.hintbutton);
        Button solutionButton = findViewById(R.id.solutionbutton);

        //ASSIGN HINT BUTTONS TO GET DATA AND DISPLAY ON @id/hintTextView
        //REASSIGN END BUTTON TO SUBMIT BUTTON AND HAVE IT CHECK THE EDITTEXT AGAINST THE SOLUTION,
        // THEN UPDATE THE LAYOUT OR SEND A TOAST THAT THE SOLUTION WAS INCORRECT

        Button endButton = findViewById(R.id.endButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalTime = countDownTimerView.getText().toString();
                timer.endCountdownTimer(countDownTimer, countDownTimerView, finalTime);
            }
        });
    }
}