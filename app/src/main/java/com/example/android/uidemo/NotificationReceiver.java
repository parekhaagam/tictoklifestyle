package com.example.android.uidemo;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class NotificationReceiver extends BroadcastReceiver {
    DatabaseAdapter databaseAdapter;
    ArrayList<ListContent> listContents = new ArrayList<>();
    Context context;
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.context = context;
        this.intent = intent;
        this.databaseAdapter = new DatabaseAdapter(this.context);
        this.sharedPreferences = context.getSharedPreferences("Notifiation_Receiver", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        if (!isNetworkAvailable()) {
            increaseAlarm();
        } else {
            editor.putBoolean("exist", false);
            editor.apply();
            Download download = new Download();
            try {
                download.execute().get();
                setAlarm();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public void increaseAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        if ((calNow.MINUTE + 1) > 60) {
            calendar.set(calendar.MINUTE, (calNow.get(Calendar.MINUTE) + 1) % 60);
            calendar.set(calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR) + 1);

        } else {
            calendar.set(calendar.MINUTE, calNow.get(Calendar.MINUTE) + 1);
            calendar.set(calendar.HOUR_OF_DAY, calNow.get(Calendar.HOUR));
        }
        calendar.set(calendar.SECOND, 0);
        calendar.set(calendar.MILLISECOND, 0);
        Log.i("Alaram Time", "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
        //Log.i("Time set", "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)+"\t"+calNow.get(Calendar.HOUR_OF_DAY)+":"+calNow.get(Calendar.MINUTE));
        Intent myIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.compareTo(calNow) <= 0) {
            //Today Set time passed, count to tomorrow
            calendar.add(Calendar.DATE, 1);
        }
        Log.i("Time set", "" + calendar.get(Calendar.DATE) + ":" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\t" + calNow.get(Calendar.HOUR_OF_DAY) + ":" + calNow.get(Calendar.MINUTE));
        Toast.makeText(context, "Alarm Set:\n" + "Date" + calendar.get(Calendar.DATE) + "\n" + "Time:" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(this.context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    public void notificationService(String data, String name, int NOTIFICATION_ID, String phone1) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.context)
                        .setSmallIcon(R.drawable.profile_pic)
                        .setContentTitle(name)
                        .setContentText(data);


        Intent targetIntent = new Intent(this.context, Detail.class);
        targetIntent.putExtra("phone", phone1);
        Log.i("Phone", phone1);
        PendingIntent contentIntent = PendingIntent.getActivity(this.context, NOTIFICATION_ID, targetIntent, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public class Download extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String stringBuffer = "";
            SharedPreferences sharedPreferencesnotification = context.getSharedPreferences("NotificationService", Context.MODE_PRIVATE);
            int id = sharedPreferences.getInt("id", 100);
            listContents.addAll(databaseAdapter.getAllDataCompany("y", "Delhivery"));
            //ListContent listContent : listContents
            for (ListContent listContent : listContents) {
                if (listContent.getTrackingNo().length() > 2) {
                    line = "";
                    try {
                        stringBuffer = "";
                        url = new URL("https://www.textise.net/showText.aspx?strURL=https%253A//track.delhivery.com/p/" + listContent.getTrackingNo().trim() + "#main");
                        is = url.openStream();  // throws an IOException
                        br = new BufferedReader(new InputStreamReader(is));

                        while ((line = br.readLine()) != null) {
                            // System.out.println(line);
                            //Log.i("Source code:", line);
                            stringBuffer += line;
                        }

                        if (stringBuffer.toLowerCase().contains("Delivered to consignee".toLowerCase())) {
                            //   Log.i("data", stringBuffer);
                            // Log.i("Index", "" + stringBuffer.indexOf("Delivered to consignee"));
                            // Log.i("Substring", stringBuffer.substring(stringBuffer.indexOf("Delivered to consignee"), stringBuffer.indexOf("Delivered to consignee") + 100));
                            notificationService("Delivered", listContent.getFirst_Name() + " " + listContent.getLast_Name(), id, listContent.getContact_Number());


                        } else {
                            Log.i("Index", "doesonot exist");
                            notificationService("Undelivered", listContent.getFirst_Name() + " " + listContent.getLast_Name(), id, listContent.getContact_Number());
                        }
                        Log.i("Detail", listContent.getFirst_Name() + " " + listContent.getLast_Name() + ":" + listContent.getContact_Number());
                        id++;
                    } catch (MalformedURLException mue) {
                        mue.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException ioe) {
                            // nothing to see here
                        }
                    }
                }
            }
            SharedPreferences.Editor editornotification = sharedPreferencesnotification.edit();
            editornotification.putInt("id", id);
            editornotification.apply();
            return null;
        }
    }
}
