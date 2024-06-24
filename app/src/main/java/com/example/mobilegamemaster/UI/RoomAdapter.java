package com.example.mobilegamemaster.UI;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilegamemaster.Entities.Room;
import com.example.mobilegamemaster.R;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> mRooms;
    private final Context context;
    private final LayoutInflater mInflater;

    public RoomAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private final TextView roomView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomView = itemView.findViewById(R.id.roomTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Room currentRoom = mRooms.get(position);
                    Intent intent = new Intent(context, RoomStart.class);
                    intent.putExtra("id", currentRoom.getRoomID());
                    intent.putExtra("name", currentRoom.getRoomName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.room_list_item, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.RoomViewHolder holder, int position) {
        if (mRooms != null) {
            Room currentRoom = mRooms.get(position);
            String name = currentRoom.getRoomName();
            holder.roomView.setText(name);
        }
        else {
            holder.roomView.setText("No room name");
        }

    }

    @Override
    public int getItemCount() {
        if (mRooms != null) {
            return mRooms.size();
        }
        else {
            return 0;
        }

    }

    public void setRooms(List<Room> rooms) {
        mRooms = rooms;
        notifyDataSetChanged();
    }
}
