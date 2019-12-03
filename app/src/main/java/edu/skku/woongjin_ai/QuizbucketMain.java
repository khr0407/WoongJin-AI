
package edu.skku.woongjin_ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuizbucketMain extends AppCompatActivity
        implements Fragment_help.OnFragmentInteractionListener, NewHoonjangFragment.OnFragmentInteractionListener {
    private DatabaseReference mPostReference, qaPostReference, nPostReference;
    public DatabaseReference m2PostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<BucketRoomInfo> BucketRoomInfos=new ArrayList<BucketRoomInfo>();

    long Solved;

    String id_key, nickname_key;
    String roomname_key, friend_nickname;
    String timestamp_key, user1_key, user2_key, user3_key, user4_key, user5_key, bucketcnt_key, script_key, state_key;
    char quizcnt, bkcnt;
    String last, solve;
    String type_key, question_key, diff_key, answer_key, ans1_key, ans2_key, ans3_key, ans4_key;
    int find, check_gamelist;

    UserInfo me;

    ListView gamelist;
    Button create;
    ImageView game_send, game_get;
    ImageView imageHome, help;
    TextView userName1, userGrade1, userCoin;

    Intent intent, intent_quizsend, intent_quizsendnext;
    Fragment Fragment_help;

    NewHoonjangFragment hoonjangFragment_bombmaster;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String nomore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbucketmain);

        setting = getSharedPreferences("nomore", MODE_PRIVATE);
        nomore=setting.getString("bombmaster", "keepgoing");


        gamelist = findViewById(R.id.gamelist);
        create = findViewById(R.id.create);
        help = findViewById(R.id.helpbtn);
        game_send = findViewById(R.id.game_send);
        game_get = findViewById(R.id.game_get);
        imageHome = (ImageView) findViewById(R.id.home);
        userName1 = (TextView) findViewById(R.id.userName1);
        userGrade1 = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);

        m2PostReference = FirebaseDatabase.getInstance().getReference();

        data = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list");
        nPostReference=FirebaseDatabase.getInstance().getReference();
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
                Intent intentHome = new Intent(QuizbucketMain.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent_nationgame = new Intent(QuizbucketMain.this, Quizbucketstart1.class);
                intent_nationgame.putExtra("id", id_key);
                intent_nationgame.putExtra("nickname", nickname_key);
                startActivity(intent_nationgame);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_help = new Fragment_help();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.comment, Fragment_help);
                Bundle bundle = new Bundle(1);
                bundle.putString("gamelist", "gamelist");
                Fragment_help.setArguments(bundle);
                transaction.commit();
            }
        });

        gamelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check_gamelist = 1;

                BucketRoomInfo listItem=BucketRoomInfos.get(position);
                roomname_key=listItem.Roomname;
                friend_nickname=listItem.WithWhom;

                final ValueEventListener checkRoom = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String temp_timestamp = postSnapshot.getKey();
                            String temp_roomname = postSnapshot.child("roomname").getValue().toString();
                            String temp_user1 = postSnapshot.child("user1").getValue().toString();
                            String temp_user2 = postSnapshot.child("user2").getValue().toString();
                            String temp_user3 = postSnapshot.child("user3").getValue().toString();
                            String temp_user4 = postSnapshot.child("user4").getValue().toString();
                            String temp_user5 = postSnapshot.child("user5").getValue().toString();
                            String temp_bucketcnt = postSnapshot.child("bucketcnt").getValue().toString();
                            String temp_script = postSnapshot.child("script").getValue().toString();
                            String temp_state = postSnapshot.child("state").getValue().toString();

                            if ((temp_roomname.equals(roomname_key) && temp_user1.equals(nickname_key) && temp_user2.equals(friend_nickname))) {
                                if (temp_state.equals("win")) {
                                    Toast.makeText(QuizbucketMain.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }
                                else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }
                                else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }

                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    quizcnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }

                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    quizcnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+ quizcnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+ quizcnt).child("solve").getValue().toString();
                                    find = 1;
                                    break;
                                }

                                else if (temp_state.equals("gamingover")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    quizcnt = state_key.charAt(6);
                                    bkcnt = bucketcnt_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+ bkcnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+ bkcnt).child("solve").getValue().toString();
                                    find = bkcnt - '0' +10;
                                    break;
                                }
                            }

                            else if (temp_roomname.equals(roomname_key) && temp_user1.equals(friend_nickname) && temp_user2.equals(nickname_key)) {
                                if (temp_state.equals("win")) {
                                    Toast.makeText(QuizbucketMain.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                }

                                else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }

                                else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                    else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                }

                                else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    quizcnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                }

                                else if (temp_state.equals("gaming1") || temp_state.equals("gaming2")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    quizcnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+ quizcnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+ quizcnt).child("solve").getValue().toString();
                                    find = 1;
                                    break;
                                }

                                else if (temp_state.equals("gamingover")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    bucketcnt_key = temp_bucketcnt;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    quizcnt = state_key.charAt(6);
                                    bkcnt = bucketcnt_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz"+ bkcnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz"+ bkcnt).child("solve").getValue().toString();
                                    find = bkcnt - '0' +10;
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

        game_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(QuizbucketMain.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 아직 문제를 풀지 않았습니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && !last.equals(nickname_key) && solve.equals("none") && !last.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "문제를 먼저 풀어주세요.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방에게 문제를 내러 가보자!", Toast.LENGTH_SHORT).show();
                        intent_quizsend = new Intent(QuizbucketMain.this, Quizbucketquiztype.class);
                        intent_quizsend.putExtra("timestamp", timestamp_key);
                        intent_quizsend.putExtra("id", id_key);
                        intent_quizsend.putExtra("nickname", nickname_key);
                        intent_quizsend.putExtra("scriptnm", script_key);
                        intent_quizsend.putExtra("user1", user1_key);
                        intent_quizsend.putExtra("user2", user2_key);
                        intent_quizsend.putExtra("bucketcnt", bucketcnt_key);
                        intent_quizsend.putExtra("roomname", roomname_key);
                        intent_quizsend.putExtra("state", state_key);
                        startActivity(intent_quizsend);

                    } else if (find == 1 && !last.equals(nickname_key) && (!solve.equals("none") || solve.equals(nickname_key)) && quizcnt - '0' != 2) {
                        Toast.makeText(QuizbucketMain.this, "상대방에게 다시 문제를 내보자!", Toast.LENGTH_SHORT).show();
                        intent_quizsend = new Intent(QuizbucketMain.this, Quizbucketquiztype.class);
                        intent_quizsend.putExtra("timestamp", timestamp_key);
                        intent_quizsend.putExtra("id", id_key);
                        intent_quizsend.putExtra("nickname", nickname_key);
                        intent_quizsend.putExtra("scriptnm", script_key);
                        intent_quizsend.putExtra("user1", user1_key);
                        intent_quizsend.putExtra("user2", user2_key);
                        intent_quizsend.putExtra("bucketcnt", bucketcnt_key);
                        intent_quizsend.putExtra("roomname", roomname_key);
                        intent_quizsend.putExtra("state", state_key);
                        startActivity(intent_quizsend);
                    }
                    else if (find == 11) {
                        Toast.makeText(QuizbucketMain.this, "상대방에게 다시 문제를 내보자!", Toast.LENGTH_SHORT).show();
                        intent_quizsendnext = new Intent(QuizbucketMain.this, QuizbucketstartNext.class);
                        intent_quizsendnext.putExtra("timestamp", timestamp_key);
                        intent_quizsendnext.putExtra("id", id_key);
                        intent_quizsendnext.putExtra("nickname", nickname_key);
                        intent_quizsendnext.putExtra("scriptnm", script_key);
                        intent_quizsendnext.putExtra("user1", user1_key);
                        intent_quizsendnext.putExtra("user2", user2_key);
                        intent_quizsendnext.putExtra("user3", user3_key);
                        intent_quizsendnext.putExtra("user4", user4_key);
                        intent_quizsendnext.putExtra("user5", user5_key);
                        intent_quizsendnext.putExtra("bucketcnt", bucketcnt_key);
                        intent_quizsendnext.putExtra("roomname", roomname_key);
                        intent_quizsendnext.putExtra("state", state_key);
                        startActivity(intent_quizsendnext);
                    }

                    else if (find == 12) {
                        // user3이 user4에게 게임을 보낼 때
                        Toast.makeText(QuizbucketMain.this, "이제 다음 친구가 다른 친구에게 문제를 낼거야!", Toast.LENGTH_SHORT).show();
                        intent_quizsendnext = new Intent(QuizbucketMain.this, QuizbucketstartNext.class);
                        intent_quizsendnext.putExtra("timestamp", timestamp_key);
                        intent_quizsendnext.putExtra("id", id_key);
                        intent_quizsendnext.putExtra("nickname", nickname_key);
                        intent_quizsendnext.putExtra("scriptnm", script_key);
                        intent_quizsendnext.putExtra("user1", user1_key);
                        intent_quizsendnext.putExtra("user2", user2_key);
                        intent_quizsendnext.putExtra("user3", user3_key);
                        intent_quizsendnext.putExtra("user4", user4_key);
                        intent_quizsendnext.putExtra("user5", user5_key);
                        intent_quizsendnext.putExtra("bucketcnt", bucketcnt_key);
                        intent_quizsendnext.putExtra("roomname", roomname_key);
                        intent_quizsendnext.putExtra("state", state_key);
                        startActivity(intent_quizsendnext);
                    }

                    else if (find == 13) {
                        // user4가 user5에게 게임을 보낼 때
                        Toast.makeText(QuizbucketMain.this, "이제 다음 친구가 다른 친구에게 문제를 낼거야!", Toast.LENGTH_SHORT).show();
                        intent_quizsendnext = new Intent(QuizbucketMain.this, QuizbucketstartNext.class);
                        intent_quizsendnext.putExtra("timestamp", timestamp_key);
                        intent_quizsendnext.putExtra("id", id_key);
                        intent_quizsendnext.putExtra("nickname", nickname_key);
                        intent_quizsendnext.putExtra("scriptnm", script_key);
                        intent_quizsendnext.putExtra("user1", user1_key);
                        intent_quizsendnext.putExtra("user2", user2_key);
                        intent_quizsendnext.putExtra("user3", user3_key);
                        intent_quizsendnext.putExtra("user4", user4_key);
                        intent_quizsendnext.putExtra("user5", user5_key);
                        intent_quizsendnext.putExtra("bucketcnt", bucketcnt_key);
                        intent_quizsendnext.putExtra("roomname", roomname_key);
                        intent_quizsendnext.putExtra("state", state_key);
                        startActivity(intent_quizsendnext);
                    }
                }
                else if (check_gamelist == 0) {
                    // user2, user3
                    Toast.makeText(QuizbucketMain.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        game_get.setOnClickListener(new View.OnClickListener() { //OX, choice, shortword type -> need to modiy!!!
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(QuizbucketMain.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals(nickname_key) && !solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 아직 문제를 제출하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "문제를 먼저 만들어주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals(nickname_key)) { //마지막으로 상대방이 문제를 내고, 내가 풀었는데, 문제를 다시 toss 하지 않았을 때
                        Toast.makeText(QuizbucketMain.this, "문제를 낼 차례입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (find == 1 && !last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 보낸 문제가 도착했어!", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    diff_key = postSnapshot.child("diff").getValue().toString();
                                    if (key.equals("quiz" + quizcnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBOox = new Intent(QuizbucketMain.this, SvQuizbucketOX.class);
                                            intent_SvQBOox.putExtra("timestamp", timestamp_key);
                                            intent_SvQBOox.putExtra("id", id_key);
                                            intent_SvQBOox.putExtra("nickname", nickname_key);
                                            intent_SvQBOox.putExtra("scriptnm", script_key);
                                            intent_SvQBOox.putExtra("user1", user1_key);
                                            intent_SvQBOox.putExtra("user2", user2_key);
                                            intent_SvQBOox.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBOox.putExtra("roomname", roomname_key);
                                            intent_SvQBOox.putExtra("state", state_key);
                                            intent_SvQBOox.putExtra("question", question_key);
                                            intent_SvQBOox.putExtra("diff", diff_key);
                                            intent_SvQBOox.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBOox);
                                            break;
                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBshortword = new Intent(QuizbucketMain.this, SvQuizbucketshortword.class);
                                            intent_SvQBshortword.putExtra("timestamp", timestamp_key);
                                            intent_SvQBshortword.putExtra("id", id_key);
                                            intent_SvQBshortword.putExtra("nickname", nickname_key);
                                            intent_SvQBshortword.putExtra("scriptnm", script_key);
                                            intent_SvQBshortword.putExtra("user1", user1_key);
                                            intent_SvQBshortword.putExtra("user2", user2_key);
                                            intent_SvQBshortword.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBshortword.putExtra("roomname", roomname_key);
                                            intent_SvQBshortword.putExtra("state", state_key);
                                            intent_SvQBshortword.putExtra("question", question_key);
                                            intent_SvQBshortword.putExtra("diff", diff_key);
                                            intent_SvQBshortword.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_SvQBchoice = new Intent(QuizbucketMain.this, SvQuizbucketchoice.class);
                                            intent_SvQBchoice.putExtra("timestamp", timestamp_key);
                                            intent_SvQBchoice.putExtra("id", id_key);
                                            intent_SvQBchoice.putExtra("nickname", nickname_key);
                                            intent_SvQBchoice.putExtra("scriptnm", script_key);
                                            intent_SvQBchoice.putExtra("user1", user1_key);
                                            intent_SvQBchoice.putExtra("user2", user2_key);
                                            intent_SvQBchoice.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBchoice.putExtra("roomname", roomname_key);
                                            intent_SvQBchoice.putExtra("state", state_key);
                                            intent_SvQBchoice.putExtra("question", question_key);
                                            intent_SvQBchoice.putExtra("diff", diff_key);
                                            intent_SvQBchoice.putExtra("answer", answer_key);
                                            intent_SvQBchoice.putExtra("answer1", ans1_key);
                                            intent_SvQBchoice.putExtra("answer2", ans2_key);
                                            intent_SvQBchoice.putExtra("answer3", ans3_key);
                                            intent_SvQBchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_SvQBchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }
                    else if (find == 11) {
                        // user2가 보낸 문제를 user3가 풀 때

                        Toast.makeText(QuizbucketMain.this, "상대방이 보낸 문제가 도착했어! (2)", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    diff_key = postSnapshot.child("diff").getValue().toString();
                                    if (key.equals("quiz" + quizcnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBOox = new Intent(QuizbucketMain.this, SvQuizbucketOXNext.class);
                                            intent_SvQBOox.putExtra("timestamp", timestamp_key);
                                            intent_SvQBOox.putExtra("id", id_key);
                                            intent_SvQBOox.putExtra("nickname", nickname_key);
                                            intent_SvQBOox.putExtra("scriptnm", script_key);
                                            intent_SvQBOox.putExtra("user1", user1_key);
                                            intent_SvQBOox.putExtra("user2", user2_key);
                                            intent_SvQBOox.putExtra("user3", user3_key);
                                            intent_SvQBOox.putExtra("user4", user4_key);
                                            intent_SvQBOox.putExtra("user5", user5_key);
                                            intent_SvQBOox.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBOox.putExtra("roomname", roomname_key);
                                            intent_SvQBOox.putExtra("state", state_key);
                                            intent_SvQBOox.putExtra("question", question_key);
                                            intent_SvQBOox.putExtra("diff", diff_key);
                                            intent_SvQBOox.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBOox);
                                            break;
                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBshortword = new Intent(QuizbucketMain.this, SvQuizbucketshortword.class);
                                            intent_SvQBshortword.putExtra("timestamp", timestamp_key);
                                            intent_SvQBshortword.putExtra("id", id_key);
                                            intent_SvQBshortword.putExtra("nickname", nickname_key);
                                            intent_SvQBshortword.putExtra("scriptnm", script_key);
                                            intent_SvQBshortword.putExtra("user1", user1_key);
                                            intent_SvQBshortword.putExtra("user2", user2_key);
                                            intent_SvQBshortword.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBshortword.putExtra("roomname", roomname_key);
                                            intent_SvQBshortword.putExtra("state", state_key);
                                            intent_SvQBshortword.putExtra("question", question_key);
                                            intent_SvQBshortword.putExtra("diff", diff_key);
                                            intent_SvQBshortword.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_SvQBchoice = new Intent(QuizbucketMain.this, SvQuizbucketchoice.class);
                                            intent_SvQBchoice.putExtra("timestamp", timestamp_key);
                                            intent_SvQBchoice.putExtra("id", id_key);
                                            intent_SvQBchoice.putExtra("nickname", nickname_key);
                                            intent_SvQBchoice.putExtra("scriptnm", script_key);
                                            intent_SvQBchoice.putExtra("user1", user1_key);
                                            intent_SvQBchoice.putExtra("user2", user2_key);
                                            intent_SvQBchoice.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBchoice.putExtra("roomname", roomname_key);
                                            intent_SvQBchoice.putExtra("state", state_key);
                                            intent_SvQBchoice.putExtra("question", question_key);
                                            intent_SvQBchoice.putExtra("diff", diff_key);
                                            intent_SvQBchoice.putExtra("answer", answer_key);
                                            intent_SvQBchoice.putExtra("answer1", ans1_key);
                                            intent_SvQBchoice.putExtra("answer2", ans2_key);
                                            intent_SvQBchoice.putExtra("answer3", ans3_key);
                                            intent_SvQBchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_SvQBchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }

                    else if (find == 12) {
                        // user3가 보낸 문제를 user4가 풀 때

                        Toast.makeText(QuizbucketMain.this, "상대방이 보낸 문제가 도착했어! (3)", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    diff_key = postSnapshot.child("diff").getValue().toString();
                                    if (key.equals("quiz" + quizcnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBOox = new Intent(QuizbucketMain.this, SvQuizbucketOXNext.class);
                                            intent_SvQBOox.putExtra("timestamp", timestamp_key);
                                            intent_SvQBOox.putExtra("id", id_key);
                                            intent_SvQBOox.putExtra("nickname", nickname_key);
                                            intent_SvQBOox.putExtra("scriptnm", script_key);
                                            intent_SvQBOox.putExtra("user1", user1_key);
                                            intent_SvQBOox.putExtra("user2", user2_key);
                                            intent_SvQBOox.putExtra("user3", user3_key);
                                            intent_SvQBOox.putExtra("user4", user4_key);
                                            intent_SvQBOox.putExtra("user5", user5_key);
                                            intent_SvQBOox.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBOox.putExtra("roomname", roomname_key);
                                            intent_SvQBOox.putExtra("state", state_key);
                                            intent_SvQBOox.putExtra("question", question_key);
                                            intent_SvQBOox.putExtra("diff", diff_key);
                                            intent_SvQBOox.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBOox);
                                            break;
                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBshortword = new Intent(QuizbucketMain.this, SvQuizbucketshortword.class);
                                            intent_SvQBshortword.putExtra("timestamp", timestamp_key);
                                            intent_SvQBshortword.putExtra("id", id_key);
                                            intent_SvQBshortword.putExtra("nickname", nickname_key);
                                            intent_SvQBshortword.putExtra("scriptnm", script_key);
                                            intent_SvQBshortword.putExtra("user1", user1_key);
                                            intent_SvQBshortword.putExtra("user2", user2_key);
                                            intent_SvQBshortword.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBshortword.putExtra("roomname", roomname_key);
                                            intent_SvQBshortword.putExtra("state", state_key);
                                            intent_SvQBshortword.putExtra("question", question_key);
                                            intent_SvQBshortword.putExtra("diff", diff_key);
                                            intent_SvQBshortword.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_SvQBchoice = new Intent(QuizbucketMain.this, SvQuizbucketchoice.class);
                                            intent_SvQBchoice.putExtra("timestamp", timestamp_key);
                                            intent_SvQBchoice.putExtra("id", id_key);
                                            intent_SvQBchoice.putExtra("nickname", nickname_key);
                                            intent_SvQBchoice.putExtra("scriptnm", script_key);
                                            intent_SvQBchoice.putExtra("user1", user1_key);
                                            intent_SvQBchoice.putExtra("user2", user2_key);
                                            intent_SvQBchoice.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBchoice.putExtra("roomname", roomname_key);
                                            intent_SvQBchoice.putExtra("state", state_key);
                                            intent_SvQBchoice.putExtra("question", question_key);
                                            intent_SvQBchoice.putExtra("diff", diff_key);
                                            intent_SvQBchoice.putExtra("answer", answer_key);
                                            intent_SvQBchoice.putExtra("answer1", ans1_key);
                                            intent_SvQBchoice.putExtra("answer2", ans2_key);
                                            intent_SvQBchoice.putExtra("answer3", ans3_key);
                                            intent_SvQBchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_SvQBchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }

                    else if (find == 13) {
                        // user4가 보낸 문제를 user5가 풀 때

                        Toast.makeText(QuizbucketMain.this, "상대방이 보낸 문제가 도착했어! (4)", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    diff_key = postSnapshot.child("diff").getValue().toString();
                                    if (key.equals("quiz" + quizcnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBOox = new Intent(QuizbucketMain.this, SvQuizbucketOXNext.class);
                                            intent_SvQBOox.putExtra("timestamp", timestamp_key);
                                            intent_SvQBOox.putExtra("id", id_key);
                                            intent_SvQBOox.putExtra("nickname", nickname_key);
                                            intent_SvQBOox.putExtra("scriptnm", script_key);
                                            intent_SvQBOox.putExtra("user1", user1_key);
                                            intent_SvQBOox.putExtra("user2", user2_key);
                                            intent_SvQBOox.putExtra("user3", user3_key);
                                            intent_SvQBOox.putExtra("user4", user4_key);
                                            intent_SvQBOox.putExtra("user5", user5_key);
                                            intent_SvQBOox.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBOox.putExtra("roomname", roomname_key);
                                            intent_SvQBOox.putExtra("state", state_key);
                                            intent_SvQBOox.putExtra("question", question_key);
                                            intent_SvQBOox.putExtra("diff", diff_key);
                                            intent_SvQBOox.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBOox);
                                            break;
                                        }
                                        else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_SvQBshortword = new Intent(QuizbucketMain.this, SvQuizbucketshortword.class);
                                            intent_SvQBshortword.putExtra("timestamp", timestamp_key);
                                            intent_SvQBshortword.putExtra("id", id_key);
                                            intent_SvQBshortword.putExtra("nickname", nickname_key);
                                            intent_SvQBshortword.putExtra("scriptnm", script_key);
                                            intent_SvQBshortword.putExtra("user1", user1_key);
                                            intent_SvQBshortword.putExtra("user2", user2_key);
                                            intent_SvQBshortword.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBshortword.putExtra("roomname", roomname_key);
                                            intent_SvQBshortword.putExtra("state", state_key);
                                            intent_SvQBshortword.putExtra("question", question_key);
                                            intent_SvQBshortword.putExtra("diff", diff_key);
                                            intent_SvQBshortword.putExtra("answer", answer_key);
                                            startActivity(intent_SvQBshortword);
                                            break;

                                        }
                                        else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_SvQBchoice = new Intent(QuizbucketMain.this, SvQuizbucketchoice.class);
                                            intent_SvQBchoice.putExtra("timestamp", timestamp_key);
                                            intent_SvQBchoice.putExtra("id", id_key);
                                            intent_SvQBchoice.putExtra("nickname", nickname_key);
                                            intent_SvQBchoice.putExtra("scriptnm", script_key);
                                            intent_SvQBchoice.putExtra("user1", user1_key);
                                            intent_SvQBchoice.putExtra("user2", user2_key);
                                            intent_SvQBchoice.putExtra("bucketcnt", bucketcnt_key);
                                            intent_SvQBchoice.putExtra("roomname", roomname_key);
                                            intent_SvQBchoice.putExtra("state", state_key);
                                            intent_SvQBchoice.putExtra("question", question_key);
                                            intent_SvQBchoice.putExtra("diff", diff_key);
                                            intent_SvQBchoice.putExtra("answer", answer_key);
                                            intent_SvQBchoice.putExtra("answer1", ans1_key);
                                            intent_SvQBchoice.putExtra("answer2", ans2_key);
                                            intent_SvQBchoice.putExtra("answer3", ans3_key);
                                            intent_SvQBchoice.putExtra("answer4", ans4_key);
                                            startActivity(intent_SvQBchoice);
                                            break;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }
                }
                else if(check_gamelist == 0) {
                    Toast.makeText(QuizbucketMain.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BucketRoomInfos.clear();
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BucketListAdapter bucketListAdapter=new BucketListAdapter();
                    //String roomname, String withwhom, String status
                    String roomname="", withwhom="", status="";
                    String lastperson="", solveperson="";
                    BucketRoomInfos.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String user1 = postSnapshot.child("user1").getValue().toString();
                        String user2 = postSnapshot.child("user2").getValue().toString();
                        String user3 = postSnapshot.child("user3").getValue().toString();
                        String user4 = postSnapshot.child("user4").getValue().toString();
                        String user5 = postSnapshot.child("user5").getValue().toString();
                        String temp_status = postSnapshot.child("state").getValue().toString();
                        String temp_bucketcnt = postSnapshot.child("bucketcnt").getValue().toString();

                        char quizcount;
                        char bkcount;

                        if(!temp_status.equals("win") && !temp_status.equals("win1") && !temp_status.equals("win2")){ //gaming0~6
                            quizcount = temp_status.charAt(6);
                            bkcount = temp_bucketcnt.charAt(6);
                        }

                        else if (temp_status.equals("gamingover")) {
                            quizcount = '1';
                            bkcount = temp_bucketcnt.charAt(6);
                        }

                        else if (temp_status.equals("gameover")){
                            quizcount = temp_status.charAt(6);
                            bkcount = temp_bucketcnt.charAt(6);
                        }

                        else { //win, win1, win2
                            quizcount='x';
                            bkcount = 'x';
                        }

                        if(quizcount=='0'){
                            lastperson="none";
                            solveperson="none";
                        }

                        else if(quizcount!='x'){
                            lastperson = postSnapshot.child("quiz_list").child("quiz" + bkcount).child("last").getValue().toString();
                            solveperson = postSnapshot.child("quiz_list").child("quiz" + bkcount).child("solve").getValue().toString();
                        }

                        // 여기까진 ㅇㅋ

                        if (user1.equals(nickname_key)) { //내가 user1
                            FirebasePost_QBlist get = postSnapshot.getValue(FirebasePost_QBlist.class);
                            //data.add(get.roomname + "-" + get.user2);
                            withwhom=get.user2;
                            roomname=get.roomname;

                            if (quizcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (quizcount=='x' && temp_status.equals("win1")){ //ok
                                status="iwin";
                            }
                            else if (quizcount=='x' && temp_status.equals("win2")){ //ok
                                status="youwin";
                            }
                            else if (quizcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status = "elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user1", "에러유");
                            }
                            BucketRoomInfo roomInfo=new BucketRoomInfo(roomname, withwhom, status);
                            BucketRoomInfos.add(roomInfo);
                            bucketListAdapter.addItem(roomname, withwhom, status);
                        }

                        else if (user2.equals(nickname_key)) { //내가 user2
                            FirebasePost_QBlist get = postSnapshot.getValue(FirebasePost_QBlist.class);
                            //data.add(get.roomname + "-" + get.user1);
                            withwhom=get.user1;
                            roomname=get.roomname;

                            if (quizcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (quizcount=='x' && temp_status.equals("win1")){ //ok
                                status="youwin";
                            }
                            else if (quizcount=='x' && temp_status.equals("win2")){ //ok
                                status="iwin";
                            }
                            else if (quizcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status="elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user2", "에러유");
                            }
                            BucketRoomInfo roomInfo=new BucketRoomInfo(roomname, withwhom, status);
                            BucketRoomInfos.add(roomInfo);
                            bucketListAdapter.addItem(roomname, withwhom, status);
                        }

                        else if (bkcnt == '2') { //내가 user2
                            FirebasePost_QBlist get = postSnapshot.getValue(FirebasePost_QBlist.class);
                            //data.add(get.roomname + "-" + get.user1);
                            withwhom=get.user1;
                            roomname=get.roomname;

                            if (quizcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (quizcount=='x' && temp_status.equals("win1")){ //ok
                                status="youwin";
                            }
                            else if (quizcount=='x' && temp_status.equals("win2")){ //ok
                                status="iwin";
                            }
                            else if (quizcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status="elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user2", "에러유");
                            }
                            BucketRoomInfo roomInfo=new BucketRoomInfo(roomname, withwhom, status);
                            BucketRoomInfos.add(roomInfo);
                            bucketListAdapter.addItem(roomname, withwhom, status);
                        }

                        else if (bkcnt == '3') { //내가 user2
                            FirebasePost_QBlist get = postSnapshot.getValue(FirebasePost_QBlist.class);
                            //data.add(get.roomname + "-" + get.user1);
                            withwhom=get.user1;
                            roomname=get.roomname;

                            if (quizcount=='x' && temp_status.equals("win")){ //ok
                                status="end";
                            }
                            else if (quizcount=='x' && temp_status.equals("win1")){ //ok
                                status="youwin";
                            }
                            else if (quizcount=='x' && temp_status.equals("win2")){ //ok
                                status="iwin";
                            }
                            else if (quizcount=='0') {
                                status="makeplease";
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status="myturn"; //내가 폭탄 만들 차례
                            }
                            else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            }
                            else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status="elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            }
                            else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status="elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            }
                            else{
                                Log.d("폭탄에러났슈user2", "에러유");
                            }
                            BucketRoomInfo roomInfo=new BucketRoomInfo(roomname, withwhom, status);
                            BucketRoomInfos.add(roomInfo);
                            bucketListAdapter.addItem(roomname, withwhom, status);
                        }
                    }
                    gamelist.setAdapter(bucketListAdapter);
                    gamelist.clearChoices();
                    bucketListAdapter.notifyDataSetChanged();
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
                Solved=0;
                DataSnapshot dataSnapshot1=dataSnapshot.child("user_list/"+id_key+"/my_week_list");
                for(DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){ //week 껍데기
                    Solved+=Long.parseLong(dataSnapshot2.child("solvebomb").getValue().toString());
                }
                Calendar calendar = Calendar.getInstance();
                Date dateS = calendar.getTime();
                String MedalUpdate = new SimpleDateFormat("yyyy-MM-dd").format(dateS);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                hoonjangFragment_bombmaster=new NewHoonjangFragment();

                if(Solved==365 && nomore.equals("stop2")) {
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 3);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev3##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop3");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 3);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }
                else if(Solved==100 && nomore.equals("stop1")){
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 2);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev2##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop2");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 2);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }else if(Solved==30 && nomore.equals("keepgoing")){
                    uploadFirebaseUserCoinInfo_H("폭탄마스터", 1);
                    nPostReference.child("user_list/" + id_key + "/my_medal_list/폭탄마스터").setValue("Lev1##"+MedalUpdate);
                    SharedPreferences sf = getSharedPreferences("nomore", MODE_PRIVATE);
                    editor=sf.edit();
                    editor.putString("bombmaster", "stop1");
                    editor.commit();
                    transaction.replace(R.id.gamelist_main, hoonjangFragment_bombmaster);
                    Bundle bundle = new Bundle(3);
                    bundle.putString("what", "bombmaster");
                    bundle.putString("from", "gamelist");
                    bundle.putInt("level", 1);
                    hoonjangFragment_bombmaster.setArguments(bundle);
                    transaction.commit();
                }
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

    private void uploadFirebaseUserCoinInfo_H(String hoonjangname, int level){
        nPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String today = new SimpleDateFormat("yyMMddHHmm").format(date);
                nPostReference.child("user_list/" + id_key + "/my_coin_list/" + today + "/get").setValue(Integer.toString(level*100));
                nPostReference.child("user_list/" + id_key + "/my_coin_list/" + today + "/why").setValue(hoonjangname+" 레벨 "+Integer.toString(level)+"달성!");

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String key=dataSnapshot1.getKey();
                    if(key.equals("user_list")){
                        String mycoin=dataSnapshot1.child(id_key).child("coin").getValue().toString();
                        int coin = Integer.parseInt(mycoin) + level*100;
                        String coin_convert = Integer.toString(coin);
                        nPostReference.child("user_list/" + id_key).child("coin").setValue(coin_convert);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}