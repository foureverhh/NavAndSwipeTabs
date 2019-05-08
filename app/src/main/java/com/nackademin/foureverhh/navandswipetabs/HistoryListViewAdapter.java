package com.nackademin.foureverhh.navandswipetabs;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class HistoryListViewAdapter extends RecyclerView.Adapter<HistoryListViewAdapter
        .HistoryListViewHolder>  {

    private Context mContext;
    private List<HistoryListItem> historyListItemList;
    private Dialog itemDetailDialog;
    private OnItemClickListener itemClickListener;
    private Cursor mCursor;

    public interface OnItemClickListener {
        void onRemoveClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //!!!!!!!!!!!!Going to add mCursor here!!!!!!!!!!
    public HistoryListViewAdapter(Context mContext, List<HistoryListItem> historyListItemList) {
        this.mContext = mContext;
        this.historyListItemList = historyListItemList;
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
                TextView title_history_dialog = itemDetailDialog.findViewById(R.id.dialog_title);
                TextView result_history_dialog = itemDetailDialog.findViewById(R.id.dialog_result);
                title_history_dialog.setText(historyListItemList.get(position).getKeyword());
                result_history_dialog.setText(historyListItemList.get(position).getResult());
                itemDetailDialog.show();
            }
        });


        return  myHistoryListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder holder, int position) {
        HistoryListItem currentHistoryListItem = historyListItemList.get(position);
        holder.keyword_tv.setText(currentHistoryListItem.getKeyword());
        holder.date_tv.setText(currentHistoryListItem.getDate());

    }

    @Override
    public int getItemCount() {
        return historyListItemList != null ? historyListItemList.size() :0 ;
    }

    public static class HistoryListViewHolder extends RecyclerView.ViewHolder{

        public TextView keyword_tv;
        public TextView date_tv;
        public ImageView remove_item;
        public CardView historyItemCard;

        public HistoryListViewHolder(View itemView, final OnItemClickListener listener) {
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
                            listener.onRemoveClick(position);
                        }

                    }
                }
            });
        }
    }
}
