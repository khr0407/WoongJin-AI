package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFriendQuizListAdapter extends BaseAdapter {

    private ArrayList<MyFriendQuizListItem> myFriendQuizListItems = new ArrayList<MyFriendQuizListItem>();

    @Override
    public int getCount() {
        return myFriendQuizListItems.size();
    }

    @Override
    public MyFriendQuizListItem getItem(int position) {
        return myFriendQuizListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_myfriendquizlist, parent, false);
        }

        ImageView profile = (ImageView) convertView.findViewById(R.id.profile);
        TextView user = (TextView) convertView.findViewById(R.id.user);
        ImageView star2 = (ImageView) convertView.findViewById(R.id.star2);
        ImageView star3 = (ImageView) convertView.findViewById(R.id.star3);
        ImageView star4 = (ImageView) convertView.findViewById(R.id.star4);
        ImageView star5 = (ImageView) convertView.findViewById(R.id.star5);
        TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
        TextView scriptName = (TextView) convertView.findViewById(R.id.scriptName);
        TextView question = (TextView) convertView.findViewById(R.id.question);

        MyFriendQuizListItem myFriendQuizListItem = getItem(position);

        profile.setImageDrawable(myFriendQuizListItem.getProfile());
        user.setText(myFriendQuizListItem.getUser());
        star2.setImageDrawable(myFriendQuizListItem.getStar2());
        star3.setImageDrawable(myFriendQuizListItem.getStar3());
        star4.setImageDrawable(myFriendQuizListItem.getStar4());
        star5.setImageDrawable(myFriendQuizListItem.getStar5());
        bookName.setText(myFriendQuizListItem.getBookName());
        scriptName.setText(myFriendQuizListItem.getScriptName());
        question.setText(myFriendQuizListItem.getQuestion());

        return convertView;
    }

    public void addItem(Drawable profile, String user, Drawable star2, Drawable star3, Drawable star4, Drawable star5, String bookName, String scriptName, String question) {
        MyFriendQuizListItem myFriendQuizListItem = new MyFriendQuizListItem();
        myFriendQuizListItem.setProfile(profile);
        myFriendQuizListItem.setUser(user);
        myFriendQuizListItem.setStar2(star2);
        myFriendQuizListItem.setStar3(star3);
        myFriendQuizListItem.setStar4(star4);
        myFriendQuizListItem.setStar5(star5);
        myFriendQuizListItem.setBookName(bookName);
        myFriendQuizListItem.setScriptName(scriptName);
        myFriendQuizListItem.setQuestion(question);
        myFriendQuizListItems.add(myFriendQuizListItem);
    }
}
