package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoomPuzzles extends AppCompatActivity {
    //DECLARE REPOSITORY AND GLOBAL VARIABLES
    Repository repository;
    String roomName, startTime, puzzleText, hint, solution, nextPuzzleText;
    int roomID, puzzleNum;
    List<Puzzle> allPuzzles;
    Puzzle currentPuzzle;
    TextView countDownTimerView, puzzleTextView, nameView, view;
    EditText solutionTextView;
    CountDownTimer countDownTimer;
    Button nudgeButton, hintButton, solutionButton, submitButton;
    Date end;

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

        //CREATE START TIME STRING
        startTime = timeFormat.format(Calendar.getInstance().getTime());

        //CREATE AND ASSIGN TEXT VIEWS
        puzzleTextView = findViewById(R.id.puzzleTextView);
        nameView = findViewById(R.id.roomNameView);
        countDownTimerView = findViewById(R.id.countdownTimerView);
        solutionTextView = findViewById(R.id.solutionEditTextView);
        view = findViewById(R.id.hintTextView);

        //POPULATE PUZZLE LIST AND OBTAIN THE FIRST PUZZLE
        allPuzzles = repository.getmRoomPuzzles(roomID);
        currentPuzzle = allPuzzles.get(puzzleNum);

        //SET ROOM NAME ON TEXT VIEW
        nameView.setText(roomName);

        //CREATE STRING FOR PUZZLE NUMBER TEXT AND SET TEXT VIEW
        puzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
        puzzleTextView.setText(puzzleText);

        //START COUNTDOWN TIMER
        countDownTimer = getCountDownTimer();

        //CREATE AND SET BUTTON FOR NUDGE
        nudgeButton = findViewById(R.id.nudge_button);
        nudgeButton.setOnClickListener(v -> {
            hint = currentPuzzle.getNudge();
            view.setText(hint);
        });

        //CREATE AND SET BUTTON FOR HINT
        hintButton = findViewById(R.id.hint_button);
        hintButton.setOnClickListener(v -> {
            hint = currentPuzzle.getHint();
            view.setText(hint);
        });

        //CREATE AND SET BUTTON FOR SOLUTION
        solutionButton = findViewById(R.id.solution_button);
        solutionButton.setOnClickListener(v -> {
            hint = currentPuzzle.getSolution();
            view.setText(hint);
        });

        //CREATE AND SET BUTTON FOR SUBMITTING AN ANSWER
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            submitButton.setEnabled(false);
            //CREATE SOLUTION STRING
            solution = currentPuzzle.getSolution();

            //CHECKS FOR CORRECT SOLUTION
            if (String.valueOf(solutionTextView.getText()).equals(solution)) {
                //RUNS WHEN SOLUTION IS CORRECT AND YOU'VE REACHED THE LAST PUZZLE
                if (currentPuzzle.getPuzzleNum() == allPuzzles.size()) {
                    countDownTimer.cancel();
                    Intent intent = new Intent(RoomPuzzles.this, RoomWin.class);
                    intent.putExtra("start_time", startTime);
                    intent.putExtra("name", roomName);
                    intent.putExtra("time_left", countDownTimerView.getText());
                    end = Calendar.getInstance().getTime();
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
                    view.setText("");

                    currentPuzzle.setPuzzleNum(currentPuzzle.getPuzzleNum()+1);
                    currentPuzzle.setNudge(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getNudge());
                    currentPuzzle.setHint(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getHint());
                    currentPuzzle.setSolution(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getSolution());

                    nextPuzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
                    puzzleTextView.setText(nextPuzzleText);
                    solutionTextView.setText("");

                    submitButton.setEnabled(true);
                }
            }
            //RUNS WHEN THE ANSWER IS INCORRECT
            else {
                String msg = "That answer is incorrect";
                Toast toast = Toast.makeText(RoomPuzzles.this, msg, Toast.LENGTH_LONG);
                toast.show();
                submitButton.setEnabled(true);
            }
        });

        //CANCEL BACK GESTURE FOR ANDROID 13 AND ABOVE
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

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
                end = Calendar.getInstance().getTime();
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
    //BY PRESSING THE BACK BUTTON
    //ONLY FOR ANDROID API LEVEL 12 AND BELOW

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}