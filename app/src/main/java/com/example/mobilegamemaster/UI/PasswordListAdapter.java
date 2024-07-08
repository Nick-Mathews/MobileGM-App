package com.example.mobilegamemaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegamemaster.Entities.Password;
import com.example.mobilegamemaster.R;

import java.util.List;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.PasswordViewHolder> {
    //CREATE LIST OF PASSWORDS, CONTEXT AND INFLATER
    private List<Password> mPasswords;
    private final Context context;
    private final LayoutInflater mInflater;

    //ADAPTER CONSTRUCTOR
    public PasswordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    //VIEW HOLDER CLASS FOR ADAPTER
    public class PasswordViewHolder extends RecyclerView.ViewHolder {
        private final TextView passwordView;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            passwordView = itemView.findViewById(R.id.passwordTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Password currentPassword = mPasswords.get(position);
                    Intent intent = new Intent(context, EditPasswords.class);
                    intent.putExtra("id", currentPassword.getPasswordID());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public PasswordListAdapter.PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.password_list_item, parent, false);
        return new PasswordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordListAdapter.PasswordViewHolder holder, int position) {
        if (mPasswords != null) {
            Password currentPassword = mPasswords.get(position);
            String name = currentPassword.getUserName();
            holder.passwordView.setText(name);
        }
        else {
            String name = "No username";
            holder.passwordView.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        if (mPasswords != null) {
            return mPasswords.size();
        }
        else {
            return 0;
        }
    }

    public void setPasswords(List<Password> passwords) {
        mPasswords = passwords;
        notifyDataSetChanged();
    }
}
