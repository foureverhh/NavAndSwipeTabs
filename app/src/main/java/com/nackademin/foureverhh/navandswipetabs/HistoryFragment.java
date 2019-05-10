package com.nackademin.foureverhh.navandswipetabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nackademin.foureverhh.navandswipetabs.HistoryContract.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    View rootView;
    private TextView titleText;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryListViewAdapter historyListViewAdapter;

    private SQLiteDatabase historyDatabase;

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


        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(historyListViewAdapter);
        recyclerView.setLayoutManager(layoutManager);


        historyListViewAdapter.setItemClickListener(new HistoryListViewAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position,long databaseId) {
                removeItem(position,databaseId);
                //historyListViewAdapter.swapCursor(getAllItems());
                //historyListViewAdapter = new HistoryListViewAdapter(getContext(), getAllItems());

            }
        });

        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HistoryDBHelper historyDBHelper = new HistoryDBHelper(getActivity());
        historyDatabase = historyDBHelper.getWritableDatabase();
        historyListViewAdapter = new HistoryListViewAdapter(getContext(), getAllItems());
    }

    public void removeItem(int position,long databaseId){

        Log.d("Position is", "removeItem: "+position);


        int deleteResult = historyDatabase.delete(HistoryEntry.TABLE_NAME,HistoryEntry._ID +"="+databaseId,null);
        Toast.makeText(getContext(),"Result "+deleteResult,Toast.LENGTH_SHORT).show();
        historyListViewAdapter.swapCursor(getAllItems());
        historyListViewAdapter.notifyItemRemoved(position);

    }
    private Cursor getAllItems(){
        return historyDatabase.query(HistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HistoryEntry.COLUMN_TIMESTAMP + " DESC");

    }
}
