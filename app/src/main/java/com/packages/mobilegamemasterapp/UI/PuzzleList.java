package com.packages.mobilegamemasterapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemasterapp.Entities.Puzzle;
import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.R;
import com.packages.mobilegamemasterapp.database.Repository;

import java.util.Collections;
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
        //EXECUTOR SERVICE FOR ASYNCHRONOUS PUZZLE RE-ARRANGING

        //POPULATE REQUIRED VARS AND RETRIEVE INTENT EXTRAS
        Repository repository = new Repository(getApplication());
        int roomID = getIntent().getIntExtra("id", -1);
        final List<Puzzle> roomPuzzles = repository.getmRoomPuzzles(roomID);
        String roomName = getIntent().getStringExtra("name");
        Room currentRoom = repository.getmRoom(roomID);
        List<Room> allRooms = repository.getmAllRooms();
        int timer = currentRoom.getRoomTime();
        String timerText = timer / 60000 + " minutes";
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //SET ROOM NAME AND TIMER
        TextView roomNameText = findViewById(R.id.roomNameText);
        roomNameText.setText(roomName);
        TextView timerViewText = findViewById(R.id.timer);
        timerViewText.setText(timerText);

        //GET INDEX OF LAST PUZZLE IN THE ROOM
        int lastIndex = roomPuzzles.size() + 1;

        //CREATE RECYCLER VIEW AND ADAPTER; SET RECYCLER VIEW AND ADAPTER
        RecyclerView recyclerView = findViewById(R.id.puzzleListRecyclerView);
        PuzzleListAdapter puzzleListAdapter = new PuzzleListAdapter(this, pgBar);
        puzzleListAdapter.setPuzzles(roomPuzzles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(puzzleListAdapter);

        //SET ITEM TOUCH HELPER TO RE-ARRANGE PUZZLE ORDER
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int sourcePosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(roomPuzzles, sourcePosition, targetPosition);
                puzzleListAdapter.notifyItemMoved(sourcePosition, targetPosition);

                return true;
            }
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @Nullable RecyclerView.ViewHolder viewHolder) {
                    int i;
                    Puzzle roomPuzzle;

                    for (i = 0; i < roomPuzzles.size(); ++i) {
                        roomPuzzle = roomPuzzles.get(i);
                        roomPuzzle.setPuzzleNum(i + 1);

                            try {
                                repository.update(roomPuzzle);
                            } catch(Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                if (viewHolder != null) {
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                int bGroundColor = Color.parseColor("#99D1D1D1");
                if (actionState == 2) {
                    if (viewHolder != null) {
                        viewHolder.itemView.setBackgroundColor(bGroundColor);
                    }
                }
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(recyclerView);

        //SETUP TIMER DIALOG
        Dialog timerDialog = new Dialog(this);
        timerDialog.setContentView(R.layout.dialog_timer);
        EditText timerEditText = timerDialog.findViewById(R.id.changeTimerEditText);

        //BUTTON THAT BRINGS UP A DIALOG TO CHANGE THE TIMER LENGTH
        Button changeTimerButton = findViewById(R.id.changeTimerButton);
        changeTimerButton.setOnClickListener(v-> timerDialog.show());

        //DIALOG BUTTONS FOR TIMER LENGTH UPDATE
        Button saveTimerButton = timerDialog.findViewById(R.id.okButton);
        saveTimerButton.setOnClickListener(v-> {
            if ((timerEditText.toString().isEmpty())) {
                Toast.makeText(this, "You must enter a valid timer length", Toast.LENGTH_LONG).show();

            }
            else {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.db_dialog_message)
                        .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {})
                        .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                            int milliTimer = Integer.parseInt(timerEditText.getText().toString()) * 60000;
                            currentRoom.setRoomTime(milliTimer);
                            try {
                                repository.update(currentRoom);
                            }
                            catch(Exception e) {
                                throw new RuntimeException(e);
                            }
                            String newTimerText = timerEditText.getText().toString() + " minutes";
                            timerViewText.setText(newTimerText);
                            timerEditText.setText("");
                            timerDialog.dismiss();
                        }).show();
            }
        });

        //SET LISTENER FOR SOFT KEYBOARD DONE FROM TIMER DIALOG
        timerEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTimerButton.callOnClick();
            }
            return true;
        });

        Button cancelTimerButton = timerDialog.findViewById(R.id.cancelButton);
        cancelTimerButton.setOnClickListener(v-> {
            timerEditText.setText("");
            timerDialog.hide();
        });

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
        deleteRoomButton.setOnClickListener(v -> new AlertDialog.Builder(this)
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
                    }).show());

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
                    saveButton.setEnabled(false);
                    boolean nameFound = false;
                    String renameText = renameEditText.getText().toString().trim();
                    if (renameText.isEmpty()) {
                        Toast.makeText(PuzzleList.this, "Please enter a name to save", Toast.LENGTH_LONG).show();
                        saveButton.setEnabled(true);
                    } else {
                        for (Room room : allRooms) {
                            if (renameText.equals(room.getRoomName())) {
                                nameFound = true;
                                Toast.makeText(PuzzleList.this, "That name belongs to another room", Toast.LENGTH_LONG).show();
                                renameEditText.setText("");
                                saveButton.setEnabled(true);
                                break;
                            }
                        }
                        if (!nameFound) {
                            new AlertDialog.Builder(this)
                                    .setMessage(R.string.db_dialog_message)
                                    .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                                    })
                                    .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                                        currentRoom.setRoomName(renameText);
                                        try {
                                            repository.update(currentRoom);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                        roomNameText.setText(renameText);
                                        renameEditText.setText("");
                                        renameDialog.dismiss();
                                    }).show();
                        }
                    }
                });

        //SET LISTENER FOR SOFT KEYBOARD DONE FROM RENAME DIALOG
        renameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveButton.callOnClick();
            }
            return true;
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
