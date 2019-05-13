package com.bulbasaur.dat256.viewmodel.uielements;

public class MarkerData {

    private boolean meetUpOrNah;
    private String title, description;
    private int titleColor, descriptionColor;

    public MarkerData(boolean meetUpOrNah, String title, int titleColor, String description, int descriptionColor) {
        this.meetUpOrNah = meetUpOrNah;
        this.title = title;
        this.titleColor = titleColor;
        this.description = description;
        this.descriptionColor = descriptionColor;
    }

    public boolean isMeetUp() {
        return meetUpOrNah;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getDescriptionColor() {
        return descriptionColor;
    }
}
