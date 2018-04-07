package com.example.android.uidemo;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Calendar;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Honey on 17-Jun-16.
 */
public class DashboardFragment extends Fragment {

    Activity mainActivity;
    DashboardSwipeRecyclerViewAdapter mAdapter;
    DatabaseAdapter databaseAdapter;
    SharedPreferences mSharePreference;
    SharedPreferences.Editor mEditor;
    int noOfRecord;
    Spinner Searchspinner;
    TextView Search;
    String data, searchQuery;
    TextWatcher textWatcher;
    SwipeRefreshLayout swipeRefreshLayout;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    View rootView;
    FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private ArrayList<ListContent> listContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mainActivity = getActivity();
        rootView = inflater.inflate(R.layout.dashbard_swipe_recyclerview, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        /*mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) rootView.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                getData(rootView);
            }
        });*/
        this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout.setColorSchemeResources(new int[]{R.color.orange, R.color.green, R.color.blue});
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(rootView);
            }
        });

        fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        databaseAdapter = new DatabaseAdapter(mainActivity);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));

        this.listContent = new ArrayList<ListContent>();

        // loadData();
        this.mAdapter = new DashboardSwipeRecyclerViewAdapter(mainActivity, this.listContent, getActivity());
        this.Searchspinner = (Spinner) rootView.findViewById(R.id.search);

        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((DashboardSwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */

        mSharePreference = mainActivity.getSharedPreferences("DataRecord", Context.MODE_PRIVATE);
        this.noOfRecord = mSharePreference.getInt("no", 0);
        this.listContent.clear();
        refresh();

        textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Searching();
            }
        };
        Search = (TextView) rootView.findViewById(R.id.Search);
        Search.addTextChangedListener(textWatcher);
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("Navigation", Context.MODE_PRIVATE);
        String show = sharedPreferences.getString("Show", "empty");
        Log.i("show", show);
        if (!show.equals("empty") && show.contains("&") && show.startsWith("0")) {
            show = show.substring(show.indexOf("&") + 1, show.length());
            Log.i("show num", show);
            for (int i = 0; i < listContent.size(); i++) {
                if (show.equals(listContent.get(i).getContact_Number()))
                    this.mRecyclerView.smoothScrollToPosition(i);
            }
        }
        return rootView;
    }


    public void Searching() {
        data = Search.getText().toString();
        searchQuery = Searchspinner.getSelectedItem().toString();
        int index = 0;
        if (searchQuery.equals("By Name")) {
            index = 1;
        } else if (searchQuery.equals("By Phone")) {
            index = 3;
        } else if (searchQuery.equals("By Watch")) {
            index = 11;
        }
        System.out.println(data + ":" + index + ":" + "b");
        listContent.clear();
        listContent.addAll(databaseAdapter.Search(data, index, "b"));
        mAdapter.notifyDataSetChanged();
    }


    public void refresh() {
        this.listContent.clear();
        try {
            listContent.addAll(databaseAdapter.getAllDataBlank("b"));
            this.mAdapter.notifyDataSetChanged();
        } catch (NullPointerException n) {
            Log.d("Null pointer", n.getMessage());
        }
    }

    public void getData(View view) {
        if (isNetworkAvailable()) {
            //  Toast.makeText(mainActivity, "Downloading......", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(mainActivity, NotificationService.class);
            // downloadContent(view);
            //mainActivity.startService(intent);
            downloadContentRetroFit(view);
            Calendar c = Calendar.getInstance();
            Log.i("Time", "" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.MILLISECOND));
        } else {
            Snackbar.make(view, "Internet Not Available\nPlease start Internet.", Snackbar.LENGTH_LONG).
                    setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getData(view);
                        }
                    }).show();
            //this.swipeRefreshLayout.setRefreshing(false);
            this.mWaveSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void save(ArrayList<ListContent> mArrayContent) {
        boolean status = databaseAdapter.Insert(mArrayContent);
        if (status)
            Log.d("Insert data", "Sucessfully");
        else
            Log.d("Insert data", "Unsucessfully");
    }

    public void downloadContentRetroFit(final View view) {
        //      progress.show();
        RetrofitInterface retrofitInterface =
                RetrofitInstance.getInstance().create(RetrofitInterface.class);
        //1DuKtWGv2phpMG5ft4TZNeG2OM2nn2Cd3h3qW4HdIBJk  dummy
        //1L0xk_gAOJ7kpaDIUZZ29-CyCO3qB8kfAEdMTdLmh9qA real
        // url https://script.google.com/macros/s/AKfycbzGvKKUIaqsMuCj7-A2YRhR-f7GZjl4kSxSN1YyLkS01_CfiyE/exec?
        //   id=1L0xk_gAOJ7kpaDIUZZ29-CyCO3qB8kfAEdMTdLmh9qA&sheet=Sheet1
        Call<SheetContent> call = retrofitInterface.getData("1L0xk_gAOJ7kpaDIUZZ29-CyCO3qB8kfAEdMTdLmh9qA","Sheet1");
        call.enqueue(new Callback<SheetContent>() {

            @Override
            public void onResponse(Call<SheetContent> call, Response<SheetContent> response) {

                int previousTotal = databaseAdapter.getTotal();

                    for (int i = 0; i < response.body().getSheet1().size(); i++) {
                        if (response.body().getSheet1().get(i).getStatus().equals(""))
                            response.body().getSheet1().get(i).setStatus("b");
                        if(response.body().getSheet1().get(i).getCompany().equals(""))
                        response.body().getSheet1().get(i).setCompany("none");
                        if (response.body().getSheet1().get(i).getContact_Number().equals(""))
                            response.body().getSheet1().remove(i);
                        else if (response.body().getSheet1().get(i).getTimestamp().equals(""))
                            response.body().getSheet1().remove(i);
                        else if (response.body().getSheet1().get(i).getTotal_Amount().equals(""))
                            response.body().getSheet1().remove(i);
                    }
                    //response.body().getSheet1()
                    save(response.body().getSheet1());
                    int currentTotal = databaseAdapter.getTotal();
                    Log.i("current total:", currentTotal + ":" + previousTotal);
                    if ((currentTotal - previousTotal) > 0) {

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mainActivity);
                        mBuilder.setSmallIcon(R.drawable.profile_pic);
                        mBuilder.setContentTitle("TIC_TOK_LIFESTYLE");
                        mBuilder.setContentText("" + (currentTotal - previousTotal) + " NEW ENTRY");
                        NotificationManager mNotificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(26, mBuilder.build());
                        refresh();
                        ((Vibrator)mainActivity.getSystemService(mainActivity.VIBRATOR_SERVICE)).vibrate(800);
                    }else {
                        Snackbar
                                .make(rootView, "No new data", Snackbar.LENGTH_LONG)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getData(view);
                                    }
                                }).show();
                        ((Vibrator)mainActivity.getSystemService(mainActivity.VIBRATOR_SERVICE)).vibrate(800);
                    }
                    //Toast.makeText(mainActivity, "Downloading Completed.", Toast.LENGTH_LONG).show();

                //progress.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                //mWaveSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<SheetContent> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                //mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /*public void downloadContent(View view) {
        String result = null;

        try {
            DownloadTsk task = new DownloadTsk();
            String SheetLink;
            SheetLink = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1L0xk_gAOJ7kpaDIUZZ29-CyCO3qB8kfAEdMTdLmh9qA";
            //SheetLink = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1DuKtWGv2phpMG5ft4TZNeG2OM2nn2Cd3h3qW4HdIBJk";
            result = task.execute(SheetLink).get();
            JSONObject jsonRootObject = null;

            String[] data = {"Timestamp", "First_Name", "Last_Name",
                    "Contact_Number", "Address", "Landmark_",
                    "City", "State", "Pin_Code",
                    "Email_ID", "Item_Purchased", "Order_Description",
                    "Mode_Of_Payment", "Total_Quantity", "Total_Amount",
                    "Status"};

            String[] receivedData = new String[data.length];

            try {
                jsonRootObject = new JSONObject(result);
                JSONArray jsonArray = jsonRootObject.optJSONArray("Sheet1");
                mSharePreference = mainActivity.getSharedPreferences("DataRecord", Context.MODE_PRIVATE);
                noOfRecord = mSharePreference.getInt("no", 0);
                if (noOfRecord < jsonArray.length()) {
                    ArrayList<ListContent> newElement = new ArrayList<>();
                    for (int i = noOfRecord; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        for (int j = 0; j < data.length; j++)
                            receivedData[j] = jsonObject.optString(data[j]).toString();
                        if (receivedData[15].equals("")) {
                            receivedData[15] = "b";
                        }
                        ListContent content = new ListContent(receivedData[0], receivedData[1], receivedData[2],
                                receivedData[3], receivedData[4], receivedData[5],
                                receivedData[6], receivedData[7], receivedData[8],
                                receivedData[9], receivedData[10], receivedData[11],
                                receivedData[12], receivedData[13], receivedData[14],
                                receivedData[15], " ", " "
                        );
                        listContent.add(0, content);
                        newElement.add(content);
                    }
                    save(newElement);
                    mEditor = mSharePreference.edit();
                    mEditor.putInt("no", jsonArray.length());
                    mEditor.commit();
                    refresh();
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mainActivity);
                    mBuilder.setSmallIcon(R.drawable.profile_pic);
                    mBuilder.setContentTitle("TIC_TOK_LIFESTYLE");
                    mBuilder.setContentText("" + (jsonArray.length() - noOfRecord) + " NEW ENTRY");
                    NotificationManager mNotificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(26, mBuilder.build());
                } else {
                    Snackbar
                            .make(view, "No new data", Snackbar.LENGTH_LONG)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getData(view);
                                }
                            }).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {

        }

    }*/

    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    public void onDestroy() {
        mAdapter.notifyDataSetChanged();
        super.onDestroy();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /*public class DownloadTsk extends AsyncTask<String, Integer, String> {

        final int totalProgressTime = 100;
        int length = 0;
        int time = 0;

        @Override
        protected void onPreExecute() {
            //     progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //   progress.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            System.out.println(values[0]);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                Log.d("Response:", "" + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {

                        response.append(inputLine);
                        time += inputLine.length();
                        // publishProgress(time);
                    }
                    in.close();
                    return response.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unsucessful";
        }
    }*/
}
