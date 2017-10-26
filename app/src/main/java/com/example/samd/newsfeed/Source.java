package com.example.samd.newsfeed;

import java.io.Serializable;

/**
 * Created by samd on 10/25/2017.
 */

public class Source implements Serializable
{
    String id;
    String name;

    public Source() {
    }

    @Override
    public String toString() {
        return name;
    }
}
