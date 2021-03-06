package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class StatisticFragment extends Fragment implements OnChartValueSelectedListener {


    private Spinner spinSemester;
    // Ab hier sin dalles Dummy daten, die in die Statistiken geladen werden
    protected String[] mExams = new String[] {
            "BMW", "MT", "NFT", "TuB"
    };

    protected String[] mLegend = new String[] {
            "Inhalt", "Punke", "Allgemein"
    };
    protected String[] mExamsLongDesc = new String[] {
            "Baumaschinen (BMW)", "Motorradtechnik (MT)", "Nutzfahrzeugtechnik (NFT)", "Traktoren und Erdbaumaschinen (TuB)"
    };
    protected String[] mSemster = new String[] {
            "SS 2015", "WS 2015/16","SS 2014", "WS 2014/15"
    };

    protected String[] mTaksLongDesc = new String[] {
            "Aufgabe 1", "Aufgabe 2", "Aufgabe 3", "Aufgabe 4", "Aufgabe 5", "Aufgabe 6", "Aufgabe 7", "Aufgabe 8", "Aufgabe 9", "Aufgabe 10"
    };
    protected String[] mTaks = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };
    protected String[] mSubTaks = new String[] {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j"
    };

    public StatisticFragment() {
        // Required empty public constructor
    }

    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private BarChartExam barChartExam = null;
    // Klasse die eine bestimmte Klausur als BAlkendiagramm löd von Aufgabe 1 bis x
    public class BarChartExam implements OnChartValueSelectedListener{
        protected BarChart mChartExam;
        private Spinner spinExams;

        public BarChartExam(View view){
            mChartExam = (BarChart) view.findViewById(R.id.barChartExam);
            spinExams = (Spinner)view.findViewById(R.id.settingsExams);
            ArrayAdapter<String> adapterExams = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mExamsLongDesc);
            adapterExams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinExams.setAdapter(adapterExams);
            spinExams.setSelection(0);
            spinExams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // wenn eine Klausur vom Spinner ausgewählt ist, werden
                    // die Daten des aktuellen Kklausur geladen
                    setDataExam(position);
                    // Die aktuelle Daten der Unterklausuraufgaben werden geladen
                    // und die spinner position der Aufgabe wird auf 0 gesetzt, d.h. Aufgabe 1
                    barChartTask.setDataTask(0);
                    // die Ausgewählte Kklausurbalken wird in dem Überlbick der Anzahl der Angemeldeten
                    // Studenten zur Kklausur markiert
                    barChartOverviewExam.setHihlightValue(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }
        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            // Wenn eien Aufgabe ausgewählt wurde über die Balken, wird das Diagramm die Daten von der
            // ausgwählten aufgabe neu berechnet und der Spinner auf die rivhtige position gesetzt
            int position = h.getXIndex();
            barChartTask.setDataTask(position);
        }

        @Override
        public void onNothingSelected() {

        }
        private  void setHihlightValue(int setHihlightValue){
            mChartExam.highlightValue(setHihlightValue, 0);

        }

        private void setupBarChartExam(){

            mChartExam.setDrawBarShadow(false);

            mChartExam.setDescription("");
            mChartExam.setOnChartValueSelectedListener(this);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChartExam.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            mChartExam.setPinchZoom(false);

            mChartExam.setDrawGridBackground(false);

            XAxis xAxis = mChartExam.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(2);


            YAxis leftAxis = mChartExam.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setDrawGridLines(false);

            mChartExam.getAxisRight().setEnabled(false);

            Legend l = mChartExam.getLegend();
            l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            setDataExam(0);
        }

        private void setDataExam(int spinPosition){
            spinExams.setSelection(spinPosition);
            mChartExam.animateY(2500);

            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < mTaks.length; i++) {
                xVals.add(mTaks[i]);
            }
            float mult = 10;
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

            for (int i = 0; i < mTaks.length; i++) {
                float val = (int) (Math.random() * mult + 10);
                yVals1.add(new BarEntry(val, i));
            }

            for (int i = 0; i < mTaks.length; i++) {
                float val = (int) (Math.random() * mult + 10);
                yVals2.add(new BarEntry(val, i));
            }

            for (int i = 0; i < mTaks.length; i++) {
                float val = (int) (Math.random() * mult + 10);
                yVals3.add(new BarEntry(val, i));
            }


            // create 3 datasets with different types
            BarDataSet set1 = new BarDataSet(yVals1, mLegend[0]);
            set1.setColor(Color.rgb(104, 241, 175));
            BarDataSet set2 = new BarDataSet(yVals2, mLegend[1]);
            set2.setColor(Color.rgb(164, 228, 251));
            BarDataSet set3 = new BarDataSet(yVals3, mLegend[2]);
            set3.setColor(Color.rgb(242, 247, 158));

            set1.setDrawValues(false);
            set2.setDrawValues(false);
            set3.setDrawValues(false);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(xVals, dataSets);

            // add space between the dataset groups in percent of bar-width
            data.setGroupSpace(80f);

            mChartExam.setData(data);
            mChartExam.invalidate();
        }
    }

    private BarChartTask barChartTask = null;
    // Balkendiagramm für die eine Aufgabe der Klasuur, umd ie Verteilung der Unteraufgaben
    // besser darzustellen
    public class BarChartTask{
        protected BarChart mChartTask;
        private Spinner spinTasks;

        public BarChartTask(View view){
            mChartTask = (BarChart) view.findViewById(R.id.barChartTask);
            spinTasks = (Spinner)view.findViewById(R.id.settingsTasks);
            ArrayAdapter<String> adapterTasks = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mTaksLongDesc);
            adapterTasks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinTasks.setAdapter(adapterTasks);
            spinTasks.setSelection(0);
            spinTasks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // aktualisieren der Data in abhängigkeit der gewählten aufgabe
                    setDataTask(position);
                    // markiere ide entsprechende Aufgabe in der Übersicht der ausgewählten Klausur
                    barChartExam.setHihlightValue(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }


        private void setupBarChartTask(){
            mChartTask.setDrawBarShadow(false);

            mChartTask.setDescription("");
            mChartTask.setHighlightPerDragEnabled(false);
            mChartTask.setHighlightPerTapEnabled(false);
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChartTask.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            mChartTask.setPinchZoom(false);

            mChartTask.setDrawGridBackground(false);

            XAxis xAxis = mChartTask.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(2);


            YAxis leftAxis = mChartTask.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setDrawGridLines(false);

            mChartTask.getAxisRight().setEnabled(false);

            Legend l = mChartTask.getLegend();
            l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            setDataTask(0);
        }
        private void setDataTask(int spinPosition){
            spinTasks.setSelection(spinPosition);
            mChartTask.animateY(2500);

            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < mSubTaks.length; i++) {
                xVals.add(mSubTaks[i]);
            }
            float mult = 10;
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
            ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

            for (int i = 0; i < mSubTaks.length; i++) {
                float val = (int) (Math.random() * mult);
                yVals1.add(new BarEntry(val, i));
            }

            for (int i = 0; i < mSubTaks.length; i++) {
                float val = (int) (Math.random() * mult);
                yVals2.add(new BarEntry(val, i));
            }

            for (int i = 0; i < mSubTaks.length; i++) {
                float val = (int) (Math.random() * mult);
                yVals3.add(new BarEntry(val, i));
            }


            // create 3 datasets with different types
            BarDataSet set1 = new BarDataSet(yVals1, mLegend[0]);

            set1.setColor(Color.rgb(104, 241, 175));
            BarDataSet set2 = new BarDataSet(yVals2, mLegend[1]);
            set2.setColor(Color.rgb(164, 228, 251));
            BarDataSet set3 = new BarDataSet(yVals3, mLegend[2]);
            set3.setColor(Color.rgb(242, 247, 158));

            set1.setDrawValues(false);
            set2.setDrawValues(false);
            set3.setDrawValues(false);

            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(xVals, dataSets);

            // add space between the dataset groups in percent of bar-width
            data.setGroupSpace(80f);

            mChartTask.setData(data);
            mChartTask.invalidate();
        }
    }
    BarChartOverviewExam barChartOverviewExam = null;
    public class BarChartOverviewExam implements OnChartValueSelectedListener {
        protected BarChart mChartOverviewExam;

        public BarChartOverviewExam(View view) {
            mChartOverviewExam = (BarChart) view.findViewById(R.id.barChartOverviewExam);
        }

        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
            int position = h.getXIndex();
            // Eine Klausur würde über das Balkendiegramm ausgewählt,
            // dementsprechend werden die Klausur Daten mit der Aufgab1 bis x geladen
            barChartExam.setDataExam(position);
            // Und die Unteraufgaben werden auch dementsprechend geladen, aber auf position 0 zurück gesettz
            barChartTask.setDataTask(0);
        }

        @Override
        public void onNothingSelected() {

        }


        private  void setHihlightValue(int setHihlightValue){
            mChartOverviewExam.highlightValue(setHihlightValue, 0);

        }
        private void setupBarChartOverviewExam(){
            mChartOverviewExam.setDrawBarShadow(false);

            mChartOverviewExam.setDescription("");
            mChartOverviewExam.setOnChartValueSelectedListener(this);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            mChartOverviewExam.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            mChartOverviewExam.setPinchZoom(false);

            mChartOverviewExam.setDrawGridBackground(false);

            XAxis xAxis = mChartOverviewExam.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(2);


            YAxis leftAxis = mChartOverviewExam.getAxisLeft();
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setDrawGridLines(false);

            mChartOverviewExam.getAxisRight().setEnabled(false);

            mChartOverviewExam.getLegend().setEnabled(false);

            setDataOverviewExam();
        }

        private void setDataOverviewExam() {
            mChartOverviewExam.animateY(2500);

            ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < mExams.length; i++) {
                xVals.add(mExams[i]);
            }
            float mult = 10;
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < mExams.length; i++) {
                float val = (int) (Math.random() * mult + 30);
                yVals1.add(new BarEntry(val, i));
            }



            // create 1 datasets with different types
            BarDataSet set1 = new BarDataSet(yVals1, "Klausuren");

            set1.setColor(Color.rgb(204, 40, 40));
            set1.setDrawValues(false);


            ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
            dataSets.add(set1);


            BarData data = new BarData(xVals, dataSets);

            // add space between the dataset groups in percent of bar-width
            data.setGroupSpace(80f);

            mChartOverviewExam.setData(data);
            mChartOverviewExam.invalidate();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        barChartExam = new BarChartExam(view);
        barChartTask = new BarChartTask(view);
        barChartOverviewExam = new BarChartOverviewExam(view);
        barChartExam.setupBarChartExam();
        barChartTask.setupBarChartTask();
        barChartOverviewExam.setupBarChartOverviewExam();


        spinSemester = (Spinner)view.findViewById(R.id.settingsSemester);
        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mSemster);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSemester.setAdapter(adapterSemester);
        spinSemester.setSelection(0);
        spinSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Spinner, der das Semeste rauswählt,
                // dementsprechend, werden alle Daten gesetzt und die Position auf 0 zurück gesetzt
                barChartExam.setDataExam(0);
                barChartTask.setDataTask(0);
                barChartOverviewExam.setDataOverviewExam();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }


}
