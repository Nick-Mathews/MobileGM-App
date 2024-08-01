package com.packages.mobilegamemaster.UI;

import android.app.AlertDialog;
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
import androidx.activity.OnBackPressedCallback;
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
        Repository repository = new Repository(getApplication());
        int roomID = getIntent().getIntExtra("id", -1);
        final List<Puzzle> roomPuzzles = repository.getmRoomPuzzles(roomID);
        String roomName = getIntent().getStringExtra("name");
        final Room currentRoom = repository.getmRoom(roomID);
        List<Room> allRooms = repository.getmAllRooms();
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //SET ROOM NAME
        TextView roomNameText = findViewById(R.id.roomNameText);
        roomNameText.setText(roomName);

        //GET INDEX OF LAST PUZZLE IN THE ROOM
        int lastIndex = roomPuzzles.size() + 1;

        //CREATE RECYCLER VIEW AND ADAPTER; SET RECYCLER VIEW AND ADAPTER
        RecyclerView recyclerView = findViewById(R.id.puzzleListRecyclerView);
        PuzzleListAdapter puzzleListAdapter = new PuzzleListAdapter(this, pgBar);
        puzzleListAdapter.setPuzzles(roomPuzzles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(puzzleListAdapter);

        //BUTTON THAT ADDS A NEW PUZZLE TO ROOM
        Button addPuzzleButton = findViewById(R.id.addPuzzleButton);
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
        Button finishButton = findViewById(R.id.finishEditsButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            finishButton.setEnabled(false);
            Intent intent = new Intent(PuzzleList.this, RoomList.class);
            startActivity(intent);
        });

        //BUTTON THAT DELETES THE EXISTING ROOM AND PUZZLES
        Button deleteRoomButton = findViewById(R.id.deleteRoomButton);
        deleteRoomButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.db_dialog_message)
                            .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                            })
                            .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                                pgBar.setVisibility(View.VISIBLE);
                                deleteRoomButton.setEnabled(false);
                                if (roomID == -1) {
                                    Intent intent = new Intent(PuzzleList.this, RoomList.class);
                                    startActivity(intent);
                                }
                                else {
                                    for(int i=0; i < roomPuzzles.size(); ++i) {
                                        Puzzle puzzle = roomPuzzles.get(i);
                                        try {
                                            repository.delete(puzzle);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
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
                            }).show();
        });

        //SET UP RENAME DIALOG
        Dialog renameDialog = new Dialog(PuzzleList.this);
        renameDialog.setContentView(R.layout.dialog_rename_room);
        EditText renameEditText = renameDialog.findViewById(R.id.roomRenameEditText);

        Button cancelButton = renameDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            renameEditText.setText("");
            renameDialog.hide();
        });

        Button saveButton = renameDialog.findViewById(R.id.okButton);
        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.db_dialog_message)
                    .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                    })
                    .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                        saveButton.setEnabled(false);
                        boolean nameFound = false;
                        String renameText = renameEditText.getText().toString();
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
                    }).show();

        });

        //TAP AND HOLD FUNCTION THAT ACTIVATES RENAME ROOM DIALOG
        roomNameText.setOnLongClickListener(v -> {
            renameDialog.show();
            return true;
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(PuzzleList.this, RoomList.class);
                startActivity(intent);
            }
        });
    }

}
