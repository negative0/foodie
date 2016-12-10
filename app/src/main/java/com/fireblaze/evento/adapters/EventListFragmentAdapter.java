package com.fireblaze.evento.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fireblaze.evento.models.Event;
import com.fireblaze.evento.viewholders.EventViewHolder;
import com.google.firebase.database.Query;

/**
 * Created by fireblaze on 9/12/16.
 */

public class EventListFragmentAdapter extends FirebaseRecyclerAdapter<Event, EventViewHolder> {
    Context mContext;


    @Override
    protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
        viewHolder.bindToPost(mContext, model, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Event clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public EventListFragmentAdapter(Class<Event> modelClass, int modelLayout, Class<EventViewHolder> viewHolderClass, Query ref,Context c) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = c;
    }
}