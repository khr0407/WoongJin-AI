package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizListAdapter extends BaseAdapter {

    private ArrayList<MyFriendQuizListItem> QuizListItems = new ArrayList<MyFriendQuizListItem>();

    @Override
    public int getCount() {
        return QuizListItems.size();
    }

    @Override
    public MyFriendQuizListItem getItem(int position) {
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

        TextView textViewQuiz = (TextView) convertView.findViewById(R.id.MyQuizContent);
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.ScriptTitle);
        ImageButton imageButtonIconLike = (ImageButton) convertView.findViewById(R.id.iconLikeMyFriendQuizList);
        TextView textViewLike = (TextView) convertView.findViewById(R.id.like);

        MyFriendQuizListItem QuizListItems = getItem(position);

        textViewQuiz.setText(QuizListItems.getQuiz());
        textViewTitle.setText(QuizListItems.getUid());
        imageButtonIconLike.setImageDrawable(QuizListItems.getIconLikr());
        textViewLike.setText(QuizListItems.getLike());

        return convertView;
    }


    public void addItem(String quiz, String uid, Drawable iconLike, String like) {
        MyFriendQuizListItem quizListItems = new MyFriendQuizListItem();
        quizListItems.setQuiz(quiz);
        quizListItems.setUid(uid);
        quizListItems.setIconLikr(iconLike);
        quizListItems.setLike(like);
        QuizListItems.add(quizListItems);
    }
}
