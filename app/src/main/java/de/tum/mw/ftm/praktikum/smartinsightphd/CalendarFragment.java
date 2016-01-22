package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private CalendarListAdapter adapter;
    private TextView txtIntroduction;
    private RecyclerView list_calendar;
    private ArrayList<Calendar> listCalendar = new ArrayList<Calendar>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CalendarFragment() {
    }

    public static CalendarFragment newInstance(int columnCount) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCalendar.clear();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            listCalendar = (ArrayList<Calendar>)getArguments().get("calendar");
        }


        adapter = new CalendarListAdapter(providerCalender);

    }

    RecyclerView recyclerView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Set the adapter
        Context context = view.getContext();
        txtIntroduction = (TextView) view.findViewById(R.id.txtInfo);
        list_calendar = (RecyclerView) view.findViewById(R.id.list);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // Create the adapter to convert the array to views
        recyclerView.setAdapter(adapter);
        updateFragmentListView(listCalendar);

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

    static ArrayList<Calendar> providerCalender = new ArrayList<Calendar>();

    public void updateFragmentListView(ArrayList<Calendar> listItems) {
        providerCalender.clear();
        if (listItems.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            list_calendar.setVisibility(View.GONE);
        } else {
            txtIntroduction.setVisibility(View.GONE);
            list_calendar.setVisibility(View.VISIBLE);
            providerCalender.addAll(listItems);
        }
        if (recyclerView != null) {
            // Create the adapter to convert the array to views
            recyclerView.setAdapter(adapter);
        }
    }


}