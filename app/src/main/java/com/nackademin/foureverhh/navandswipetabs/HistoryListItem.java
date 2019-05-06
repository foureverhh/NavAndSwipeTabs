package com.nackademin.foureverhh.navandswipetabs;

public class HistoryListItem {

    private String keyword;
    private String date;
    private String result;

    public HistoryListItem(String keyword, String date, String result) {
        this.keyword = keyword;
        this.date = date;
        this.result = result;

    }

    public String getKeyword() {
        return keyword;
    }

    public String getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }
}
