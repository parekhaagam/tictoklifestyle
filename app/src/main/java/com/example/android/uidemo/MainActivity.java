package com.example.android.uidemo;

//import android.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    public static Toolbar toolbar;
    final String dashBoard = "dashBoard";
    final String list = "list";
    final String status = "status";
    final String analysis = "dashBoard";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FloatingActionButton fab;
    DashboardFragment dashboardFragment;
    ListFragment listFragment;
    AnalysisFragment analysisFragment;
    DeliveryStatusFragment deliveryStatusFragment;
    PinCodeFragment pinCodeFragment;
    private com.mxn.soul.flowingdrawer_core.FlowingDrawer mDrawerLayout;
    private NavigationView navigationView;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    public void onBackPressed() {
        this.finish();
        SharedPreferences sharedPreferences = getSharedPreferences("Navigation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Show", "0");
        editor.commit();
        System.exit(0);
        // super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dashboardFragment = new DashboardFragment();
        listFragment = new ListFragment();
        analysisFragment = new AnalysisFragment();
        deliveryStatusFragment = new DeliveryStatusFragment();
        pinCodeFragment = new PinCodeFragment();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawerLayout.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Navigation", Context.MODE_PRIVATE);
        String show = sharedPreferences.getString("Show", "0");
        if (show.startsWith("0"))
            updateDisplay(dashboardFragment, dashBoard, 0);
        else if (show.startsWith("1"))
            updateDisplay(listFragment, list, 1);
        else if (show.startsWith("3"))
            updateDisplay(deliveryStatusFragment, status, 3);



        //navigationView.getMenu().getItem(3).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeMenu();

                switch (menuItem.getItemId()) {

                    case R.id.navigation_item_attachment:
                        updateDisplay(dashboardFragment, dashBoard, 0);
                        break;

                    case R.id.navigation_item_images:
                        updateDisplay(listFragment, list, 1);
                        break;

                   /* case R.id.navigation_item_location:
                        updateDisplay(analysisFragment, analysis, 2);
                        break;
                    case R.id.navigation_deliver_status:
                        //updateDisplay(new AnalysisFragment());
                        updateDisplay(deliveryStatusFragment, status, 3);

                    case R.id.navigation_pincode_list:
                        //updateDisplay(new AnalysisFragment());
                        updateDisplay(pinCodeFragment, status, 3);
                        break;*/

                }

                return true;
            }
        });

//        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());
        this.sharedPreferences = getSharedPreferences("Notifiation_Receiver", MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        boolean status = sharedPreferences.getBoolean("exist", false);
        if (!status) {
            setAlarm();
            editor.putBoolean("exist", true);
            editor.apply();
        }
    }

    public void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
        Log.i("Time set", calendar.get(Calendar.DATE) + ":" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\t" + calNow.get(Calendar.HOUR_OF_DAY) + ":" + calNow.get(Calendar.MINUTE));
        Toast.makeText(this, "Alarm Set:\n" + "Date" + calendar.get(Calendar.DATE) + "\n" + "Time:" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }*/

    public void updateDisplay(android.support.v4.app.Fragment fragment, String fragmentName, int position) {
        Log.i("AT", "Update Display");
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, fragmentName).commitAllowingStateLoss();
        navigationView.getMenu().getItem(position).setChecked(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.toggleMenu();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    voiceInput(result.get(0));
                }
                break;
            }
        }
    }

    public void voiceInput(String input) {
        input = input.toLowerCase();
        Log.i("voice Input", input);


        if (input.toLowerCase().contains("open")) {

            if (input.contains("dashboard"))
                updateDisplay(dashboardFragment, dashBoard, 0);
            else if (input.contains("list"))
                updateDisplay(listFragment, list, 1);
            else if (input.contains("analysis"))
                updateDisplay(analysisFragment, analysis, 2);
            else if (input.contains("status"))
                updateDisplay(deliveryStatusFragment, status, 3);

        } else if (input.contains("find")) {
            Pattern word = Pattern.compile("find");
            Matcher match = word.matcher(input.toLowerCase());

            if (match.find()) {
                SharedPreferences sharedPreferences = getSharedPreferences("Speech", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String find = input.substring(match.end(), input.length());
                Log.i("AT", "Update Display");
                /*ArrayList<String> arrayList;
                String history=sharedPreferences.getString("history", "n");
                arrayList=new ArrayList<>();
                if(!history.equals("n")){
                    String[] historyArry=history.split("%");
                    for (String historyString:historyArry){
                        arrayList.add(0,historyString);
                    }*/
                String history = sharedPreferences.getString("history", "empty");
                if (history.equals("empty"))
                    editor.putString("History", input);
                else
                    editor.putString("History", history + "%" + input);

                editor.putBoolean("Status", true);
                editor.putString("Find", find);
                editor.commit();
                updateDisplay(listFragment, list, 1);
            }
        }
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe left' action only

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

         /*
         Toast.makeText(getBaseContext(),
          event1.toString() + "\n\n" +event2.toString(),
          Toast.LENGTH_SHORT).show();
         */

            if (event2.getX() < event1.getX()) {
                Toast.makeText(getBaseContext(),
                        "Swipe left - startActivity()",
                        Toast.LENGTH_SHORT).show();

                //switch another activity
                Intent intent = new Intent(
                        MainActivity.this, Detail.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
            return true;
        }
    }

}
