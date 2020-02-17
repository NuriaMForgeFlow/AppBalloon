package com.example.appballoon.Model;

public class Wind {
    private double speed ;
    private double deg ;

    public Wind() {
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    @Override
    public String toString() {
        return this.speed + " m/s" + " "+this.deg + " º";

    }
}
