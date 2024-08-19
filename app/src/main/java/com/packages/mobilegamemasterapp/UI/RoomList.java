package com.packages.mobilegamemasterapp.UI;

import static com.packages.mobilegamemasterapp.UI.MainActivity.PREFS_NAME;

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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.R;
import com.packages.mobilegamemasterapp.database.Repository;

import java.util.List;

public class RoomList extends AppCompatActivity {
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
        Repository repository = new Repository(getApplication());
        List<Room> allRooms = repository.getmAllRooms();

        //SET PROGRESS BAR VIEW
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //SET ROOM LIST ON ADAPTER; SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
        RecyclerView recyclerView = findViewById(R.id.roomListRecyclerView);
        final RoomListAdapter roomListAdapter = new RoomListAdapter(this, pgBar);
        roomListAdapter.setRooms(allRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomListAdapter);

        //SETUP AND CHECK FOR STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialog5Checked = settings.getBoolean("dialog5Checked", false);

        if (!dialog5Checked){
            Dialog startupDialog5 = new Dialog(this);
            startupDialog5.setContentView(R.layout.dialog_startup);
            Button okButton5 = startupDialog5.findViewById(R.id.okButton);
            CheckBox dialogCheckBox5 = startupDialog5.findViewById(R.id.dialogCheckBox);
            TextView startupText5 = startupDialog5.findViewById(R.id.dialog_startup_textview);
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
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(RoomList.this, AdminMenu.class);
            startActivity(intent);
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(RoomList.this, AdminMenu.class);
                startActivity(intent);
            }
        });
        }
    }

