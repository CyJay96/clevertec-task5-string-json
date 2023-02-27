package ru.clevertec.data;

import java.util.Objects;

public class Floor {

    private Byte width;

    private Float height;

    public Floor() {
        width = 8;
        height = 6.4f;
    }

    public Byte getWidth() {
        return width;
    }

    public void setWidth(Byte width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return Objects.equals(width, floor.width) && Objects.equals(height, floor.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "Floor{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
