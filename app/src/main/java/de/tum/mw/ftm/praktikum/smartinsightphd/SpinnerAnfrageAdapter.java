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


public class SpinnerAnfrageAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    AnfrageProvider tempValues=null;
    LayoutInflater inflater;

    public SpinnerAnfrageAdapter(FloatingActivity activitySpinner, int textViewResourceId, ArrayList objects)
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
        View row = inflater.inflate(R.layout.spinner_list_item, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (AnfrageProvider) data.get(position);

        TextView editor = (TextView) row.findViewById(R.id.editor);
        TextView endTime = (TextView) row.findViewById(R.id.endTime);
        TextView question = (TextView) row.findViewById(R.id.question);
        TextView startTime = (TextView) row.findViewById(R.id.startTime);
        TextView taskNumber = (TextView) row.findViewById(R.id.taskNumber);
        TextView taskSubNumber = (TextView) row.findViewById(R.id.taskSubNumber);
        TextView sitzNumber = (TextView) row.findViewById(R.id.sitzNumb);

        editor.setText(tempValues.editor);
        endTime.setText(tempValues.endTime);
        startTime.setText(tempValues.startTime);
        question.setText(tempValues.question);
        taskNumber.setText(tempValues.taskNumber);
        taskSubNumber.setText(tempValues.taskSubNumber);
        sitzNumber.setText(tempValues.sitzNumber);

        return row;
    }
}
/*
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position) {
        viewHolder.mItem = mValues.get(position);
        viewHolder.editor.setText(mValues.get(position).editor);
        viewHolder.endTime.setText(mValues.get(position).endTime);
        viewHolder.startTime.setText(mValues.get(position).startTime);
        viewHolder.question.setText(mValues.get(position).question);
        viewHolder.taskNumber.setText(mValues.get(position).taskNumber);
        viewHolder.taskSubNumber.setText(mValues.get(position).taskSubNumber);
        viewHolder.sitzNumber.setText(mValues.get(position).sitzNumber);

    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolderextends ArrayAdapter<String>.ViewH  {
        public final View mView;
        public AnfrageProvider mItem;

        public TextView startTime;
        public TextView endTime;
        public TextView question;
        public TextView editor;
        public TextView taskNumber;
        public TextView taskSubNumber;
        public TextView sitzNumber;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            editor = (TextView) view.findViewById(R.id.editor);
            endTime = (TextView) view.findViewById(R.id.endTime);
            question = (TextView) view.findViewById(R.id.question);
            startTime = (TextView) view.findViewById(R.id.startTime);
            taskNumber = (TextView) view.findViewById(R.id.taskNumber);
            taskSubNumber = (TextView) view.findViewById(R.id.taskSubNumber);
            sitzNumber = (TextView) view.findViewById(R.id.sitzNumb);
        }

    }*/

