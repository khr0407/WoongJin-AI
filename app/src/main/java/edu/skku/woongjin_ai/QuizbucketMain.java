package edu.skku.woongjin_ai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuizbucketMain extends AppCompatActivity
      {
    private DatabaseReference mPostReference, lsPostReference, qaPostReference;
    public DatabaseReference m2PostReference;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<GameRoomInfo> gameRoomInfos = new ArrayList<GameRoomInfo>();

    String id_key, nickname_key;
    String roomname_key, friend_nickname;
    String timestamp_key, user1_key, user2_key, user3_key, user4_key, user5_key, script_key, state_key;
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
        setContentView(R.layout.activity_quizbucketmain);

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
        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list");
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
                Intent intent_quizbucketstart2 = new Intent(QuizbucketMain.this, Quizbucketstart2.class);
                intent_quizbucketstart2.putExtra("id", id_key);
                intent_quizbucketstart2.putExtra("nickname", nickname_key);
                startActivity(intent_quizbucketstart2);
            }
        });

        gamelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check_gamelist = 1;
                GameRoomInfo listItem = gameRoomInfos.get(position);
                roomname_key = listItem.Roomname;
                friend_nickname = listItem.WithWhom;
                //roomname_key = gamelist.getItemAtPosition(position).toString().split("-")[0];
                //friend_nickname = gamelist.getItemAtPosition(position).toString().split("-")[1];
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
                            String temp_script = postSnapshot.child("script").getValue().toString();
                            String temp_state = postSnapshot.child("state").getValue().toString();
                            if ((temp_roomname.equals(roomname_key) && temp_user1.equals(nickname_key) && temp_user2.equals(friend_nickname))) {
                                if (temp_state.equals("win")) {
                                    Toast.makeText(QuizbucketMain.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                } else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    } else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                } else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    } else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                } else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                } else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz" + bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz" + bomb_cnt).child("solve").getValue().toString();
                                    find = 1;
                                    break;
                                }
                            } else if (temp_roomname.equals(roomname_key) && temp_user1.equals(friend_nickname) && temp_user2.equals(nickname_key)) {
                                if (temp_state.equals("win")) {
                                    Toast.makeText(QuizbucketMain.this, "Both win!", Toast.LENGTH_SHORT).show();
                                    find = 0;
                                    break;
                                } else if (temp_state.equals("win1")) {
                                    if (temp_user1.equals(nickname_key)) { //user1이 이겼고, user1이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    } else if (!temp_user1.equals(nickname_key)) { //user1이 이겼는데, user1이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                } else if (temp_state.equals("win2")) {
                                    if (temp_user2.equals(nickname_key)) { //user2이 이겼고, user2이 본인일 때
                                        Toast.makeText(QuizbucketMain.this, "You win!", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    } else if (!temp_user2.equals(nickname_key)) { //user2이 이겼는데, user2이 본인이 아니라 상대방일 때
                                        Toast.makeText(QuizbucketMain.this, "You lose...", Toast.LENGTH_SHORT).show();
                                        find = 0;
                                        break;
                                    }
                                } else if (temp_state.equals("gaming0")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    last = "none";
                                    solve = "none";
                                    bomb_cnt = state_key.charAt(6);
                                    find = 1;
                                    break;
                                } else if (temp_state.equals("gaming1") || temp_state.equals("gaming2") || temp_state.equals("gaming3") || temp_state.equals("gaming4")
                                        || temp_state.equals("gaming5") || temp_state.equals("gaming6")) {
                                    timestamp_key = temp_timestamp;
                                    user1_key = temp_user1;
                                    user2_key = temp_user2;
                                    user3_key = temp_user3;
                                    user4_key = temp_user4;
                                    user5_key = temp_user5;
                                    roomname_key = temp_roomname;
                                    script_key = temp_script;
                                    state_key = temp_state;
                                    bomb_cnt = state_key.charAt(6);
                                    last = postSnapshot.child("quiz_list").child("quiz" + bomb_cnt).child("last").getValue().toString();
                                    solve = postSnapshot.child("quiz_list").child("quiz" + bomb_cnt).child("solve").getValue().toString();
                                    find = 1;
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
                        Toast.makeText(QuizbucketMain.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals(nickname_key) && bomb_cnt - '0' == 6) {
                        Toast.makeText(QuizbucketMain.this, "문제를 더 이상 만들 수 없습니다!", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 아직 문제를 풀지 않았습니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        intent_makebombtype = new Intent(QuizbucketMain.this, Quizbucketquiztype.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("script", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("user3", user3_key);
                        intent_makebombtype.putExtra("user4", user4_key);
                        intent_makebombtype.putExtra("user5", user5_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    } else if (find == 1 && !last.equals(nickname_key) && !solve.equals("none") && bomb_cnt - '0' != 6) {
                        intent_makebombtype = new Intent(QuizbucketMain.this, Quizbucketquiztype.class);
                        intent_makebombtype.putExtra("timestamp", timestamp_key);
                        intent_makebombtype.putExtra("id", id_key);
                        intent_makebombtype.putExtra("nickname", nickname_key);
                        intent_makebombtype.putExtra("script", script_key);
                        intent_makebombtype.putExtra("user1", user1_key);
                        intent_makebombtype.putExtra("user2", user2_key);
                        intent_makebombtype.putExtra("user3", user3_key);
                        intent_makebombtype.putExtra("user4", user4_key);
                        intent_makebombtype.putExtra("user5", user5_key);
                        intent_makebombtype.putExtra("roomname", roomname_key);
                        intent_makebombtype.putExtra("state", state_key);
                        startActivity(intent_makebombtype);
                    }
                } else if (check_gamelist == 0) {
                    Toast.makeText(QuizbucketMain.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        solvebomb.setOnClickListener(new View.OnClickListener() { //OX, choice, shortword type -> need to modify!!!
            public void onClick(View view) {
                if (check_gamelist == 1) {
                    if (find == 0) {
                        Toast.makeText(QuizbucketMain.this, "이미 끝난 게임입니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals(nickname_key) && !solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 아직 문제를 제출하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && last.equals("none") && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "문제를 먼저 만들어주세요.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && !last.equals(nickname_key) && solve.equals(nickname_key)) { //마지막으로 상대방이 문제를 내고, 내가 풀었는데, 문제를 다시 toss 하지 않았을 때
                        Toast.makeText(QuizbucketMain.this, "문제를 낼 차례입니다.", Toast.LENGTH_SHORT).show();
                    } else if (find == 1 && !last.equals(nickname_key) && solve.equals("none")) {
                        Toast.makeText(QuizbucketMain.this, "상대방이 보낸 폭탄이 도착했어!", Toast.LENGTH_SHORT).show();
                        qaPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");
                        final ValueEventListener findQna = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String key = postSnapshot.getKey();
                                    type_key = postSnapshot.child("type").getValue().toString();
                                    question_key = postSnapshot.child("question").getValue().toString();
                                    if (key.equals("quiz" + bomb_cnt)) {
                                        if (type_key.equals("ox")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombox = new Intent(QuizbucketMain.this, SvQuizbucketOX.class);
                                            intent_solvebombox.putExtra("timestamp", timestamp_key);
                                            intent_solvebombox.putExtra("id", id_key);
                                            intent_solvebombox.putExtra("nickname", nickname_key);
                                            intent_solvebombox.putExtra("scriptnm", script_key);
                                            intent_solvebombox.putExtra("user1", user1_key);
                                            intent_solvebombox.putExtra("user2", user2_key);
                                            intent_solvebombox.putExtra("user3", user3_key);
                                            intent_solvebombox.putExtra("user4", user4_key);
                                            intent_solvebombox.putExtra("user5", user5_key);
                                            intent_solvebombox.putExtra("roomname", roomname_key);
                                            intent_solvebombox.putExtra("state", state_key);
                                            intent_solvebombox.putExtra("question", question_key);
                                            intent_solvebombox.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombox);
                                            break;

                                        } else if (type_key.equals("shortword")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = "";
                                            ans2_key = "";
                                            ans3_key = "";
                                            ans4_key = "";
                                            Intent intent_solvebombshortword = new Intent(QuizbucketMain.this, SvQuizbucketshortword.class);
                                            intent_solvebombshortword.putExtra("timestamp", timestamp_key);
                                            intent_solvebombshortword.putExtra("id", id_key);
                                            intent_solvebombshortword.putExtra("nickname", nickname_key);
                                            intent_solvebombshortword.putExtra("scriptnm", script_key);
                                            intent_solvebombshortword.putExtra("user1", user1_key);
                                            intent_solvebombshortword.putExtra("user2", user2_key);
                                            intent_solvebombshortword.putExtra("user3", user3_key);
                                            intent_solvebombshortword.putExtra("user4", user4_key);
                                            intent_solvebombshortword.putExtra("user5", user5_key);
                                            intent_solvebombshortword.putExtra("roomname", roomname_key);
                                            intent_solvebombshortword.putExtra("state", state_key);
                                            intent_solvebombshortword.putExtra("question", question_key);
                                            intent_solvebombshortword.putExtra("answer", answer_key);
                                            startActivity(intent_solvebombshortword);
                                            break;

                                        } else if (type_key.equals("choice")) {
                                            answer_key = postSnapshot.child("answer").getValue().toString();
                                            ans1_key = postSnapshot.child("answer1").getValue().toString();
                                            ans2_key = postSnapshot.child("answer2").getValue().toString();
                                            ans3_key = postSnapshot.child("answer3").getValue().toString();
                                            ans4_key = postSnapshot.child("answer4").getValue().toString();
                                            Intent intent_solvebombchoice = new Intent(QuizbucketMain.this, SvQuizbucketchoice.class);
                                            intent_solvebombchoice.putExtra("timestamp", timestamp_key);
                                            intent_solvebombchoice.putExtra("id", id_key);
                                            intent_solvebombchoice.putExtra("nickname", nickname_key);
                                            intent_solvebombchoice.putExtra("scriptnm", script_key);
                                            intent_solvebombchoice.putExtra("user1", user1_key);
                                            intent_solvebombchoice.putExtra("user2", user2_key);
                                            intent_solvebombchoice.putExtra("user3", user3_key);
                                            intent_solvebombchoice.putExtra("user4", user4_key);
                                            intent_solvebombchoice.putExtra("user5", user5_key);
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
                        qaPostReference.addListenerForSingleValueEvent(findQna);
                    }
                } else if (check_gamelist == 0) {
                    Toast.makeText(QuizbucketMain.this, "게임방을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gameRoomInfos.clear();
        getFirebaseDatabase();
    }

    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GameListAdapter gameListAdapter = new GameListAdapter();
                    //String roomname, String withwhom, String status
                    String roomname = "", withwhom = "", status = "";
                    String lastperson = "", solveperson = "";
                    gameRoomInfos.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String user1 = postSnapshot.child("user1").getValue().toString();
                        String user2 = postSnapshot.child("user2").getValue().toString();
                        String user3 = postSnapshot.child("user3").getValue().toString();
                        String user4 = postSnapshot.child("user4").getValue().toString();
                        String user5 = postSnapshot.child("user5").getValue().toString();
                        String temp_status = postSnapshot.child("state").getValue().toString();
                        char bombcount;
                        if (!temp_status.equals("win") && !temp_status.equals("win1") && !temp_status.equals("win2")) { //gaming0~6
                            bombcount = temp_status.charAt(6);
                        } else { //win, win1, win2
                            bombcount = 'x';
                        }
                        if (bombcount == '0') {
                            lastperson = "none";
                            solveperson = "none";
                        } else if (bombcount != 'x') {
                            lastperson = postSnapshot.child("quiz_list").child("quiz" + bombcount).child("last").getValue().toString();
                            solveperson = postSnapshot.child("quiz_list").child("quiz" + bombcount).child("solve").getValue().toString();
                        }

                        if (user1.equals(nickname_key)) { //내가 user1
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            //data.add(get.roomname + "-" + get.user2);
                            withwhom = get.user2;
                            roomname = get.roomname;

                            if (bombcount == 'x' && temp_status.equals("win")) { //ok
                                status = "end";
                            } else if (bombcount == 'x' && temp_status.equals("win1")) { //ok
                                status = "iwin";
                            } else if (bombcount == 'x' && temp_status.equals("win2")) { //ok
                                status = "youwin";
                            } else if (bombcount == '0') {
                                status = "makeplease";
                            } else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status = "myturn"; //내가 폭탄 만들 차례
                            } else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status = "newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            } else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status = "elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            } else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status = "elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            } else {
                                Log.d("폭탄에러났슈user1", "에러유");
                            }
                            GameRoomInfo roomInfo = new GameRoomInfo(roomname, withwhom, status);
                            gameRoomInfos.add(roomInfo);
                            gameListAdapter.addItem(roomname, withwhom, status);
                        } else if (user2.equals(nickname_key)) { //내가 user2
                            FirebasePost_list get = postSnapshot.getValue(FirebasePost_list.class);
                            //data.add(get.roomname + "-" + get.user1);
                            withwhom = get.user1;
                            roomname = get.roomname;

                            if (bombcount == 'x' && temp_status.equals("win")) { //ok
                                status = "end";
                            } else if (bombcount == 'x' && temp_status.equals("win1")) { //ok
                                status = "youwin";
                            } else if (bombcount == 'x' && temp_status.equals("win2")) { //ok
                                status = "iwin";
                            } else if (bombcount == '0') {
                                status = "makeplease";
                            } else if (!lastperson.equals(nickname_key) && solveperson.equals(nickname_key)) {
                                status = "myturn"; //내가 폭탄 만들 차례
                            } else if (!lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status = "newbomb"; //새 폭탄이 도착했어요 (내가 폭탄을 풀 차례)
                            } else if (lastperson.equals(nickname_key) && solveperson.equals("none")) { //ok
                                status = "elsenotsolve"; //친구가 폭탄을 아직 풀지 않음
                            } else if (lastperson.equals(nickname_key) && !solveperson.equals("none")) { //ok
                                status = "elsenotmake"; //친구가 폭탄을 아직 만들지 않음
                            } else {
                                Log.d("폭탄에러났슈user2", "에러유");
                            }
                            GameRoomInfo roomInfo = new GameRoomInfo(roomname, withwhom, status);
                            gameRoomInfos.add(roomInfo);
                            gameListAdapter.addItem(roomname, withwhom, status);
                        }
                    }
                    gamelist.setAdapter(gameListAdapter);
                    gamelist.clearChoices();
                    gameListAdapter.notifyDataSetChanged();
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m2PostReference.addValueEventListener(postListener);
    }
}