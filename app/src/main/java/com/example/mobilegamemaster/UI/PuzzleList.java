package com.example.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;

import java.util.List;

public class PuzzleList extends AppCompatActivity {
    //CREATE REPOSITORY, LIST OF PUZZLES, CURRENT ROOM, ROOM NAME STRING, ROOM ID INT, AND LAST INDEX INT
    Repository repository;
    List<Puzzle> roomPuzzles;
    Room currentRoom;
    String roomName;
    int roomID;
    int lastIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puzzle_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY, ROOM ID, PUZZLE LIST, AND ROOM NAME
        repository = new Repository(getApplication());
        roomID = getIntent().getIntExtra("id", -1);
        roomPuzzles = repository.getmRoomPuzzles(roomID);
        roomName = getIntent().getStringExtra("name");

        //SET ROOM NAME
        TextView roomNameText = findViewById(R.id.roomNameText);
        roomNameText.setText(roomName);

        //GET INDEX OF LAST PUZZLE IN THE ROOM
        lastIndex = roomPuzzles.size() + 1;

        //CREATE RECYCLER VIEW AND ADAPTER; SET RECYCLER VIEW AND ADAPTER
        RecyclerView recyclerView = findViewById(R.id.puzzleListRecyclerView);
        PuzzleListAdapter puzzleListAdapter = new PuzzleListAdapter(this);
        puzzleListAdapter.setPuzzles(roomPuzzles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(puzzleListAdapter);

        //BUTTON THAT ADDS A NEW PUZZLE TO ROOM
        Button addPuzzleButton = findViewById(R.id.addPuzzleButton);
        addPuzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuzzleList.this, AddPuzzles.class);
                intent.putExtra("name", roomName);
                intent.putExtra("id", roomID);
                intent.putExtra("puzzle_num", lastIndex);
                startActivity(intent);
            }
        });

        //BUTTON THAT RETURNS TO THE MAIN ACTIVITY
        Button finishButton = findViewById(R.id.finishEditsButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuzzleList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //BUTTON THAT DELETES THE EXISTING ROOM AND PUZZLES
        Button deleteRoomButton = findViewById(R.id.deleteRoomButton);
        deleteRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomID == -1) {
                    Intent intent = new Intent(PuzzleList.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    for(Room room: repository.getmAllRooms()) {
                        if (room.getRoomID() == roomID) {
                            currentRoom = room;
                        }
                        List<Puzzle> roomPuzzles = repository.getmRoomPuzzles(roomID);
                        if (roomPuzzles != null) {
                            for(int i=0; i < roomPuzzles.size(); ++i) {
                                Puzzle puzzle = roomPuzzles.get(i);
                                try {
                                    repository.delete(puzzle);
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
                }
                Intent intent = new Intent(PuzzleList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
