package com.example.olio_uusi.ui;

// this class is getter/setter-class for the user carbon footprint.
// with this class we are able to save the users results to database and get it from there.

public class CarbonfpValue {
    private int totalfp;

    public CarbonfpValue() {
    }

    public CarbonfpValue(int totalfp) {
        this.totalfp = totalfp;
    }


    public int getTotalfp() {
        return totalfp;
    }


    public void setTotalfp(int totalfp) {
        this.totalfp = totalfp;
    }
}


