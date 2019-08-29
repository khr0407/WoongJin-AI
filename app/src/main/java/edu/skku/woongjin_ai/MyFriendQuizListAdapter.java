package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyFriendQuizListAdapter extends BaseAdapter implements FriendOXQuizFragment.OnFragmentInteractionListener{

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

        TextView textViewQuiz = (TextView) convertView.findViewById(R.id.quizMyFriendQuizList);
        TextView textViewUid = (TextView) convertView.findViewById(R.id.uidMyFriendQuizList);
        ImageButton imageButtonIconLike = (ImageButton) convertView.findViewById(R.id.iconLikeMyFriendQuizList);
        TextView textViewLike = (TextView) convertView.findViewById(R.id.likeMyFriendQuizList);

        MyFriendQuizListItem myFriendQuizListItem = getItem(position);

        textViewQuiz.setText(myFriendQuizListItem.getQuiz());
        textViewUid.setText(myFriendQuizListItem.getUid());
        imageButtonIconLike.setImageDrawable(myFriendQuizListItem.getIconLikr());
        textViewLike.setText(myFriendQuizListItem.getLike());

        return convertView;
    }

    public void addItem(String quiz, String uid, Drawable iconLike, String like) {
        MyFriendQuizListItem myFriendQuizListItem = new MyFriendQuizListItem();
        myFriendQuizListItem.setQuiz(quiz);
        myFriendQuizListItem.setUid(uid);
        myFriendQuizListItem.setIconLikr(iconLike);
        myFriendQuizListItem.setLike(like);
        myFriendQuizListItems.add(myFriendQuizListItem);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
