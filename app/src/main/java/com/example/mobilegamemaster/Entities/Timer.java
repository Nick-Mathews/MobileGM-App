package com.example.mobilegamemaster.Entities;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.mobilegamemaster.database.Repository;
import com.google.android.material.timepicker.TimeFormat;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName="timers")
public class Timer {
    @PrimaryKey(autoGenerate = true)
    int timerID;
    int roomID;
    Date startTime;
    Date endTime;
    Date gameDate;
    String simpleGameDate;
    String simpleStartTime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);

    public Timer(int roomID) {
        this.roomID = roomID;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Date getGameDate() {
        return gameDate;
    }
    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public CountDownTimer startCountdownTimer(TextView textView) throws RuntimeException {
        startTime = Calendar.getInstance().getTime();
        try {
            simpleStartTime = timeFormat.format(startTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            simpleGameDate = dateFormat.format(startTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CountDownTimer roomTimer = new CountDownTimer(3600000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String timeText = f.format(hour) + ":" + f.format(min) + ":" + f.format(sec);
                textView.setText(timeText);
            }


            public void onFinish() {
                String endTime = "00:00:00";
                textView.setText(endTime);
            }
        };
        return roomTimer;
    }

    public void endCountdownTimer(CountDownTimer timer, TextView textView, String finalTime) {
        timer.cancel();
        textView.setText(finalTime);
    }
}
