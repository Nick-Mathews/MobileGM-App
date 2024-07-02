package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;

public class EditPuzzles extends AppCompatActivity {
    //CREATE REPOSITORY
    Repository repository;
    Room currentRoom;
    Puzzle currentPuzzle;
    int roomID;
    int puzzleID;
    int puzzleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_puzzles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY
        repository  = new Repository(getApplication());

        //RETRIEVE INTENT EXTRA FOR USE, CREATE ROOM OBJECT, CREATE PUZZLE OBJECT
        roomID = getIntent().getIntExtra("room_id", -1);
        puzzleID = getIntent().getIntExtra("puzzle_id", -1);
        currentRoom = repository.getmRoom(roomID);
        currentPuzzle = repository.getmPuzzle(puzzleID);
        puzzleNum = currentPuzzle.getPuzzleNum();

        //FIND AND SET ROOM NAME TEXTVIEW
        TextView roomNameView = findViewById(R.id.roomNameText);
        roomNameView.setText(currentRoom.getRoomName());

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES AND POPULATE WITH CURRENT CLUES
        EditText nudgeEntry = findViewById(R.id.editNudgeText);
        EditText hintEntry = findViewById(R.id.editHintText);
        EditText solutionEntry = findViewById(R.id.editSolutionText);

        nudgeEntry.setText(currentPuzzle.getNudge());
        hintEntry.setText(currentPuzzle.getHint());
        solutionEntry.setText(currentPuzzle.getSolution());

        //SET TEXTVIEWS FOR HINT ENTRY FIELDS
        TextView nudgeLabel = findViewById(R.id.nudgeNum);
        String nudgeText = "Puzzle " + puzzleNum + " Nudge:";
        nudgeLabel.setText(nudgeText);

        TextView hintLabel = findViewById(R.id.hintNum);
        String hintText = "Puzzle " + puzzleNum + " Hint:";
        hintLabel.setText(hintText);

        TextView solutionLabel = findViewById(R.id.solutionNum);
        String solutionText = "Puzzle " + puzzleNum + " Solution:";
        solutionLabel.setText(solutionText);

        //CREATE BUTTON AND LISTENER FOR SAVE PUZZLE BUTTON
        Button saveButton = findViewById(R.id.savePuzzleButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomID == -1) {
                    Toast msg = Toast.makeText(EditPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                    msg.show();
                }
                else {
                    String nudgeText = String.valueOf(nudgeEntry.getText());
                    String hintText = String.valueOf(hintEntry.getText());
                    String solutionText = String.valueOf(solutionEntry.getText());
                    if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty())) {
                        Toast msg = Toast.makeText(EditPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                        msg.show();
                    } else {
                            currentPuzzle.setNudge(nudgeText);
                            currentPuzzle.setHint(hintText);
                            currentPuzzle.setSolution(solutionText);
                        try {
                            repository.update(currentPuzzle);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
                        intent.putExtra("name", currentRoom.getRoomName());
                        intent.putExtra("id", roomID);
                        startActivity(intent);
                    }
                }
            }
        });

        //CREATE BUTTON AND LISTENER FOR CANCEL CHANGES BUTTON
        Button cancelChangesButton = findViewById(R.id.cancelChangesButton);
        cancelChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
                intent.putExtra("name", currentRoom.getRoomName());
                intent.putExtra("id", roomID);
                startActivity(intent);
            }
        });

        //CREATE BUTTON AND LISTENER FOR DELETE PUZZLE BUTTON
        Button deletePuzzleButton = findViewById(R.id.deletePuzzleButton);
        deletePuzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    repository.delete(currentPuzzle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
                intent.putExtra("name", currentRoom.getRoomName());
                intent.putExtra("id", roomID);
                startActivity(intent);
            }
        });
    }
}