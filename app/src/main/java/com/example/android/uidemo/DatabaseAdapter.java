package com.example.android.uidemo;

/**
 * Created by Honey on 16-Jun-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Honey on 08-Jun-16.
 */


public class DatabaseAdapter {
    DatabaseHelper databaseHelper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean Insert(List<ListContent> Listcontent) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id;

        for (ListContent mListcontent : Listcontent) {
            if (!dataExist(mListcontent.getTimestamp(), mListcontent.getContact_Number())) {
                Log.i("Exist", "doesnot");
                contentValues.clear();
                contentValues.put(DatabaseHelper.data[0], mListcontent.getTimestamp());
                contentValues.put(DatabaseHelper.data[1], mListcontent.getFirst_Name());
                contentValues.put(DatabaseHelper.data[2], mListcontent.getLast_Name());
                contentValues.put(DatabaseHelper.data[3], mListcontent.getContact_Number());
                contentValues.put(DatabaseHelper.data[4], mListcontent.getAddress());
                contentValues.put(DatabaseHelper.data[5], mListcontent.getLandmark());
                contentValues.put(DatabaseHelper.data[6], mListcontent.getCity());
                contentValues.put(DatabaseHelper.data[7], mListcontent.getState());
                contentValues.put(DatabaseHelper.data[8], mListcontent.getPin_Code());
                contentValues.put(DatabaseHelper.data[9], mListcontent.getEmail_ID());
                contentValues.put(DatabaseHelper.data[10], mListcontent.getItem_Purchased());
                contentValues.put(DatabaseHelper.data[11], mListcontent.getOrder_Description());
                contentValues.put(DatabaseHelper.data[12], mListcontent.getMode_Of_Payment());
                contentValues.put(DatabaseHelper.data[13], mListcontent.getTotal_Quantity());
                contentValues.put(DatabaseHelper.data[14], mListcontent.getTotal_Amount());
                contentValues.put(DatabaseHelper.data[15], mListcontent.getStatus());
                contentValues.put(DatabaseHelper.data[16], mListcontent.getCompany());
                contentValues.put(DatabaseHelper.data[17], mListcontent.getTrackingNo());

                id = db.insert(DatabaseHelper.Table_Name, null, contentValues);
                if (id < 0)
                    return false;
            }
        }
        return true;
    }

    public int getTotal() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + DatabaseHelper.Table_Name, null);
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);

        }
        Log.i("size", "" + count);
        cursor.close();
        return count;
    }

    public boolean dataExist(String TimeStamp, String phone) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = db.query(DatabaseHelper.Table_Name,
                new String[]{DatabaseHelper.data[0], DatabaseHelper.data[3]},
                DatabaseHelper.data[0] + " = ? AND " + DatabaseHelper.data[3] + " = ?",
                new String[]{TimeStamp, phone},
                null, null, null);
        while (c.moveToNext()) {
            Log.i("value", c.getString(0) + "-" + c.getString(1));

            if (TimeStamp.equals(c.getString(0)) || phone.equals(c.getString(1))) {
                Log.i("Staus", "true");
                return true;
            }
        }
        Log.i("Status", "false");
        return false;
    }

    public List<ListContent> getAllData() {
        ListContent current;
        List<ListContent> Listcontent = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, null, null, null, null, null);


            while (cursor.moveToNext()) {
                Listcontent.add(
                        new ListContent(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                        ));
            }

        } catch (SQLException s) {
            Log.d("getAllaData", s.getMessage());
            return null;
        }
        return Listcontent;
    }

    public List<ListContent> getAllDataBlank(String status) {
        List<ListContent> Listcontent = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[15] + " = '" + status + "'", null, null, null, null);
            //Cursor cursor =db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, null, null, null, null, null);

            ListContent current;
            while (cursor.moveToNext()) {
                Listcontent.add(0,
                        new ListContent(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                        ));
            }
        } catch (SQLException s) {
            Log.d("Get data:", s.getMessage());
            return null;
        }
        return Listcontent;
    }


    public List<ListContent> getAllDataCompany(String status, String Company) {
        List<ListContent> Listcontent = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query(
                    DatabaseHelper.Table_Name,
                    DatabaseHelper.data,
                    DatabaseHelper.data[15] + " = '" + status + "' AND " +
                            DatabaseHelper.data[16] + " = '" + Company + "'",
                    null, null, null, null);
            //Cursor cursor =db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, null, null, null, null, null);

            ListContent current;
            while (cursor.moveToNext()) {
                Listcontent.add(0,
                        new ListContent(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                        ));
            }
        } catch (SQLException s) {
            Log.d("Get data:", s.getMessage());
            return null;
        }
        return Listcontent;
    }

    public int getTotal(String status) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + DatabaseHelper.Table_Name + " WHERE " + DatabaseHelper.data[15] + " = ?", new String[]{status});
        int count = 0;
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);

        }
        cursor.close();
        return count;
    }

    public List<ListContent> getAllDataExcept(String status) {
        List<ListContent> Listcontent = new ArrayList<>();
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[15] + " != '" + status + "'", null, null, null, null);
            //Cursor cursor =db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, null, null, null, null, null);

            ListContent current;
            while (cursor.moveToNext()) {
                Listcontent.add(0,
                        new ListContent(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                        ));
            }
        } catch (SQLException s) {
            Log.d("Get data:", s.getMessage());
            return null;
        }
        return Listcontent;
    }

    public String getStatus(String Phone) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.Table_Name, new String[]{DatabaseHelper.data[15]}, DatabaseHelper.data[3] + " = '" + Phone + "'", null, null, null, null);

        String status = "";
        ListContent listContent = null;
        while (cursor.moveToNext()) {
            status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15]));

        }
        return status;
    }

    public void Update(String Contact_Number, String Status, String TimeStamp) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.data[15], Status);
        Log.d("UPDATED DATA VALUE:", Contact_Number + ":" + Status);
        int i = db.update(databaseHelper.Table_Name, contentValues, DatabaseHelper.data[3] + " = '" + Contact_Number + "' AND " + DatabaseHelper.data[0] + " = '" + TimeStamp + "'", null);
        Log.d("Update shld be > 0:", "" + i);
    }

    public void Update(String Contact_Number, String Company, String TrackingNo, String TimeStamp) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.data[16], Company);
        contentValues.put(DatabaseHelper.data[17], TrackingNo);
        int i = db.update(databaseHelper.Table_Name, contentValues, DatabaseHelper.data[3] + " = '" + Contact_Number + "' AND " + DatabaseHelper.data[0] + " = '" + TimeStamp + "'", null);
        Log.d("Update shld be > 0:", "" + i);
    }

    public ListContent getAllData(String phone) {
        ListContent listContent = null;
        try {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[3] + " = '" + phone + "'", null, null, null, null);

            while (cursor.moveToNext()) {
                listContent = new ListContent(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))

                );
            }
        } catch (SQLException s) {
            Log.d("Get data:", s.getMessage());
            return null;
        }
        return listContent;
    }

    public void DeleteRecord(String Phone, String TimeStamp) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.Table_Name, DatabaseHelper.data[3] + " = '" + Phone + "' AND " + DatabaseHelper.data[0] + " = '" + TimeStamp + "'", null);
    }

    public List<ListContent> getAllCourierInfo(String courierName) {
        List<ListContent> Listcontent = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[16] + " = '" + courierName +
                "'" + " AND " + DatabaseHelper.data[15] + "= 'y'"
                , null, null, null, null);
        //Cursor cursor =db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, null, null, null, null, null);

        ListContent current;
        try {
            while (cursor.moveToNext()) {
                Listcontent.add(0,
                        new ListContent(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                        ));
            }
        } catch (SQLException s) {
            Log.d("Get data:", s.getMessage());
            return null;
        }
        return Listcontent;
    }

    public List<ListContent> Search(String searchdata, int index, String status) {
        List<ListContent> Listcontent = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] SearchColumnValue = {searchdata + "%", status};
        String selectQuery = "";
        Cursor cursor = null;
        if (status.equals("a")) {
            selectQuery = "SELECT * " + " FROM " + DatabaseHelper.Table_Name + " WHERE  " +
                    DatabaseHelper.data[index] + " LIKE ?"
                    + " AND " + DatabaseHelper.data[15] + " != ?";
            cursor = db.rawQuery(selectQuery, new String[]{SearchColumnValue[0], "b"});
        } else {
            selectQuery = "SELECT * " + " FROM " + DatabaseHelper.Table_Name + " WHERE  " +
                    DatabaseHelper.data[index] + " LIKE ?"
                    + " AND " + DatabaseHelper.data[15] + " = ?";
            cursor = db.rawQuery(selectQuery, SearchColumnValue);
        }

        //Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[index] + "=?" + " AND " + DatabaseHelper.data[15] + "=?", SearchColumnValue, null, null, null);
        while (cursor.moveToNext()) {
            Listcontent.add(0,
                    new ListContent(
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                    )
            );
        }
        return Listcontent;
    }

    public List<ListContent> SearchCompany(String searchdata, int index, String company) {
        List<ListContent> Listcontent = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String selectQuery = "SELECT * " + " FROM " + DatabaseHelper.Table_Name + " WHERE  " +
                DatabaseHelper.data[index] + " LIKE ?" + " AND " +
                DatabaseHelper.data[16] + " = ?"
                + " AND " + DatabaseHelper.data[15] + "= 'y'";
        String[] SearchColumnValue = {searchdata + "%", company};
        Cursor cursor = db.rawQuery(selectQuery, SearchColumnValue);


        //Cursor cursor = db.query(DatabaseHelper.Table_Name, DatabaseHelper.data, DatabaseHelper.data[index] + "=?" + " AND " + DatabaseHelper.data[15] + "=?", SearchColumnValue, null, null, null);
        while (cursor.moveToNext()) {
            Listcontent.add(0,
                    new ListContent(
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[0])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[1])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[2])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[3])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[4])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[5])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[6])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[7])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[8])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[9])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[10])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[11])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[12])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[13])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[14])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[15])),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[16])), cursor.getString(cursor.getColumnIndex(DatabaseHelper.data[17]))
                    )
            );
        }
        return Listcontent;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String Database_Name = "userdata";
        private static final String Table_Name = "Customer_Database";
        private static final int Database_Version = 2;
        private static final String[] data = {"Timestamp", "First_Name", "Last_Name",
                "Contact_Number", "Address", "Landmark_",
                "City", "State", "Pin_Code",
                "Email_ID", "Item_Purchased", "Order_Description",
                "Mode_Of_Payment", "Total_Quantity", "Total_Amount",
                "Status", "Company", "TrackingNo"};
        private static final String Create_Query = "CREATE TABLE " + Table_Name + " (" +
                data[0] + " VARCHAR(255), " + data[1] + " VARCHAR(255), " + data[2] + " VARCHAR(255), " +
                data[3] + " VARCHAR(255), " + data[4] + " VARCHAR(255), " + data[5] + " VARCHAR(255), " +
                data[6] + " VARCHAR(255), " + data[7] + " VARCHAR(255), " + data[8] + " VARCHAR(255), " +
                data[9] + " VARCHAR(255), " + data[10] + " VARCHAR(255), " + data[11] + " VARCHAR(255), " +
                data[12] + " VARCHAR(255), " + data[13] + " VARCHAR(255), " + data[14] + " VARCHAR(255), " +
                data[15] + " VARCHAR(255), " + data[16] + " VARCHAR(255), " + data[17] + " VARCHAR(255));";
        Context context = null;
        private SQLiteDatabase db;

        public DatabaseHelper(Context context) {
            super(context, Database_Name, null, Database_Version);
            this.context = context;
        }

        public void open() {
            db = getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(Create_Query);
            } catch (SQLException s) {
                System.out.println("SqlException:" + s.getMessage().toString());

            }
            System.out.println("Create succesfully");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        }
    }
}
