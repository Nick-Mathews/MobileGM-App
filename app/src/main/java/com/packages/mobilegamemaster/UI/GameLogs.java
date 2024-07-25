package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.Entities.Timer;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GameLogs extends AppCompatActivity {
    //CREATE REPOSITORY AND LIST OF TIMERS
    Repository repository;
    List<Timer> allTimers, roomTimers;
    String searchTerm;
    EditText searchEditText;
    int roomID;
    Dialog startupDialog4;
    Button okButton4, searchButton;
    CheckBox dialogCheckBox4;
    boolean dialog4Checked;
    TextView startupText4;
    FloatingActionButton backButton;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_logs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //POPULATE REPOSITORY AND TIMER LIST; SET TIMER LIST ON ADAPTER
        repository = new Repository(getApplication());
        allTimers = repository.getmAllTimers();
        final LogAdapter logAdapter = new LogAdapter(this);
        logAdapter.setmTimers(allTimers);

        //SETUP FIRST TIME STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dialog4Checked = settings.getBoolean("dialog4Checked", false);

        if (!dialog4Checked) {
            startupDialog4 = new Dialog(GameLogs.this);
            startupDialog4.setContentView(R.layout.dialog_startup);
            okButton4 = startupDialog4.findViewById(R.id.okButton);
            dialogCheckBox4 = startupDialog4.findViewById(R.id.dialogCheckBox);
            startupText4 = startupDialog4.findViewById(R.id.dialog_startup_textview);
            startupText4.setText(R.string.report_logs_intro);
            startupDialog4.show();
            okButton4.setOnClickListener(v -> {
                startupDialog4.dismiss();
                if (dialogCheckBox4.isChecked()) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialog4Checked", true);
                    editor.apply();
                }
            });
        }

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
        recyclerView = findViewById(R.id.gameLogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(logAdapter);

        //SET TOOLBAR AS ACTION BAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CREATE SEARCH BUTTON AND SET ONCLICK LISTENER
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            roomID = -1;
            searchEditText = findViewById(R.id.searchEditText);
            searchTerm = searchEditText.getText().toString();
            for (Room current: repository.getmAllRooms()) {
                if (current.getRoomName().toLowerCase().contains(searchTerm.toLowerCase()) && !searchTerm.isEmpty()){
                    roomID = current.getRoomID();
                }
            }
            if (!searchTerm.isEmpty()) {
                if (roomID == -1) {
                    logAdapter.setmTimers(null);
                    recyclerView.setAdapter(logAdapter);
                } else {
                    roomTimers = repository.getmTimers(roomID);
                    logAdapter.setmTimers(roomTimers);
                    recyclerView.setAdapter(logAdapter);
                }
            }
            else {
                logAdapter.setmTimers(allTimers);
                recyclerView.setAdapter(logAdapter);
            }
        });

        //BACK NAVIGATION BUTTON
        backButton = findViewById(R.id.floating_back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameLogs.this, AdminMenu.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_logs, menu);
        return true;
    }

    //ADMIN MENU FOR DELETING ALL LOGS
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.delete_logs) {
            for (Timer timer: allTimers) {
                try {
                    repository.delete(timer);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
            //REFRESH RECYCLERVIEW AFTER DELETING LOGS
            allTimers = repository.getmAllTimers();
            final LogAdapter logAdapter = new LogAdapter(this);
            logAdapter.setmTimers(allTimers);

            //SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
            recyclerView = findViewById(R.id.gameLogRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(logAdapter);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }
}