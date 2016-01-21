package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AnfrageListAdapter extends RecyclerView.Adapter<AnfrageListAdapter.ViewHolder> {

    private final ArrayList<AnfrageProvider> mValues = new ArrayList<AnfrageProvider>();
    customButtonListener customListner;
    private Boolean refreshActive = false; //


    public void setRefreshActive(Boolean refreshActive) {
        this.refreshActive = refreshActive;
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position,AnfrageProvider value);
        public void refreshListListener(int position, long timer);
    }

    public AnfrageListAdapter(List<AnfrageProvider> anfrageProvider , customButtonListener listener) {
        mValues.addAll(anfrageProvider);
        customListner = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void updateData(ArrayList<AnfrageProvider> anfrageProvider){
        mValues.clear();
        mValues.addAll(anfrageProvider);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position) {
        viewHolder.mItem = mValues.get(position);
        String endTime = mValues.get(position).getEndTime();
        String startTime = mValues.get(position).getStartTime();
        viewHolder.editor.setText(mValues.get(position).getStudent());
        Boolean requestFinished = Boolean.valueOf(mValues.get(position).getRequestFinished());
        viewHolder.endTime.setText(endTime);
        viewHolder.startTime.setText(startTime);
        viewHolder.question.setText(mValues.get(position).getQuestion());
        viewHolder.taskNumber.setText(mValues.get(position).getTaskNumber());
        viewHolder.taskSubNumber.setText(mValues.get(position).getTaskSubNumber());
        viewHolder.sitzNumber.setText(mValues.get(position).getSitzNumber());
        viewHolder.exam.setText(mValues.get(position).getExam());

        long requestStartDate = 0;
        long requestEndDate = 0;
        Time time = new Time("Europe/Berlin");
        time.setToNow();
        long actualDate = (time.hour * 60 + time.minute) * 60 + time.second;
        SimpleDateFormat curFormater = new SimpleDateFormat("HH:mm");

        try {
            Date startDate = curFormater.parse(startTime);
            requestStartDate = (startDate.getHours() * 60 + startDate.getMinutes()) * 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date endDate = curFormater.parse(endTime);
            requestEndDate = (endDate.getHours() * 60 + endDate.getMinutes()) * 60;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(requestEndDate != 0 && requestStartDate != 0) {

            if(actualDate < requestEndDate && !refreshActive)
            {
                refreshActive = true;
                long calcTime = 0 ;
                if(actualDate >= requestStartDate){
                    calcTime = (requestEndDate - actualDate + 10) * 1000;
                }
                else{
                    calcTime = (requestStartDate - actualDate + 10) * 1000;
                }

                if (customListner != null) {
                    customListner.refreshListListener(position, calcTime);
                }
            }

            if(actualDate >= requestEndDate){
                if(requestFinished){
                    viewHolder.card.setCardBackgroundColor(Color.LTGRAY);
                }
                else {
                    viewHolder.card.setCardBackgroundColor(Color.rgb(255, 112, 112));
                }
            }
            else
            {
                viewHolder.card.setCardBackgroundColor(Color.WHITE);

            }

            if(requestFinished || actualDate < requestStartDate){
                viewHolder.listViewButton.setVisibility(View.GONE);
            }
            else // Erledigt icon nur anzeigen, wenn die startzeit in der Vergangenheit liegt. Man soll nicht in der Zukunft etwas ändern können
            {
                viewHolder.listViewButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (customListner != null) {
                            customListner.onButtonClickListner(position, viewHolder.mItem);
                        }

                    }
                });

            }


        }
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
        private CardView card;
        public ImageButton listViewButton;

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
            exam = (TextView) view.findViewById(R.id.exam);
            card = (CardView) view.findViewById(R.id.card_view_request_list);
            listViewButton = (ImageButton) view.findViewById(R.id.listViewButton);

        }

    }
}
