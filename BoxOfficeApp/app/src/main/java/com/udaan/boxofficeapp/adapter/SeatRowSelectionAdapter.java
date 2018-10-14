package com.udaan.boxofficeapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.model.MovieSeatRow;
import com.udaan.boxofficeapp.model.MovieSeatRow;

import java.util.List;

/**
 * Created by shrikanthravi on 01/03/18.
 */


public class SeatRowSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieSeatRow> movieSeatRowList;
    Context context;
    private SeatRowSelectionAdapterListener listener;
    public static int selectedpos=-1;
    public static ProgressBar selectedprogressBar = null;


    public  class MovieSeatRowHolder extends RecyclerView.ViewHolder {

        public TextView movieSeatRowTV;
        public ProgressBar progressBar;
        public MovieSeatRowHolder(View view) {
            super(view);

            movieSeatRowTV = view.findViewById(R.id.movieSeatRowTV);
            progressBar = view.findViewById(R.id.seatsProgress);

        }
    }

    public SeatRowSelectionAdapter(List<MovieSeatRow> verticalList, Context context, SeatRowSelectionAdapterListener listener) {
        this.movieSeatRowList = verticalList;
        this.context = context;
        this.listener=listener;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.seat_row_item, parent, false);
            return new MovieSeatRowHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final MovieSeatRow movieSeatRow = movieSeatRowList.get(position);
        final MovieSeatRowHolder movieSeatRowHolder = ((MovieSeatRowHolder) holder);
        movieSeatRowHolder.movieSeatRowTV.setText(movieSeatRow.getRow());
        movieSeatRowHolder.progressBar.setMax(100);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        Typeface bold = Typeface.createFromAsset(context.getAssets(), "fonts/product_sans_bold.ttf");

        movieSeatRowHolder.movieSeatRowTV.setTypeface(font);

        movieSeatRowHolder.progressBar.setProgress(100);
        if(movieSeatRow.isSelected()){
            movieSeatRowHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            movieSeatRowHolder.movieSeatRowTV.setTextColor(context.getResources().getColor(android.R.color.black));
            movieSeatRowHolder.movieSeatRowTV.setTypeface(bold);
        }
        else{
            movieSeatRowHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(android.R.color.tab_indicator_text)));
            movieSeatRowHolder.movieSeatRowTV.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
            movieSeatRowHolder.movieSeatRowTV.setTypeface(font);
        }
        movieSeatRowHolder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedpos == position){
                    movieSeatRowList.get(position).setSelected(false);
                    notifyItemChanged(position);
                    selectedpos = -1;
                }
                else{
                    if(selectedpos>=0) {
                        movieSeatRowList.get(position).setSelected(true);
                        movieSeatRowList.get(selectedpos).setSelected(false);
                        listener.onSeatRowSelected(movieSeatRowList.get(position));
                        notifyItemChanged(position);
                        notifyItemChanged(selectedpos);
                        selectedpos = position;
                    }
                    else{
                        movieSeatRowList.get(position).setSelected(true);
                        listener.onSeatRowSelected(movieSeatRowList.get(position));
                        notifyItemChanged(position);
                        selectedpos = position;
                    }
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return movieSeatRowList.size();
    }

    public interface SeatRowSelectionAdapterListener {
        void onSeatRowSelected(MovieSeatRow seatRow);
    }
}


