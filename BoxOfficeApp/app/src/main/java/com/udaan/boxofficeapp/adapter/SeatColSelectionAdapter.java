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
import com.udaan.boxofficeapp.model.MovieSeatCol;

import java.util.List;

/**
 * Created by shrikanthravi on 01/03/18.
 */


public class SeatColSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieSeatCol> movieSeatColList;
    Context context;
    public static int selectedpos=-1;
    public static ProgressBar selectedprogressBar = null;
    private SeatColSelectionAdapterListener listener;

    public  class MovieSeatColHolder extends RecyclerView.ViewHolder {

        public TextView movieSeatColTV;
        public ProgressBar progressBar;
        public MovieSeatColHolder(View view) {
            super(view);

            movieSeatColTV = view.findViewById(R.id.movieSeatColTV);
            progressBar = view.findViewById(R.id.seatsProgress);

        }
    }

    public SeatColSelectionAdapter(List<MovieSeatCol> verticalList, Context context, SeatColSelectionAdapterListener listener) {
        this.movieSeatColList = verticalList;
        this.context = context;
        this.listener=listener;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.seat_col_item, parent, false);
            return new MovieSeatColHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final MovieSeatCol movieSeatCol = movieSeatColList.get(position);
        final MovieSeatColHolder movieSeatColHolder = ((MovieSeatColHolder) holder);
        movieSeatColHolder.movieSeatColTV.setText(movieSeatCol.getCol());
        movieSeatColHolder.progressBar.setMax(100);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        Typeface bold = Typeface.createFromAsset(context.getAssets(), "fonts/product_sans_bold.ttf");

        movieSeatColHolder.movieSeatColTV.setTypeface(font);

        movieSeatColHolder.progressBar.setProgress(100);
        if(movieSeatCol.isSelected()){
            movieSeatColHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            movieSeatColHolder.movieSeatColTV.setTextColor(context.getResources().getColor(android.R.color.black));
            movieSeatColHolder.movieSeatColTV.setTypeface(bold);
        }
        else{
            movieSeatColHolder.progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(android.R.color.tab_indicator_text)));
            movieSeatColHolder.movieSeatColTV.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
            movieSeatColHolder.movieSeatColTV.setTypeface(font);
        }
        movieSeatColHolder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedpos == position){
                    movieSeatColList.get(position).setSelected(false);
                    notifyItemChanged(position);
                    selectedpos = -1;
                }
                else{
                    if(selectedpos>=0) {
                        movieSeatColList.get(position).setSelected(true);
                        movieSeatColList.get(selectedpos).setSelected(false);
                        listener.onSeatColSelected(movieSeatColList.get(position));
                        notifyItemChanged(position);
                        notifyItemChanged(selectedpos);
                        selectedpos = position;
                    }
                    else{
                        movieSeatColList.get(position).setSelected(true);
                        listener.onSeatColSelected(movieSeatColList.get(position));
                        notifyItemChanged(position);
                        selectedpos = position;
                    }
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return movieSeatColList.size();
    }
    public interface SeatColSelectionAdapterListener {
        void onSeatColSelected(MovieSeatCol seatCol);
    }

}


