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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

public class EditPuzzles extends AppCompatActivity {
    //CREATE REPOSITORY
    Repository repository;
    Room currentRoom;
    Puzzle currentPuzzle;
    int roomID, puzzleID, puzzleNum;
    String nudgeText, hintText, solutionText;
    TextView roomNameView, nudgeLabel, hintLabel, solutionLabel;
    EditText nudgeEntry, hintEntry, solutionEntry;
    Button saveButton, cancelChangesButton, deletePuzzleButton;
    ProgressBar pgBar;

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

        //SET PROGRESS BAR VIEWS
        pgBar = findViewById(R.id.progressBar);

        //RETRIEVE INTENT EXTRA FOR USE, CREATE ROOM OBJECT, CREATE PUZZLE OBJECT
        roomID = getIntent().getIntExtra("room_id", -1);
        puzzleID = getIntent().getIntExtra("puzzle_id", -1);
        currentRoom = repository.getmRoom(roomID);
        currentPuzzle = repository.getmPuzzle(puzzleID);
        puzzleNum = currentPuzzle.getPuzzleNum();

        //FIND AND SET ROOM NAME TEXTVIEW
        roomNameView = findViewById(R.id.roomNameText);
        roomNameView.setText(currentRoom.getRoomName());

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES AND POPULATE WITH CURRENT CLUES
        nudgeEntry = findViewById(R.id.editNudgeText);
        hintEntry = findViewById(R.id.editHintText);
        solutionEntry = findViewById(R.id.editSolutionText);

        nudgeEntry.setText(currentPuzzle.getNudge());
        hintEntry.setText(currentPuzzle.getHint());
        solutionEntry.setText(currentPuzzle.getSolution());

        //SET TEXTVIEWS FOR HINT ENTRY FIELDS
         nudgeLabel = findViewById(R.id.nudgeNum);
         nudgeText = "Puzzle " + puzzleNum + " Nudge:";
         nudgeLabel.setText(nudgeText);

         hintLabel = findViewById(R.id.hintNum);
         hintText = "Puzzle " + puzzleNum + " Hint:";
         hintLabel.setText(hintText);

         solutionLabel = findViewById(R.id.solutionNum);
         solutionText = "Puzzle " + puzzleNum + " Solution:";
         solutionLabel.setText(solutionText);

        //CREATE BUTTON AND LISTENER FOR SAVE PUZZLE BUTTON
        saveButton = findViewById(R.id.savePuzzleButton);
        saveButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            if (roomID == -1) {
                Toast msg = Toast.makeText(EditPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                msg.show();
                pgBar.setVisibility(View.INVISIBLE);
            }
            else {
                String nudgeText1 = String.valueOf(nudgeEntry.getText());
                String hintText1 = String.valueOf(hintEntry.getText());
                String solutionText1 = String.valueOf(solutionEntry.getText());
                if ((nudgeText1.isEmpty()) || (hintText1.isEmpty()) || (solutionText1.isEmpty())) {
                    Toast msg = Toast.makeText(EditPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                    msg.show();
                    pgBar.setVisibility(View.INVISIBLE);
                } else {
                        currentPuzzle.setNudge(nudgeText1);
                        currentPuzzle.setHint(hintText1);
                        currentPuzzle.setSolution(solutionText1);
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
        });

        //CREATE BUTTON AND LISTENER FOR CANCEL CHANGES BUTTON
        cancelChangesButton = findViewById(R.id.cancelChangesButton);
        cancelChangesButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
            intent.putExtra("name", currentRoom.getRoomName());
            intent.putExtra("id", roomID);
            startActivity(intent);

        });

        //CREATE BUTTON AND LISTENER FOR DELETE PUZZLE BUTTON
        deletePuzzleButton = findViewById(R.id.deletePuzzleButton);
        deletePuzzleButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            try {
                repository.delete(currentPuzzle);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int i = 1;
            for (Puzzle puzzle: repository.getmRoomPuzzles(roomID)){
                puzzle.setPuzzleNum(i);
                try {repository.update(puzzle);}
                catch(Exception e) {throw new RuntimeException(e);}
                ++i;
            }

            Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
            intent.putExtra("name", currentRoom.getRoomName());
            intent.putExtra("id", currentRoom.getRoomID());
            startActivity(intent);
        });
    }
}