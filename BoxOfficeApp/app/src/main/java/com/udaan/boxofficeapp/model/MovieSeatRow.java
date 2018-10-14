package com.udaan.boxofficeapp.model;

/**
 * Created by shrikanthravi on 01/03/18.
 */

public class MovieSeatRow {
    String row;
    boolean selected=false;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MovieSeatRow(String row, boolean selected) {
        this.row = row;
        this.selected = selected;
    }
}