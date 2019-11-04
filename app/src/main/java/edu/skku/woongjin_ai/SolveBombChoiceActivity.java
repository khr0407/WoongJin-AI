package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SolveBombChoiceActivity extends AppCompatActivity {
    DatabaseReference mPostReference;
    Intent intent;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key, question_key, answer_key;
    String ans1_key, ans2_key, ans3_key, ans4_key;
    char bomb_cnt;
    TextView roomname, gamers, question;
    ImageButton imageButtonScript;
    Button imageButtonCheck;
    String user_answer;
    TextView textViewAns1, textViewAns2, textViewAns3, textViewAns4;
    int flagA1 = 0, flagA2 = 0, flagA3 = 0, flagA4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvebombchoice);

        roomname = (TextView) findViewById(R.id.roomname);
        gamers = (TextView) findViewById(R.id.gamers);
        question = (TextView) findViewById(R.id.question);
        imageButtonScript = (ImageButton) findViewById(R.id.script);
        imageButtonCheck = (Button)findViewById(R.id.check);

        textViewAns1 = (TextView) findViewById(R.id.ans1);
        textViewAns2 = (TextView) findViewById(R.id.ans2);
        textViewAns3 = (TextView) findViewById(R.id.ans3);
        textViewAns4 = (TextView) findViewById(R.id.ans4);

        intent = getIntent();
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

        ans1_key = intent.getStringExtra("answer1");
        ans2_key = intent.getStringExtra("answer2");
        ans3_key = intent.getStringExtra("answer3");
        ans4_key = intent.getStringExtra("answer4");

        bomb_cnt = state_key.charAt(6);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");

        roomname.setText(roomname_key);
        gamers.setText(user1_key + " vs " + user2_key);
        question.setText(question_key);

        textViewAns1.setText(ans1_key);
        textViewAns2.setText(ans2_key);
        textViewAns3.setText(ans3_key);
        textViewAns4.setText(ans4_key);

        textViewAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA1 == 0) {
                    user_answer = ans1_key;
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA1 = 1;
                    flagA2 = flagA3 = flagA4 = 0;
                } else {
                    user_answer = "";
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA1 = 0;
                }
            }
        });

        textViewAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA2 == 0){
                    user_answer = ans2_key;
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA2 = 1;
                    flagA1 = flagA3 = flagA4 = 0;
                } else {
                    user_answer = "";
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA2 = 0;
                }
            }
        });

        textViewAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA3 == 0){
                    user_answer = ans3_key;
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA3 = 1;
                    flagA2 = flagA1 = flagA4 = 0;
                } else {
                    user_answer = "";
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA3 = 0;
                }
            }
        });

        textViewAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagA4 == 0){
                    user_answer = ans4_key;
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_correct);
                    textViewAns2.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns3.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    textViewAns1.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA4 = 1;
                    flagA2 = flagA3 = flagA1 = 0;
                } else {
                    user_answer = "";
                    textViewAns4.setBackgroundResource(R.drawable.ic_icons_selector_standard);
                    flagA4 = 0;
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        imageButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ValueEventListener check = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String quiznum = postSnapshot.getKey();
                            if (quiznum.contains("quiz" + bomb_cnt)) {
                                mPostReference.child(quiznum).child("state").setValue(nickname_key);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                };
                mPostReference.addValueEventListener(check);

                if (user_answer.equals("")) {
                    Toast.makeText(SolveBombChoiceActivity.this, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user_answer.equals(answer_key)) {
                        if (bomb_cnt == '6') {
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
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
                            transaction.commit();
                        }
                    }
                    else if (!user_answer.equals(answer_key)) {
                        Toast.makeText(SolveBombChoiceActivity.this, "다시 시도해보세요.", Toast.LENGTH_SHORT).show();
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

            }
        });

    }
}
