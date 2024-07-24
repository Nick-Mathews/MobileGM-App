package com.packages.mobilegamemaster.UI;

import android.app.Dialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Puzzle;
import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.util.List;

public class PuzzleList extends AppCompatActivity {
    //CREATE REPOSITORY, LIST OF PUZZLES, CURRENT ROOM, ROOM NAME STRING, ROOM ID INT, AND LAST INDEX INT
    Repository repository;
    List<Puzzle> roomPuzzles;
    List<Room> allRooms;
    Room currentRoom;
    String roomName, renameText;
    int roomID, lastIndex;
    boolean nameFound;
    Button addPuzzleButton, finishButton, deleteRoomButton, saveButton, cancelButton;
    RecyclerView recyclerView;
    TextView roomNameText;
    PuzzleListAdapter puzzleListAdapter;
    Dialog renameDialog;
    EditText renameEditText;
    ProgressBar pgBar;

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
        allRooms = repository.getmAllRooms();
        pgBar = findViewById(R.id.progressBar);

        //SET ROOM NAME
        roomNameText = findViewById(R.id.roomNameText);
        roomNameText.setText(roomName);

        //GET INDEX OF LAST PUZZLE IN THE ROOM
        lastIndex = roomPuzzles.size() + 1;

        //CREATE RECYCLER VIEW AND ADAPTER; SET RECYCLER VIEW AND ADAPTER
        recyclerView = findViewById(R.id.puzzleListRecyclerView);
        puzzleListAdapter = new PuzzleListAdapter(this);
        puzzleListAdapter.setPuzzles(roomPuzzles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(puzzleListAdapter);

        //BUTTON THAT ADDS A NEW PUZZLE TO ROOM
        addPuzzleButton = findViewById(R.id.addPuzzleButton);
        addPuzzleButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            addPuzzleButton.setEnabled(false);
            Intent intent = new Intent(PuzzleList.this, AddPuzzles.class);
            intent.putExtra("name", roomName);
            intent.putExtra("id", roomID);
            intent.putExtra("puzzle_num", lastIndex);
            startActivity(intent);
        });

        //BUTTON THAT RETURNS TO THE MAIN ACTIVITY
        finishButton = findViewById(R.id.finishEditsButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            finishButton.setEnabled(false);
            Intent intent = new Intent(PuzzleList.this, RoomList.class);
            startActivity(intent);
        });

        //BUTTON THAT DELETES THE EXISTING ROOM AND PUZZLES
        deleteRoomButton = findViewById(R.id.deleteRoomButton);
        deleteRoomButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            deleteRoomButton.setEnabled(false);
            if (roomID == -1) {
                Intent intent = new Intent(PuzzleList.this, RoomList.class);
                startActivity(intent);
            }
            else {
                currentRoom = repository.getmRoom(roomID);
                roomPuzzles = repository.getmRoomPuzzles(roomID);
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

            Intent intent = new Intent(PuzzleList.this, RoomList.class);
            startActivity(intent);
        });

    //SET UP RENAME DIALOG
        renameDialog = new Dialog(PuzzleList.this);
        renameDialog.setContentView(R.layout.dialog_rename_room);
        renameEditText = renameDialog.findViewById(R.id.roomRenameEditText);
        cancelButton = renameDialog.findViewById(R.id.cancelButton);
        saveButton = renameDialog.findViewById(R.id.saveButton);
        cancelButton.setOnClickListener(v -> {
            renameEditText.setText("");
            renameDialog.hide();
        });

        saveButton.setOnClickListener(v -> {
            saveButton.setEnabled(false);
            nameFound = false;
            renameText = renameEditText.getText().toString();
            if (renameText.isEmpty()) {
                Toast msg = Toast.makeText(PuzzleList.this, "Please enter a name to save", Toast.LENGTH_LONG);
                msg.show();
                renameEditText.setText("");
                saveButton.setEnabled(true);
            }
            else {
                for (Room room: allRooms) {
                    if (renameText.equals(room.getRoomName())) {
                        nameFound = true;
                        Toast msg = Toast.makeText(PuzzleList.this, "That name belongs to another room", Toast.LENGTH_LONG);
                        msg.show();
                        saveButton.setEnabled(true);
                    }
                }
                if (!nameFound) {
                    currentRoom = repository.getmRoom(roomID);
                    currentRoom.setRoomName(renameText);
                    try {
                        repository.update(currentRoom);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    roomNameText.setText(renameText);
                    renameEditText.setText("");
                    renameDialog.dismiss();
                }

            }
        });

        //TAP AND HOLD FUNCTION THAT ACTIVATES RENAME ROOM DIALOG
        roomNameText.setOnLongClickListener(v -> {
            renameDialog.show();
            return true;
        });
    }

}
