package com.example.otto.forum;

import android.os.Parcelable;

/**
 * Created by Gabriel on 07.01.2018.
 */

public class Item {
    public long timestamp;
    public String value;

    public Item(long timestamp, String value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Item(){

    }
}
