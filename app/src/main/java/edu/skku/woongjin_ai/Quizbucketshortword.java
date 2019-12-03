package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Quizbucketshortword extends AppCompatActivity
        implements ShowScriptFragment.OnFragmentInteractionListener {

    DatabaseReference mPostReference;
    EditText editQuiz, editDiff, editAns;
    Intent intent, intentHome, intentGameList;
    String id_key, scriptnm, backgroundID;
    String timestamp_key, nickname_key, user1_key, user2_key, bucketcnt_key, roomname_key, state_key;
    char quizcnt;
    String quiz = "", diff = "", ans = "";
    ImageButton checkButton, scriptButton;
    Fragment showScriptFragment;
    ImageView imageHome;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizbucketshortword);

        intent = getIntent();
        id_key = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");
        backgroundID = intent.getStringExtra("background");
        timestamp_key = intent.getStringExtra("timestamp");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        bucketcnt_key = intent.getStringExtra("bucketcnt");
        state_key = intent.getStringExtra("state");
        roomname_key = intent.getStringExtra("roomname");

        imageHome = (ImageView) findViewById(R.id.home);
        editQuiz = (EditText) findViewById(R.id.quiz);
        editDiff = (EditText) findViewById(R.id.diff);
        editAns = (EditText) findViewById(R.id.ans);
        title = (TextView) findViewById(R.id.title);
        checkButton = (ImageButton) findViewById(R.id.check);
        scriptButton = (ImageButton) findViewById(R.id.script);

        title.setText("지문 제목: " + scriptnm);
        quizcnt = state_key.charAt(6);

        mPostReference = FirebaseDatabase.getInstance().getReference().child("chatroom_bucket_list");

        scriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScriptFragment = new ShowScriptFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contents3, showScriptFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("scriptnm", scriptnm);
                bundle.putString("type", "makebombshortword");
                showScriptFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = editQuiz.getText().toString();
                diff = editDiff.getText().toString();
                ans = editAns.getText().toString();

                if(quiz.length() == 0 || ans.length() == 0) {
                    Toast.makeText(Quizbucketshortword.this, "Fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else {
                    postFirebaseDatabaseQuizShortword();
                    Toast.makeText(Quizbucketshortword.this, "출제 완료!", Toast.LENGTH_SHORT).show();
                    intentGameList = new Intent(Quizbucketshortword.this, QuizbucketMain.class);
                    intentGameList.putExtra("id", id_key);
                    intentGameList.putExtra("nickname", nickname_key);
                    startActivity(intentGameList);
                    finish();
                }
            }
        });

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(Quizbucketshortword.this, MainActivity.class);
                intentHome.putExtra("id", id_key);
                startActivity(intentHome);
                finish();
            }
        });
    }

    private void postFirebaseDatabaseQuizShortword() {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Firebase_QBOXShortword post = new Firebase_QBOXShortword(quiz, diff, ans, "shortword", "none", nickname_key);
        postValues = post.toMap();
        int temp_cnt = quizcnt - '0' + 1;
        childUpdates.put("quiz"+temp_cnt, postValues);
        mPostReference.child(timestamp_key).child("quiz_list").updateChildren(childUpdates);
        mPostReference.child(timestamp_key).child("state").setValue("gaming"+temp_cnt);
        mPostReference.child(timestamp_key).child("bucketcnt").setValue("bucket"+temp_cnt);
        editQuiz.setText("");
        editDiff.setText("");
        editAns.setText("");
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}