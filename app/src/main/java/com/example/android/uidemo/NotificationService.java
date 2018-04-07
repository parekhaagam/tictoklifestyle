package com.example.android.uidemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NotificationService extends Service {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
    ArrayList<ListContent> listContents = new ArrayList<>();

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Download download = new Download();
        try {
            download.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        stopService(intent);
        return START_NOT_STICKY;
    }

    public void notificationService(String data, String name, int NOTIFICATION_ID, String phone) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.profile_pic)
                        .setContentTitle(name)
                        .setContentText(data);


        Intent targetIntent = new Intent(this, Detail.class);
        targetIntent.putExtra("phone", phone);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    public class Download extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String stringBuffer = "";
            SharedPreferences sharedPreferences = getSharedPreferences("NotificationService", MODE_PRIVATE);
            int id = sharedPreferences.getInt("id", 100);

            listContents.addAll(databaseAdapter.getAllDataCompany("y", "Delhivery"));
            //ListContent listContent : listContents
            for (ListContent listContent : listContents) {
                if (listContent.getTrackingNo().length() > 2) {
                    line = "";
                    try {

                        url = new URL("https://www.textise.net/showText.aspx?strURL=https%253A//track.delhivery.com/p/" + listContent.getTrackingNo().trim() + "#main");
                        is = url.openStream();  // throws an IOException
                        br = new BufferedReader(new InputStreamReader(is));

                        while ((line = br.readLine()) != null) {
                            // System.out.println(line);
                            //Log.i("Source code:", line);
                            stringBuffer += line;
                        }

                        if (stringBuffer.contains("Delivered to consignee")) {
                            Log.i("data", stringBuffer);
                            Log.i("Index", "" + stringBuffer.indexOf("Delivered to consignee"));
                            Log.i("Substring", stringBuffer.substring(stringBuffer.indexOf("Delivered to consignee"), stringBuffer.indexOf("Delivered to consignee") + 100));
                            notificationService("Delivered", listContent.getFirst_Name() + " " + listContent.getLast_Name(), id, listContent.getContact_Number());
                            id++;

                        } else {
                            Log.i("Index", "doesonot exist");
                            notificationService("UnDelivered", listContent.getFirst_Name() + " " + listContent.getLast_Name(), id, listContent.getContact_Number());
                        }
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
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", id);
            editor.apply();
            return null;
        }
    }
}
