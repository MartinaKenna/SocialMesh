package it.unimib.socialmesh.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
            ImageView profile_pic;

            UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userNameTextView = itemView.findViewById(R.id.textview_username);
                profile_pic = itemView.findViewById(R.id.profile_image);
            }

            void bind(String userName, OnItemClickListener clickListener, String userId) {
                userNameTextView.setText(userName);
                StorageReference userImageRef = FirebaseStorage.getInstance().getReference().child("pictures").child(userId).child("profilePic.jpg");
                userImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Carica l'immagine nell'ImageView utilizzando Glide o un'altra libreria di caricamento delle immagini
                    Glide.with(itemView.getContext())
                            .load(uri)
                            .placeholder(R.drawable.baseline_error_black_24dp) // Immagine di caricamento placeholder
                            .error(R.drawable.baseline_error_black_24dp) // Immagine di errore nel caso di problemi di caricamento
                            .apply(RequestOptions.circleCropTransform())
                            .into(profile_pic); // Sostituisci 'profileImageView' con il tuo ImageView
                }).addOnFailureListener(exception -> {
                    // Gestisci eventuali errori durante il recupero dell'URL dell'immagine o il caricamento dell'immagine
                    Log.e("PartecipantsAdapter", "Errore durante il caricamento dell'immagine: " + exception.getMessage());
                });
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onUserClick(userId);
                    }
                });
            }
        }
    }

