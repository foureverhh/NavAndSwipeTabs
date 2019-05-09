package com.nackademin.foureverhh.navandswipetabs;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    private SQLiteDatabase myHistoryDatabase;

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
        historyListViewAdapter = new HistoryListViewAdapter(getContext() ,getAllItems());

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
        //To delete item from myDataBase
        //historyListItemList.remove(position);
        //historyListViewAdapter.notifyItemRemoved(position);
        //String selection = HistoryEntry.COLUMN_NAME_KEYWORD + " = ?";
        //String[] selectionArgs = { current_keyword };
        HistoryDBHelper historyDBHelper = new HistoryDBHelper(getActivity());
        myHistoryDatabase = historyDBHelper.getWritableDatabase();
        myHistoryDatabase.delete(HistoryEntry.TABLE_NAME,HistoryEntry._ID +"="+position,null);
        historyListViewAdapter.swapCursor(getAllItems());
        historyListViewAdapter.notifyItemRemoved(position);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Cursor getAllItems(){
        HistoryDBHelper historyDBHelper = new HistoryDBHelper(getActivity());
        myHistoryDatabase = historyDBHelper.getReadableDatabase();
        return myHistoryDatabase.query(HistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HistoryEntry.COLUMN_TIMESTAMP + " DESC");
    }
}
