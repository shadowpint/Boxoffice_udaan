package com.udaan.boxofficeapp.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.udaan.boxofficeapp.model.Cinema;
import com.udaan.boxofficeapp.util.SyncUtils;

import java.util.List;

import retrofit.RetrofitError;

public class CinemaLoader extends AsyncTaskLoader<List<Cinema>> {

    private static final String LOG_TAG = "BlogFeedLoader";

    private List<Cinema> mCinemaCache;
private String city_id;
    public CinemaLoader(Context context, String city_id) {
        super(context);
        this.city_id=city_id;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "onStartLoading()");

        if (mCinemaCache != null) {
            // Deliver cached data.
            deliverResult(mCinemaCache);
        } else {
            // We have no data, so start loading it.
            forceLoad();
        }
    }

    @Override
    public List<Cinema> loadInBackground() {
        List<Cinema> feed = null;

        try {
            feed = SyncUtils.sWebService.getCinemas(city_id);
        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return feed;
    }

    @Override
    public void deliverResult(List<Cinema> feed) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            mCinemaCache = null;
            return;
        }

        mCinemaCache = feed;

        if (isStarted()) {
            super.deliverResult(mCinemaCache);
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
        mCinemaCache = null;
    }

}
