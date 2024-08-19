package com.packages.mobilegamemasterapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.packages.mobilegamemasterapp.Entities.Room;
import com.packages.mobilegamemasterapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainRoomAdapter extends RecyclerView.Adapter<MainRoomAdapter.RoomViewHolder> {
    //CREATE LIST OF ROOM, CONTEXT AND INFLATER
    private List<Room> mRooms;
    private final Context context;
    private final LayoutInflater mInflater;
    int position;

    public MainRoomAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private final TextView roomView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomView = itemView.findViewById(R.id.roomTextView);
            itemView.setOnClickListener(v -> {
                position = getAdapterPosition();
                final Room currentRoom = mRooms.get(position);
                Intent intent = new Intent(context, RoomStart.class);
                intent.putExtra("id", currentRoom.getRoomID());
                intent.putExtra("name", currentRoom.getRoomName());
                intent.putExtra("timer", currentRoom.getRoomTime());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public MainRoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.room_list_item, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRoomAdapter.RoomViewHolder holder, int position) {
        if (mRooms != null) {
            Room currentRoom = mRooms.get(position);
            String name = currentRoom.getRoomName();
            holder.roomView.setText(name);
        }
        else {
            String name = "No room name";
            holder.roomView.setText(name);
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

    //SET LIST OF ROOM FROM ACTIVITY
    public void setRooms(List<Room> rooms) {
        mRooms = rooms;
        notifyDataSetChanged();
    }
}
