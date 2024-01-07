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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
public class SimpleEventsAdapter extends RecyclerView.Adapter<SimpleEventsAdapter.ViewHolder> {

    private final List<Event> eventsList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public SimpleEventsAdapter(List<Event> eventsList, OnItemClickListener listener) {
        this.eventsList = eventsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventsList.get(position);
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(event.getRemoteId()).child("url");
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String eventImageUrl = snapshot.getValue(String.class);
                    holder.bind(event, eventImageUrl, listener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestione degli errori durante il recupero dei dati
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventNameTextView;
        private final ImageView imageViewEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewEvent = itemView.findViewById(R.id.imageview_event);
            eventNameTextView = itemView.findViewById(R.id.textview_event_title);
        }

        public void bind(final Event event, String image, final OnItemClickListener listener) {
            eventNameTextView.setText(event.getName1());

            Glide.with(itemView.getContext())
                    .load(image) // Assicurati che 'image' sia un URL valido per l'immagine dell'evento
                    .placeholder(R.drawable.baseline_error_black_24dp) // Immagine di caricamento placeholder
                    .error(R.drawable.baseline_error_black_24dp) // Immagine di errore nel caso di problemi di caricamento
                    .into(imageViewEvent);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("adapty", "onClick called");

                    // Chiamare il listener solo se è stato impostato
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(eventsList.get(position));
                            Log.d("adapty", event.getName1());
                        }
                    }
                }
            });
        }
    }
}
