package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Repository repository = new Repository(getApplication());

        String currentName = getIntent().getStringExtra("name");
        int currentID = getIntent().getIntExtra("id", -1);
        int puzzleNum = getIntent().getIntExtra("puzzle_num", -1);

        List<Puzzle> allPuzzles = repository.getmRoomPuzzles(currentID);
        Puzzle currentPuzzle = allPuzzles.get(puzzleNum);

        TextView nameView = findViewById(R.id.roomNameView);
        nameView.setText(currentName);
        TextView puzzleTextView = findViewById(R.id.puzzleTextView);
        String puzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum() + 1);
        puzzleTextView.setText(puzzleText);

        Button nudgeButton = findViewById(R.id.nudgebutton);
        Button hintButton = findViewById(R.id.hintbutton);
        Button solutionButton = findViewById(R.id.solutionbutton);

        TextView countDownTimerView = findViewById(R.id.countdownTimerView);
        NumberFormat f = new DecimalFormat("00");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);

        //TODO: SET TIMER TO 60 MINUTES
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String timeText = f.format(min) + ":" + f.format(sec);
                countDownTimerView.setText(timeText);
            }

            @Override
            public void onFinish() {
                String endTime = String.valueOf(countDownTimerView.getText());
                Intent intent = new Intent(RoomPuzzles.this, RoomLoss.class);
                intent.putExtra("time_left", endTime);
                intent.putExtra("name", currentName);
                Date end = Calendar.getInstance().getTime();
                intent.putExtra("end_time", timeFormat.format(end));
                intent.putExtra("end_date", dateFormat.format(end));
                intent.putExtra("id", currentID);
                startActivity(intent);
            }
        };
        countDownTimer.start();


        nudgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getNudge();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getHint();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        solutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getSolution();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText solutionTextView = findViewById(R.id.solutionEditTextView);
                String solution = currentPuzzle.getSolution();
                if (String.valueOf(solutionTextView.getText()).equals(solution)) {
                    if (currentPuzzle.getPuzzleNum() == allPuzzles.size() - 1) {
                        countDownTimer.cancel();
                        Intent intent = new Intent(RoomPuzzles.this, RoomWin.class);
                        intent.putExtra("name", currentName);
                        intent.putExtra("time_left", countDownTimerView.getText());
                        Date end = Calendar.getInstance().getTime();
                        intent.putExtra("end_time", timeFormat.format(end));
                        intent.putExtra("end_date", dateFormat.format(end));
                        intent.putExtra("id", currentID);
                        startActivity(intent);
                    } else {
                        String msg = "Correct!";
                        Toast toast = Toast.makeText(RoomPuzzles.this, msg, Toast.LENGTH_LONG);
                        toast.show();
                        TextView view = findViewById(R.id.hintTextView);
                        view.setText("");

                        currentPuzzle.setPuzzleNum(currentPuzzle.getPuzzleNum() + 1);
                        currentPuzzle.setNudge(allPuzzles.get(currentPuzzle.getPuzzleNum()).getNudge());
                        currentPuzzle.setHint(allPuzzles.get(currentPuzzle.getPuzzleNum()).getHint());
                        currentPuzzle.setSolution(allPuzzles.get(currentPuzzle.getPuzzleNum()).getSolution());

                        TextView puzzleText = findViewById(R.id.puzzleTextView);
                        String text = "Puzzle " + (currentPuzzle.getPuzzleNum() + 1);
                        puzzleText.setText(text);
                        solutionTextView.setText("");
                    }
                } else {
                    String msg = "That answer is incorrect";
                    Toast toast = Toast.makeText(RoomPuzzles.this, msg, Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });
    }
}