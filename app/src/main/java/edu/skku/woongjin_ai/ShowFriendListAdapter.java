package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowFriendListAdapter extends BaseAdapter {

    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;

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

        if (!showFriendListItem.getFaceFriend().equals("noimage")) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getInstance().getReference();
            dataReference = storageReference.child("/profile/" + showFriendListItem.getFaceFriend());
            dataReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .error(R.drawable.btn_x)
                            .into(imageViewFace);
                }
            });
        }
        textViewName.setText(showFriendListItem.getNameFriend());
        textViewGrade.setText(showFriendListItem.getGradeFriend()+"학년");
        textViewSchool.setText(showFriendListItem.getSchoolFriend());

        return convertView;
    }

    public void addItem(String faceFriend, String nameFriend, String gradeFriend, String schoolFriend) {
        ShowFriendListItem showFriendListItem = new ShowFriendListItem();

        showFriendListItem.setFaceFriend(faceFriend);
        showFriendListItem.setNameFriend(nameFriend);
        showFriendListItem.setGradeFriend(gradeFriend);
        showFriendListItem.setSchoolFriend(schoolFriend);
        showFriendListItems.add(showFriendListItem);
    }
}
