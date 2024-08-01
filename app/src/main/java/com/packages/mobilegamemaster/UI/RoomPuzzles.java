package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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

    String roomName, startTime, hint;
    int roomID;
    TextView countDownTimerView;

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
        Repository repository = new Repository(getApplication());

        roomName = getIntent().getStringExtra("name");
        roomID = getIntent().getIntExtra("id", -1);
        int puzzleNum = getIntent().getIntExtra("puzzle_num", -1);
        int timerLength = getIntent().getIntExtra("timer", -1);

        //CREATE START TIME STRING
        startTime = timeFormat.format(Calendar.getInstance().getTime());

        //CREATE AND ASSIGN TEXT VIEWS
        TextView puzzleTextView = findViewById(R.id.puzzleTextView);
        TextView nameView = findViewById(R.id.roomNameView);
        countDownTimerView = findViewById(R.id.countdownTimerView);
        TextView solutionTextView = findViewById(R.id.solutionEditTextView);
        TextView hintView = findViewById(R.id.hintTextView);

        //POPULATE PUZZLE LIST AND OBTAIN THE FIRST PUZZLE
        List<Puzzle> allPuzzles = repository.getmRoomPuzzles(roomID);
        Puzzle currentPuzzle = allPuzzles.get(puzzleNum);

        //SET ROOM NAME ON TEXT VIEW
        nameView.setText(roomName);

        //CREATE STRING FOR PUZZLE NUMBER TEXT AND SET TEXT VIEW
        String puzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
        puzzleTextView.setText(puzzleText);

        //START COUNTDOWN TIMER
        CountDownTimer countDownTimer = getCountDownTimer(timerLength);

        //CREATE AND SET BUTTON FOR NUDGE
        Button nudgeButton = findViewById(R.id.nudge_button);
        nudgeButton.setOnClickListener(v -> {
            hint = currentPuzzle.getNudge();
            hintView.setText(hint);
        });

        //CREATE AND SET BUTTON FOR HINT
        Button hintButton = findViewById(R.id.hint_button);
        hintButton.setOnClickListener(v -> {
            hint = currentPuzzle.getHint();
            hintView.setText(hint);
        });

        //CREATE AND SET BUTTON FOR SOLUTION
        Button solutionButton = findViewById(R.id.solution_button);
        solutionButton.setOnClickListener(v -> {
            hint = currentPuzzle.getSolution();
            hintView.setText(hint);
        });

        //CREATE AND SET BUTTON FOR SUBMITTING AN ANSWER
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            submitButton.setEnabled(false);
            //CREATE SOLUTION STRING
            String solution = currentPuzzle.getSolution();

            //CHECKS FOR CORRECT SOLUTION
            if (String.valueOf(solutionTextView.getText()).equals(solution)) {
                //RUNS WHEN SOLUTION IS CORRECT AND YOU'VE REACHED THE LAST PUZZLE
                if (currentPuzzle.getPuzzleNum() == allPuzzles.size()) {
                    countDownTimer.cancel();
                    Date end = Calendar.getInstance().getTime();
                    Intent intent = new Intent(RoomPuzzles.this, RoomWin.class);
                    intent.putExtra("start_time", startTime);
                    intent.putExtra("name", roomName);
                    intent.putExtra("time_left", countDownTimerView.getText());
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
                    hintView.setText("");

                    currentPuzzle.setPuzzleNum(currentPuzzle.getPuzzleNum()+1);
                    currentPuzzle.setNudge(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getNudge());
                    currentPuzzle.setHint(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getHint());
                    currentPuzzle.setSolution(allPuzzles.get(currentPuzzle.getPuzzleNum()-1).getSolution());

                    String nextPuzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
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

        //LISTENER THAT RUNS SUBMIT BUTTON ACTION WHEN USER PRESSES ENTER ON KEYBOARD
        solutionTextView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String solution = currentPuzzle.getSolution();

                //CHECKS FOR CORRECT SOLUTION
                if (String.valueOf(solutionTextView.getText()).equals(solution)) {
                    //RUNS WHEN SOLUTION IS CORRECT AND YOU'VE REACHED THE LAST PUZZLE
                    if (currentPuzzle.getPuzzleNum() == allPuzzles.size()) {
                        countDownTimer.cancel();
                        Date end = Calendar.getInstance().getTime();
                        Intent intent = new Intent(RoomPuzzles.this, RoomWin.class);
                        intent.putExtra("start_time", startTime);
                        intent.putExtra("name", roomName);
                        intent.putExtra("time_left", countDownTimerView.getText());
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
                        hintView.setText("");

                        currentPuzzle.setPuzzleNum(currentPuzzle.getPuzzleNum() + 1);
                        currentPuzzle.setNudge(allPuzzles.get(currentPuzzle.getPuzzleNum() - 1).getNudge());
                        currentPuzzle.setHint(allPuzzles.get(currentPuzzle.getPuzzleNum() - 1).getHint());
                        currentPuzzle.setSolution(allPuzzles.get(currentPuzzle.getPuzzleNum() - 1).getSolution());

                        String nextPuzzleText = "Puzzle " + (currentPuzzle.getPuzzleNum());
                        puzzleTextView.setText(nextPuzzleText);
                        solutionTextView.setText("");
                    }

                }

            }
            return true;
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    //FUNCTION THAT CREATES AND RUNS A COUNTDOWN TIMER OF 60 MINUTES, WHICH CALLS ROOM LOSS ACTIVITY WHEN ON FINISH RUNS
    @NonNull
    private CountDownTimer getCountDownTimer(int length) {
        NumberFormat f = new DecimalFormat("00");
        CountDownTimer countDownTimer = new CountDownTimer(length, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long min = (millisUntilFinished / 60000);
                long sec = (millisUntilFinished / 1000) % 60;
                String timeText = f.format(min) + ":" + f.format(sec);
                countDownTimerView.setText(timeText);
            }

            @Override
            public void onFinish() {
                Date end = Calendar.getInstance().getTime();
                Intent intent = new Intent(RoomPuzzles.this, RoomLoss.class);
                intent.putExtra("start_time", startTime);
                intent.putExtra("time_left", countDownTimerView.getText());
                intent.putExtra("name", roomName);
                intent.putExtra("end_time", timeFormat.format(end));
                intent.putExtra("end_date", dateFormat.format(end));
                intent.putExtra("id", roomID);
                startActivity(intent);
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }
}