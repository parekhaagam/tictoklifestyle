package com.example.android.uidemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import au.com.bytecode.opencsv.CSVReader;

public class PinCodeFragment extends Fragment {

    View rootView;
    EditText pincode;
    TextView DELHIVERYCOD,FEDEXCOD;
    TableLayout CODTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pin_code, container, false);
        pincode = (EditText) rootView.findViewById(R.id.pinCodeText);
        DELHIVERYCOD = (TextView) rootView.findViewById(R.id.DELHIVERYCOD);
        FEDEXCOD = (TextView) rootView.findViewById(R.id.FEDEXCOD);
        CODTable = (TableLayout) rootView.findViewById(R.id.CODTable);

        if(pincode.getEditableText().toString().length() == 6){

            CODTable.setVisibility(View.VISIBLE);
            try {

                new ExcelTsk().execute(" ").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }
        else{
            Snackbar.make(rootView , "Please Enter valid Pin Code",Snackbar.LENGTH_LONG).show();
        }
        return rootView;
    }

    public class ExcelTsk extends AsyncTask<String, Integer, String> {

        final int totalProgressTime = 100;
        int length = 0;
        int time = 0;

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
        private String readCSVFile(String csvFilename, int index) {
            CSVReader csvReader = null;
            try {
                csvReader = new CSVReader(new FileReader(csvFilename));

                String[] row = null;
                while ((row = csvReader.readNext()) != null) {
                    if (row[0].equals(pincode.getEditableText().toString())) {
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
    }

}
