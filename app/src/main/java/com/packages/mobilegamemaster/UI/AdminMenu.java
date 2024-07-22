package com.packages.mobilegamemaster.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.R;

public class AdminMenu extends AppCompatActivity {
    String adminName;
    TextView nameView;
    Button menuAddRoom, menuEditRoom, menuEditPassword, menuGameLogs, menuSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //GET INTENT AND SET ADMIN NAME
        adminName = getIntent().getStringExtra("name");
        nameView = findViewById(R.id.adminNameText);
        nameView.setText(adminName);

        menuAddRoom = findViewById(R.id.menuAddRoom);
        menuEditRoom = findViewById(R.id.menuEditRoom);
        menuEditPassword = findViewById(R.id.menuEditPassword);
        menuGameLogs = findViewById(R.id.menuGameLogs);
        menuSignOut = findViewById(R.id.menuSignOut);

        menuAddRoom.setOnClickListener(v->{
            Intent intent = new Intent(AdminMenu.this, AddRoom.class);
            startActivity(intent);
        });

        menuEditRoom.setOnClickListener(v->{
            Intent intent = new Intent(AdminMenu.this, RoomList.class);
            startActivity(intent);
        });

        menuEditPassword.setOnClickListener(v->{
            Intent intent = new Intent(AdminMenu.this, PasswordList.class);
            startActivity(intent);
        });

        menuGameLogs.setOnClickListener(v->{
            Intent intent = new Intent(AdminMenu.this, GameLogs.class);
            startActivity(intent);
        });

        menuSignOut.setOnClickListener(v->{
            Intent intent = new Intent(AdminMenu.this, MainActivity.class);
            startActivity(intent);
        });
    }
}