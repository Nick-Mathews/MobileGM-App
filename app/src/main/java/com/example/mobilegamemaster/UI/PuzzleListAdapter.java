package com.example.mobilegamemaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Puzzle;
import com.example.mobilegamemaster.R;
import java.util.List;

public class PuzzleListAdapter extends RecyclerView.Adapter<PuzzleListAdapter.PuzzleListViewHolder> {
    //CREATE LIST OF PUZZLES, CONTEXT, AND INFLATER
    private List<Puzzle> mPuzzles;
    private final Context context;
    private final LayoutInflater mInflater;


    public PuzzleListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class PuzzleListViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public PuzzleListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.editTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Puzzle currentPuzzle = mPuzzles.get(position);

                    int roomID = currentPuzzle.getRoomID();
                    int puzzleID = currentPuzzle.getPuzzleID();

                    Intent intent = new Intent(context, EditPuzzles.class);
                    intent.putExtra("room_id", roomID);
                    intent.putExtra("puzzle_id", puzzleID);
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public PuzzleListAdapter.PuzzleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.edit_list_item, parent, false);
        return new PuzzleListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PuzzleListViewHolder holder, int position) {
        if (mPuzzles != null) {
            Puzzle currentPuzzle = mPuzzles.get(position);
            String name = "Puzzle " + currentPuzzle.getPuzzleNum();
            holder.textView.setText(name);
        }
        else {
            String name = "No puzzle name";
            holder.textView.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if (mPuzzles != null) {
            return mPuzzles.size();
        }
        else {
            return 0;
        }
    }

    //SET LIST OF PUZZLES FROM ACTIVITY
    public void setPuzzles(List<Puzzle> puzzles) {
        mPuzzles = puzzles;
        notifyDataSetChanged();
    }


}
