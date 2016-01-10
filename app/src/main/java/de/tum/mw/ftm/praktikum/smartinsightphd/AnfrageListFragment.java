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
import android.widget.TextView;

import java.util.ArrayList;


public class AnfrageListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    AnfrageListAdapter adapter;
    TextView txtIntroduction;
    ArrayList<AnfrageProvider> listAnfrageProvider = new ArrayList<AnfrageProvider>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnfrageListFragment() {
    }

    public static AnfrageListFragment newInstance(int columnCount) {
        AnfrageListFragment fragment = new AnfrageListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAnfrageProvider.clear();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            listAnfrageProvider = (ArrayList<AnfrageProvider>)getArguments().get("requests");

        }

        adapter = new AnfrageListAdapter(anfrageProviders);

    }
    RecyclerView recyclerView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anfrage_list, container, false);

        // Set the adapter
            Context context = view.getContext();
            txtIntroduction = (TextView) view.findViewById(R.id.txtInfo);
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Create the adapter to convert the array to views
            recyclerView.setAdapter(adapter);

        updateFragmentListView(listAnfrageProvider);
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
    static  ArrayList<AnfrageProvider> anfrageProviders =  new ArrayList<AnfrageProvider>();

    public void updateFragmentListView(ArrayList<AnfrageProvider> requests) {
        anfrageProviders.clear();
        if (requests.isEmpty()) {
            txtIntroduction.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            txtIntroduction.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            for (AnfrageProvider request : requests) {
                anfrageProviders.add(new AnfrageProvider(request.id, request.startTime, request.endTime, request.taskNumber, request.taskSubNumber, request.question, request.editor, request.sitzNumber, request.exam));
            }
        }
        if (recyclerView != null){
            // Create the adapter to convert the array to views
            recyclerView.setAdapter(adapter);
        }


    }

}
