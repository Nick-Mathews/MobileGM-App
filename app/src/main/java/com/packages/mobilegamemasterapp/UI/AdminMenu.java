package com.packages.mobilegamemasterapp.UI;

import static com.packages.mobilegamemasterapp.UI.MainActivity.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.packages.mobilegamemasterapp.R;

public class AdminMenu extends AppCompatActivity {
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
        String settingsName = settings.getString("adminName", "Admin");
        String adminName = getIntent().getStringExtra("name");
        if ((adminName != null) && (!(adminName.equals(settingsName)))) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("adminName", adminName);
            editor.apply();
            settingsName = settings.getString("adminName", "Admin");
        }
        TextView nameView = findViewById(R.id.adminNameText);
        nameView.setText(settingsName);

        //SET PROGRESS BAR VIEW
        ProgressBar pgBar = findViewById(R.id.progressBar);

        //SET BUTTONS AND LISTENERS
        Button menuAddRoom = findViewById(R.id.menuAddRoom);
        Button menuEditRoom = findViewById(R.id.menuEditRoom);
        Button menuEditPassword = findViewById(R.id.menuEditPassword);
        Button menuGameLogs = findViewById(R.id.menuGameLogs);
        Button menuStartupReset = findViewById(R.id.menuStartupReset);
        Button menuSignOut = findViewById(R.id.menuSignOut);

        menuAddRoom.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(AdminMenu.this, AddRoom.class);
            startActivity(intent);
        });

        menuEditRoom.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(AdminMenu.this, RoomList.class);
            startActivity(intent);
        });

        menuEditPassword.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(AdminMenu.this, PasswordList.class);
            startActivity(intent);
        });

        menuGameLogs.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(AdminMenu.this, GameLogs.class);
            startActivity(intent);
        });

        menuSignOut.setOnClickListener(v->{
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            Intent intent = new Intent(AdminMenu.this, MainActivity.class);
            startActivity(intent);
        });

        menuStartupReset.setOnClickListener(v-> {
            pgBar.setVisibility(View.VISIBLE);
            pgBar.bringToFront();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialog1Checked", false);
            editor.putBoolean("dialog2Checked", false);
            editor.putBoolean("dialog3Checked", false);
            editor.putBoolean("dialog4Checked", false);
            editor.putBoolean("dialog5Checked", false);
            editor.apply();
            pgBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Tutorial pop-ups enabled", Toast.LENGTH_LONG).show();
        });

        //HANDLE BACK GESTURE/BUTTON
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                Intent intent = new Intent(AdminMenu.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}