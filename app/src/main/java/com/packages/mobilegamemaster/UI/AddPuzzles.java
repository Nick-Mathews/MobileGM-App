package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;


public class AddPuzzles extends AppCompatActivity {
    //CREATE REPOSITORY, ROOMID AND PUZZLEID VARIABLES
    Repository repository;
    Room currentRoom;
    int puzzleID, roomID, puzzleNum;
    String roomName, nudgeLabel, hintLabel, solutionLabel, nudgeText, hintText, solutionText;
    Puzzle newPuzzle;
    TextView roomNameView, nudgeView, hintView, solutionView;
    EditText nudgeEntry, hintEntry, solutionEntry;
    Button abandonButton, finishButton, addNextButton;


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
        repository  = new Repository(getApplication());

        //RETRIEVE INTENT EXTRAS FOR USE
        roomName = getIntent().getStringExtra("name");
        roomID = getIntent().getIntExtra("id", -1);
        puzzleNum = getIntent().getIntExtra("puzzle_num", -1);

        //FIND AND SET ROOM NAME TEXTVIEW
        roomNameView = findViewById(R.id.roomNameView);
        roomNameView.setText(roomName);

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES
        nudgeEntry = findViewById(R.id.editNudgeText);
        hintEntry = findViewById(R.id.editHintText);
        solutionEntry = findViewById(R.id.editSolutionText);

        //SET TEXTVIEWS FOR HINT ENTRY FIELDS
        nudgeView = findViewById(R.id.nudgeNum);
        nudgeLabel = "Puzzle " + puzzleNum + " Nudge:";
        nudgeView.setText(nudgeLabel);

        hintView = findViewById(R.id.hintNum);
        hintLabel = "Puzzle " + puzzleNum + " Hint:";
        hintView.setText(hintLabel);

        solutionView = findViewById(R.id.solutionNum);
        solutionLabel = "Puzzle " + puzzleNum + " Solution:";
        solutionView.setText(solutionLabel);

        //CREATE BUTTON AND LISTENER FOR 'ABANDON PUZZLE' BUTTON
        abandonButton = findViewById(R.id.abandonButton);
        abandonButton.setOnClickListener(v -> {
            abandonButton.setEnabled(false);
            if(roomID == -1) {
                Intent intent = new Intent(AddPuzzles.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(AddPuzzles.this, PuzzleList.class);
                intent.putExtra("id", roomID);
                intent.putExtra("name", roomName);
                startActivity(intent);
            }
        });

        //CREATE BUTTON AND LISTENER FOR 'SAVE AND FINISH' BUTTON
        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            finishButton.setEnabled(false);
            if (roomID == -1) {
                Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                msg.show();
                finishButton.setEnabled(true);
            }
            else {
                nudgeText = String.valueOf(nudgeEntry.getText());
                hintText = String.valueOf(hintEntry.getText());
                solutionText = String.valueOf(solutionEntry.getText());
                if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty())) {
                    Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                    msg.show();
                    finishButton.setEnabled(true);
                } else {
                    if (repository.getmAllPuzzles().isEmpty()){
                        puzzleID = 1;
                    }
                    else {
                        puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size()-1).getPuzzleID() + 1;
                    }
                    newPuzzle = new Puzzle(puzzleID, puzzleNum, roomID, nudgeText, hintText, solutionText);
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
            }
        });

        //CREATE BUTTON AND LISTENER FOR 'ADD NEXT' BUTTON
        addNextButton = findViewById(R.id.nextPuzzleButton);
        addNextButton.setOnClickListener(v -> {
            addNextButton.setEnabled(false);
        if (roomID == -1) {
            Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
            msg.show();
            addNextButton.setEnabled(true);
        }
        else {
            nudgeText = String.valueOf(nudgeEntry.getText());
            hintText = String.valueOf(hintEntry.getText());
            solutionText = String.valueOf(solutionEntry.getText());
            if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty())) {
                Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                msg.show();
                addNextButton.setEnabled(true);
            } else {
                if (repository.getmAllPuzzles().isEmpty()){
                    puzzleID = 1;
                }
                else {
                    puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size()-1).getPuzzleID() + 1;
                }
                newPuzzle = new Puzzle(puzzleID, puzzleNum, roomID, nudgeText, hintText, solutionText);
                try {
                    repository.insert(newPuzzle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (Room room: repository.getmAllRooms()) {
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
            }
        }
        });
    }
}