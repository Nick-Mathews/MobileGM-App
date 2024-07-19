package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
    Room newRoom;

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
        EditText nameTextView = findViewById(R.id.enterNameText);

        //CREATE BUTTON AND CLICK LISTENER FOR CANCEL
        Button cancelButton = findViewById(R.id.add_room_cancel_button);
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRoom.this, MainActivity.class);
            startActivity(intent);
        });

        //CREATE BUTTON AND CLICK LISTENER FOR CONTINUE
        Button continueButton = findViewById(R.id.add_room_continue_button);
        continueButton.setOnClickListener(v -> {
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
            }


        });
    }
}