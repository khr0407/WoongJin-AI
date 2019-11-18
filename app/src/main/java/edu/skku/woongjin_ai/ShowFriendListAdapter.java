package edu.skku.woongjin_ai;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowFriendListAdapter extends BaseAdapter {

    FirebaseStorage storage;
    private StorageReference storageReference, dataReference;
    private DatabaseReference mPostReference;
    private FirebaseDatabase database;

    SharedPreferences WhoAmI;

    String myid, myschool, mygrade, mynickname, myname, myprofile, whatcase;

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

        WhoAmI=context.getSharedPreferences("myinfo", Context.MODE_PRIVATE);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");

        myid=WhoAmI.getString("myid", "nil");
        mynickname=WhoAmI.getString("mynick", "nil");
        mygrade=WhoAmI.getString("mygrade", "nil");
        myprofile=WhoAmI.getString("myprofile", "nil");
        myname=WhoAmI.getString("myname", "nil");
        myschool=WhoAmI.getString("myschool", "nil");

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_showfriendlist, parent, false);
        }

        ImageView imageViewFace = (ImageView) convertView.findViewById(R.id.friendFace);
        TextView textViewName = (TextView) convertView.findViewById(R.id.friendName);
        TextView textViewGrade = (TextView) convertView.findViewById(R.id.friendGrade);
        TextView textViewSchool = (TextView) convertView.findViewById(R.id.friendSchool);
        Button addFriend = (Button) convertView.findViewById(R.id.addrecommendfriend);



        ShowFriendListItem showFriendListItem = getItem(position);

        if(showFriendListItem.getVisible_and_clickable()){
            addFriend.setClickable(true);
            addFriend.setVisibility(View.VISIBLE);
        }
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
        textViewName.setText(showFriendListItem.getFriendnick());
        textViewGrade.setText(showFriendListItem.getGradeFriend()+"학년");
        textViewSchool.setText(showFriendListItem.getSchoolFriend());

        addFriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //친구추가 파베
                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/grade").setValue(showFriendListItem.getGradeFriend());
                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/name").setValue(showFriendListItem.getNameFriend());
                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/nickname").setValue(showFriendListItem.getFriendnick());
                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/profile").setValue(showFriendListItem.getFaceFriend());
                mPostReference.child(myid+"/my_friend_list/"+showFriendListItem.getFriendid()+"/school").setValue(showFriendListItem.getSchoolFriend());


                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/grade").setValue(mygrade);
                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/name").setValue(myname);
                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/nickname").setValue(mynickname);
                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/profile").setValue(myprofile);
                mPostReference.child(showFriendListItem.getFriendid()+"/my_friend_list/"+myid+"/school").setValue(myschool);
                //토스트 띄우기
                Toast.makeText(context, "친구 추가 되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;

    }

    public void addItem(String faceFriend, String nameFriend, String gradeFriend, String schoolFriend, String friendid, String friendnick, Boolean v_and_c) {
        ShowFriendListItem showFriendListItem = new ShowFriendListItem();

        showFriendListItem.setFaceFriend(faceFriend);
        showFriendListItem.setNameFriend(nameFriend);
        showFriendListItem.setGradeFriend(gradeFriend);
        showFriendListItem.setSchoolFriend(schoolFriend);
        showFriendListItem.setFriendid(friendid);
        showFriendListItem.setFriendnick(friendnick);
        showFriendListItem.setVisible_and_clickable(v_and_c);
        showFriendListItems.add(showFriendListItem);
    }
}
