package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.security.MessageDigest;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Map;

public class ChatListActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    QuizBombDialog Bomb;

    String id_key, friend_key, name_key;

    ListView chatListView;
    EditText name;
    TextView friend;
    Button search, create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        chatListView = findViewById(R.id.listView);
        name = findViewById(R.id.roomname);
        friend = findViewById(R.id.friend);
        search = findViewById(R.id.search);
        create = findViewById(R.id.create);

        data = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_list");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent_showfriend = new Intent(ChatListActivity.this, ShowFriendActivity.class);
                Intent intent = getIntent();
                id_key = intent.getExtras().getString("id");
                intent_showfriend.putExtra("id", id_key);
                startActivity(intent_showfriend);
            }
        });

        Intent intent1 = getIntent();
        id_key = intent1.getExtras().getString("id");
        //id_key="DY";
        friend_key = intent1.getStringExtra("chatfriend");
        friend.setText(friend_key);

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name_key = name.getText().toString();
                //mPostReference.addListenerForSingleValueEvent(checkRoomRegister);
                if (spaceCheck(name_key) == false && name_key.length() > 0) { //create chat room
                    postFirebaseDatabase(true);
                }
                else if (spaceCheck(name_key) == true || name_key.length() == 0) {
                    name.setText("");
                }
            }
        });
        chatListView.setAdapter(arrayAdapter);
        getFirebaseDatabase();
        //채팅방 쭉 생성쓰



        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                //이걸 클릭했을때 팝업창이 떠야함.
                //Intent intent=new Intent(getApplicationContext(), ChatroomActivity.class);

                name_key = chatListView.getItemAtPosition(position).toString();

                String[] arr=name_key.split(" with ");

                //intent.putExtra("room_name", arr[0]);
                //intent.putExtra("user2", arr);

                //startActivity(intent);

                Bomb=new QuizBombDialog(ChatListActivity.this, arr[0], id_key,  arr[1]);
                Bomb.show();

                Display display = getWindowManager().getDefaultDisplay();
                Point size=new Point();
                display.getSize(size);

                Window window = Bomb.getWindow();

                int x=(int)(size.x *0.8f);
                int y=(int)(size.y *0.7f);


                window.setLayout(x,y);
            }
        });

        Button button = (Button) findViewById(R.id.popuptest);

        //팝업 테스트용 임의의 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                Bomb=new QuizBombDialog(ChatListActivity.this, "test", id_key,  "seng");
                Bomb.show();

                Display display = getWindowManager().getDefaultDisplay();
                Point size=new Point();
                display.getSize(size);

                Window window = Bomb.getWindow();

                int x=(int)(size.x *0.8f);
                int y=(int)(size.y *0.7f);


                window.setLayout(x,y);
            }
        });

    }

    public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add) {
            FirebasePost_list post = new FirebasePost_list(name.getText().toString(), id_key, friend_key);
            postValues = post.toMap();
        }
        childUpdates.put(id_key+"-"+friend_key+":"+name.getText().toString(), postValues);
        mPostReference.updateChildren(childUpdates);
        name.setText("");
    }

    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String user1 = postSnapshot.child("user1").getValue().toString();
                        String user2 = postSnapshot.child("user2").getValue().toString();
                        Log.d("_id_key", id_key);
                        Log.d("_user1", user1);
                        Log.d("_user2", user2);
                        if (user1.equals(id_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + " with " + get.user2);
                        } else if (user2.equals(id_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + " with " + get.user1);
                        }
                    }
                    arrayAdapter.clear();
                    arrayAdapter.addAll(data);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        }catch(java.lang.NullPointerException e){
        }
    }


    private ValueEventListener checkRoomRegister = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                String user1 = postSnapshot.child("user1").getValue().toString();
                String user2 = postSnapshot.child("user2").getValue().toString();
                Log.d("_id_key", id_key);
                Log.d("_user1", user1);
                Log.d("_user2", user2);
                if ((user1.equals(id_key) && user2.equals(friend_key)) || (user2.equals(id_key) && user1.equals(friend_key))) {
                    Toast.makeText(getApplicationContext(), "이미 " + friend_key + " 와 게임에 참여중입니다.\n 진행중인 request를 먼저 완료해주세요", Toast.LENGTH_SHORT).show();
                    mPostReference.removeEventListener(this);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), friend_key + "와의 게임방이 생성되었습니다.", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public boolean spaceCheck(String spaceCheck) {
        for (int i = 0 ; i < spaceCheck.length() ; i++) {
            if (spaceCheck.charAt(i) == ' ')
                continue;
            else if (spaceCheck.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}