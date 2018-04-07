package com.example.android.uidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeliveryStatusFragment extends Fragment {
    Activity mainActivity;
    View rootView;
    DatabaseAdapter databaseAdapter;
    Spinner Searchspinner;
    FedexRecyclerViewAdapter mAdapter;
    ArrayList<ListContent> listContent;
    ArrayList<ListContent> DelhiverylistContent;
    ArrayList<ListContent> FedexlistContent;
    String searchQuery, data;
    TextView Search;
    TableLayout tl;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
//    SwipeRefreshLayout swipeRefreshLayout;
    private View.OnClickListener tablerowOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //GET TEXT HERE
            String currenttext = DelhiverylistContent.get(v.getId()).getFirst_Name();
            Log.i("Name", currenttext);
            Intent intent = new Intent(mainActivity, Detail.class);
            intent.putExtra("phone", DelhiverylistContent.get(v.getId()).getContact_Number());
            mainActivity.startActivity(intent);
            mainActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            mainActivity.finish();
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("Navigation", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Show", "3");
            editor.apply();

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentfragment_deliver_statusy
        rootView = inflater.inflate(R.layout.fragment_fedex, container, false);

        this.mainActivity = getActivity();
        this.listContent = new ArrayList<>();
        this.DelhiverylistContent = new ArrayList<>();
        this.FedexlistContent = new ArrayList<>();
        databaseAdapter = new DatabaseAdapter(mainActivity);
        /*this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout.setColorSchemeResources(new int[]{R.color.orange, R.color.green, R.color.blue});
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayTableInfo(rootView);
            }
        });
*/
        this.DelhiverylistContent.addAll(databaseAdapter.getAllDataCompany("y", "Delhivery"));
        this.FedexlistContent.addAll(databaseAdapter.getAllDataCompany("y", "FedEx"));
        tl = (TableLayout) rootView.findViewById(R.id.maintable);

        sharedPreferences = mainActivity.getSharedPreferences("DeliveryStatus", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String availableStatus = sharedPreferences.getString("statusInfo", "Unavailable");
        if (!availableStatus.equalsIgnoreCase("Unavailable")) {
            DisplayTableInfo(availableStatus);
        } else {
            Toast.makeText(mainActivity, "No Data to show", Toast.LENGTH_LONG).show();
        }
        //getFedExLink();
        return rootView;
    }

    public void getFedExLink() {
        String FedExWebLink = "";
        String TrackingList = "";
        for (ListContent tmp : this.FedexlistContent) {
            TrackingList += tmp.TrackingNo + ",";
        }

        TrackingList = TrackingList.substring(0, TrackingList.lastIndexOf(','));
        Log.i("Tracking no link", TrackingList);
        FedExWebLink = "https://www.fedex.com/apps/fedextrack/?action=track&tracknumbers="
                + TrackingList
                + "&locale=en_IN&cntry_code=in";
        Log.i("Web Link", FedExWebLink);

    }

    public void displayTableInfo(View v) {
        if (!isNetworkAvailable()) {
            Snackbar.make(v, "Network Not Available", Snackbar.LENGTH_LONG).show();
        } else {
            int i = 0;
            Download download = new Download();
            String[] statusDisplay = {""};
            try {
                statusDisplay = download.execute().get().split(",");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            tl.removeAllViews();
            String statusInfo = "";
            for (ListContent list : DelhiverylistContent) {
                TableRow row = new TableRow(mainActivity);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setBackgroundResource(R.drawable.border);
                row.setLayoutParams(lp);
                TextView name = new TextView(mainActivity);
                name.setTextSize(20);
                name.setTypeface(null, Typeface.BOLD);
                name.setPadding(4, 0, 0, 0);
                TextView status = new TextView(mainActivity);
                name.setText(list.getFirst_Name() + " " + list.getLast_Name());
                status.setText(statusDisplay[i]);
                statusInfo += list.getFirst_Name() + " " + list.getLast_Name() + "&" + statusDisplay[i] + ",";
                if (statusDisplay[i].equalsIgnoreCase("Delivered")) {
                    status.setTextColor(Color.GREEN);
                } else {
                    status.setTextColor(Color.RED);
                }
                row.addView(name);
                row.addView(status);
                row.setClickable(true);
                row.setOnClickListener(tablerowOnClickListener);//add OnClickListener Here
                //row.setClickable(true);
                row.setId(i);
                tl.addView(row, i);
                i++;

            }
            editor.putString("statusInfo", statusInfo);
            editor.apply();
        }
        //this.swipeRefreshLayout.setRefreshing(false);
    }

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

    public void DisplayTableInfo(String info) {
        Log.i("info", info);
        if (info.contains(",")) {
            info = info.substring(0, info.lastIndexOf(","));
            int i = 0;
            String[] rows = info.split(",");
            for (String currentRow : rows) {
                TableRow row = new TableRow(mainActivity);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setBackgroundResource(R.drawable.border);
                row.setLayoutParams(lp);
                TextView name = new TextView(mainActivity);
                name.setTextSize(23);
                name.setTypeface(null, Typeface.BOLD);
                name.setPadding(4, 0, 0, 0);
                TextView status = new TextView(mainActivity);
                String[] currentRowInfoData = currentRow.split("&");
                name.setText(currentRowInfoData[0]);
                status.setText(currentRowInfoData[1]);
                if (currentRowInfoData[1].equalsIgnoreCase("Delivered")) {
                    status.setTextColor(Color.GREEN);
                } else {
                    status.setTextColor(Color.RED);
                }
                row.addView(name);
                row.addView(status);
                row.setClickable(true);
                row.setId(i);
                row.setOnClickListener(tablerowOnClickListener);//add OnClickListener Here
                //row.setClickable(true);

                tl.addView(row, i);
                i++;
            }
        }
    }

    @Override
    public void onStart() {
        // viewPagerAdapter.notifyDataSetChanged();
        super.onStart();
        Log.i("Called", "start");
    }

 /*   @Override
    public void onResume() {
        super.onResume();
        viewPagerAdapter.addFragmants(new FedexFragment(), "FedEx");
        viewPagerAdapter.addFragmants(new DelhiveryFragment(), "Delhivery");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Log.i("Called", "Resume");
       // viewPagerAdapter.notifyDataSetChanged();
    }*/

    public void refresh() {
        listContent.clear();
        listContent.addAll(databaseAdapter.getAllCourierInfo("FedEx"));
        mAdapter.notifyDataSetChanged();
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
        listContent.clear();
        listContent.addAll(databaseAdapter.SearchCompany(data, index, "FedEx"));
        mAdapter.notifyDataSetChanged();
    }

    public class Download extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String returnString = "";
            String stringBuffer = "";
            ArrayList<ListContent> listContents = new ArrayList<>();
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
                            //Log.i("line",line);
                        }

                        if (stringBuffer.toLowerCase().contains("Delivered to consignee".toLowerCase())) {
                            Log.i("status", "exist");
                            //Log.i("Index", "" + stringBuffer.indexOf("Delivered to consignee"));
                            //Log.i("Substring", stringBuffer.substring(stringBuffer.indexOf("Delivered to consignee"), stringBuffer.indexOf("Delivered to consignee") + 100));
                            returnString += "Delivered,";

                        } else {
                            Log.i("status", "doesnot exist");
                            returnString += "Undelivered,";
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
            return returnString;

        }
    }

    public void findStatus(){
        String webData="<tbody>\n" +
                "              \n" +
                "                \n" +
                "                <tr>\n" +
                "                    <td>Delivered</td>\n" +
                "                    <td>Delivered</td>\n" +
                "                    <td>Nov. 2, 2016, 7:09 p.m.</td>\n" +
                "                    <td>Delivered to consignee</td>\n" +
                "                    <td>Ranchi_2 (Jharkhand)</td>\n" +
                "                    <td>Delhivery</td>\n" +
                "                </tr>\n" +
                "                \n" +
                "                <tr>\n" +
                "                    <td>Undelivered</td>\n" +
                "                    <td>Dispatched</td>\n" +
                "                    <td>Nov. 2, 2016, 2:20 p.m.</td>\n" +
                "                    <td>Out for delivery</td>\n" +
                "                    <td>Ranchi_2 (Jharkhand)</td>\n" +
                "                    <td>Delhivery</td>\n" +
                "                </tr>                " +
                "<tr>\n" +
                "                    <td>Undelivered</td>\n" +
                "                    <td>In Transit</td>\n" +
                "                    <td>Oct. 29, 2016, 5:40 p.m.</td>\n" +
                "                    <td>Shipment Picked Up at Origin Center</td>\n" +
                "                    <td>Mumbai_MaladWest_CP (Maharashtra)</td>\n" +
                "                    <td>Delhivery</td>\n" +
                "                </tr>\n" +
                "                \n" +
                "                <tr>\n" +
                "                    <td>Undelivered</td>\n" +
                "                    <td>Manifested</td>\n" +
                "                    <td>Oct. 28, 2016, 10:49 p.m.</td>\n" +
                "                    <td>Consignment Manifested</td>\n" +
                "                    <td>Mumbai_MaladWest_CP (Maharashtra)</td>\n" +
                "                    <td>Delhivery</td>\n" +
                "                </tr>\n" +
                "                \n" +
                "              \n" +
                "            </tbody>";
        String StartpatternString = "<tbody>";
        String EndpatternString = "</tbody>";

        Pattern Startpattern = Pattern.compile(StartpatternString);
        Pattern Endpattern = Pattern.compile(EndpatternString);

        Matcher Startmatcher = Startpattern.matcher(webData);
        Matcher endMatcher = Endpattern.matcher(webData);

        if(Startmatcher.find() && endMatcher.find()) {
            System.out.println("Start Index:"+Startmatcher.start()+"\tEnd Index:"+Startmatcher.end());
            System.out.println("Start Index:"+endMatcher.start()+"\tEnd Index:"+endMatcher.end());
            int startIndex = Startmatcher.end();
            int endIndex = endMatcher.start();
            String data =webData.substring(startIndex,endIndex).trim();

            StartpatternString = "<tr>";
            EndpatternString = "</tr>";

            Startpattern = Pattern.compile(StartpatternString);
            Endpattern = Pattern.compile(EndpatternString);

            Startmatcher = Startpattern.matcher(data);
            endMatcher = Endpattern.matcher(data);

            if(Startmatcher.find() && endMatcher.find()) {
                startIndex = Startmatcher.end();
                endIndex = endMatcher.start();

                data=data.substring(startIndex, endIndex).trim();
                data=data.replaceAll("<td>","");
                data=data.replaceAll("</td>",",");
                data=data.replaceAll("\\s+","");
                System.out.println(data);
            }
        }
    }
}


