package com.packages.mobilegamemasterapp.UI;

import static com.packages.mobilegamemasterapp.UI.MainActivity.PREFS_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.R;
import com.packages.mobilegamemasterapp.database.Repository;

public class AddRoom extends AppCompatActivity {
    //CREATE ROOM ID
    int roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY AND SET INITIAL PUZZLE NUM
        Repository repository  = new Repository(getApplication());
        int puzzleNum = 1;

        //SET ROOM NAME TEXTVIEW
        EditText nameTextView = findViewById(R.id.enterNameText);
        EditText timeTextView = findViewById(R.id.enterTimeText);

        //SETUP FIRST TIME STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialog2Checked = settings.getBoolean("dialog2Checked", false);

        //SET PROGRESS BAR VIEW
        ProgressBar pgBar = findViewById(R.id.progressBar);

        if (!dialog2Checked) {
            Dialog startupDialog2 = new Dialog(AddRoom.this);
            startupDialog2.setContentView(R.layout.dialog_startup);
            Button okButton2 = startupDialog2.findViewById(R.id.okButton);
            CheckBox dialogCheckBox2 = startupDialog2.findViewById(R.id.dialogCheckBox);
            TextView startupText2 = startupDialog2.findViewById(R.id.dialog_startup_textview);
            startupText2.setText(R.string.add_room_intro);
            startupDialog2.show();
            okButton2.setOnClickListener(v -> {
                startupDialog2.dismiss();
                if (dialogCheckBox2.isChecked()) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialog2Checked", true);
                    editor.apply();
                }
            });
        }

        //CREATE BUTTON AND CLICK LISTENER FOR CANCEL
        Button cancelButton = findViewById(R.id.add_room_cancel_button);
        cancelButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            cancelButton.setEnabled(false);
            Intent intent = new Intent(AddRoom.this, AdminMenu.class);
            startActivity(intent);
        });

        //CREATE BUTTON AND CLICK LISTENER FOR CONTINUE
        Button continueButton = findViewById(R.id.add_room_continue_button);
        continueButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            continueButton.setEnabled(false);
            boolean roomFound = false;
            String nameText = String.valueOf(nameTextView.getText()).trim();
            int timer = Integer.parseInt(timeTextView.getText().toString());
            int milliTime = timer * 60000;
            if (!nameText.isEmpty()) {
                for (Room room: repository.getmAllRooms()) {
                    if (room.getRoomName().equals(nameText)){
                        roomFound = true;
                        break;
                    }
                }
                if(!roomFound) {
                    if (repository.getmAllRooms().isEmpty()) {
                        roomID = 1;
                    } else {
                        roomID = repository.getmAllRooms().get(repository.getmAllRooms().size() - 1).getRoomID() + 1;
                    }
                    Room newRoom = new Room(roomID, nameText, milliTime);
                    try {
                        repository.insert(newRoom);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent(AddRoom.this, AddPuzzles.class);
                    intent.putExtra("name", newRoom.getRoomName());
                    intent.putExtra("id", newRoom.getRoomID());
                    intent.putExtra("puzzle_num", puzzleNum);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AddRoom.this, "That name matches an existing room", Toast.LENGTH_LONG).show();
                    pgBar.setVisibility(View.INVISIBLE);
                    continueButton.setEnabled(true);
                }
            }
            else{
                Toast.makeText(AddRoom.this, "You must enter a valid room name to continue", Toast.LENGTH_LONG).show();

                pgBar.setVisibility(View.INVISIBLE);
                continueButton.setEnabled(true);
            }
        });

        //LISTENER THAT HANDLES SOFT KEYBOARD DONE FROM ROOM TIMER EDITTEXT
        timeTextView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                continueButton.callOnClick();
            }
            return true;
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(AddRoom.this, AdminMenu.class);
                startActivity(intent);

            }
        });
    }
}