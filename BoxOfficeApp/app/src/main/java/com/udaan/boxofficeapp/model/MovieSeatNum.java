package com.udaan.boxofficeapp.model;

/**
 * Created by shrikanthravi on 01/03/18.
 */

public class MovieSeatNum {
    String num;
    boolean selected=false;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MovieSeatNum(String num, boolean selected) {
        this.num = num;
        this.selected = selected;
    }
}