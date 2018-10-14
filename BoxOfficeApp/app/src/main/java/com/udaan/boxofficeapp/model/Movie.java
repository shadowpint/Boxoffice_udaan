package com.udaan.boxofficeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shrikanthravi on 27/02/18.
 */

public class Movie implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("movie_title")
    @Expose
    private String title;
    @SerializedName("poster")
    @Expose
    private String posterPath;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("cinema")
    @Expose

    private Integer cinemaId;

    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        posterPath = in.readString();
        description = in.readString();
        genre = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0) {
            cinemaId = null;
        } else {
            cinemaId = in.readInt();
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public Movie(Integer id, String title, String posterPath, String description, String genre, String releaseDate, Integer cinemaId) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.description = description;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.cinemaId = cinemaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(description);
        dest.writeString(genre);
        dest.writeString(releaseDate);
        if (cinemaId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cinemaId);
        }
    }
}
