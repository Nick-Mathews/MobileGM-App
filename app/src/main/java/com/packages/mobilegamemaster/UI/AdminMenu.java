package com.packages.mobilegamemaster.UI;

import static com.packages.mobilegamemaster.UI.MainActivity.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemaster.R;

public class AdminMenu extends AppCompatActivity {
    String adminName, settingsName;
    TextView nameView;
    Button menuAddRoom, menuEditRoom, menuEditPassword, menuGameLogs, menuSignOut;
    ProgressBar pgBar;
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
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settingsName = settings.getString("adminName", "Admin");
        adminName = getIntent().getStringExtra("name");
        if ((adminName != null) && (!(adminName.equals(settingsName)))) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("adminName", adminName);
            editor.apply();
            settingsName = settings.getString("adminName", "Admin");
        }
        nameView = findViewById(R.id.adminNameText);
        nameView.setText(settingsName);

        menuAddRoom = findViewById(R.id.menuAddRoom);
        menuEditRoom = findViewById(R.id.menuEditRoom);
        menuEditPassword = findViewById(R.id.menuEditPassword);
        menuGameLogs = findViewById(R.id.menuGameLogs);
        menuSignOut = findViewById(R.id.menuSignOut);

        menuAddRoom.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AdminMenu.this, AddRoom.class);
            startActivity(intent);
        });

        menuEditRoom.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AdminMenu.this, RoomList.class);
            startActivity(intent);
        });

        menuEditPassword.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AdminMenu.this, PasswordList.class);
            startActivity(intent);
        });

        menuGameLogs.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AdminMenu.this, GameLogs.class);
            startActivity(intent);
        });

        menuSignOut.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(AdminMenu.this, MainActivity.class);
            startActivity(intent);
        });

        //SET PROGRESS BAR VIEW
        pgBar = findViewById(R.id.progressBar);
    }
}