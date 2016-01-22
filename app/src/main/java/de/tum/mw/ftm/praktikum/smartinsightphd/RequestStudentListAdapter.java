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

/**
 * Created by Rebecca on 22.01.2016.
 */
public class RequestStudentListAdapter extends RecyclerView.Adapter<RequestStudentListAdapter.ViewHolder> {

    private final ArrayList<RequestsStudent> mValues = new ArrayList<RequestsStudent>();
    private customButtonListener customListner;
    private Boolean refreshActive = false; //


    public void setRefreshActive(Boolean refreshActive) {
        this.refreshActive = refreshActive;
    }

    public interface customButtonListener {
        // Daten übermittleung wenn der PhD eine Anfrage bestätigt hat
        public void onButtonClickListner(int position,RequestsStudent value);
        // startet ein Postdeay mti dem delay von timer in ms
        public void refreshListListener(int position, long timer);
    }

    public RequestStudentListAdapter(List<RequestsStudent> anfrageProvider , customButtonListener listener) {
        mValues.addAll(anfrageProvider);
        customListner = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }
    // Methode zum update der aktuellen listen werte
    public void updateData(ArrayList<RequestsStudent> anfrageProvider){
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
        // ermitteln der aktuellen Uhrzeit
        Time time = new Time("Europe/Berlin");
        time.setToNow();
        long actualDate = (time.hour * 60 + time.minute) * 60 + time.second;
        SimpleDateFormat curFormater = new SimpleDateFormat("HH:mm");
        // Umwandeln von start time in s
        try {
            Date startDate = curFormater.parse(startTime);
            requestStartDate = (startDate.getHours() * 60 + startDate.getMinutes()) * 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // umwandln von Endzeti in s
        try {
            Date endDate = curFormater.parse(endTime);
            requestEndDate = (endDate.getHours() * 60 + endDate.getMinutes()) * 60;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // nur wenn die Umwandlung von der End- und Startzeit funktioniert hat, soll diese
        // Anfrage gestartet werden
        if(requestEndDate != 0 && requestStartDate != 0) {
            /*
            * Hier wird überprüft ob die aktuelle Uhrzeit kleiner ist und ob ein aktualisierung der
            * Anfrageliste gerade stattfinden soll.
            * Dieser If-Zweig ermittelt die Zeit die benötigt wird bis die nächste Anfrage bearbeiet
            * werden soll
            * */
            if(actualDate < requestEndDate && !refreshActive)
            {
                refreshActive = true;
                long calcTime = 0 ;
                // wenn die aktuelle Zeit noch größer als die Startzeit ist, muss die timer Zeit
                // so ermittelt werden, von der Endzeit, sonst von der Startzeit
                if(actualDate >= requestStartDate){
                    calcTime = (requestEndDate - actualDate + 10) * 1000;
                }
                else{
                    calcTime = (requestStartDate - actualDate + 10) * 1000;
                }
                // starten des times
                if (customListner != null) {
                    customListner.refreshListListener(position, calcTime);
                }
            }
            // bestimmen der HIntergrundfarbe der Anfragen
            if(actualDate >= requestEndDate){
                // grau, wenn die Anfrage bearbeitet ist und in er vergangenheit liegt,
                // sonst rot
                if(requestFinished){
                    viewHolder.card.setCardBackgroundColor(Color.LTGRAY);
                }
                else {
                    viewHolder.card.setCardBackgroundColor(Color.rgb(255, 112, 112));
                }
            }
            else
            {   // wenn die Anrage in der Zukunft liegt, ist der HIntergrund immer weiß
                viewHolder.card.setCardBackgroundColor(Color.WHITE);

            }

            //  wenn die aktuelle Anfrage bearbeitet wurde und die Anfrage in der Vergangenheit liegt
            // von der Startzeit aus gesehen, wird der Check Button in der Anfragelister nicht mehr
            // Angezeigt, sonst schon
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
        public RequestsStudent mItem;

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
