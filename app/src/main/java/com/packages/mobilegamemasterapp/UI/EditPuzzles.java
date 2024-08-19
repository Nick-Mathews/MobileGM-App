package com.packages.mobilegamemasterapp.UI;

import android.app.AlertDialog;
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

import com.packages.mobilegamemasterapp.Entities.Puzzle;
import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.R;
import com.packages.mobilegamemasterapp.database.Repository;

public class EditPuzzles extends AppCompatActivity {
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
        Repository repository  = new Repository(getApplication());

        //SET PROGRESS BAR VIEWS
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //RETRIEVE INTENT EXTRA FOR USE, CREATE ROOM OBJECT, CREATE PUZZLE OBJECT
        int roomID = getIntent().getIntExtra("room_id", -1);
        int puzzleID = getIntent().getIntExtra("puzzle_id", -1);
        Room currentRoom = repository.getmRoom(roomID);
        Puzzle currentPuzzle = repository.getmPuzzle(puzzleID);
        int puzzleNum = currentPuzzle.getPuzzleNum();

        //FIND AND SET ROOM NAME TEXTVIEW
        TextView roomNameView = findViewById(R.id.roomNameText);
        roomNameView.setText(currentRoom.getRoomName());

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES AND POPULATE WITH CURRENT CLUES
        EditText nameEntry = findViewById(R.id.editNameText);
        EditText nudgeEntry = findViewById(R.id.editNudgeText);
        EditText hintEntry = findViewById(R.id.editHintText);
        EditText solutionEntry = findViewById(R.id.editSolutionText);

        nameEntry.setText(currentPuzzle.getPuzzleName());
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
        saveButton.setOnClickListener(v -> {
                        if (roomID == -1) {
                            Toast.makeText(EditPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String nameText = String.valueOf(nameEntry.getText());
                            String nudgeText1 = String.valueOf(nudgeEntry.getText());
                            String hintText1 = String.valueOf(hintEntry.getText());
                            String solutionText1 = String.valueOf(solutionEntry.getText());
                            if ((nudgeText1.isEmpty()) || (hintText1.isEmpty()) || (solutionText1.isEmpty()) || (nameText.isEmpty())) {
                                Toast.makeText(EditPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG).show();
                                pgBar.setVisibility(View.INVISIBLE);
                            } else {
                                new AlertDialog.Builder(this)
                                        .setMessage(R.string.db_puzzle_save)
                                        .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                                            pgBar.setVisibility(View.VISIBLE);
                                            pgBar.bringToFront();
                                            currentPuzzle.setPuzzleName(nameText);
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
                                    })
                                        .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                                }).show();
                            }
            }

        });

        //CREATE BUTTON AND LISTENER FOR CANCEL CHANGES BUTTON
        Button cancelChangesButton = findViewById(R.id.cancelChangesButton);
        cancelChangesButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
            intent.putExtra("name", currentRoom.getRoomName());
            intent.putExtra("id", roomID);
            startActivity(intent);

        });

        //CREATE BUTTON AND LISTENER FOR DELETE PUZZLE BUTTON
        Button deletePuzzleButton = findViewById(R.id.deletePuzzleButton);
        deletePuzzleButton.setOnClickListener(v -> new AlertDialog.Builder(this).
                setMessage(R.string.db_puzzle_delete)
                .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                    pgBar.setVisibility(View.VISIBLE);
                    pgBar.bringToFront();
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
                })
                .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                }).show());
        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(EditPuzzles.this, PuzzleList.class);
                intent.putExtra("name", currentRoom.getRoomName());
                intent.putExtra("id", roomID);
                startActivity(intent);
            }
        });
    }
}