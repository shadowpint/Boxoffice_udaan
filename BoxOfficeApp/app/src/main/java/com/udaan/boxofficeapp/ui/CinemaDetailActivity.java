package com.udaan.boxofficeapp.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.adapter.MovieAdapter;
import com.udaan.boxofficeapp.loader.CinemaLoader;
import com.udaan.boxofficeapp.loader.MovieLoader;
import com.udaan.boxofficeapp.model.Cinema;
import com.udaan.boxofficeapp.model.GlobalData;
import com.udaan.boxofficeapp.model.Movie;
import com.udaan.boxofficeapp.ui.widget.EmptyView;
import com.udaan.boxofficeapp.util.FontChanger;
import com.udaan.boxofficeapp.util.LinePagerIndicatorDecoration;
import com.udaan.boxofficeapp.util.MiddleItemFinder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CinemaDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    RecyclerView moviesRV;
    List<Movie> movieList;
    MovieAdapter movieAdapter;
    LinearLayoutManager layoutManager;
    ImageView backdropIV;
    SnapHelper snapHelper;
    private ProgressBar mProgressBar;
    private ViewStub mEmptyViewStub;
    private EmptyView mEmptyView;
    private Cinema cinema;
    private View transparentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getParcelable("cinema") != null) {
                cinema= bundle.getParcelable("cinema");
                Log.e("movie_fragment_cinema", "cinema " + cinema.getName());
            }

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cinema_detail);
        init();
        regularFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
//        for(int i=0;i< GlobalData.movies.length;i++){
//            List<String> genreList = new ArrayList<>();
//            genreList.add(GlobalData.genres[i]);
//            Movie movie = new Movie();
//            movie.setOriginalTitle(GlobalData.movies[i]);
//            movie.setPosterPath(GlobalData.posters[i]);
//            movie.setOverview(GlobalData.desc[i]);
//            movie.setGenres(genreList);
//            movieList.add(movie);
//            movieAdapter.notifyDataSetChanged();
//        }



    }

    public void init(){

        //Changing the font throughout the activity
        mEmptyViewStub = (ViewStub) findViewById(R.id.stub_empty_view);
        transparentView = (View) findViewById(R.id.view_transparent);
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);
        moviesRV = findViewById(R.id.moviesRV);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(movieList,CinemaDetailActivity.this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(moviesRV);
        moviesRV.setLayoutManager(layoutManager);

        moviesRV.setAdapter(movieAdapter);

        backdropIV = findViewById(R.id.backdropIV);
        MiddleItemFinder.MiddleItemCallback callback =
                new MiddleItemFinder.MiddleItemCallback() {
                    @Override
                    public void scrollFinished(int middleElement) {
                        // interaction with middle item
                        onActiveCardChange(middleElement);
                    }
                };
        moviesRV.addOnScrollListener(
                new MiddleItemFinder(getApplicationContext(), layoutManager,
                        callback, RecyclerView.SCROLL_STATE_IDLE));

        getSupportLoaderManager().initLoader(0, null, this);


    }

    public void onActiveCardChange(int pos){

        Picasso.with(getApplicationContext()).load(movieList.get(pos).getPosterPath()).into(backdropIV);


    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        //mProgressBar.setVisibility(View.VISIBLE);
        moviesRV.setVisibility(View.GONE);
        setEmptyViewVisibility(View.GONE);
        return new MovieLoader(this,cinema.getId());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> entries) {
       // mProgressBar.setVisibility(View.GONE);

        if (entries == null) {
            moviesRV.setVisibility(View.GONE);
            showErrorEmptyView();
        } else if (entries.size() == 0) {
            moviesRV.setVisibility(View.GONE);
            showEmptyFeedView();
        } else {

movieList=entries;
            moviesRV.setVisibility(View.VISIBLE);
            movieAdapter.setMovieEntries(entries);
            moviesRV.addItemDecoration(new LinePagerIndicatorDecoration(CinemaDetailActivity.this));
            Picasso.with(getApplicationContext()).load(entries.get(0).getPosterPath()).into(backdropIV);
            setEmptyViewVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieAdapter.setMovieEntries(null);
    }

    private void reloadFeedData() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void setEmptyViewVisibility(int visibility) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visibility);
        }
    }

    private void showEmptyFeedView() {

        if (mEmptyView == null) {
            mEmptyView = (EmptyView) mEmptyViewStub.inflate();
        }

        // TODO: Change to string resources.
        mEmptyView.reset();
        mEmptyView.setImageResource(R.drawable.ic_empty_newsfeed);
        mEmptyView.setTitle("No Events");
        mEmptyView.setSubtitle("There are no upcoming movies by the time, check later.");
        mEmptyView.setAction("Try Again", v -> reloadFeedData());
        mEmptyView.setVisibility(View.VISIBLE);

    }

    private void showErrorEmptyView() {
        transparentView.setBackgroundColor(Color.parseColor("#ffffff"));
        if (mEmptyView == null) {
            mEmptyView = (EmptyView) mEmptyViewStub.inflate();
        }

        // TODO: Change to string resources.
        mEmptyView.reset();
        mEmptyView.setTitle("No connection");
        mEmptyView.setSubtitle("Check your network state and try again.");
        mEmptyView.setAction("Try Again", v -> reloadFeedData());
        mEmptyView.setVisibility(View.VISIBLE);
    }


}
