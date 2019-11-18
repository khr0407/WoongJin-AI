package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowFriendListAdapter extends BaseAdapter {

    private ArrayList<ShowFriendListItem> showFriendListItems = new ArrayList<ShowFriendListItem>();

    @Override
    public int getCount() {
        return showFriendListItems.size();
    }

    @Override
    public ShowFriendListItem getItem(int position) {
        return showFriendListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_showfriendlist, parent, false);
        }

        ImageView imageViewFace = (ImageView) convertView.findViewById(R.id.friendFace);
        TextView textViewName = (TextView) convertView.findViewById(R.id.friendName);
        TextView textViewGrade = (TextView) convertView.findViewById(R.id.friendGrade);
        TextView textViewSchool = (TextView) convertView.findViewById(R.id.friendSchool);

        ShowFriendListItem showFriendListItem = getItem(position);

        imageViewFace.setImageDrawable(showFriendListItem.getFaceFriend());
        textViewName.setText(showFriendListItem.getNameFriend());
        textViewGrade.setText(showFriendListItem.getGradeFriend());
        textViewSchool.setText(showFriendListItem.getSchoolFriend());

        return convertView;
    }

    public void addItem(Drawable faceFriend, String nameFriend, String gradeFriend, String schoolFriend) {
        ShowFriendListItem showFriendListItem = new ShowFriendListItem();
        showFriendListItem.setFaceFriend(faceFriend);
        showFriendListItem.setNameFriend(nameFriend);
        showFriendListItem.setGradeFriend(gradeFriend);
        showFriendListItem.setSchoolFriend(schoolFriend);
        showFriendListItems.add(showFriendListItem);
    }
}
