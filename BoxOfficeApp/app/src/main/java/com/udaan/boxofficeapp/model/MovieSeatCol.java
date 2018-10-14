package com.udaan.boxofficeapp.model;

/**
 * Created by shrikanthravi on 01/03/18.
 */

public class MovieSeatCol {
    String col;
    boolean selected=false;

    public MovieSeatCol(String col, boolean selected) {
        this.col = col;
        this.selected = selected;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}