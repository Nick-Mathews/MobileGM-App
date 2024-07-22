package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    Repository repository;
    List<Password> allPasswords;
    Password currentPassword;
    String userName, password,userNameText, newPasswordText, currentPasswordText;
    int passwordID;
    Button saveButton, cancelChangesButton, deleteUserButton;
    TextView userNameView;
    EditText userNameEntry, currentPasswordEntry, newPasswordEntry;

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
        repository = new Repository(getApplication());
        allPasswords = repository.getmAllPasswords();



        //RETRIEVE INTENT FOR USE, CREATE PASSWORD OBJECT, RETRIEVE USERNAME AND PASSWORD
        passwordID = getIntent().getIntExtra("id", -1);
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
            userNameEntry = findViewById(R.id.editUsernameText);
            currentPasswordEntry = findViewById(R.id.currentPasswordText);
            newPasswordEntry = findViewById(R.id.newPasswordText);

            userNameEntry.setText(userName);


        //CREATE BUTTON AND LISTENER FOR SAVE USER BUTTON
        saveButton = findViewById(R.id.saveUserButton);
        saveButton.setOnClickListener(v -> {
            saveButton.setEnabled(false);
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
                            currentPassword = new Password(passwordID, userNameText, newPasswordText);
                            try {
                                repository.insert(currentPassword);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                            startActivity(intent);
                        }
                        //CHECK FOR ALL ENTRY FIELDS AND MATCH CURRENT USER PASSWORDS PRIOR TO UPDATE
                    } else {
                        userNameText = String.valueOf(userNameEntry.getText());
                        currentPasswordText = String.valueOf(currentPasswordEntry.getText());
                        newPasswordText = String.valueOf(newPasswordEntry.getText());
                        if (userNameText.isEmpty() || currentPasswordText.isEmpty() || newPasswordText.isEmpty()) {
                            Toast msg = Toast.makeText(EditPasswords.this, "You must complete all fields before saving", Toast.LENGTH_LONG);
                            msg.show();
                            saveButton.setEnabled(true);
                        } else {
                            if (currentPasswordText.equals(currentPassword.getPassword())) {
                                currentPassword.setUserName(userNameText);
                                currentPassword.setPassword(newPasswordText);
                                try {
                                    repository.update(currentPassword);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                Intent intent = new Intent(EditPasswords.this, PasswordList.class);
                                startActivity(intent);

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
        cancelChangesButton = findViewById(R.id.cancelChangesButton);
        cancelChangesButton.setOnClickListener(v -> {
           Intent intent = new Intent(EditPasswords.this, PasswordList.class);
           startActivity(intent);

        });

        //CREATE BUTTON AND LISTENER FOR DELETE USER BUTTON
        deleteUserButton = findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(v -> {
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
    });
    }
}