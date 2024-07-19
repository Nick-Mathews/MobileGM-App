package com.packages.mobilegamemaster.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.packages.mobilegamemaster.Entities.Password;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;
import com.packages.mobilegamemaster.Entities.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //CREATE REPOSITORY, ROOM LIST
    Repository repository;
    List<Room> allRooms;
    Button signInButton, cancelButton;
    EditText username, password;
    Dialog loginDialog, startupDialog;
    Button okButton;
    boolean found, dialogShown;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //POPULATE REPOSITORY, SET ROOM LIST, AND ADD ROOM LIST TO THE ADAPTER
        repository = new Repository(getApplication());
        allRooms = repository.getmAllRooms();
        final RoomAdapter roomAdapter = new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);

        //CHECK IF REPOSITORY IS EMPTY
        if (repository.getmAllPasswords().isEmpty()) {
            Password first = new Password(1, "Admin", "8675");
            try {
                repository.insert(first);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //SETUP SIGN-IN DIALOG FOR ADMIN MENU
        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setContentView(R.layout.dialog_login);
        signInButton = loginDialog.findViewById(R.id.signInButton);
        cancelButton = loginDialog.findViewById(R.id.cancelButton);

        //SETUP FIRST TIME STARTUP DIALOG AND SHARED PREFS SETTINGS
        startupDialog = new Dialog(MainActivity.this);
        startupDialog.setContentView(R.layout.dialog_startup);
        okButton = startupDialog.findViewById(R.id.okButton);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            startupDialog.show();
            okButton.setOnClickListener(v -> startupDialog.dismiss());
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.apply();
        }

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);

        //SET TOOLBAR TO ACTIONBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    //ADMIN MENU OPTIONS FOR ADDING, EDITING OR DELETING ROOMS, PUZZLES, AND USERS AND ACCESS TO THE LOG TABLES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loginDialog.show();
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        signInButton.setOnClickListener(v -> {
            //ASSIGN EDIT TEXT VIEW AND SET FOUND TO FALSE
            username = loginDialog.findViewById(R.id.username);
            password = loginDialog.findViewById(R.id.password);
            found = false;

                //CHECK REPOSITORY FOR MATCHING PASSWORD
                for (Password pass : repository.getmAllPasswords()) {
                    if ((pass.getPassword().equals(String.valueOf(password.getText()).trim())) && (pass.getUserName().equals(String.valueOf(username.getText()).trim()))) {
                        found = true;
                        loginDialog.dismiss();
                        hideKeyboardFrom(this, v);
                        if (item.getItemId() == R.id.add_room) {
                            Intent intent = new Intent(MainActivity.this, AddRoom.class);
                            startActivity(intent);
                        }

                        if (item.getItemId() == R.id.edit_delete_room) {
                            Intent intent = new Intent(MainActivity.this, RoomList.class);
                            startActivity(intent);
                        }

                        if (item.getItemId() == R.id.edit_delete_passwords) {
                            Intent intent = new Intent(MainActivity.this, PasswordsList.class);
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.report_logs) {
                            Intent intent = new Intent(MainActivity.this, RoomLogs.class);
                            startActivity(intent);
                        }
                    }
                }
                //DISPLAY TOAST DIALOG IF PASSWORD IS NOT FOUND
                if (!found) {
                    Toast msg = Toast.makeText(MainActivity.this, "User or password incorrect", Toast.LENGTH_LONG);
                    msg.show();
                }
                username.setText("");
                password.setText("");
        });

        cancelButton.setOnClickListener(v -> {
            username = loginDialog.findViewById(R.id.username);
            password = loginDialog.findViewById(R.id.password);
            username.setText("");
            password.setText("");
            loginDialog.dismiss();
        });

        return true;
    }

    //UPDATES LIST OF ROOMS AND RECYCLERVIEW WHEN RETURNING FROM ADMIN FUNCTIONS
    @Override
    protected void onResume(){
        super.onResume();
        //SET ROOM LIST AND ADD ROOM LIST TO THE ADAPTER
        allRooms = repository.getmAllRooms();
        final RoomAdapter roomAdapter = new RoomAdapter(this);
        roomAdapter.setRooms(allRooms);

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);
    }
    //HIDES KEYBOARD IN FRAGMENT AFTER CORRECT LOGIN ATTEMPT
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}