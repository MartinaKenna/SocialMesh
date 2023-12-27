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

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.ui.main.EventFragment;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.EventsViewHolder> {
    private static final String TAG = RecyclerViewEventsAdapter.class.getSimpleName();
    private final int viewType;
    private final  List<Event> eventsList; // Lista originale
    private List<Event> filteredList; // Lista filtrata
    private int genre;


    public RecyclerViewEventsAdapter(List<Event> eventsList, int viewType) {
        this.eventsList = eventsList;
        this.viewType = viewType;
        this.filteredList= new ArrayList<>(eventsList);

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

    public void clearFilters() { filteredList.clear();
        filteredList.addAll(eventsList);
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
            Glide.with(itemView.getContext()).load(image).into(imageView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}