package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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
    RecyclerView recyclerView;
    ProgressBar pgBar;
    EditText searchEditText;

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

        //POPULATE REPOSITORY AND TIMER LIST; SET TIMER LIST ON ADAPTER; SET SEARCH EDITTEXT
        repository = new Repository(getApplication());
        allTimers = repository.getmAllTimers();
        final GameLogAdapter gameLogAdapter = new GameLogAdapter(this);
        gameLogAdapter.setmTimers(allTimers);
        searchEditText = findViewById(R.id.searchEditText);

        //SET PROGRESS BAR
        pgBar = findViewById(R.id.progressBar);

        //SETUP FIRST TIME STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialog4Checked = settings.getBoolean("dialog4Checked", false);

        if (!dialog4Checked) {
            Dialog startupDialog4 = new Dialog(GameLogs.this);
            startupDialog4.setContentView(R.layout.dialog_startup);
            Button okButton4 = startupDialog4.findViewById(R.id.okButton);
            CheckBox dialogCheckBox4 = startupDialog4.findViewById(R.id.dialogCheckBox);
            TextView startupText4 = startupDialog4.findViewById(R.id.dialog_startup_textview);
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
        recyclerView.setAdapter(gameLogAdapter);

        //SET TOOLBAR AS ACTION BAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CREATE SEARCH BUTTON AND SET ONCLICK LISTENER
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            int roomID = -1;
            String searchTerm = searchEditText.getText().toString();
            for (Room current: repository.getmAllRooms()) {
                if (current.getRoomName().toLowerCase().contains(searchTerm.toLowerCase()) && !searchTerm.isEmpty()){
                    roomID = current.getRoomID();
                }
            }
            if (!searchTerm.isEmpty()) {
                if (roomID == -1) {
                    gameLogAdapter.setmTimers(null);
                    recyclerView.setAdapter(gameLogAdapter);
                } else {
                    roomTimers = repository.getmTimers(roomID);
                    gameLogAdapter.setmTimers(roomTimers);
                    recyclerView.setAdapter(gameLogAdapter);
                }
            }
            else {
                gameLogAdapter.setmTimers(allTimers);
                recyclerView.setAdapter(gameLogAdapter);
            }
            pgBar.setVisibility(View.INVISIBLE);
        });

        //SET LISTENER FOR SOFT KEYBOARD DONE ON SEARCH BOX
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchButton.callOnClick();
            }
            return true;
        });

        //BACK NAVIGATION BUTTON
        FloatingActionButton backButton = findViewById(R.id.floating_back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameLogs.this, AdminMenu.class);
            startActivity(intent);
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(GameLogs.this, AdminMenu.class);
                startActivity(intent);
            }
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
            new AlertDialog.Builder(this)
                    .setMessage(R.string.db_dialog_message)
                    .setNegativeButton(R.string.db_dialog_negative, (dialog, which)-> {})
                    .setPositiveButton(R.string.db_dialog_positive, (dialog, which)-> {
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
                        final GameLogAdapter gameLogAdapter = new GameLogAdapter(this);
                        gameLogAdapter.setmTimers(allTimers);

                        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLERVIEW
                        recyclerView = findViewById(R.id.gameLogRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(gameLogAdapter);
                    }).show();

            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }
}