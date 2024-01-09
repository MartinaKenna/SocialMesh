package it.unimib.socialmesh.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.jsonFields.Location;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.EventsViewHolder> {
    private static final String TAG = RecyclerViewEventsAdapter.class.getSimpleName();
    private final int viewType;
    private final  List<Event> eventsList; // Lista originale
    private List<Event> filteredList; // Lista filtrata
    private int genre;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEventItemClick(Event event);
    }


    public RecyclerViewEventsAdapter(List<Event> eventsList, int viewType,
                                     OnItemClickListener onItemClickListener) {
        this.eventsList = eventsList;
        this.viewType = viewType;
        this.filteredList= new ArrayList<>(eventsList);
        this.onItemClickListener = onItemClickListener;

    }

    public void filterByGenre(String genre) {
        filteredList.clear();
        for (Event event : eventsList) {
            if (event.getGenreName().equalsIgnoreCase(genre)) {
                filteredList.add(event);
            }
        }
        notifyDataSetChanged();
    }


    public void filterByPosition(double userLatitudine, double userLongitudine) {
        filteredList.clear();
        Location userPosition = new Location();
        userPosition.setLatitude(userLatitudine);
        userPosition.setLongitude(userLongitudine);
        for (Event event : eventsList) {
            double eventLatitude = event.getLatitude();
            double eventLongitude = event.getLongitude();

            //double distance = distance(userLatitudine, userLongitudine, eventLatitude, eventLongitude);

            // Controll0 se la distanza tra l'evento e la posizione dell'utente è <= 10 km
            // possiamo aggiungere la possibilità di filtrare per 5km, 10km, 50km, illimitato
            //if (distance <= 10) {
            //    filteredList.add(event);
            //}
        }
        notifyDataSetChanged();
    }

    public void filterByQuery(String query){
        filteredList.clear();
        for (Event event : eventsList) {
            if (event.getGenreName().equalsIgnoreCase(query)) {
                filteredList.add(event);
            }
            if (event.getName().equalsIgnoreCase(query)){
                filteredList.add(event);
            }
            if(event.getDates1().equalsIgnoreCase(query)){
                filteredList.add(event);
            }
        }
        if (filteredList.isEmpty()) {
            filteredList.addAll(eventsList);
        }
        notifyDataSetChanged();
    }

    public void setItems(List<Event> eventsList){
        filteredList.clear();
        filteredList.addAll(eventsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = viewType == 0 ? R.layout.list_nearyou : R.layout.event_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event event = filteredList.get(position);
        //eventuale selezione per far stampare certe cose solo in una recycler o in entrambe
        if (viewType == 0) {
            holder.bind(event.getName1(), event.getDates1(),event.getUrlImages());
        } else {
            holder.bind(event.getName1(), event.getDates1(),event.getUrlImages());
            }
        }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        }
        return 0;
    }
    public void updateData() {
        // Aggiorna la lista degli eventi
        eventsList.clear();
        eventsList.addAll(eventsList);

        // Notifica la RecyclerView
        notifyDataSetChanged();
    }
    public void clearFilters() {
        filteredList.clear();
        filteredList.addAll(eventsList);
        notifyDataSetChanged();
    }

    public void notifyRoba() {
        notifyDataSetChanged();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewName, textViewDate;
        private final ImageView imageView;

        public EventsViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item1);
            textViewDate = itemView.findViewById(R.id.item2);
            imageView = itemView.findViewById(R.id.item3);
            itemView.setOnClickListener(this);
        }

        public void bind(String name, String date, String image) {
            textViewName.setText(name);
            textViewDate.setText(date);
            CircularProgressDrawable drawable = new CircularProgressDrawable(itemView.getContext());
            drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            drawable.start();
            Glide.with(itemView.getContext())
                    .load(image)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called");

            // Chiamare il listener solo se è stato impostato
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onEventItemClick(eventsList.get(position));
                }
            }
        }
    }
}