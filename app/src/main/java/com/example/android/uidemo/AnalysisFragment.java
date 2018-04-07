package com.example.android.uidemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by Honey on 17-Jun-16.
 */
public class AnalysisFragment extends Fragment {
    Button tvDisplayGraph;
    LinearLayout Graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.mylocation_fragment, container, false);
        final AnalysisFragment analysisFragment = this;
        Graph = (LinearLayout) rootView.findViewById(R.id.PieChart);
        tvDisplayGraph = (Button) rootView.findViewById(R.id.tvDisplayGraph);
        tvDisplayGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDisplayGraph.getText().toString().equals("Display Pie-Chart")) {
                    tvDisplayGraph.setText("Hide Pie-Chart");
                    Graph.setVisibility(View.VISIBLE);
                    Graph.addView(createDognutChart1(v.getContext()));
                } else {
                    tvDisplayGraph.setText("Display Pie-Chart");
                    Graph.setVisibility(View.GONE);
                }
            }
        });
        return rootView;
    }

    public GraphicalView createPieChart(Context context) {

        DatabaseAdapter databaseAdapter;
        int statusNo;
        String[] DisplayMessage = {"Recent", "Deliver", "Dispatch", "Nt Avail", "Cancel"};
        String[] SearchStatus = {"b", "g", "y", "p", "r"};
        int[] allcolors = new int[]{Color.LTGRAY, 0xFF76FF03, 0xFFFFFF00, 0xFFE91E63, 0xFFB71C1C};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        ;

        databaseAdapter = new DatabaseAdapter(context);
        // this is my data of performance; data is collected in array.
        CategorySeries series = new CategorySeries("pie"); // adding series to charts. //collect 3 value in array. therefore add three series.
        statusNo = databaseAdapter.getTotal("b");
        for (int i = 0; i < 5; i++) {
            if ((statusNo = databaseAdapter.getTotal(SearchStatus[i])) != 0) {
                series.add(DisplayMessage[i], statusNo);
                colors.add(allcolors[i]);
            }
        }

// add three colors for three series respectively

// set style for series
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            r.setDisplayChartValues(true);
            r.setChartValuesSpacing(5);
            //r.setDisplayChartValuesDistance(5);
            r.setDisplayChartValues(true);
            r.setChartValuesTextSize(15);
            renderer.addSeriesRenderer(r);
        }
        // renderer.isInScroll();
        //renderer.setZoomButtonsVisible(true);   //set zoom button in Graph
        //renderer.setExternalZoomEnabled(false);
        //renderer.setClickEnabled(false);
        //renderer.setFitLegend(true);

        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE); //set background color
        //renderer.setChartTitle("PIE CHART");
        renderer.setChartTitleTextSize((float) 50);
        renderer.setLabelsColor(Color.DKGRAY);

        renderer.setShowLabels(true);
        renderer.setShowLegend(false);
        renderer.setLabelsTextSize(30);
        renderer.setLegendTextSize(45);
        renderer.setShowGrid(true);
        renderer.setDisplayValues(true);
        //return ChartFactory.getPieChartIntent(context, series, renderer, "PieChart");
        ChartFactory.getPieChartView(context, series, renderer);
        return ChartFactory.getPieChartView(context, series, renderer);
    }

    public GraphicalView createDognutChart(Context context) {
        DefaultRenderer renderer = new DefaultRenderer();

        MultipleCategorySeries multipleCategorySeries = new MultipleCategorySeries("DognutChart");
        double[] value = {(double) 1, (double) 2, (double) 3, (double) 4, (double) 5};
        String[] DisplayMessage = {"Recent", "Deliver", "Dispatch", "Nt Avail", "Cancel"};
        multipleCategorySeries.add(DisplayMessage, value);
        int[] colors = new int[]{Color.LTGRAY, 0xFF76FF03, 0xFFFFFF00, 0xFFE91E63, 0xFFB71C1C};
        ;
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            r.setDisplayChartValues(true);
            r.setChartValuesSpacing(5);
            //r.setDisplayChartValuesDistance(5);
            r.setDisplayChartValues(true);
            r.setChartValuesTextSize(15);

            renderer.addSeriesRenderer(r);
        }

        //renderer.setFitLegend(true);
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE); //set background color
        //renderer.setChartTitle("PIE CHART");
        renderer.setChartTitleTextSize((float) 50);
        renderer.setLabelsColor(Color.DKGRAY);

        renderer.setShowLabels(true);
        renderer.setLabelsTextSize(30);
        renderer.setLegendTextSize(45);
        renderer.setShowGrid(true);
        renderer.setDisplayValues(true);

        return ChartFactory.getDoughnutChartView(context, multipleCategorySeries, renderer);
    }

    public GraphicalView createDognutChart1(Context context) {
        DefaultRenderer renderer = new DefaultRenderer();

        MultipleCategorySeries multipleCategorySeries = new MultipleCategorySeries("DognutChart");

        int statusNo;
        double[] allvalue=new double[5] ;
        String[] allMessage=new String[5];
        String[] DisplayMessage = {"Recent", "Deliver", "Dispatch", "Nt Avail", "Cancel"};
        DatabaseAdapter databaseAdapter=new DatabaseAdapter(context);
        int[] allcolors = new int[]{Color.LTGRAY, 0xFF76FF03, 0xFFFFFF00, 0xFFE91E63, 0xFFB71C1C};
        ArrayList<Integer> colors=new ArrayList<>();
        ArrayList<String> displayMessage=new ArrayList<>();
        ArrayList<Double> value=new ArrayList<>();

        String[] SearchStatus = {"b", "g", "y", "p", "r"};
        for (int i = 0; i < 5; i++) {
            if ((statusNo = databaseAdapter.getTotal(SearchStatus[i])) != 0) {
                colors.add(allcolors[i]);
                allMessage[i]=DisplayMessage[i];
                allvalue[i]=((double) statusNo);
            }
        }
        //multipleCategorySeries.
        multipleCategorySeries.add(allMessage,allvalue);
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            r.setDisplayChartValues(true);
            r.setChartValuesSpacing(5);
            //r.setDisplayChartValuesDistance(5);
            r.setDisplayChartValues(true);
            r.setChartValuesTextSize(15);

            renderer.addSeriesRenderer(r);
        }

        //renderer.setFitLegend(true);
        //renderer.setPanEnabled(false);
        renderer.setZoomEnabled(true);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.WHITE); //set background color
        //renderer.setChartTitle("PIE CHART");
        renderer.setChartTitleTextSize((float) 50);
        renderer.setLabelsColor(Color.DKGRAY);

        renderer.setShowLabels(true);
        renderer.setLabelsTextSize(30);
        renderer.setLegendTextSize(45);
        renderer.setShowGrid(true);
        renderer.setDisplayValues(true);
        return ChartFactory.getDoughnutChartView(context, multipleCategorySeries, renderer);
    }
}
