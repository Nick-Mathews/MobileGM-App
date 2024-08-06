package com.packages.mobilegamemaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.packages.mobilegamemaster.Entities.Room;
import com.packages.mobilegamemaster.R;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.EditViewHolder> {
    //CREATE LIST OF ROOMS, CONTEXT AND INFLATER
    private List<Room> mRooms;
    private final Context context;
    private final LayoutInflater mInflater;
    ProgressBar pgBar;

    //ADAPTER CONSTRUCTOR
    public RoomListAdapter(Context context, ProgressBar pgBar) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.pgBar = pgBar;
    }

    //VIEW HOLDER CLASS FOR ADAPTER
    public class EditViewHolder extends RecyclerView.ViewHolder {
        private final TextView editView;


        public EditViewHolder(@NonNull View itemView) {
            super(itemView);
            editView = itemView.findViewById(R.id.editTextView);
            itemView.setOnClickListener(v -> {
                pgBar.setVisibility(View.VISIBLE);
                pgBar.bringToFront();
                int position = getAdapterPosition();
                Room currentRoom = mRooms.get(position);
                Intent intent = new Intent(context, PuzzleList.class);
                intent.putExtra("name", currentRoom.getRoomName());
                intent.putExtra("id", currentRoom.getRoomID());
                context.startActivity(intent);
            });
        }
    }
    @NonNull
    @Override
    public RoomListAdapter.EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.edit_room_list_item, parent, false);
        return new EditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListAdapter.EditViewHolder holder, int position) {
        if (mRooms != null) {
            Room currentRoom = mRooms.get(position);
            String name = currentRoom.getRoomName();
            holder.editView.setText(name);
        }
        else {
            String name = "No room name";
            holder.editView.setText(name);

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

    //FUNCTION THAT SETS ADAPTER ROOM LIST IN ACTIVITY
    public void setRooms(List<Room> rooms) {
        mRooms = rooms;
        notifyDataSetChanged();
    }
}
