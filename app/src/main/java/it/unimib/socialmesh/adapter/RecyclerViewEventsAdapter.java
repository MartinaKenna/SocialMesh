package it.unimib.socialmesh.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.EventsViewHolder>{
    public interface OnItemClickListener {
        void onEventsItemClick(Event event);
        void onFavoriteButtonPressed(int position);
    }

    private final List<Event> eventsList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public RecyclerViewEventsAdapter(List<Event> eventsList, Application application,
                                   OnItemClickListener onItemClickListener) {
        this.eventsList = eventsList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.events_item, parent, false);

        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        holder.bind(eventsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (eventsList != null) {
            return eventsList.size();
        }
        return 0;
    }


    public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewName, textViewDate;
        private final Button buttonclick;

        public EventsViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textview_name);
            textViewDate = itemView.findViewById(R.id.textview_date);
            buttonclick = itemView.findViewById(R.id.buttonclick);
            itemView.setOnClickListener(this);
            buttonclick.setOnClickListener(this);
        }

        public void bind(Event event) {
            textViewName.setText(event.getName());
            textViewDate.setText(event.getStartDate());
        }

        @Override
        public void onClick(View v) {
        }

    }


}
