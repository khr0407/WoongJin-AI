package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolveBombOXActivity extends AppCompatActivity implements ShowScriptFragment.OnFragmentInteractionListener {
    DatabaseReference mPostReference, wPostReference;
    Intent intent, intent_correct;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key, question_key, answer_key;
    char bomb_cnt;
    TextView timer, gamers, question;
    ImageView imageO, imageX;
    ImageButton imageButtonScript;
    Button imageButtonCheck;
    String user_answer;
    int flagAO = 0, flagAX = 0;
    Fragment showScriptFragment;

    int second = 60;
    int correct_end = 0;
    int wrong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvebombox);

        timer = (TextView) findViewById(R.id.timer);
        mHandler.sendEmptyMessage(0);
        gamers = (TextView) findViewById(R.id.gamers);
        question = (TextView) findViewById(R.id.question);
        imageO = (ImageView) findViewById(R.id.otype);
        imageX = (ImageView) findViewById(R.id.xtype);
        imageButtonScript = (ImageButton) findViewById(R.id.script);
        imageButtonCheck = (Button)findViewById(R.id.check);

        intent = getIntent();
        intent_correct = new Intent(SolveBombOXActivity.this, CorrectBombFragment.class);
        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        script_key = intent.getStringExtra("scriptnm");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        roomname_key = intent.getStringExtra("roomname");
        state_key = intent.getStringExtra("state");
        question_key = intent.getStringExtra("question");
        answer_key = intent.getStringExtra("answer");

        bomb_cnt = state_key.charAt(6);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");
        wPostReference  = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key);

        timer.setText(roomname_key);
        gamers.setText(user1_key + " vs " + user2_key);
        question.setText(question_key);

        imageO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAO == 0) {
                    if(flagAX == 1) {
                        imageX.setImageResource(R.drawable.x_white);
                        flagAX = 0;
                    }
                    user_answer = "o";
                    imageO.setImageResource(R.drawable.o_orange);
                    flagAO = 1;
                } else {
                    user_answer = "";
                    imageO.setImageResource(R.drawable.o_white);
                    flagAO = 0;
                }
            }
        });

        imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagAX == 0) {
                    if(flagAO == 1) {
                        imageO.setImageResource(R.drawable.o_white);
                        flagAO = 0;
                    }
                    user_answer = "x";
                    imageX.setImageResource(R.drawable.x_orange);
                    flagAX = 1;
                } else {
                    user_answer = "";
                    imageX.setImageResource(R.drawable.x_white);
                    flagAX = 0;
                }
            }
        });

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_answer.equals("")) {
                    Toast.makeText(SolveBombOXActivity.this, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user_answer.equals(answer_key)) {
                        final ValueEventListener check = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String quiznum = postSnapshot.getKey();
                                    if (quiznum.equals("quiz" + bomb_cnt)) {
                                        mPostReference.child(quiznum).child("solve").setValue(nickname_key);
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        };
                        mPostReference.addListenerForSingleValueEvent(check);

                        correct_end = 1;

                        if (bomb_cnt == '6') {
                            wPostReference.child("state").setValue("win");
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            EndBombFragment fragment = new EndBombFragment();
                            Bundle bundle = new Bundle(4);
                            bundle.putString("id", id_key);
                            bundle.putString("nickname", nickname_key);
                            bundle.putString("user1", user1_key);
                            bundle.putString("user2", user2_key);
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.contents, fragment);
                            transaction.commit();
                        }
                        else if (bomb_cnt != '6') {
                            intent_correct.putExtra("timestamp", timestamp_key);
                            intent_correct.putExtra("id", id_key);
                            intent_correct.putExtra("nickname", nickname_key);
                            intent_correct.putExtra("user1", user1_key);
                            intent_correct.putExtra("user2", user2_key);
                            intent_correct.putExtra("roomname", roomname_key);
                            intent_correct.putExtra("scriptnm", script_key);
                            intent_correct.putExtra("state", state_key);
                            startActivity(intent_correct);
                            finish();
                            /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            CorrectBombFragment fragment = new CorrectBombFragment();
                            Bundle bundle = new Bundle(8);
                            bundle.putString("timestamp", timestamp_key);
                            bundle.putString("id", id_key);
                            bundle.putString("nickname", nickname_key);
                            bundle.putString("user1", user1_key);
                            bundle.putString("user2", user2_key);
                            bundle.putString("roomname", roomname_key);
                            bundle.putString("scriptnm", script_key);
                            bundle.putString("state", state_key);
                            fragment.setArguments(bundle);
                            transaction.replace(R.id.contents, fragment);
                            transaction.commit();*/
                        }
                    }
                    else if (!user_answer.equals(answer_key)) {
                        wrong = 1;
                        if (nickname_key.equals(user1_key)) {
                            wPostReference.child("state").setValue("win2");
                        }
                        else if (nickname_key.equals(user2_key)) {
                            wPostReference.child("state").setValue("win1");
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        WrongBombFragment fragment = new WrongBombFragment();
                        Bundle bundle = new Bundle(4);
                        bundle.putString("id", id_key);
                        bundle.putString("nickname", nickname_key);
                        bundle.putString("user1", user1_key);
                        bundle.putString("user2", user2_key);
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.contents, fragment);
                        transaction.commit();
                }
                }
            }
        });

        imageButtonScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contents, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", script_key);
                bundle.putString("type", "solvebombox");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            second--;
            timer.setText("00 :  " + second);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            mHandler.sendEmptyMessageDelayed(0,1000);

            if (second == 0 && correct_end == 0 && wrong == 0) { //correct_end 정답일 때 1로 바뀜
                if (nickname_key.equals(user1_key)) {
                    wPostReference.child("state").setValue("win2");
                }
                else if (nickname_key.equals(user2_key)) {
                    wPostReference.child("state").setValue("win1");
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                WrongBombFragment fragment = new WrongBombFragment();
                Bundle bundle = new Bundle(4);
                bundle.putString("id", id_key);
                bundle.putString("nickname", nickname_key);
                bundle.putString("user1", user1_key);
                bundle.putString("user2", user2_key);
                fragment.setArguments(bundle);
                transaction.replace(R.id.contents, fragment);
                transaction.commitAllowingStateLoss();
            }
            if (correct_end == 1) {} //답을 맞췄을 때
            if (wrong == 1) {} //답을 틀렸을 때
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}