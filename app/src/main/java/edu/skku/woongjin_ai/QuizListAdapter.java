package edu.skku.woongjin_ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizListAdapter extends BaseAdapter {

    private ArrayList<QuizListItem> QuizListItems = new ArrayList<QuizListItem>();

    @Override
    public int getCount() {
        return QuizListItems.size();
    }

    @Override
    public QuizListItem getItem(int position) {
        return QuizListItems.get(position);
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

        QuizListItem quizListItem = getItem(position);

        textViewTmp.setText(quizListItem.getTmp());

        return convertView;
    }

    public void addItem(String tmp) {
        QuizListItem quizListItem = new QuizListItem();
        quizListItem.setTmp(tmp);
        QuizListItems.add(quizListItem);
    }
}
