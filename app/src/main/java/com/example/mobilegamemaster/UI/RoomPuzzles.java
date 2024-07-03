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
import androidx.annotation.NonNull;
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
    //CREATE REPOSITORY, ROOM NAME STRING, ROOM ID STRING, PUZZLE NUMBER INT, LIST OF PUZZLES, AND CURRENT PUZZLE
    Repository repository;
    String roomName;
    int roomID;
    int puzzleNum;
    List<Puzzle> allPuzzles;
    Puzzle currentPuzzle;
    TextView countDownTimerView;
    String startTime;
    //CREATE NUMBER AND DATE FORMATS
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

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
        //POPULATE REPOSITORY, ROOM NAME, ROOM ID AND PUZZLE NUMBER
        repository = new Repository(getApplication());
        roomName = getIntent().getStringExtra("name");
        roomID = getIntent().getIntExtra("id", -1);
        puzzleNum = getIntent().getIntExtra("puzzle_num", -1);

        //CREATE AND ASSIGN TEXT VIEWS
        TextView puzzleTextView = findViewById(R.id.puzzleTextView);
        TextView nameView = findViewById(R.id.roomNameView);
        countDownTimerView = findViewById(R.id.countdownTimerView);
        EditText solutionTextView = findViewById(R.id.solutionEditTextView);

        //POPULATE PUZZLE LIST AND OBTAIN THE FIRST PUZZLE
        allPuzzles = repository.getmRoomPuzzles(roomID);
        currentPuzzle = allPuzzles.get(puzzleNum);

        //SET ROOM NAME ON TEXT VIEW
        nameView.setText(roomName);

        //CREATE STRING FOR PUZZLE NUMBER TEXT AND SET TEXT VIEW
        String puzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
        puzzleTextView.setText(puzzleText);

        //CREATE AND START COUNTDOWN TIMER
        CountDownTimer countDownTimer = getCountDownTimer();

        //CREATE AND SET BUTTON FOR NUDGE
        Button nudgeButton = findViewById(R.id.nudgebutton);
        nudgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getNudge();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        //CREATE AND SET BUTTON FOR HINT
        Button hintButton = findViewById(R.id.hintbutton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getHint();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        //CREATE AND SET BUTTON FOR SOLUTION
        Button solutionButton = findViewById(R.id.solutionbutton);
        solutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hint = currentPuzzle.getSolution();
                TextView view = findViewById(R.id.hintTextView);
                view.setText(hint);
            }
        });

        //CREATE AND SET BUTTON FOR SUBMITTING AN ANSWER
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //CREATE START TIME STRING
                startTime = timeFormat.format(Calendar.getInstance().getTime());

                //CREATE SOLUTION STRING
                String solution = currentPuzzle.getSolution();

                //CHECKS FOR CORRECT SOLUTION
                if (String.valueOf(solutionTextView.getText()).equals(solution)) {
                    //RUNS WHEN SOLUTION IS CORRECT AND YOU'VE REACHED THE LAST PUZZLE
                    if (currentPuzzle.getPuzzleNum() == allPuzzles.size()) {
                        countDownTimer.cancel();
                        Intent intent = new Intent(RoomPuzzles.this, RoomWin.class);
                        intent.putExtra("start_time", startTime);
                        intent.putExtra("name", roomName);
                        intent.putExtra("time_left", countDownTimerView.getText());
                        Date end = Calendar.getInstance().getTime();
                        intent.putExtra("end_time", timeFormat.format(end));
                        intent.putExtra("end_date", dateFormat.format(end));
                        intent.putExtra("id", roomID);
                        startActivity(intent);
                    }
                    //RUNS WHEN SOLUTION IS CORRECT BUT YOU HAVEN'T REACHED THE LAST PUZZLE
                    else {
                        String msg = "Correct!";
                        Toast toast = Toast.makeText(RoomPuzzles.this, msg, Toast.LENGTH_LONG);
                        toast.show();
                        TextView view = findViewById(R.id.hintTextView);
                        view.setText("");

                        currentPuzzle.setPuzzleNum(currentPuzzle.getPuzzleNum()+1);
                        currentPuzzle.setNudge(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getNudge());
                        currentPuzzle.setHint(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getHint());
                        currentPuzzle.setSolution(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getSolution());

                        TextView puzzleText = findViewById(R.id.puzzleTextView);
                        String text = "Puzzle " + (currentPuzzle.getPuzzleNum());
                        puzzleText.setText(text);
                        solutionTextView.setText("");
                    }
                }
                //RUNS WHEN THE ANSWER IS INCORRECT
                else {
                    String msg = "That answer is incorrect";
                    Toast toast = Toast.makeText(RoomPuzzles.this, msg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    //FUNCTION THAT CREATES AND RUNS A COUNTDOWN TIMER OF 60 MINUTES, WHICH CALLS ROOM LOSS ACTIVITY WHEN ON FINISH RUNS
    @NonNull
    private CountDownTimer getCountDownTimer() {
        NumberFormat f = new DecimalFormat("00");
        CountDownTimer countDownTimer = new CountDownTimer(3600000, 1000) {
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
                intent.putExtra("start_time", startTime);
                intent.putExtra("time_left", endTime);
                intent.putExtra("name", roomName);
                Date end = Calendar.getInstance().getTime();
                intent.putExtra("end_time", timeFormat.format(end));
                intent.putExtra("end_date", dateFormat.format(end));
                intent.putExtra("id", roomID);
                startActivity(intent);
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }
    //INTENDED TO STOP USERS FROM LEAVING THE ROOM PUZZLES ACTIVITY WITHOUT COMPLETING THE GAME
    //METHOD IS DEPRECATED AND WILL NOT WORK AT HIGHER API LEVELS
    //TODO:REPLACE ON BACK PRESSED FUNCTIONALITY
    @Override
    public void onBackPressed() {

    }
}