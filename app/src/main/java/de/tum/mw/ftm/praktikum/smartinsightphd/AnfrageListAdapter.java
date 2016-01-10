package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;


public class AnfrageListAdapter extends RecyclerView.Adapter<AnfrageListAdapter.ViewHolder> {

    private final List<AnfrageProvider> mValues;

    public AnfrageListAdapter(List<AnfrageProvider> anfrageProvider) {
        mValues = anfrageProvider;
    }

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
        viewHolder.exam.setText(mValues.get(position).exam);
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public AnfrageProvider mItem;

        public TextView startTime;
        public TextView endTime;
        public TextView question;
        public TextView editor;
        public TextView taskNumber;
        public TextView taskSubNumber;
        public TextView sitzNumber;
        public TextView exam;

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
            sitzNumber = (TextView) view.findViewById(R.id.exam);

        }

    }
}
