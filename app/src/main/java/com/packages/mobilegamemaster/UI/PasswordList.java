package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.R.layout.activity_passwords_list;
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
import androidx.activity.OnBackPressedCallback;
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

public class PasswordList extends AppCompatActivity {
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
        Repository repository = new Repository(getApplication());
        List<Password> allPasswords = repository.getmAllPasswords();
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //SETUP FIRST TIME STARTUP DIALOG
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialog3Checked = settings.getBoolean("dialog3Checked", false);

        if (!dialog3Checked) {
            Dialog startupDialog3 = new Dialog(PasswordList.this);
            startupDialog3.setContentView(R.layout.dialog_startup);
            Button okButton3 = startupDialog3.findViewById(R.id.okButton);
            CheckBox dialogCheckBox3 = startupDialog3.findViewById(R.id.dialogCheckBox);
            TextView startupText3 = startupDialog3.findViewById(R.id.dialog_startup_textview);
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
        RecyclerView recyclerView = findViewById(R.id.passwordListRecyclerView);
        PasswordListAdapter passwordListAdapter = new PasswordListAdapter(this, pgBar);
        passwordListAdapter.setPasswords(allPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordListAdapter);

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(PasswordList.this, AdminMenu.class);
            startActivity(intent);
        });

        //CREATE AND SET CLICK LISTENER FOR ADD USER BUTTON
        Button addUserButton = findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(PasswordList.this, EditPasswords.class);
            intent.putExtra("id", allPasswords.get(allPasswords.size() -1 ).getPasswordID() + 1);
            startActivity(intent);
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(PasswordList.this, AdminMenu.class);
                startActivity(intent);
            }
        });
    }
}