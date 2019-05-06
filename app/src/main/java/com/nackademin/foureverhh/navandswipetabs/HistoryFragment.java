package com.nackademin.foureverhh.navandswipetabs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    View rootView;
    private TextView titleText;
    private RecyclerView recyclerView;
    private List<HistoryListItem> historyListItemList;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryListViewAdapter historyListViewAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        titleText = rootView.findViewById(R.id.history_textView_id);
        recyclerView = rootView.findViewById(R.id.recyclerView_history_id);
        titleText.setText(getString(R.string.history_title));
        historyListViewAdapter = new HistoryListViewAdapter(getContext() ,historyListItemList);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(historyListViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        historyListViewAdapter.setItemClickListener(new HistoryListViewAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                removeItem(position);
            }
        });

        return rootView;
    }

    public void removeItem(int position){
        historyListItemList.remove(position);
        historyListViewAdapter.notifyItemRemoved(position);
    }


    public void createData(){
        historyListItemList = new ArrayList<>();
        historyListItemList.add(new HistoryListItem("keyword1","date1","Result1"));
        historyListItemList.add(new HistoryListItem("keyword2","date2","Result2"));
        historyListItemList.add(new HistoryListItem("keyword3","date3","Result3"));
        historyListItemList.add(new HistoryListItem("keyword4","date4","Result4"));
        historyListItemList.add(new HistoryListItem("keyword5","date5","Result5"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createData();
    }
}
