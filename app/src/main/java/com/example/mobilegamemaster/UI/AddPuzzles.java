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

import java.util.List;

public class AddPuzzles extends AppCompatActivity {
    //CREATE REPOSITORY, ROOMID AND PUZZLEID VARIABLES
    Repository repository;
    Room currentRoom;
    int puzzleID;

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
        String roomName = getIntent().getStringExtra("name");
        int roomID = getIntent().getIntExtra("id", -1);
        int puzzleNum = getIntent().getIntExtra("puzzle_num", -1);

        //FIND AND SET ROOM NAME TEXTVIEW
        TextView roomNameView = findViewById(R.id.roomNameView);
        roomNameView.setText(roomName);

        //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES
        EditText nudgeEntry = findViewById(R.id.editNudgeText);
        EditText hintEntry = findViewById(R.id.editHintText);
        EditText solutionEntry = findViewById(R.id.editSolutionText);

        //SET TEXTVIEWS FOR HINT ENTRY FIELDS
        TextView nudgeLabel = findViewById(R.id.nudgeNum);
        nudgeLabel.setText("Puzzle " + puzzleNum + " Nudge:");

        TextView hintLabel = findViewById(R.id.hintNum);
        hintLabel.setText("Puzzle " + puzzleNum + " Hint:");

        TextView solutionLabel = findViewById(R.id.solutionNum);
        solutionLabel.setText("Puzzle " + puzzleNum + " Solution:");

        //CREATE BUTTON AND LISTENER FOR 'ABANDON ROOM' BUTTON
        Button abandonButton = findViewById(R.id.abandonButton);
        abandonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomID == -1) {
                    Intent intent = new Intent(AddPuzzles.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    for (Room room: repository.getmAllRooms()) {
                        if (room.getRoomID() == roomID) {
                            currentRoom = room;
                        }
                        List<Puzzle> roomPuzzles = repository.getmRoomPuzzles(roomID);
                        if (roomPuzzles != null) {
                            for(int i=0; i < roomPuzzles.size(); ++i) {
                                Puzzle puzz = roomPuzzles.get(i);
                                try {
                                    repository.delete(puzz);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    try {
                        repository.delete(currentRoom);
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                    Intent intent = new Intent(AddPuzzles.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        //CREATE BUTTON AND LISTENER FOR 'SAVE AND FINISH' BUTTON
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomID == -1) {
                    Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                    msg.show();
                }
                else {
                    String nudgeText = String.valueOf(nudgeEntry.getText());
                    String hintText = String.valueOf(hintEntry.getText());
                    String solutionText = String.valueOf(solutionEntry.getText());
                    if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty())) {
                        Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                        msg.show();
                    } else {
                        if (repository.getmAllPuzzles().isEmpty()){
                            puzzleID = 1;
                        }
                        else {
                            puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size()-1).getPuzzleID() + 1;
                        }
                        Puzzle newPuzzle = new Puzzle(puzzleID, puzzleNum, roomID, nudgeText, hintText, solutionText);
                        try {
                            repository.insert(newPuzzle);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(AddPuzzles.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        //CREATE BUTTON AND LISTENER FOR 'ADD NEXT' BUTTON
        Button addNextButton = findViewById(R.id.nextPuzzleButton);
        addNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (roomID == -1) {
                Toast msg = Toast.makeText(AddPuzzles.this, "Your room ID is invalid", Toast.LENGTH_LONG);
                msg.show();
            }
            else {
                String nudgeText = String.valueOf(nudgeEntry.getText());
                String hintText = String.valueOf(hintEntry.getText());
                String solutionText = String.valueOf(solutionEntry.getText());
                if ((nudgeText.isEmpty()) || (hintText.isEmpty()) || (solutionText.isEmpty())) {
                    Toast msg = Toast.makeText(AddPuzzles.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                    msg.show();
                } else {
                    if (repository.getmAllPuzzles().isEmpty()){
                        puzzleID = 1;
                    }
                    else {
                        puzzleID = repository.getmAllPuzzles().get(repository.getmAllPuzzles().size()-1).getPuzzleID() + 1;
                    }
                    Puzzle newPuzzle = new Puzzle(puzzleID, puzzleNum, roomID, nudgeText, hintText, solutionText);
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
            }
        });
    }
}