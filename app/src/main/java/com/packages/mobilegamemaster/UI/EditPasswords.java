package com.packages.mobilegamemaster.UI;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.Entities.Password;
import com.packages.mobilegamemaster.R;
import com.packages.mobilegamemaster.database.Repository;

import java.util.List;

public class EditPasswords extends AppCompatActivity {
    //CREATE GLOBAL OBJECTS
    Password currentPassword;
    String userName, password;
    TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_passwords);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY AND ALL PASSWORDS LIST
        Repository repository = new Repository(getApplication());
        List<Password> allPasswords = repository.getmAllPasswords();
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //RETRIEVE INTENT FOR USE, CREATE PASSWORD OBJECT, RETRIEVE USERNAME AND PASSWORD
        int passwordID = getIntent().getIntExtra("id", -1);
        if (passwordID == allPasswords.get(allPasswords.size() - 1).getPasswordID() + 1) {
            userNameView = findViewById(R.id.userNameText);
            String newUser = "New User";
            userNameView.setText(newUser);
        }
        else {
            currentPassword = repository.getmPassword(passwordID);
            userName = currentPassword.getUserName();
            password = currentPassword.getPassword();

            //FIND AND SET USERNAME TEXTVIEW
            userNameView = findViewById(R.id.userNameText);
            userNameView.setText(userName);
            }

            //CREATE CONTAINERS FOR THE EDITTEXT ENTRIES, POPULATE WITH CURRENT NAME
            EditText userNameEntry = findViewById(R.id.editUsernameText);
            EditText currentPasswordEntry = findViewById(R.id.currentPasswordText);
            EditText newPasswordEntry = findViewById(R.id.newPasswordText);

            userNameEntry.setText(userName);


        //CREATE BUTTON AND LISTENER FOR SAVE USER BUTTON
        Button saveButton = findViewById(R.id.saveUserButton);
        saveButton.setOnClickListener(v -> {
                        saveButton.setEnabled(false);
                        String userNameText;
                        String newPasswordText;
                        //CHECK FOR VALID PASSWORD ID
                        if (passwordID==-1) {
                            Toast msg = Toast.makeText(EditPasswords.this, "Your username is invalid", Toast.LENGTH_LONG);
                            msg.show();
                            saveButton.setEnabled(true);
                        }
                        //CHECK PASSWORD ID FOR NEW USER
                        else {
                            if (passwordID == allPasswords.get(allPasswords.size() - 1).getPasswordID() + 1) {
                                userNameText = String.valueOf(userNameEntry.getText());
                                newPasswordText = String.valueOf(newPasswordEntry.getText());
                                if (userNameText.isEmpty() || newPasswordText.isEmpty()) {
                                    Toast msg = Toast.makeText(EditPasswords.this, "You must enter username and new password before saving", Toast.LENGTH_LONG);
                                    msg.show();
                                    saveButton.setEnabled(true);
                                }
                                //INSERT NEW USER
                                else {
                                    new AlertDialog.Builder(this)
                                            .setMessage(R.string.db_dialog_message)
                                            .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> saveButton.setEnabled(true))
                                            .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                                                pgBar.setVisibility(View.VISIBLE);
                                                pgBar.bringToFront();
                                                currentPassword = new Password(passwordID, userNameText, newPasswordText);
                                                try {
                                                    repository.insert(currentPassword);
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                                Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                                                startActivity(intent);
                                            }).show();
                                }
                                //CHECK FOR ALL ENTRY FIELDS AND MATCH CURRENT USER PASSWORDS PRIOR TO UPDATE
                            } else {
                                userNameText = String.valueOf(userNameEntry.getText());
                                String currentPasswordText = String.valueOf(currentPasswordEntry.getText());
                                newPasswordText = String.valueOf(newPasswordEntry.getText());
                                if (userNameText.isEmpty() || currentPasswordText.isEmpty() || newPasswordText.isEmpty()) {
                                    Toast msg = Toast.makeText(EditPasswords.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                                    msg.show();
                                    saveButton.setEnabled(true);
                                } else {
                                    if (currentPasswordText.equals(currentPassword.getPassword())) {
                                        new AlertDialog.Builder(this)
                                                .setMessage(R.string.db_dialog_message)
                                                .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> saveButton.setEnabled(true))
                                                .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                                                    pgBar.setVisibility(View.VISIBLE);
                                                    pgBar.bringToFront();
                                                    currentPassword.setUserName(userNameText);
                                                    currentPassword.setPassword(newPasswordText);
                                                    try {
                                                        repository.update(currentPassword);
                                                    } catch (Exception e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                                                    startActivity(intent);
                                                }).show();

                                    } else {
                                        Toast msg = Toast.makeText(EditPasswords.this, "You must enter the current password correctly", Toast.LENGTH_LONG);
                                        msg.show();
                                        saveButton.setEnabled(true);

                                    }
                                }
                            }
                        }
        });

        //CREATE BUTTON AND LISTENER FOR CANCEL CHANGES BUTTON
        Button cancelChangesButton = findViewById(R.id.cancelChangesButton);
        cancelChangesButton.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(EditPasswords.this, PasswordList.class);
            startActivity(intent);

        });

        //CREATE BUTTON AND LISTENER FOR DELETE USER BUTTON
        Button deleteUserButton = findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.db_dialog_message)
                .setNegativeButton(R.string.db_dialog_negative, (dialog, which) -> {
                })
                .setPositiveButton(R.string.db_dialog_positive, (dialog, which) -> {
                    pgBar.setVisibility(View.VISIBLE);
                    pgBar.bringToFront();
                    if (String.valueOf(userNameView.getText()).equals("New User")) {
                        Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                        startActivity(intent);
                    }
                    else {
                        try {
                            repository.delete(currentPassword);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                        startActivity(intent);
                    }
                }).show());
        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                startActivity(intent);
            }
        });
    }

}