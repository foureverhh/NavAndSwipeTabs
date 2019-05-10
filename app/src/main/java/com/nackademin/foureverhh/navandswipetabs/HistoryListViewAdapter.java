package com.nackademin.foureverhh.navandswipetabs;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;
import com.nackademin.foureverhh.navandswipetabs.HistoryContract.*;

public class HistoryListViewAdapter extends RecyclerView.Adapter<HistoryListViewAdapter
        .HistoryListViewHolder>  {

    private Context mContext;
    private Dialog itemDetailDialog;
    private OnItemClickListener itemClickListener;
    private Cursor mCursor;

    public interface OnItemClickListener {
        void onRemoveClick(int position, long databaseId);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HistoryListViewAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public HistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_history_list,parent,false);

        itemDetailDialog = new Dialog(mContext);
        itemDetailDialog.setContentView(R.layout.item_history_detail);

        final HistoryListViewHolder myHistoryListViewHolder = new HistoryListViewHolder(view,
                itemClickListener);

        myHistoryListViewHolder.historyItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = myHistoryListViewHolder.getAdapterPosition();
                if(!mCursor.moveToPosition(position))
                    return;
                String keyword = mCursor
                        .getString(mCursor.getColumnIndex(HistoryEntry.COLUMN_NAME_KEYWORD));
                String result = mCursor
                        .getString(mCursor.getColumnIndex(HistoryEntry.COLUMN_NAME_RESULT));
                TextView title_history_dialog = itemDetailDialog.findViewById(R.id.dialog_title);
                TextView result_history_dialog = itemDetailDialog.findViewById(R.id.dialog_result);
                result_history_dialog.setMovementMethod(new ScrollingMovementMethod());
                title_history_dialog.setText(keyword);
                result_history_dialog.setText(result);
                itemDetailDialog.show();
            }
        });

        return  myHistoryListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;
        String keyword = mCursor.getString(mCursor.getColumnIndex(HistoryEntry.COLUMN_NAME_KEYWORD));
        long id = mCursor.getLong(mCursor.getColumnIndex(HistoryEntry._ID));
        holder.keyword_tv.setText(keyword);
        holder.date_tv.setText("Date shows here");
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        //return historyListItemList != null ? historyListItemList.size() :0 ;
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public static class HistoryListViewHolder extends RecyclerView.ViewHolder{

        public TextView keyword_tv;
        public TextView date_tv;
        public ImageView remove_item;
        public CardView historyItemCard;

        public HistoryListViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            keyword_tv = itemView.findViewById(R.id.keyword_textView);
            date_tv = itemView.findViewById(R.id.date_textView);
            remove_item = itemView.findViewById(R.id.image_delete);
            historyItemCard = itemView.findViewById(R.id.item_history_list_cardView);


            remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            long databaseId = (long)itemView.getTag();
                            listener.onRemoveClick(position,databaseId);
                        }
                    }
                }
            });
        }
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;
/*
        if(mCursor != null){
            notifyDataSetChanged();
        }
*/
    }
}
