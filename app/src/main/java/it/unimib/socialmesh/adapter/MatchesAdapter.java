package it.unimib.socialmesh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.main.ChatActivity;
//import it.unimib.socialmesh.ui.main.ChatActivity;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<User> matchesList;

    public MatchesAdapter(Context context, ArrayList<User> matchesList) {
        this.context = context;
        this.matchesList = matchesList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = matchesList.get(position);
        holder.textName.setText(currentUser.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", currentUser.getName());
                intent.putExtra("email", currentUser.getEmail());
                context.startActivity(intent);
            }
        });

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textName;

        public UserViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txt_name);
        }
    }
}
