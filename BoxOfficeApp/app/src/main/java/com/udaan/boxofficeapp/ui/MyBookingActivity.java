package com.udaan.boxofficeapp.ui;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.loader.CinemaLoader;
import com.udaan.boxofficeapp.model.Cinema;
import com.udaan.boxofficeapp.model.City;
import com.udaan.boxofficeapp.ui.widget.EmptyView;
import com.udaan.boxofficeapp.util.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import org.apache.commons.lang3.text.WordUtils;

public class MyBookingActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<List<Cinema>>{

    private static final int LOADER_ID = 0x02;
    private CursorAdapter adapter;
    private EventFeedAdapter mEventFeedAdapter;
    private RecyclerView mEventRecyclerView;
    private ViewStub mEmptyViewStub;
    private EmptyView mEmptyView;

    private City city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_mybooking);


        mEmptyViewStub = (ViewStub) findViewById(R.id.stub_empty_view);
        mEventFeedAdapter = new EventFeedAdapter(null);
        mEventFeedAdapter.setOnItemClickListener((itemView, position) -> {
//            Log.d(LOG_TAG, "onItemClick: " + position);
            Cinema entry = mEventFeedAdapter.getEvent(position);
            Log.e("Cinema",entry.getName());
//            Intent intent = new Intent(this, EventDetailActivity.class);
//
//            intent.putExtra("cinema", entry);
//
//
//          startActivity(intent);
        });

        mEventRecyclerView = (RecyclerView) findViewById(R.id.blog_feed_recycler_view);
        mEventRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mEventRecyclerView.setAdapter(mEventFeedAdapter);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }
    @Override
    public Loader<List<Cinema>> onCreateLoader(int id, Bundle args) {

        mEventRecyclerView.setVisibility(View.GONE);
        setEmptyViewVisibility(View.GONE);
        return new CinemaLoader(this,"6");
    }

    @Override
    public void onLoadFinished(Loader<List<Cinema>> loader, List<Cinema> entries) {


        if (entries == null) {
            mEventRecyclerView.setVisibility(View.GONE);
            showErrorEmptyView();
        } else if (entries.size() == 0) {
            mEventRecyclerView.setVisibility(View.GONE);
            showEmptyFeedView();
        } else {

            mEventRecyclerView.setVisibility(View.VISIBLE);
            mEventFeedAdapter.setEventEntries(entries);
            setEmptyViewVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Cinema>> loader) {
        mEventFeedAdapter.setEventEntries(null);
    }

    private void reloadFeedData() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
        mEmptyView.setTitle("Nothing to see here");
        mEmptyView.setSubtitle("No tickets booked");
        mEmptyView.setAction("Try Again", v -> reloadFeedData());
        mEmptyView.setVisibility(View.VISIBLE);

    }

    private void showErrorEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = (EmptyView) mEmptyViewStub.inflate();
        }

        // TODO: Change to string resources.
        mEmptyView.reset();
        mEmptyView.setTitle("No Account");
        mEmptyView.setSubtitle("Please sign in");
        mEmptyView.setAction("Try Again", v -> reloadFeedData());
        mEmptyView.setVisibility(View.VISIBLE);
    }



    private class EventFeedAdapter extends RecyclerView.Adapter<EventFeedAdapter.ViewHolder> {

        private List<Cinema> mCinemaEntries;
        private OnItemClickListener mItemClickListener;

        public EventFeedAdapter(List<Cinema> entries) {
            mCinemaEntries = entries;
        }

        @Override
        public EventFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater()
                    .inflate(R.layout.booking_card, parent, false);

            final ViewHolder holder = new ViewHolder(itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (mItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        mItemClickListener.onItemClick(holder.itemView, position);
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(EventFeedAdapter.ViewHolder holder, int position) {
            Cinema entry = mCinemaEntries.get(position);

            holder.mTitleView.setText(WordUtils.capitalizeFully(entry.getName()));

            holder.mAddressView.setText(entry.getAddress());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            // Date date = formatter.parse(entry.getDate().replaceAll("Z$", "+0000"));


//            holder.mExcerptView.setText(entry.getDescription());

            Picasso.with(getApplicationContext())
                    .load(entry.getLeadImageUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mCinemaEntries != null ? mCinemaEntries.size() : 0;
        }

        public void setEventEntries(List<Cinema> entries) {
            mCinemaEntries = entries;
            notifyDataSetChanged();
        }

        public Cinema getEvent(int position) {
            return mCinemaEntries.get(position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mItemClickListener = listener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView mImageView;
            public TextView mTitleView;
            public TextView mTagView;

            public TextView mAddressView;
//            public TextView mExcerptView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image);
                mTitleView = (TextView) itemView.findViewById(R.id.title);

                mAddressView = (TextView) itemView.findViewById(R.id.address);

//                mExcerptView = (TextView) itemView.findViewById(R.id.excerpt);
            }
        }

    }





    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}