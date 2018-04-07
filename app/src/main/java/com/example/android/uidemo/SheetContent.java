package com.example.android.uidemo;

import java.util.ArrayList;

/**
 * Created by Honey on 23-Jul-16.
 */
public class SheetContent {
    ArrayList<ListContent> records;

    public SheetContent(ArrayList<ListContent> records) {
        this.records = records;
    }

    public ArrayList<ListContent> getSheet1() {
        return records;
    }

    public void setSheet1(ArrayList<ListContent> sheet1) {
        records = records;
    }
}
