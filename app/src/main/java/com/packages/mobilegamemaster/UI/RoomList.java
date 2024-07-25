package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.util.List;

public class RoomList extends AppCompatActivity {
    //CREATE REPOSITORY AND ROOM LIST
    Repository repository;
    List<Room> allRooms;
    RecyclerView recyclerView;
    TextView startupText5;
    Button finishButton, okButton5;
    ProgressBar pgBar;
    Dialog startupDialog5;
    CheckBox dialogCheckBox5;
    boolean dialog5Checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY, AND POPULATE ROOMS LIST
        repository = new Repository(getApplication());
        allRooms = repository.getmAllRooms();

        //SET ROOM LIST ON ADAPTER; SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
        recyclerView = findViewById(R.id.roomListRecyclerView);
        final EditRoomAdapter editRoomAdapter = new EditRoomAdapter(this);
        editRoomAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editRoomAdapter);

        //SET PROGRESS BAR VIEW
        pgBar = findViewById(R.id.progressBar);

        //SETUP AND CHECK FOR STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dialog5Checked = settings.getBoolean("dialog5Checked", false);

        if (!dialog5Checked){
            startupDialog5 = new Dialog(this);
            startupDialog5.setContentView(R.layout.dialog_startup);
            okButton5 = startupDialog5.findViewById(R.id.okButton);
            dialogCheckBox5 = startupDialog5.findViewById(R.id.dialogCheckBox);
            startupText5 = startupDialog5.findViewById(R.id.dialog_startup_textview);
            startupText5.setText(R.string.edit_room_intro);
            startupDialog5.show();
            okButton5.setOnClickListener(v-> {
                startupDialog5.dismiss();
                if(dialogCheckBox5.isChecked()) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialog5Checked", true);
                    editor.apply();
                }
            });
        }

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(RoomList.this, AdminMenu.class);
            startActivity(intent);
        });
        }
    }

