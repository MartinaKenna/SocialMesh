package it.unimib.socialmesh.adapter;

import android.content.Context;
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

public class RecyclerViewAllEvents extends RecyclerView.Adapter<RecyclerViewAllEvents.EventsViewHolder>{
    private static final String TAG = RecyclerViewAllEvents.class.getSimpleName();
    private final int viewType;
    private final  List<Event> eventsList; // Lista originale
    private List<Event> filteredList; // Lista filtrata
    private int genre, km;
    private final RecyclerViewAllEvents.OnItemClickListener onItemClickListener;
    private Double userLongitude, userLatitude;
    Context context;

    public interface OnItemClickListener {
        void onEventItemClick(Event event);
    }


    public RecyclerViewAllEvents(Context context, Double latitude, Double longitude, List<Event> eventsList, int viewType,
                                 RecyclerViewAllEvents.OnItemClickListener onItemClickListener) {
        this.eventsList = eventsList;
        this.viewType = viewType;
        this.filteredList = new ArrayList<>(eventsList);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        this.userLongitude = longitude;
        this.userLatitude = latitude;
        this.km = 10000000;
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

    public static double distance(double startLat, double startLong, double endLat, double endLong) {
        int earthRadius = 6371; // in chilometri

        double latDistance = Math.toRadians(endLat - startLat);
        double longDistance = Math.toRadians(endLong - startLong);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
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


    public void setKM(Integer km){
        this.km=km;
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
    public RecyclerViewAllEvents.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = viewType == 0 ? R.layout.list_nearyou : R.layout.event_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new RecyclerViewAllEvents.EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAllEvents.EventsViewHolder holder, int position) {
        Event event = filteredList.get(position);
        //eventuale selezione per far stampare certe cose solo in una recycler o in entrambe
        if (viewType == 0) {
            if(userLatitude != null && userLongitude != null){
                for (Event events : filteredList) {
                    double eventLatitude = event.getLatitude();
                    double eventLongitude = event.getLongitude();
                    double distance = distance(userLatitude, userLongitude, eventLatitude, eventLongitude);
                    // possiamo aggiungere la possibilità di filtrare per 5km, 10km, 50km, illimitato
                    if (distance <= km) { //10km
                        holder.bind(event.getName1(), event.getDates1(), event.getUrlImages());
                    }
                }
            }
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

    public void updateLocation(Double userLatitude, Double userLongitude){
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
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
            // Chiamare il listener solo se è stato impostato
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onEventItemClick(filteredList.get(position));
                }
            }
        }
    }
}