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

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.ui.main.EventFragment;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.EventsViewHolder> {
    private static final String TAG = RecyclerViewEventsAdapter.class.getSimpleName();
    private final List<Event> eventsList;
    private final int viewType;
    private int genre;

    public RecyclerViewEventsAdapter(List<Event> eventsList, int viewType, int genre) {
        this.eventsList = eventsList;
        this.viewType = viewType;
        this.genre = genre;
    }

    public RecyclerViewEventsAdapter(List<Event> eventsList, int viewType) {
        this.eventsList = eventsList;
        this.viewType = viewType;
        this.genre = -1;
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
        Event event = eventsList.get(position);
        if (genre == -1) {
            if (viewType == 0) {
               holder.bind(event.getName(""), event.getDates1(),event.getUrlImages());
            } else {
                holder.bind(event.getName(""), event.getDates1(),event.getUrlImages());
            }
        } else if (genre == 0) {
            if (viewType == 0) {
                if (event.getName("hiphoprap") != null) {
                   holder.bind(event.getName("hiphoprap"), event.getDates1(),event.getUrlImages());
                } else { //setto a 0 la visibilità, l'altezza e la larghezza dell'item ignorandolo
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }

            } else {
                if (event.getName("hiphoprap") != null) {
                   holder.bind(event.getName("hiphoprap"), event.getType(),event.getUrlImages());
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }

            }
        } else if (genre == 1) {
            if (viewType == 0) {
                if (event.getName("latin") != null) {
                    holder.bind(event.getName("latin"), event.getType(),event.getUrlImages());
                } else { //setto a 0 la visibilità, l'altezza e la larghezza dell'item ignorandolo
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }
            } else {
                if (event.getName("latin") != null) {
                   holder.bind(event.getName("latin"), event.getType(),event.getUrlImages());
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }
            }
        } else if (genre == 2) {
            if (viewType == 0) {
                if (event.getName("rock") != null) {
                  holder.bind(event.getName("rock"), event.getDates1(),event.getUrlImages());
                } else { //setto a 0 la visibilità, l'altezza e la larghezza dell'item ignorandolo
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }
            } else {
                if (event.getName("rock") != null) {
                  holder.bind(event.getName("rock"), event.getDates1(), event.getUrlImages());
                } else { //setto a 0 la visibilità, l'altezza e la larghezza dell'item ignorandolo
                    holder.itemView.setVisibility(View.GONE);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }
            }
        }



    }

    @Override
    public int getItemCount() {
        if (eventsList != null) {
            return eventsList.size();
        }
        return 0;
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