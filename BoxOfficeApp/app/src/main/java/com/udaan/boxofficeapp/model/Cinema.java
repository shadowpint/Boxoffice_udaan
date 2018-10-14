package com.udaan.boxofficeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Neeraj on 19/03/17.
 */
public class Cinema implements Parcelable {

    @SerializedName("cinema_city")

    private String city;
    @SerializedName("cinema_name")
    private String name;

    @SerializedName("cinema_addr")
    private String address;

    protected Cinema(Parcel in) {
        city = in.readString();
        name = in.readString();
        address = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readFloat();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readFloat();
        }
        contact = in.readString();
        leadImageUrl = in.readString();
        id = in.readString();
    }

    public static final Creator<Cinema> CREATOR = new Creator<Cinema>() {
        @Override
        public Cinema createFromParcel(Parcel in) {
            return new Cinema(in);
        }

        @Override
        public Cinema[] newArray(int size) {
            return new Cinema[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLeadImageUrl() {
        return leadImageUrl;
    }

    public void setLeadImageUrl(String leadImageUrl) {
        this.leadImageUrl = leadImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("lat")
    private Float lat;
    @SerializedName("lng")
    private Float lng;



    @SerializedName("cinema_contact")

    private String contact;
    @SerializedName("leadImageUrl")
    private String leadImageUrl;
    @SerializedName("id")
    private String id;



    public Cinema(String city, String name, String address, Float lat, Float lng, String contact, String leadImageUrl, String id) {
        this.city = city;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.contact = contact;
        this.leadImageUrl = leadImageUrl;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(name);
        dest.writeString(address);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(lng);
        }
        dest.writeString(contact);
        dest.writeString(leadImageUrl);
        dest.writeString(id);
    }
}

