package edu.skku.woongjin_ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyQuizListAdapter extends BaseAdapter {

    private ArrayList<MyQuizListItem> myQuizListItems = new ArrayList<MyQuizListItem>();

    @Override
    public int getCount() {
        return myQuizListItems.size();
    }

    @Override
    public MyQuizListItem getItem(int position) {
        return myQuizListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_myquizlist, parent, false);
        }

        TextView textViewTmp = (TextView) convertView.findViewById(R.id.tmpMyQuiz);

        MyQuizListItem myQuizListItem = getItem(position);

        textViewTmp.setText(myQuizListItem.getTmp());

        return convertView;
    }

    public void addItem(String tmp) {
        MyQuizListItem myQuizListItem = new MyQuizListItem();
        myQuizListItem.setTmp(tmp);
        myQuizListItems.add(myQuizListItem);
    }
}
