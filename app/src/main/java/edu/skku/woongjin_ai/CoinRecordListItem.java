package edu.skku.woongjin_ai;

import android.graphics.drawable.Drawable;

public class CoinRecordListItem {
    private String howmany; //코인 받은 amount
    private String date;  //코인 받은 날짜 (년,월,일,시,분)
    private String why; //코인 받은/잃은 사유 (ex: 지문 읽음, 폭탄 해체함 등)

    public String getHowmany() {
        return howmany;
    }

    public void setHowmany(String howmany1) {
        this.howmany = howmany1;
    }

    public String getDate() { return date; }

    public void setDate(String date1) {
        this.date = date1;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why1) {
        this.why = why1;
    }

}
