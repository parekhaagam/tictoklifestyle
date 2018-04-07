package com.example.android.uidemo;

import java.util.ArrayList;

/**
 * Created by Honey on 23-Jul-16.
 */
public class SheetContent {
    ArrayList<ListContent> Sheet1;

    public SheetContent(ArrayList<ListContent> sheet1) {
        Sheet1 = sheet1;
    }

    public ArrayList<ListContent> getSheet1() {
        return Sheet1;
    }

    public void setSheet1(ArrayList<ListContent> sheet1) {
        Sheet1 = sheet1;
    }
}
