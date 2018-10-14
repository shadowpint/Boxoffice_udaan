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
import com.udaan.boxofficeapp.model.MovieSeatNum;

import java.util.List;

/**
 * Created by shrikanthravi on 01/03/18.
 */


public class SeatNumSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieSeatNum> movieSeatNumList;
    Context context;
    public static int selectedpos=-1;
    public static ProgressBar selectedprogressBar = null;
    private SeatNumSelectionAdapterListener listener;

    public  class MovieSeatNumHolder extends RecyclerView.ViewHolder {

        public TextView movieSeatNumTV;
        public ProgressBar progressBar;
        public MovieSeatNumHolder(View view) {
            super(view);

            movieSeatNumTV = view.findViewById(R.id.movieSeatNumTV);
            progressBar = view.findViewById(R.id.seatsProgress);

        }
    }

    public SeatNumSelectionAdapter(List<MovieSeatNum> verticalList, Context context, SeatNumSelectionAdapterListener listener) {
        this.movieSeatNumList = verticalList;
        this.context = context;
        this.listener=listener;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.seat_num_item, parent, false);
            return new MovieSeatNumHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final MovieSeatNum movieSeatNum = movieSeatNumList.get(position);
        final MovieSeatNumHolder movieSeatNumHolder = ((MovieSeatNumHolder) holder);
        movieSeatNumHolder.movieSeatNumTV.setText(movieSeatNum.getNum());
        movieSeatNumHolder.progressBar.setMax(100);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        Typeface bold = Typeface.createFromAsset(context.getAssets(), "fonts/product_sans_bold.ttf");

        movieSeatNumHolder.movieSeatNumTV.setTypeface(font);

        movieSeatNumHolder.progressBar.setProgress(100);
        if(movieSeatNum.isSelected()){
            movieSeatNumHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            movieSeatNumHolder.movieSeatNumTV.setTextColor(context.getResources().getColor(android.R.color.black));
            movieSeatNumHolder.movieSeatNumTV.setTypeface(bold);
        }
        else{
            movieSeatNumHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(android.R.color.tab_indicator_text)));
            movieSeatNumHolder.movieSeatNumTV.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
            movieSeatNumHolder.movieSeatNumTV.setTypeface(font);
        }
        movieSeatNumHolder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedpos == position){
                    movieSeatNumList.get(position).setSelected(false);
                    notifyItemChanged(position);
                    selectedpos = -1;
                }
                else{
                    if(selectedpos>=0) {
                        movieSeatNumList.get(position).setSelected(true);
                        movieSeatNumList.get(selectedpos).setSelected(false);
                        listener.onSeatNumSelected(movieSeatNumList.get(position));
                        notifyItemChanged(position);
                        notifyItemChanged(selectedpos);
                        selectedpos = position;
                    }
                    else{
                        movieSeatNumList.get(position).setSelected(true);
                        listener.onSeatNumSelected(movieSeatNumList.get(position));
                        notifyItemChanged(position);
                        selectedpos = position;
                    }
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return movieSeatNumList.size();
    }
    public interface SeatNumSelectionAdapterListener {
        void onSeatNumSelected(MovieSeatNum seatNum);
    }

}


