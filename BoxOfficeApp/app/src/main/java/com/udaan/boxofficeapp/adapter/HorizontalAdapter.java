package com.udaan.boxofficeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.model.Cinema;

import java.util.List;

/**
 * Created by takusemba on 2017/08/03.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<Cinema> cinemaList;
    private HorizontalAdapterListener listener;

    public HorizontalAdapter(Context context, List<Cinema> cinemaList, HorizontalAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cinemaList = cinemaList;

    }

    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_event_card, viewGroup, false);
        return new HorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder holder, int position) {
        final Cinema cinema = cinemaList.get(position);
        holder.title.setText(cinema.getName());


        Glide.with(context)
                .load(cinema.getLeadImageUrl())

                .into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected city in callback
                listener.onEventSelected(cinemaList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return cinemaList.size();
    }

    public interface HorizontalAdapterListener {
        void onEventSelected(Cinema cinema);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        private TextView title;

        ViewHolder(final View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected city in callback
                    listener.onEventSelected(cinemaList.get(getAdapterPosition()));
                }
            });
        }
    }
}