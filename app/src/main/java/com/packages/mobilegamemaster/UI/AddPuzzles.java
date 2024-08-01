package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;


public class AddPuzzles extends AppCompatActivity {
    Room currentRoom;
    int puzzleID;
    Puzzle newPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_puzzles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY
        Repository repository  = new Repository(getApplication());

        //SET PROGRESS BAR VIEW
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //RETRIEVE INTENT EXTRAS FOR USE
        String roomName = getIntent().getStringExtra("name");
        int roomID = getIntent().getIntExtra("id", -1);
        int puzzleNum = getIntent().getIntExtra("puzzle_num", -1);

        //FIND AND SET ROOM NAME TEXTVIEW
        TextView roomNameView = findViewById(R.id.roomNameView);
        roomNameView.setText(roomName);

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES
        EditText nameEntry = findViewById(R.id.editNameText);
        EditText nudgeEntry = findViewById(R.id.editNudgeText);
        EditText hintEntry = findViewById(R.id.editHintText);
        EditText solutionEntry = findViewById(R.id.editSolutionText);

        //SET TEXTVIEWS FOR HINT ENTRY FIELDS
        TextView nudgeView = findViewById(R.id.nudgeNum);
        String nudgeLabel = "Puzzle " + puzzleNum + " Nudge:";
        nudgeView.setText(nudgeLabel);

        TextView hintView = findViewById(R.id.hintNum);
        String hintLabel = "Puzzle " + puzzleNum + " Hint:";
        hintView.setText(hintLabel);

        TextView solutionView = findViewById(R.id.solutionNum);
        String solutionLabel = "Puzzle " + puzzleNum + " Solution:";
        solutionView.setText(solutionLabel);

        //CREATE BUTTON AND LISTENER FOR 'ABANDON PUZZLE' BUTTON
        Button abandonButton = findViewById(R.id.abandonButton);
        abandonButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            abandonButton.setEnabled(false);
            Intent intent;
            if(roomID == -1) {
                intent = new Intent(AddPuzzles.this, MainActivity.class);
            }
            else {
                intent = new Intent(AddPuzzles.this, PuzzleList.class);
                intent.putExtra("id", roomID);
                intent.putExtra("name", roomName);
            }
            startActivity(intent);
        });

        //CREATE BUTTON AND LISTENER FOR 'SAVE AND FINISH' BUTTON
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            finishButton.setEnabled(false);
            boolean found = false;
            if (roomID == -1) {
                Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                msg.show();
                finishButton.setEnabled(true);
                pgBar.setVisibility(View.INVISIBLE);
            }
            else {
                String nameText = String.valueOf(nameEntry.getText());
                String nudgeText = String.valueOf(nudgeEntry.getText());
                String hintText = String.valueOf(hintEntry.getText());
                String solutionText = String.valueOf(solutionEntry.getText());
                if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty()) || (nameText.isEmpty())) {
                    Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                    msg.show();
                    finishButton.setEnabled(true);
                    pgBar.setVisibility(View.INVISIBLE);
                } else {
                    for (Puzzle puzzle: repository.getmAllPuzzles()) {
                        if (puzzle.getPuzzleName().equals(nameText)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (repository.getmAllPuzzles().isEmpty()) {
                            puzzleID = 1;
                        } else {
                            puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size() - 1).getPuzzleID() + 1;
                        }
                        Puzzle newPuzzle = new Puzzle(puzzleID, puzzleNum, nameText, roomID, nudgeText, hintText, solutionText);
                        try {
                            repository.insert(newPuzzle);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(AddPuzzles.this, PuzzleList.class);
                        intent.putExtra("id", roomID);
                        intent.putExtra("name", roomName);
                        startActivity(intent);
                    }
                    else {
                        Toast msg = Toast.makeText(this, "Puzzle name matches an existing puzzle", Toast.LENGTH_LONG);
                        msg.show();
                        pgBar.setVisibility(View.INVISIBLE);
                        finishButton.setEnabled(true);
                    }
                }
            }
        });

        //CREATE BUTTON AND LISTENER FOR 'ADD NEXT' BUTTON
        Button addNextButton = findViewById(R.id.nextPuzzleButton);
        addNextButton.setOnClickListener(v -> {
                    pgBar.setVisibility(View.VISIBLE);
                    pgBar.bringToFront();
                    addNextButton.setEnabled(false);
                    boolean found = false;
                    if (roomID == -1) {
                        Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                        msg.show();
                        addNextButton.setEnabled(true);
                        pgBar.setVisibility(View.INVISIBLE);
                    } else {
                        String nameText = String.valueOf(nameEntry.getText());
                        String nudgeText = String.valueOf(nudgeEntry.getText());
                        String hintText = String.valueOf(hintEntry.getText());
                        String solutionText = String.valueOf(solutionEntry.getText());
                        if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty()) || (nameText.isEmpty())) {
                            Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                            msg.show();
                            addNextButton.setEnabled(true);
                            pgBar.setVisibility(View.INVISIBLE);
                        } else {
                            for (Puzzle puzzle : repository.getmAllPuzzles()) {
                                if (puzzle.getPuzzleName().equals(nameText)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (repository.getmAllPuzzles().isEmpty()) {
                                    puzzleID = 1;
                                } else {
                                    puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size() - 1).getPuzzleID() + 1;
                                }
                                newPuzzle = new Puzzle(puzzleID, puzzleNum, nameText, roomID, nudgeText, hintText, solutionText);
                                try {
                                    repository.insert(newPuzzle);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                for (Room room : repository.getmAllRooms()) {
                                    if (room.getRoomID() == roomID) {
                                        currentRoom = room;
                                    }
                                }
                                Intent intent = new Intent(AddPuzzles.this, AddPuzzles.class);
                                intent.putExtra("name", currentRoom.getRoomName());
                                intent.putExtra("id", currentRoom.getRoomID());
                                int nextPuzzleNum = newPuzzle.getPuzzleNum() + 1;
                                intent.putExtra("puzzle_num", nextPuzzleNum);
                                startActivity(intent);
                            } else {
                                Toast msg = Toast.makeText(this, "Puzzle name matches an existing puzzle", Toast.LENGTH_LONG);
                                msg.show();
                                pgBar.setVisibility(View.INVISIBLE);
                                addNextButton.setEnabled(true);
                            }
                        }
                    }
                });
        //HANDLE BACK GESTURE
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent;
                if(roomID == -1) {
                    intent = new Intent(AddPuzzles.this, MainActivity.class);
                }
                else {
                    intent = new Intent(AddPuzzles.this, PuzzleList.class);
                    intent.putExtra("id", roomID);
                    intent.putExtra("name", roomName);
                }
                startActivity(intent);
            }
        });
    }
}