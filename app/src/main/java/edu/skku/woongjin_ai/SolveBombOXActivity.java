package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
    DatabaseReference mPostReference;
    Intent intent;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key, question_key, answer_key;
    char bomb_cnt;
    TextView timer, gamers, question;
    ImageView imageO, imageX;
    ImageButton imageButtonScript;
    Button imageButtonCheck;
    String user_answer;
    int flagAO = 0, flagAX = 0;
    Fragment showScriptFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solvebombox);

        timer = (TextView) findViewById(R.id.timer);
        gamers = (TextView) findViewById(R.id.gamers);
        question = (TextView) findViewById(R.id.question);
        imageO = (ImageView) findViewById(R.id.otype);
        imageX = (ImageView) findViewById(R.id.xtype);
        imageButtonScript = (ImageButton) findViewById(R.id.script);
        imageButtonCheck = (Button)findViewById(R.id.check);

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

        bomb_cnt = state_key.charAt(6);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("gameroom_list").child(timestamp_key).child("quiz_list");

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
                    Toast.makeText(SolveBombOXActivity.this, "정답을 입력하세요.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SolveBombOXActivity.this, "다시 시도해보세요.", Toast.LENGTH_SHORT).show();
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
                    transaction.replace(R.id.contentShowScriptOX, showScriptFragment);
                    Bundle bundle = new Bundle(2);
                    bundle.putString("scriptnm", script_key);
                    bundle.putString("type", "ox");
                    showScriptFragment.setArguments(bundle);
                    transaction.addToBackStack(null);
                    transaction.commit();
            }
        });
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
