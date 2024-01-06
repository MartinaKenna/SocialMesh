package it.unimib.socialmesh.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.User;



    public class PartecipantsAdapter extends RecyclerView.Adapter<PartecipantsAdapter.UserViewHolder> {

        private List<String> userIdList;
        private List<String> userNameList;
        private OnItemClickListener clickListener;

        public interface OnItemClickListener {
            void onUserClick(String userId);
        }

        public PartecipantsAdapter(List<String> userIdList, List<String> userNameList, OnItemClickListener clickListener) {
            this.userIdList = userIdList;
            this.userNameList = userNameList;
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            String userName = userNameList.get(position);
            holder.bind(userName, clickListener, userIdList.get(position));
        }

        @Override
        public int getItemCount() {
            return userIdList.size();
        }

        static class UserViewHolder extends RecyclerView.ViewHolder {
            TextView userNameTextView;

            UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userNameTextView = itemView.findViewById(R.id.textview_username);
            }

            void bind(String userName, OnItemClickListener clickListener, String userId) {
                userNameTextView.setText(userName);

                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onUserClick(userId);
                    }
                });
            }
        }
    }

