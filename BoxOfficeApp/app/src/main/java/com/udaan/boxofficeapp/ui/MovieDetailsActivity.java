package com.udaan.boxofficeapp.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.udaan.boxofficeapp.R;
import com.udaan.boxofficeapp.adapter.PTAdapter;
import com.udaan.boxofficeapp.adapter.SeatColSelectionAdapter;
import com.udaan.boxofficeapp.adapter.SeatNumSelectionAdapter;
import com.udaan.boxofficeapp.adapter.SeatRowSelectionAdapter;
import com.udaan.boxofficeapp.adapter.TimeSelectionAdapter;
import com.udaan.boxofficeapp.model.GlobalData;
import com.udaan.boxofficeapp.model.Movie;
import com.udaan.boxofficeapp.model.MovieSeatCol;
import com.udaan.boxofficeapp.model.MovieSeatNum;
import com.udaan.boxofficeapp.model.MovieSeatRow;
import com.udaan.boxofficeapp.model.MovieTime;
import com.udaan.boxofficeapp.model.PTMovie;
import com.udaan.boxofficeapp.model.RequestError;
import com.udaan.boxofficeapp.model.Ticket;
import com.udaan.boxofficeapp.provider.BoxOfficeContract;
import com.udaan.boxofficeapp.sync.AccountAuthenticator;
import com.udaan.boxofficeapp.util.AccountUtils;
import com.udaan.boxofficeapp.util.FontChanger;
import com.udaan.boxofficeapp.util.LinePagerIndicatorDecoration;
import com.udaan.boxofficeapp.util.MiddleItemFinder;
import com.udaan.boxofficeapp.util.SyncUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MovieDetailsActivity extends AppCompatActivity implements TimeSelectionAdapter.TimeSelectionAdapterListener,SeatRowSelectionAdapter.SeatRowSelectionAdapterListener,SeatColSelectionAdapter.SeatColSelectionAdapterListener ,SeatNumSelectionAdapter.SeatNumSelectionAdapterListener{

    Typeface regular, bold;
    FontChanger regularFontChanger, boldFontChanger;
    TextView movieNameTV, genreTV, descTV;
    ImageView backdropIV;
    LinearLayout sessionLL;
    RecyclerView picturesRV;
    List<PTMovie> ptMovieList;
    PTAdapter ptAdapter;
    LinearLayoutManager layoutManager;
    SnapHelper snapHelper;
    Button sessionTimeButton;
    int currentPosition = 0;
    int pos;
    RecyclerView movieTimeRV;
    RecyclerView movieSeatRowRV;
    RecyclerView movieSeatColRV;
    RecyclerView movieSeatNumRV;
    List<MovieTime> movieTimeList;
    List<MovieSeatRow> movieSeatRowList;
    List<MovieSeatCol> movieSeatColList;
    List<MovieSeatNum> movieSeatNumList;
    TimeSelectionAdapter timeSelectionAdapter;
    SeatRowSelectionAdapter seatRowSelectionAdapter;
    SeatColSelectionAdapter seatColSelectionAdapter;
    SeatNumSelectionAdapter seatNumSelectionAdapter;
    LinearLayout detailsLL, timeSelectionLL;
    LinearLayout detailsLLRow, rowSelectionLL;
    boolean timeopen = false;
    Button bookTicketsBTN;
    Button seeTicketsBTN;
    CardView descCV;
    private Movie movie;
    private ImageView left_arrow;
    private ImageView right_arrow;
    private TextView date;
    private TextView day;
    private TextView month;


    private String selected_time;
    private String selected_date;
    private String selected_row;
    private String selected_col;
    private String currentDate;
    private JsonObject object;
    private JsonObject js_result;
    private JsonObject booking_response;
    private JsonObject seatobject;
    private JsonObject js_seat;
    private String selected_num;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        init();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getParcelable("movie") != null) {
                movie = bundle.getParcelable("movie");
                Log.e("movie_fragment_movie", "movie " + movie.getTitle());
            }

        }

        currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("EEE, d MMM yyyy");
        String smyDate = sdf.format(myDate);


//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(new SimpleDateFormat("MM-dd-yyyy").parse(currentDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//
//        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
//        LocalDate cdate = LocalDate.parse(currentDate, formatter);
//
//        DayOfWeek dow = cdate.getDayOfWeek();
//        String month_today= String.valueOf(cdate.getMonth());
//        String day_today= String.valueOf(cdate.getDayOfMonth());
//        String dayName = dow.getDisplayName(TextStyle.FULL, Locale.getDefault());
        regularFontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        date.setText(smyDate.substring(5, 7));
        day.setText(smyDate.substring(0, 3));
        month.setText(smyDate.substring(8, 11));
        movieNameTV.setText(movie.getTitle());
        genreTV.setText(movie.getGenre());
        descTV.setText(movie.getDescription());
        Picasso.with(getApplicationContext()).load(movie.getPosterPath()).into(backdropIV);
        ptMovieList.add(new PTMovie("Picture", movie.getPosterPath()));
        ptMovieList.add(new PTMovie("Video", "https://www.youtube.com/watch?v=OZZ81-vW-Kc"));
        ptAdapter.notifyDataSetChanged();
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(getApplicationContext()).inflateTransition(R.transition.detail_activity_enter_transition));
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                descTV.animate().translationY(sessionLL.getHeight()).setDuration(500).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        sessionLL.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();

            }
        }, 300);

        sessionTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detailsLL.animate().alpha(0).setDuration(500);
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        detailsLL.setVisibility(View.GONE);
                        timeSelectionLL.setVisibility(View.VISIBLE);
                        timeopen = true;
                    }
                }, 500);


            }
        });
        seeTicketsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSeatListTask().execute(currentDate, selected_time, selected_row, selected_col, String.valueOf(movie.getId()), String.valueOf(movie.getCinemaId()));
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
//                builder1.setMessage("Seats Available");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Proceed",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();

            }
        });
        bookTicketsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSeatavailibiltyTask().execute(currentDate, selected_time, selected_row, selected_col, String.valueOf(movie.getId()), String.valueOf(movie.getCinemaId()));
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
//                builder1.setMessage("Seats Available");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Proceed",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init() {


        postponeEnterTransition();
        descCV = findViewById(R.id.descCV);
        //Changing the font throughout the activity
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);


        left_arrow = findViewById(R.id.left_arrow);
        right_arrow = findViewById(R.id.right_arrow);
        date = findViewById(R.id.day_num);
        day = findViewById(R.id.day);

        month = findViewById(R.id.month);


        movieNameTV = findViewById(R.id.movieNameTV);
        genreTV = findViewById(R.id.genreTV);
        descTV = findViewById(R.id.descriptionTV);
        backdropIV = findViewById(R.id.backdropIV);
        sessionLL = findViewById(R.id.sessionLL);
        timeSelectionLL = findViewById(R.id.timeSelectionLL);
        detailsLL = findViewById(R.id.detailsLL);
        sessionTimeButton = findViewById(R.id.sessionTimeBTN);
        picturesRV = findViewById(R.id.picturesRV);
        ptMovieList = new ArrayList<>();
        ptAdapter = new PTAdapter(ptMovieList, MovieDetailsActivity.this);
        seeTicketsBTN = findViewById(R.id.seeTicketsBTN);
        bookTicketsBTN = findViewById(R.id.bookTicketsBTN);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(picturesRV);
        picturesRV.setLayoutManager(layoutManager);
        picturesRV.addItemDecoration(new LinePagerIndicatorDecoration(MovieDetailsActivity.this));
        picturesRV.setAdapter(ptAdapter);
        picturesRV.post(new Runnable() {
            @Override
            public void run() {
                supportStartPostponedEnterTransition();


            }
        });


        MiddleItemFinder.MiddleItemCallback callback =
                new MiddleItemFinder.MiddleItemCallback() {
                    @Override
                    public void scrollFinished(int middleElement) {
                        // interaction with middle item
                        currentPosition = middleElement;

                    }
                };
        picturesRV.addOnScrollListener(
                new MiddleItemFinder(getApplicationContext(), layoutManager,
                        callback, RecyclerView.SCROLL_STATE_IDLE));

        picturesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("Testing " + dx);

            }
        });

        movieTimeRV = findViewById(R.id.movieTimeRV);
        movieSeatRowRV = findViewById(R.id.movieSeatRowRV);
        movieSeatColRV = findViewById(R.id.movieSeatColRV);
        movieSeatNumRV = findViewById(R.id.movieSeatNumRV);
        movieTimeList = new ArrayList<>();
        movieSeatRowList = new ArrayList<>();
        movieSeatColList = new ArrayList<>();
        movieSeatNumList = new ArrayList<>();
        seatNumSelectionAdapter = new SeatNumSelectionAdapter(movieSeatNumList, MovieDetailsActivity.this, MovieDetailsActivity.this);

        seatColSelectionAdapter = new SeatColSelectionAdapter(movieSeatColList, MovieDetailsActivity.this, MovieDetailsActivity.this);
        seatRowSelectionAdapter = new SeatRowSelectionAdapter(movieSeatRowList, MovieDetailsActivity.this, MovieDetailsActivity.this);
        timeSelectionAdapter = new TimeSelectionAdapter(movieTimeList, MovieDetailsActivity.this, MovieDetailsActivity.this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);

        movieTimeRV.setLayoutManager(layoutManager1);
        movieTimeRV.setAdapter(timeSelectionAdapter);
        movieSeatRowRV.setLayoutManager(layoutManager2);
        movieSeatRowRV.setAdapter(seatRowSelectionAdapter);
        movieSeatColRV.setLayoutManager(layoutManager3);
        movieSeatColRV.setAdapter(seatColSelectionAdapter);
        movieSeatNumRV.setLayoutManager(layoutManager4);
        movieSeatNumRV.setAdapter(seatNumSelectionAdapter);
        movieSeatRowList.add(new MovieSeatRow("A", false));
        movieSeatRowList.add(new MovieSeatRow("B", false));
        movieSeatRowList.add(new MovieSeatRow("C", false));
        movieSeatRowList.add(new MovieSeatRow("D", false));
        movieSeatRowList.add(new MovieSeatRow("E", false));
        movieSeatRowList.add(new MovieSeatRow("F", false));
        movieSeatRowList.add(new MovieSeatRow("G", false));
        movieSeatRowList.add(new MovieSeatRow("H", false));
        for (int i = 0; i <= 10; i++) {
            movieSeatColList.add(new MovieSeatCol(String.valueOf(i + 1), false));
        }
        for (int i = 0; i <= 10; i++) {
            movieSeatNumList.add(new MovieSeatNum(String.valueOf(i + 1), false));
        }
        seatColSelectionAdapter.notifyDataSetChanged();
        seatNumSelectionAdapter.notifyDataSetChanged();
        movieTimeList.add(new MovieTime("7:00 am", 300, 250));
        movieTimeList.add(new MovieTime("11:00 am", 300, 120));
        movieTimeList.add(new MovieTime("3:00 pm", 300, 60));
        movieTimeList.add(new MovieTime("6:45 pm", 300, 50));
        movieTimeList.add(new MovieTime("10:00 pm", 300, 170));
        timeSelectionAdapter.notifyDataSetChanged();
        seatRowSelectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {


        if (timeopen) {
            System.out.println("TESTING " + timeopen);
            timeSelectionLL.animate().alpha(0).setDuration(500);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    timeSelectionLL.setVisibility(View.GONE);
                    detailsLL.setVisibility(View.VISIBLE);
                    timeopen = false;
                }
            }, 500);
        } else {
            if (!timeopen) {
                System.out.println("Testing Hello");
                sessionLL.setVisibility(View.GONE);
                supportFinishAfterTransition();
                super.onBackPressed();
            }
        }

    }

    @Override
    public void onTimeSelected(MovieTime time) {
        Log.e("selected_time", time.getTime());
        selected_time = time.getTime();
    }

    @Override
    public void onSeatRowSelected(MovieSeatRow seatRow) {
        Log.e("selected_row", seatRow.getRow());
        selected_row = seatRow.getRow();
    }

    @Override
    public void onSeatColSelected(MovieSeatCol seatCol) {
        Log.e("selected_col", seatCol.getCol());
        selected_col = seatCol.getCol();
    }

    @Override
    public void onSeatNumSelected(MovieSeatNum seatNum) {
        selected_num=seatNum.getNum();
    }

    private class GetSeatListTask extends AsyncTask<String, Void, JsonObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JsonObject doInBackground(String... params) {
            String date = params[0];
            String time = params[1];
            String row = params[2];
            String col = params[3];
            String movie_id = params[4];
            String cinema_id = params[5];
            String choice = row + col;

//            Account account = new Account(accountName, AccountAuthenticator.ACCOUNT_TYPE);
//            AccountManager manager = AccountManager.get(getActivity());
//            String authToken = manager.peekAuthToken(account, AccountAuthenticator.AUTHTOKEN_TYPE);
            String result = null;

            try {
                seatobject = SyncUtils.sWebService.getSeatList( Integer.parseInt(movie_id), Integer.parseInt(cinema_id));
//                long newAddressId = object.get("id").getAsLong();


            } catch (RetrofitError error) {

                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    result = "No connection.";
                } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                    Response response = error.getResponse();
                    String json = new String(((TypedByteArray) response.getBody()).getBytes());
                    RequestError reqError = new Gson().fromJson(json, RequestError.class);
                    result = reqError.getDescription();
                } else {
                    result = error.getLocalizedMessage();
                }
            }

            /*try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return seatobject;
        }

        @Override
        protected void onPostExecute(JsonObject result) {
            super.onPostExecute(result);
            js_seat=result;
            String seats_available = "";
            String message="";
            Log.e("result", String.valueOf(result.get("Seats").getAsJsonArray().get(0)));
            try {
               JsonArray seatlist = result.get("Seats").getAsJsonArray();
for(int i=0;i<seatlist.size();i++){
    seats_available+=String.valueOf(seatlist.get(i))+" ";
}

                message="Seats Available " + seats_available;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                Log.e("error_message",message.substring(0,3));



                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } catch (IllegalStateException |NullPointerException e) {
                message="No seats Available";
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                Log.e("error_message",message.substring(0,3));



                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }



//            sAddressBookOperationInProgress = false;
//
//            if (mAddNewAddressCallback != null) {
//                mAddNewAddressCallback.onAddNewAddressResult(result);
//            }
        }

//        private void saveNewAddress(long serverId, String fullName, String addressLine1, String addressLine2, String city, String state, String zipCode, String country, String phone) {
//            ContentValues values = new ContentValues();
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_SERVER_ID, serverId);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_FULL_NAME, fullName);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ADDRESS_LINE_1, addressLine1);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ADDRESS_LINE_2, addressLine2);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_CITY, city);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_STATE, state);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ZIP_CODE, zipCode);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_COUNTRY, country);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_PHONE_NUMBER, phone);
//
//            getContext().getContentResolver().insert(
//                    BoxOfficeContract.AddressBookEntry.CONTENT_URI, values);
//        }
    }
    private class GetSeatavailibiltyTask extends AsyncTask<String, Void, JsonObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JsonObject doInBackground(String... params) {
            String date = params[0];
            String time = params[1];
            String row = params[2];
            String col = params[3];
            String movie_id = params[4];
            String cinema_id = params[5];
            String choice = row + col;


//            Account account = new Account(accountName, AccountAuthenticator.ACCOUNT_TYPE);
//            AccountManager manager = AccountManager.get(getActivity());
//            String authToken = manager.peekAuthToken(account, AccountAuthenticator.AUTHTOKEN_TYPE);
            String result = null;

            try {
                object = SyncUtils.sWebService.getSeatAvailability(selected_num,date, time, choice, Integer.parseInt(movie_id), Integer.parseInt(cinema_id));
//                long newAddressId = object.get("id").getAsLong();


            } catch (RetrofitError error) {

                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    result = "No connection.";
                } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                    Response response = error.getResponse();
                    String json = new String(((TypedByteArray) response.getBody()).getBytes());
                    RequestError reqError = new Gson().fromJson(json, RequestError.class);
                    result = reqError.getDescription();
                } else {
                    result = error.getLocalizedMessage();
                }
            }

            /*try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return object;
        }

        @Override
        protected void onPostExecute(JsonObject result) {
            super.onPostExecute(result);
            js_result=result;
            StringBuilder seats_available = new StringBuilder();
            String message="";
            Log.e("result", String.valueOf(result));
            try {
                JsonArray seatlist = result.get("availableSeats").getAsJsonObject().get(selected_row).getAsJsonArray();

                for (int i = 0; i < seatlist.size(); i++) {
                    seats_available.append(selected_row).append(String.valueOf(seatlist.get(i))).append(" ");
                }
                message="Seats Available " + seats_available;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                Log.e("error_message",message.substring(0,3));

                builder1.setPositiveButton(

                        "Proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new TicketBookingTask().execute(selected_row, selected_col,"2",  js_result.get("show_id").getAsString());
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            } catch (IllegalStateException |NullPointerException e) {
                message="No seats Available as per your Choice";
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MovieDetailsActivity.this);
                builder1.setMessage(message);
                builder1.setCancelable(true);
                Log.e("error_message",message.substring(0,3));



                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }



//            sAddressBookOperationInProgress = false;
//
//            if (mAddNewAddressCallback != null) {
//                mAddNewAddressCallback.onAddNewAddressResult(result);
//            }
        }

//        private void saveNewAddress(long serverId, String fullName, String addressLine1, String addressLine2, String city, String state, String zipCode, String country, String phone) {
//            ContentValues values = new ContentValues();
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_SERVER_ID, serverId);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_FULL_NAME, fullName);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ADDRESS_LINE_1, addressLine1);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ADDRESS_LINE_2, addressLine2);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_CITY, city);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_STATE, state);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_ZIP_CODE, zipCode);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_COUNTRY, country);
//            values.put(BoxOfficeContract.AddressBookEntry.COLUMN_PHONE_NUMBER, phone);
//
//            getContext().getContentResolver().insert(
//                    BoxOfficeContract.AddressBookEntry.CONTENT_URI, values);
//        }
    }

    private class TicketBookingTask extends AsyncTask<String, Void, JsonObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JsonObject doInBackground(String... params) {
            String row = params[0];
            String col = params[1];
            String num = params[2];
            String showId = params[3];
            Account account = AccountUtils.getActiveAccount(MovieDetailsActivity.this);
            if (account == null) {
                // mErrorText = "No active account is present";
                return null;
            }

            AccountManager manager = AccountManager.get(MovieDetailsActivity.this);
            String authToken = manager.peekAuthToken(account, AccountAuthenticator.AUTHTOKEN_TYPE);
            String result = null;

            try {
                booking_response =SyncUtils.sWebService.doBooking("Bearer " + authToken, row+col, Integer.parseInt(num),showId);
//                long newAddressId = object.get("id").getAsLong();


            } catch (RetrofitError error) {

                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    result = "No connection.";
                } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                    Response response = error.getResponse();
                    String json = new String(((TypedByteArray) response.getBody()).getBytes());
                    RequestError reqError = new Gson().fromJson(json, RequestError.class);
                    result = reqError.getDescription();
                } else {
                    result = error.getLocalizedMessage();
                }
            }

            /*try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return booking_response;
        }

        @Override
        protected void onPostExecute(JsonObject result) {
            super.onPostExecute(result);

            Log.e("ticket_result", String.valueOf(js_result.get("availableSeats").getAsJsonObject().get(selected_row).getAsJsonArray()));
            Intent intent = new Intent(MovieDetailsActivity.this, TicketDetailActivity.class);
            JsonArray js=js_result.get("availableSeats").getAsJsonObject().get(selected_row).getAsJsonArray();
            List<String> seatls=new ArrayList<>();
            for (int i=0;i<js.size();i++){
                seatls.add(selected_row+ String.valueOf(js.get(i).getAsInt()));
            }
            Ticket ticket = new Ticket("Gold",
                    "300", "reewt4yryrggdrhdyryrtyeredrdhfhfh"
                    ,
                    movie.getTitle(),
                    selected_time,
                    movie.getPosterPath(),
                    movie.getGenre(),
                    seatls,"India",
                    js_result.get("show_id").getAsString()
            );
            intent.putExtra("ticket", ticket);


            startActivity(intent);

        }
    }
}
