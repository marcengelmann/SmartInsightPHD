package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
* Diese KLasse erzeugt einen Adapter für den Spinner in der Floating Action Screen
* um die anfrage aufzulisten
* */
public class SpinnerRequestListAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    AnfrageProvider tempValues=null;
    LayoutInflater inflater;

    public SpinnerRequestListAdapter(FloatingActivity activitySpinner, int textViewResourceId, ArrayList objects)
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.fragment_list_item, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (AnfrageProvider) data.get(position);
        row.findViewById(R.id.listViewButton).setVisibility(View.GONE);
        // hol die layout elemente
        TextView editor = (TextView) row.findViewById(R.id.editor);
        TextView endTime = (TextView) row.findViewById(R.id.endTime);
        TextView question = (TextView) row.findViewById(R.id.question);
        TextView startTime = (TextView) row.findViewById(R.id.startTime);
        TextView taskNumber = (TextView) row.findViewById(R.id.taskNumber);
        TextView taskSubNumber = (TextView) row.findViewById(R.id.taskSubNumber);
        TextView sitzNumber = (TextView) row.findViewById(R.id.sitzNumb);
        TextView exam = (TextView) row.findViewById(R.id.exam);
        // setz die akutellen werte für die layout elemente
        editor.setText(tempValues.getStudent());
        endTime.setText(tempValues.getEndTime());
        startTime.setText(tempValues.getStartTime());
        question.setText(tempValues.getQuestion());
        taskNumber.setText(tempValues.getTaskNumber());
        taskSubNumber.setText(tempValues.getTaskSubNumber());
        sitzNumber.setText(tempValues.getSitzNumber());
        exam.setText(tempValues.getExam());

        return row;
    }
}
