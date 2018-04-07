package com.example.android.uidemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FedexFragment extends Fragment {

    View rootView;
    Activity mainActivity;
    DatabaseAdapter databaseAdapter;
    Spinner Searchspinner;
    FedexRecyclerViewAdapter mAdapter;
    ArrayList<ListContent> listContent;
    ArrayList<ListContent> DelhiverylistContent;
    String searchQuery, data;
    TextView Search;
    TextWatcher textWatcher;
    TableLayout tableLayout;
    TableLayout tl;
    TableRow tr;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mainActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_fedex, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        this.listContent = new ArrayList<>();
        this.DelhiverylistContent = new ArrayList<>();
        databaseAdapter = new DatabaseAdapter(mainActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.DelhiverylistContent.addAll(databaseAdapter.getAllDataCompany("y", "Delhivery"));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));
        this.mAdapter = new FedexRecyclerViewAdapter(mainActivity, this.listContent, getActivity());
        this.Searchspinner = (Spinner) rootView.findViewById(R.id.search);
        tl = (TableLayout) rootView.findViewById(R.id.maintable);
        TextView name = new TextView(this.mainActivity);
        TextView status = new TextView(this.mainActivity);
        int i = 0;
        Download download = new Download();
        String statusDisplay = "";
        for (ListContent list : this.DelhiverylistContent) {
            TableRow row = new TableRow(this.mainActivity);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setBackgroundResource(R.drawable.border);
            row.setLayoutParams(lp);

            name.setText(list.getFirst_Name() + " " + list.getLast_Name());
            try {
                statusDisplay = download.execute(list.getTrackingNo()).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status.setText(statusDisplay);
            row.addView(name);
            row.addView(status);
            tl.addView(row, i);
            i++;
        }
        mRecyclerView.setAdapter(mAdapter);
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

        this.Search = (TextView) rootView.findViewById(R.id.Search);
        Search.addTextChangedListener(textWatcher);
        refresh();
        return rootView;
    }

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

    public class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String stringBuffer = "";
            SharedPreferences sharedPreferencesnotification = getActivity().getSharedPreferences("NotificationService", Context.MODE_PRIVATE);
            //ListContent listContent : listContents

            if (params[0].length() > 2) {
                line = "";
                try {

                    url = new URL("https://www.textise.net/showText.aspx?strURL=https%253A//track.delhivery.com/p/" + params[0] + "#main");
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
                        return "Delivered";

                    } else {
                        Log.i("Index", "doesonot exist");
                        return "Undelivered";
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
            return "Invalid Tracking No";
        }
    }
}
