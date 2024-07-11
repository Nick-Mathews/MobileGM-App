package com.example.mobilegamemaster.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Password;
import com.example.mobilegamemaster.R;
import com.example.mobilegamemaster.database.Repository;
import com.example.mobilegamemaster.Entities.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //CREATE REPOSITORY, ROOM LIST
    Repository repository;
    List<Room> allRooms;
    TextView signInText, cancelText;
    EditText password;
    Dialog dialog;

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

        //SETUP SIGN-IN DIALOG FOR ADMIN MENU
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_login);
        signInText = dialog.findViewById(R.id.signInText);
        cancelText = dialog.findViewById(R.id.cancelText);

        //SET LAYOUT MANAGER AND ADAPTER ON RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.chooseGameRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    //ADMIN MENU OPTIONS FOR ADDING, EDITING OR DELETING ROOMS, PUZZLES, AND USERS AND ACCESS TO THE LOG TABLES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dialog.show();
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        signInText.setOnClickListener(v -> {
            password = dialog.findViewById(R.id.password);
            //CHECK IF REPOSITORY IS EMPTY
            if (repository.getmAllPasswords().isEmpty()) {
                Password first = new Password(1, "Admin", "8675");
                try {
                    repository.insert(first);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
                //CHECK REPOSITORY FOR MATCHING PASSWORD
                for (Password pass : repository.getmAllPasswords()) {
                    if (pass.getPassword().equals(String.valueOf(password.getText()))) {
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
                    else {
                        Toast msg = Toast.makeText(MainActivity.this, "Passcode not found", Toast.LENGTH_LONG);
                        msg.show();
                    }
                }
                password.setText("");
        });

        cancelText.setOnClickListener(v -> {
            password = dialog.findViewById(R.id.password);
            password.setText("");
            dialog.dismiss();
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
}