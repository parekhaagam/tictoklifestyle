package com.example.android.uidemo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Detail extends ActionBarActivity {
    Toolbar toolbar;
    ExpandableListView expandableListView;
    String phone;
    TextView getDelveryStatus;
    WebView browser;
    TextView tvName;
    TextView tvBuyingPrice;
    TextView tvCityState;
    TextView tvShowMore;
    ImageView ivWhatsAppSend;
    String TrackingNo;
    TextView tvAddres;
    TextView tvAddres2;
    TextView pinCode;
    TextView FEDEXCOD;
    TextView DELHIVERYCOD;
    TextView GETCOD;
    TableLayout AddressTable;
    TableLayout CODTable;
    DatabaseAdapter databaseAdapter;
    RadioButton FedEx, Delhivery;
    RadioGroup CompanyGrp;
    Activity detailActivity;
    FloatingActionButton fab;
    boolean statusEdit;
    ListContent listContent;

    // private GestureDetectorCompat gestureDetectorCompat;
    float x1, x2;
    float y1, y2;
    CardView rootview;
    TextView imageView;

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;

    }

    @Override
    public void onBackPressed() {
        String TrackingNotxt = TrackingNo;
        Log.d("Tracking no value:", TrackingNotxt);
        String Company = "";
        if (FedEx.isChecked()) {
            Company = "FedEx";
        }
        if (Delhivery.isChecked()) {
            Company = "Delhivery";

        }
        databaseAdapter.Update(this.phone, Company, TrackingNotxt, listContent.getTimestamp());
        Intent intent = new Intent(detailActivity, MainActivity.class);
        detailActivity.startActivity(intent);
        detailActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        //detailActivity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        this.finish();
        detailActivity.finish();
        //  super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        detailActivity = this;
        Intent startingIntent = getIntent();
        this.phone = startingIntent.getStringExtra("phone");
        this.browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);

        this.getDelveryStatus = (TextView) findViewById(R.id.getDeliveyStatus);
        tvName = (TextView) findViewById(R.id.Name);
        tvBuyingPrice = (TextView) findViewById(R.id.tvBuyingPrice);
        tvCityState = (TextView) findViewById(R.id.tvCityState);
        tvShowMore = (TextView) findViewById(R.id.ShowMore);

        ivWhatsAppSend = (ImageView) findViewById(R.id.whatsAppSend);

        tvAddres = (TextView) findViewById(R.id.tvAddres);
        tvAddres2 = (TextView) findViewById(R.id.tvAddres2);
        pinCode = (TextView) findViewById(R.id.tvPinCode);


        CODTable = (TableLayout) findViewById(R.id.CODTable);
        FEDEXCOD = (TextView) findViewById(R.id.FEDEXCOD);
        DELHIVERYCOD = (TextView) findViewById(R.id.DELHIVERYCOD);
        GETCOD = (TextView) findViewById(R.id.getCOD);

        final ExcelTsk task = new ExcelTsk();
        GETCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CODTable.getVisibility() == View.GONE) {
                    CODTable.setVisibility(View.VISIBLE);
                    try {

                        task.execute(" ").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    CODTable.setVisibility(View.GONE);
                }
            }
        });

        AddressTable = (TableLayout) findViewById(R.id.TableLayout2);
        FedEx = (RadioButton) findViewById(R.id.radioButton1);
        Delhivery = (RadioButton) findViewById(R.id.radioButton2);
        databaseAdapter = new DatabaseAdapter(this);
        CompanyGrp = (RadioGroup) findViewById(R.id.radioGroup);
        statusEdit = true;
        listContent = databaseAdapter.getAllData(this.phone);
        //binding.setUser(listContent);
        rootview = (CardView) findViewById(R.id.rootCard);
        imageView = (TextView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picture picture = browser.capturePicture();
                Bitmap b = Bitmap.createBitmap(
                        1200, 800, Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(b);
                picture.draw(c);

                FileOutputStream fos = null;

                File file = null;
                File folder = new File("/sdcard/tictok/");
                try {
                    file = new File("/sdcard/tictok/" + phone + ".jpeg");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    fos = new FileOutputStream(file);
                    if (fos != null) {
                        b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.close();
                    }
                    Snackbar.make(v, "Saved Sucessfully", Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    System.out.println("-----error--" + e);
                }
                Uri uri = Uri.parse("smsto:" + "9920680049");
                Intent share = new Intent(Intent.ACTION_SEND, uri);
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                share.setType("image/*");
                share.setPackage("com.whatsapp");
                startActivity(share);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        this.getDelveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWebView(v);
            }
        });

        tvName.setText((listContent.getFirst_Name()) + " " + listContent.getLast_Name());
        if (listContent.getStatus().equalsIgnoreCase("y")) {
            tvName.setTextColor(Color.parseColor("#FFEB3B"));

        } else if (listContent.getStatus().equalsIgnoreCase("r")) {
            tvName.setTextColor(Color.parseColor("#F44336"));
        } else if (listContent.getStatus().equalsIgnoreCase("g")) {
            tvName.setTextColor(Color.parseColor("#4CAF50"));
        } else if (listContent.getStatus().equalsIgnoreCase("p")) {
            tvName.setTextColor(Color.parseColor("#E91E63"));
        }

        this.tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvShowMore.getText().toString().equalsIgnoreCase("Show More")) {
                    tvShowMore.setText("Show Less");
                    AddressTable.setVisibility(View.VISIBLE);
                } else {
                    tvShowMore.setText("Show More");
                    AddressTable.setVisibility(View.GONE);
                }
            }
        });

        try {
            if (!(listContent.getCompany().equalsIgnoreCase("none"))) {
                if (listContent.getCompany().equals("FedEx"))
                    FedEx.setChecked(true);
                if (listContent.getCompany().equals("Delhivery"))
                    Delhivery.setChecked(true);
            }
        } catch (NullPointerException n) {

        }
        //tvDelivery
        int baseprice = Integer.parseInt(listContent.getTotal_Amount()) - 150;
        tvBuyingPrice.setText("₹" + baseprice);
        Log.i("Address1", listContent.getAddress());
        tvAddres.setText(listContent.getAddress());
        tvAddres2.setText(listContent.getLandmark());
        pinCode.setText(listContent.getPin_Code());

        ivWhatsAppSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listContent.getTrackingNo() != null && !listContent.getTrackingNo().isEmpty()) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra("jid", listContent.getContact_Number() + "@s.whatsapp.net");

                    String message = "";
                    if (FedEx.isChecked())
                        message = "https://www.fedex.com/apps/fedextrack/?action=track&tracknumbers=" + listContent.getTrackingNo().trim() + "&locale=en_IN&cntry_code=in";
                    else if (Delhivery.isChecked()) {
                        message = "https://track.delhivery.com/p/" + listContent.getTrackingNo().trim();

                    }

                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        });

        tvCityState.setText(listContent.getCity() + "," + listContent.getState());
        TrackingNo = listContent.getTrackingNo();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusEdit) {
                    changeStatus(true);
                    statusEdit = false;
                    fab.setImageResource(R.drawable.ic_save);
                } else {
                    changeStatus(false);
                    statusEdit = true;
                    fab.setImageResource(R.drawable.ic_mode_edit);

                }
            }
        });
    }

    public void displayWebView(View v) {
        if (!isNetworkAvailable()) {
            Snackbar.make(v, "Internet Not Available\nPlease start Internet.", Snackbar.LENGTH_LONG).show();
        } else {

            if (FedEx.isChecked() || Delhivery.isChecked()) {
                if (TrackingNo.length() > 1) {
                    if (getDelveryStatus.getText().toString().equalsIgnoreCase("Show Status")) {
                        getDelveryStatus.setText("Hide Status");
                        browser.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        if (FedEx.isChecked())
                            browser.loadUrl("https://www.fedex.com/apps/fedextrack/?action=track&tracknumbers=" + TrackingNo.toString().trim() + "&locale=en_IN&cntry_code=in");
                        else if (Delhivery.isChecked()) {
                            //  ClipboardManager _clipboard = (ClipboardManager) detailActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                            //_clipboard.setText(TrackingNo.getText().toString());
                            System.out.println("https://track.delhivery.com/p/" + TrackingNo.toString());
                            browser.loadUrl("https://track.delhivery.com/p/" + TrackingNo.trim());
                        }
                        //browser.buildDrawingCache();
                    } else {
                        getDelveryStatus.setText("Show Status");
                        browser.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                    }

                } else {
                    Snackbar.make(v, "Please Enter Tracking No", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(v, "Please Checked Radio Button", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                //if left to right sweep event on screen
                if (x1 < x2) {
                    System.out.println("Left to Right Swap Performed");
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2) {
                    System.out.println("Right to Left Swap Performed");
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2) {
                    System.out.println("UP to Down Swap Performed");
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                if (y1 > y2) {
                    System.out.println("Down to UP Swap Performed");
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }

    public void changeStatus(boolean State) {
        tvBuyingPrice.setEnabled(State);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private String readCSVFile(String csvFilename, int index) {
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(csvFilename));

            String[] row = null;
            while ((row = csvReader.readNext()) != null) {
                if (row[0].equals(listContent.getPin_Code())) {
                    return row[index];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public class ExcelTsk extends AsyncTask<String, Integer, String> {

        final int totalProgressTime = 100;
        int length = 0;
        int time = 0;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            String[] codStatus = s.split(":");

            if (codStatus[0].equalsIgnoreCase("Y")) {
                DELHIVERYCOD.setText("Available");
            } else {
                DELHIVERYCOD.setText("NOT Available");
            }

            if (codStatus[1].equalsIgnoreCase("COD")) {
                FEDEXCOD.setText("Available");
            } else {
                FEDEXCOD.setText("NOT Available");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected String doInBackground(String... urls) {

            // return readExcelFile("agam.xlsx");

            // Creating Input Stream
            String content = "";
            content += readCSVFile("/sdcard/DELHIVERY.csv", 1);
            content += ":" + readCSVFile("/sdcard/FEDEX.csv", 1);

            return content;
        }

    }

    public class UploadTsk extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            String fullUrl = "https://docs.google.com/forms/d/1UsMU6lZEwqEFTm2qBYi-6ntYz3wC63DOSo29RZCA0j0/prefill";
            String[] colmns = {"entry.355861568", "entry.874321976", "entry.1715249756",
                    "entry.2118468651", "entry.966762525", "entry.1094134142",
                    "entry.1663305248", "entry.1770279636", "entry.2040036667",
                    "entry.230494558", "entry.1116520847", "entry.617468208",
                    "entry.82789206", "entry.114845291"};
            try {

                urlConnection.setRequestMethod("Post");

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
