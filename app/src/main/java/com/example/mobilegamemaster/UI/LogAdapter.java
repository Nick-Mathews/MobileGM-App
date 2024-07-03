package com.example.mobilegamemaster.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Timer;
import com.example.mobilegamemaster.R;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    //CREATE LIST OF TIMERS AND INFLATER
    private List<Timer> mTimers;
    private final LayoutInflater mInflater;

    //ADAPTER CONSTRUCTOR
    public LogAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    //VIEW HOLDER CLASS FOR ADAPTER
    public class LogViewHolder extends RecyclerView.ViewHolder {
        private final TextView logNameView;
        private final TextView logTimeLeft;
        private final TextView logGameDate;
        private final TextView logStartTime;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            logNameView = itemView.findViewById(R.id.logName);
            logTimeLeft = itemView.findViewById(R.id.logTimeLeft);
            logGameDate = itemView.findViewById(R.id.logGameDate);
            logStartTime = itemView.findViewById(R.id.logStartTime);
        }
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.log_list_item, parent, false);
        return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        if (mTimers != null) {
            Timer currentTimer = mTimers.get(position);

            String name = currentTimer.getRoomName();
            String timeLeft = currentTimer.getTimeLeft();
            String gameDate = currentTimer.getGameDate();
            String startTime = currentTimer.getStartTime();

            holder.logNameView.setText(name);
            holder.logTimeLeft.setText(timeLeft);
            holder.logGameDate.setText(gameDate);
            holder.logStartTime.setText(startTime);
        }
    }

    @Override
    public int getItemCount() {
        if (mTimers != null) {
            return mTimers.size();
        }
        else {
            return 0;
        }
    }

    //FUNCTION THAT SETS ADAPTER TIMER LIST FROM ACTIVITY
    public void setmTimers(List<Timer> timers) {
        this.mTimers = timers;
    }

}
