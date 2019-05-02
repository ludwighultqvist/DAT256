package com.bulbasaur.dat256.viewmodel.uielements;

public class MarkerData {

    private String title, description;
    private int titleColor, descriptionColor;

    public MarkerData(String title, int titleColor, String description, int descriptionColor) {
        this.title = title;
        this.titleColor = titleColor;
        this.description = description;
        this.descriptionColor = descriptionColor;
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
