package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passwords_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //POPULATE REPOSITORY AND PASSWORDS LIST
        repository = new Repository(getApplication());
        allPasswords = repository.getmAllPasswords();

        //SET PASSWORD LIST ON ADAPTER; SET LAYOUT MANGER AND ADAPTER ON RECYCLERVIEW
        RecyclerView recyclerView = findViewById(R.id.passwordListRecyclerView);
        PasswordListAdapter passwordListAdapter = new PasswordListAdapter(this);
        passwordListAdapter.setPasswords(allPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordListAdapter);

        //CREATE AND SET CLICK LISTENER FOR FINISH BUTTON
        Button finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordsList.this, MainActivity.class);
            startActivity(intent);
        });

        //CREATE AND SET CLICK LISTENER FOR ADD USER BUTTON
        Button addUserButton = findViewById(R.id.addUserButton);
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
        List<Password> allPasswords = repository.getmAllPasswords();
        RecyclerView recyclerView = findViewById(R.id.passwordListRecyclerView);
        PasswordListAdapter passwordListAdapter = new PasswordListAdapter(this);
        passwordListAdapter.setPasswords(allPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passwordListAdapter);
    }
}