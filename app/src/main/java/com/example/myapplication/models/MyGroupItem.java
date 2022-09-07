package com.example.myapplication.models;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;

public class MyGroupItem {

    @Expose
    private int id;
    @Expose
    private String title;
    @Expose
    private int availableSlots = 0;
    @Expose
    private List<MyChildItems> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<MyChildItems> getChildren() {
        return children;
    }

    public void setChildren(List<MyChildItems> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyGroupItem that = (MyGroupItem) o;
        return getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}
