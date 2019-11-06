package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameListActivity extends AppCompatActivity {
    private DatabaseReference mPostReference, lsPostReference, qaPostReference;
    public DatabaseReference m2PostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    String id_key, nickname_key;
    String roomname_key, friend_nickname;
    String timestamp_key, user1_key, user2_key, script_key, state_key;
    char bomb_cnt;
    String last, solve;
    String type_key, question_key, answer_key, ans1_key, ans2_key, ans3_key, ans4_key;
    int find, check_gamelist;
    UserInfo me;

    ListView gamelist;
    Button create;
    ImageView makebomb, solvebomb;
    ImageView imageHome, help;
    TextView userName1, userGrade1, userCoin;

    Intent intent, intent_makebombtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);

        gamelist = findViewById(R.id.gamelist);
        create = findViewById(R.id.create);
        help = findViewById(R.id.helpbtn);
        makebomb = findViewById(R.id.makebomb);
        solvebomb = findViewById(R.id.solvebomb);
        imageHome = (ImageView) findViewById(R.id.home);
        userName1 = (TextView) findViewById(R.id.userName1);
        userGrade1 = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);

        m2PostReference = FirebaseDatabase.getInstance().getReference();

        data = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        find = 0;
        check_gamelist = 0;

        intent = getIntent();
        id_key = intent.getExtras().getString("id");
        nickname_key = intent.getExtras().getString("nickname");


        getFirebaseDatabaseUserInfo();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(GameListActivity.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_nationgame = new Intent(GameListActivity.this, NationGameActivity.class);
                intent_nationgame.putExtra("id", id_key);
                intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_nationgame);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment_help fragment = new Fragment_help();
                transaction.replace(R.id.comment, fragment);
                transaction.commit();
            }
        });

        gamelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check_gamelist = 1;
                roomname_key = gamelist.getItemAtPosition(position).toString().split("-")[0];
                friend_nickname = gamelist.getItemAtPosition(position).toString().split("-")[1];
                final ValueEventListener checkRoom = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String temp_timestamp = postSnapshot.getKey();
                            String temp_roomname = postSnapshot.child("roomname").getValue().toString();
                            String temp_user1 = postSnapshot.child("user1").getValue().toString();
                            String temp_user2 = postSnapshot.child("user2").getValue().toString();
                            String temp_script = postSnapshot.child("script").getValue().toString();
                            String temp_state = postSnapshot.child("state").getValue().toString();
                            if ((temp_roomname.equals(roomname_key) && temp_user1.equals(nickname_key) && temp_user2.equals(friend_nickname))) {
                                if (temp_state.equals("win") || temp_state.equals("win1") || temp_state.equals("win2")) {
                                    Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }
                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }
                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    Log.d("GameListActivityError", roomname_key+script_key+state_key);
                                    last = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("solve").getValue().toString();
                                    Log.d("GameListActivityError", last+" / "+solve);
                                    find = 1;
                                    //Toast.makeText(GameListActivity.this, timestamp_key+user1_key+user2_key+roomname_key+script_key+state_key+bomb_cnt, Toast.LENGTH_SHORT).show();

                                    /*
                                    lsPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key);
                                    final ValueEventListener checklast = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.child("quiz_list").getChildren()) {
                                                String quiznum = postSnapshot.getKey();
                                                last = quiznum.split("quiz")[0];
                                                solve = postSnapshot.child("solve").getValue().toString();
                                                if (quiznum.contains("quiz" + bomb_cnt)) {
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    };
                                    lsPostReference.addValueEventListener(checklast);
                                    */
                                    break;
                                }
                            }
                            else if (temp_roomname.equals(roomname_key) && temp_user1.equals(friend_nickname) && temp_user2.equals(nickname_key)) {
                                if (temp_state.equals("win") || temp_state.equals("win1") || temp_state.equals("win2")) {
                                    Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }
                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }
                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    Log.d("GameListActivityError", roomname_key+script_key+state_key);
                                    last = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+bomb_cnt).child("solve").getValue().toString();
                                    Log.d("GameListActivityError", last+" / "+solve);
                                    find = 1;
                                    //Toast.makeText(GameListActivity.this, timestamp_key+user1_key+user2_key+roomname_key+script_key+state_key+bomb_cnt, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mPostReference.addValueEventListener(checkRoom);
            }
        });


        makebomb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key) && bomb_cnt-'0' == 6) {
                        Toast.makeText(GameListActivity.this, "문제를 더 이상 만들 수 없습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(GameListActivity.this, "상대방이 아직 문제를 풀지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        intent_makebombtype = new Intent(GameListActivity.this, MakeBombTypeActivity.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("scriptnm", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    }
                    else if (find == 1 && !last.equals(nickname_key) && !solve.equals("none") && bomb_cnt-'0' != 6) {
                        intent_makebombtype = new Intent(GameListActivity.this, MakeBombTypeActivity.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("scriptnm", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    }
                }
                else if (check_gamelist == 0) {
                    Toast.makeText(GameListActivity.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        solvebomb.setOnClickListener(new View.OnClickListener() { //OX, choice, shortword type -> need to modify!!!
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(GameListActivity.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key)) {
                        Toast.makeText(GameListActivity.this, "상대방이 아직 문제를 제출하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals("none") && solve.equals("none")){
                        Toast.makeText(GameListActivity.this, "문제를 먼저 만들어주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals("none")) {
                        Log.d("GameListActivityError", "correct else if");
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    Log.d("GameListActivityError", key+type_key+question_key);
                                    if (key.equals("quiz" + bomb_cnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            Log.d("GameListActivityError", key+type_key+question_key+answer_key);
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombox = new Intent(GameListActivity.this, SolveBombOXActivity.class);
                                            intent_solvebombox.putExtra("timestamp", timestamp_key);
                                            intent_solvebombox.putExtra("id", id_key);
                                            intent_solvebombox.putExtra("nickname", nickname_key);
                                            intent_solvebombox.putExtra("scriptnm", script_key);
                                            intent_solvebombox.putExtra("user1", user1_key);
                                            intent_solvebombox.putExtra("user2", user2_key);
                                            intent_solvebombox.putExtra("roomname", roomname_key);
                                            intent_solvebombox.putExtra("state", state_key);
                                            intent_solvebombox.putExtra("question", question_key);
                                            intent_solvebombox.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombox);
                                            break;

                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            Log.d("GameListActivityError", key+type_key+question_key+answer_key);
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombshortword = new Intent(GameListActivity.this, SolveBombShortwordActivity.class);
                                            intent_solvebombshortword.putExtra("timestamp", timestamp_key);
                                            intent_solvebombshortword.putExtra("id", id_key);
                                            intent_solvebombshortword.putExtra("nickname", nickname_key);
                                            intent_solvebombshortword.putExtra("scriptnm", script_key);
                                            intent_solvebombshortword.putExtra("user1", user1_key);
                                            intent_solvebombshortword.putExtra("user2", user2_key);
                                            intent_solvebombshortword.putExtra("roomname", roomname_key);
                                            intent_solvebombshortword.putExtra("state", state_key);
                                            intent_solvebombshortword.putExtra("question", question_key);
                                            intent_solvebombshortword.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Log.d("GameListActivityError", key+type_key+question_key+answer_key);
                                            Intent intent_solvebombchoice = new Intent(GameListActivity.this, SolveBombChoiceActivity.class);
                                            intent_solvebombchoice.putExtra("timestamp", timestamp_key);
                                            intent_solvebombchoice.putExtra("id", id_key);
                                            intent_solvebombchoice.putExtra("nickname", nickname_key);
                                            intent_solvebombchoice.putExtra("scriptnm", script_key);
                                            intent_solvebombchoice.putExtra("user1", user1_key);
                                            intent_solvebombchoice.putExtra("user2", user2_key);
                                            intent_solvebombchoice.putExtra("roomname", roomname_key);
                                            intent_solvebombchoice.putExtra("state", state_key);
                                            intent_solvebombchoice.putExtra("question", question_key);
                                            intent_solvebombchoice.putExtra("answer", answer_key);
                                            intent_solvebombchoice.putExtra("answer1", ans1_key);
                                            intent_solvebombchoice.putExtra("answer2", ans2_key);
                                            intent_solvebombchoice.putExtra("answer3", ans3_key);
                                            intent_solvebombchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_solvebombchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addValueEventListener(findQna);
                    }
                }
                else if(check_gamelist == 0) {
                    Toast.makeText(GameListActivity.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gamelist.setAdapter(arrayAdapter);
        getFirebaseDatabase();
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
                        if (user1.equals(nickname_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + "-" + get.user2);
                        }
                        else if (user2.equals(nickname_key)) {
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            data.add(get.roomname + "-" + get.user1);
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
        } catch (java.lang.NullPointerException e) {
        }
    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("user_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if (key1.equals(id_key)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName1.setText(me.name + " 학생");
                                userGrade1.setText(me.grade + "학년");
                                userCoin.setText(me.coin + " 코인");
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        };
        m2PostReference.addValueEventListener(postListener);
    }
}