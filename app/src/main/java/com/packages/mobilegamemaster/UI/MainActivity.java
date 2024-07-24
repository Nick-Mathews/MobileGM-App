package com.packages.mobilegamemaster.UI;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    String name;
    TextView startupText;
    RecyclerView recyclerView;
    Toolbar toolbar;
    Dialog loginDialog, startupDialog1;
    ProgressBar pgBar;
    Button okButton;
    CheckBox dialogCheckBox;
    boolean found, dialog1Checked;
    SharedPreferences settings;
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
            Password first = new Password(1, "Admin", "password");
            try {
                repository.insert(first);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //SETUP FIRST TIME STARTUP DIALOG AND SHARED PREFS SETTINGS
        settings = getSharedPreferences(PREFS_NAME, 0);
        dialog1Checked = settings.getBoolean("dialog1Checked", false);
        if (!dialog1Checked) {
            startupDialog1 = new Dialog(MainActivity.this);
            startupDialog1.setContentView(R.layout.dialog_startup);
            okButton = startupDialog1.findViewById(R.id.saveButton);
            dialogCheckBox = startupDialog1.findViewById(R.id.dialogCheckBox);
            startupText = startupDialog1.findViewById(R.id.dialog_startup_textview);
            startupText.setText(R.string.welcome_intro);
            startupDialog1.show();
            okButton.setOnClickListener(v -> {
                        startupDialog1.dismiss();
                        if (dialogCheckBox.isChecked()) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("dialog1Checked", true);
                            editor.apply();
                        }
                    });
        }

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);

        //SET TOOLBAR TO ACTIONBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SET PROGRESS BAR VIEW
        pgBar = findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    //ADMIN MENU OPTIONS FOR ADDING, EDITING OR DELETING ROOMS, PUZZLES, AND USERS AND ACCESS TO THE LOG TABLES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //SETUP SIGN-IN DIALOG FOR ADMIN MENU
        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setContentView(R.layout.dialog_login);
        signInButton = loginDialog.findViewById(R.id.signInButton);
        cancelButton = loginDialog.findViewById(R.id.cancelButton);
        loginDialog.show();

        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        signInButton.setOnClickListener(v -> {
            //ASSIGN EDIT TEXT VIEW AND SET FOUND TO FALSE
            pgBar.setVisibility(View.VISIBLE);
            username = loginDialog.findViewById(R.id.username);
            password = loginDialog.findViewById(R.id.password);
            found = false;

                //CHECK REPOSITORY FOR MATCHING PASSWORD
                for (Password pass : repository.getmAllPasswords()) {
                    if ((pass.getPassword().equals(String.valueOf(password.getText()).trim())) && (pass.getUserName().equals(String.valueOf(username.getText()).trim()))) {
                        found = true;
                        loginDialog.dismiss();
                        if (item.getItemId() == R.id.adminMenu) {
                            name = String.valueOf(username.getText()).trim();
                            Intent intent = new Intent(MainActivity.this, AdminMenu.class);
                            intent.putExtra("name", name);
                            startActivity(intent);
                        }
                    }
                }
                //DISPLAY TOAST DIALOG IF PASSWORD IS NOT FOUND
                if (!found) {
                    Toast msg = Toast.makeText(MainActivity.this, "User or password incorrect", Toast.LENGTH_LONG);
                    msg.show();
                    pgBar.setVisibility(View.INVISIBLE);
                }
                username.setText("");
                password.setText("");
        });

        cancelButton.setOnClickListener(v -> {
            username = loginDialog.findViewById(R.id.username);
            password = loginDialog.findViewById(R.id.password);
            username.setText("");
            password.setText("");
            loginDialog.hide();
        });

        return true;
    }
}