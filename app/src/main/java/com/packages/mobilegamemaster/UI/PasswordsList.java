package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.R.layout.activity_passwords_list;
import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Password;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.util.List;

public class PasswordsList extends AppCompatActivity {
    //CREATE REPOSITORY AND PASSWORD LIST
    Repository repository;
    List<Password> allPasswords;
    Dialog startupDialog3;
    Button okButton3, finishButton, addUserButton;
    CheckBox dialogCheckBox3;
    boolean dialog3Checked;
    TextView startupText3;
    RecyclerView recyclerView;
    PasswordListAdapter passwordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(activity_passwords_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY AND PASSWORDS LIST
        repository = new Repository(getApplication());
        allPasswords = repository.getmAllPasswords();

        //SETUP FIRST TIME STARTUP DIALOG
        startupDialog3 = new Dialog(PasswordsList.this);
        startupDialog3.setContentView(R.layout.dialog_startup);
        okButton3 = startupDialog3.findViewById(R.id.saveButton);
        dialogCheckBox3 = startupDialog3.findViewById(R.id.dialogCheckBox);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dialog3Checked = settings.getBoolean("dialog3Checked", false);

        if (!dialog3Checked) {
            startupText3 = startupDialog3.findViewById(R.id.dialog_startup_textview);
            startupText3.setText(R.string.edit_users_intro);
            startupDialog3.show();
            okButton3.setOnClickListener(v -> {
                startupDialog3.dismiss();
                if (dialogCheckBox3.isChecked()) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("dialog3Checked", true);
                    editor.apply();
                }
            });
        }

        //SET PASSWORD LIST ON ADAPTER; SET LAYOUT MANGER AND ADAPTER ON RECYCLERVIEW
        recyclerView = findViewById(R.id.passwordListRecyclerView);
        passwordListAdapter = new PasswordListAdapter(this);
        passwordListAdapter.setPasswords(allPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordListAdapter);

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordsList.this, MainActivity.class);
            startActivity(intent);
        });

        //CREATE AND SET CLICK LISTENER FOR ADD USER BUTTON
        addUserButton = findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordsList.this, EditPasswords.class);
            intent.putExtra("id", allPasswords.get(allPasswords.size() -1 ).getPasswordID() + 1);
            startActivity(intent);

        });
    }
    //ON RESUME FUNCTION THAT REPOPULATES AND REFRESHES THE ADAPTER
    @Override
    public void onResume() {
        super.onResume();
        allPasswords = repository.getmAllPasswords();
        recyclerView = findViewById(R.id.passwordListRecyclerView);
        passwordListAdapter = new PasswordListAdapter(this);
        passwordListAdapter.setPasswords(allPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordListAdapter);
    }
}