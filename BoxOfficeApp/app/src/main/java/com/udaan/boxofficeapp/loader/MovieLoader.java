package com.udaan.boxofficeapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.udaan.boxofficeapp.model.Movie;
import com.udaan.boxofficeapp.util.SyncUtils;

import java.util.List;

import retrofit.RetrofitError;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = "BlogFeedLoader";

    private List<Movie> mMovieCache;
private String cinema_id;
    public MovieLoader(Context context, String cinema_id) {
        super(context);
        this.cinema_id=cinema_id;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "onStartLoading()");

        if (mMovieCache != null) {
            // Deliver cached data.
            deliverResult(mMovieCache);
        } else {
            // We have no data, so start loading it.
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> feed = null;

        try {
            feed = SyncUtils.sWebService.getMovies(cinema_id);
        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return feed;
    }

    @Override
    public void deliverResult(List<Movie> feed) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            mMovieCache = null;
            return;
        }

        mMovieCache = feed;

        if (isStarted()) {
            super.deliverResult(mMovieCache);
        }
    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        mMovieCache = null;
    }

}
