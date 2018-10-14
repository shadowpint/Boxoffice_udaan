package com.udaan.boxofficeapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.loader.CinemaLoader;
import com.udaan.boxofficeapp.model.Cinema;
import com.udaan.boxofficeapp.model.City;
import com.udaan.boxofficeapp.ui.widget.EmptyView;
import com.udaan.boxofficeapp.util.OnItemClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class CinemaFragment extends BaseNavigationFragment implements LoaderManager.LoaderCallbacks<List<Cinema>>, OnMapReadyCallback{
    private static final String LOG_TAG = "CinemaFeedFragment";
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private ToggleButton mTogglebutton;
    public static CinemaFragment newInstance() {
        return new CinemaFragment();
    }
    /*enum UiState {
        LOADING,
        EMPTY,
        ERROR,
        DISPLAY_CONTENT
    }*/

    private RecyclerView mEventRecyclerView;
    private EventFeedAdapter mEventFeedAdapter;
    private ProgressBar mProgressBar;
    private ViewStub mEmptyViewStub;
    private EmptyView mEmptyView;
    private City city;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getParcelable("city") != null) {
                city = bundle.getParcelable("city");
                Log.e("event_fragment_city", "city " + city.getName());
            }

        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Multiplexes in " + city.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cinema, container, false);
        getActivity().setTitle("your name");
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        mEmptyViewStub = (ViewStub) rootView.findViewById(R.id.stub_empty_view);

        mMapView = (MapView) rootView.findViewById(R.id.map_view);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEventFeedAdapter = new EventFeedAdapter(null);
        mEventFeedAdapter.setOnItemClickListener((itemView, position) -> {
            Log.d(LOG_TAG, "onItemClick: " + position);
            Cinema entry = mEventFeedAdapter.getEvent(position);
Log.e("Cinema",entry.getName());
            Intent intent = new Intent(getActivity(), CinemaDetailActivity.class);

            intent.putExtra("cinema", entry);


            getActivity().startActivity(intent);
        });

        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.blog_feed_recycler_view);
        mTogglebutton = (ToggleButton) view.findViewById(R.id.tb2);
        mTogglebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTogglebutton.isChecked()){

                    mEventRecyclerView.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);




                }
                else{
                    mEventRecyclerView.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);


                }
            }
        });
        mEventRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mEventRecyclerView.setAdapter(mEventFeedAdapter);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//        mGoogleMap.addMarker(new MarkerOptions().position(/*some location*/));


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(city.getLat(), city.getLng()), 10));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        mMapView.setVisibility(View.GONE);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mEmptyView = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Cinema>> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        mEventRecyclerView.setVisibility(View.GONE);
        setEmptyViewVisibility(View.GONE);
        return new CinemaLoader(getActivity(),city.getId());
    }

    @Override
    public void onLoadFinished(Loader<List<Cinema>> loader, List<Cinema> entries) {
        mProgressBar.setVisibility(View.GONE);

        if (entries == null) {
            mEventRecyclerView.setVisibility(View.GONE);
            showErrorEmptyView();
        } else if (entries.size() == 0) {
            mEventRecyclerView.setVisibility(View.GONE);
            showEmptyFeedView();
        } else {
            mTogglebutton.setVisibility(View.VISIBLE);
            for(Cinema cinema :entries){
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(cinema.getLat(), cinema.getLng()))
                        .title(cinema.getName()));
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

//                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
//
//                        intent.putExtra("cinema", cinema);
//
//
//                        getActivity().startActivity(intent);
                        return false;
                    }
                });
            }
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
        getLoaderManager().restartLoader(0, null, CinemaFragment.this);
    }

    private void setEmptyViewVisibility(int visibility) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visibility);
        }
    }

    private void showEmptyFeedView() {
        mTogglebutton.setVisibility(View.GONE);
        if (mEmptyView == null) {
            mEmptyView = (EmptyView) mEmptyViewStub.inflate();
        }

        // TODO: Change to string resources.
        mEmptyView.reset();
        mEmptyView.setImageResource(R.drawable.ic_empty_newsfeed);
        mEmptyView.setTitle("No Events");
        mEmptyView.setSubtitle("There are no upcoming events by the time, check later.");
        mEmptyView.setAction("Try Again", v -> reloadFeedData());
        mEmptyView.setVisibility(View.VISIBLE);

    }

    private void showErrorEmptyView() {
        mTogglebutton.setVisibility(View.GONE);
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




    private class EventFeedAdapter extends RecyclerView.Adapter<EventFeedAdapter.ViewHolder> {

        private List<Cinema> mCinemaEntries;
        private OnItemClickListener mItemClickListener;

        public EventFeedAdapter(List<Cinema> entries) {
            mCinemaEntries = entries;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getActivity().getLayoutInflater()
                    .inflate(R.layout.cinema_card, parent, false);

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
        public void onBindViewHolder(ViewHolder holder, int position) {
            Cinema entry = mCinemaEntries.get(position);

            holder.mTitleView.setText(WordUtils.capitalizeFully(entry.getName()));
//
            holder.mAddressView.setText(WordUtils.capitalizeFully(entry.getAddress()));



            Picasso.with(getActivity())
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
}
