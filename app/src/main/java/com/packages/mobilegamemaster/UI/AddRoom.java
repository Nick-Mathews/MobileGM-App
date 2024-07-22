package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

public class AddRoom extends AppCompatActivity {
    //CREATE REPOSITORY, PUZZLENUM, ROOMID AND NEWROOM VARIABLES
    Repository repository;
    int puzzleNum, roomID;
    TextView startupText2;
    Room newRoom;
    EditText nameTextView;
    Dialog startupDialog2;
    boolean dialog2Checked;
    Button cancelButton, continueButton,  okButton2;
    CheckBox dialogCheckBox2;

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
        //POPULATE REPOSITORY AND SET INITIAL PUZZLENUM
        repository  = new Repository(getApplication());
        puzzleNum = 1;

        //SET ROOM NAME TEXTVIEW
        nameTextView = findViewById(R.id.enterNameText);

        //SETUP FIRST TIME STARTUP DIALOG
        startupDialog2 = new Dialog(AddRoom.this);
        startupDialog2.setContentView(R.layout.dialog_startup);
        okButton2 = startupDialog2.findViewById(R.id.saveButton);
        dialogCheckBox2 = startupDialog2.findViewById(R.id.dialogCheckBox);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dialog2Checked = settings.getBoolean("dialog2Checked", false);

        if (!dialog2Checked) {
            startupText2 = startupDialog2.findViewById(R.id.dialog_startup_textview);
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
        cancelButton = findViewById(R.id.add_room_cancel_button);
        cancelButton.setOnClickListener(v -> {
            cancelButton.setEnabled(false);
            Intent intent = new Intent(AddRoom.this, AdminMenu.class);
            startActivity(intent);
        });

        //CREATE BUTTON AND CLICK LISTENER FOR CONTINUE
        continueButton = findViewById(R.id.add_room_continue_button);
        continueButton.setOnClickListener(v -> {
            continueButton.setEnabled(false);
            String nameText = String.valueOf(nameTextView.getText());
            if (!nameText.isEmpty()) {
                if (repository.getmAllRooms().isEmpty()) {
                    roomID = 1;
                }
                else {
                    roomID = repository.getmAllRooms().get(repository.getmAllRooms().size()-1).getRoomID() + 1;
                }
                newRoom = new Room(roomID, nameText);
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
            else{
                Toast toast = Toast.makeText(AddRoom.this, "You must enter a room name to continue", Toast.LENGTH_LONG);
                toast.show();
                continueButton.setEnabled(true);
            }


        });
    }
}