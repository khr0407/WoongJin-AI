package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements MainQuizTypeFragment.OnFragmentInteractionListener{

    public DatabaseReference mPostReference;
    Intent intent, intentBook, intentChatlist, intentMyPage;
    String id, nickname = "";
    LinearLayout bookButton, quizButton, gameButton;
    Button myPageButton;
    TextView userNickname;
    MainQuizTypeFragment mainQuizTypeFragment;
    UserInfo me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookButton = (LinearLayout) findViewById(R.id.ReadActivity);
        quizButton = (LinearLayout) findViewById(R.id.QuizActivity);
        gameButton = (LinearLayout) findViewById(R.id.GameActivity);
        myPageButton = (Button) findViewById(R.id.myPage);
        userNickname = (TextView) findViewById(R.id.main);
        mainQuizTypeFragment = new MainQuizTypeFragment();

        intent = getIntent();
        id = intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        getFirebaseDatabaseUserInfo();

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBook = new Intent(MainActivity.this, NationBookActivity.class);
                intentBook.putExtra("id", id);
                intentBook.putExtra("nickname", nickname);
                startActivity(intentBook);
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainQuizSelect, mainQuizTypeFragment);
                Bundle bundle = new Bundle(2);
                bundle.putString("id", id);
                bundle.putString("nickname", nickname);
                mainQuizTypeFragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentChatlist = new Intent(MainActivity.this, ChatListActivity.class);
                intentChatlist.putExtra("id", id);
                intentChatlist.putExtra("nickname", nickname);
                startActivity(intentChatlist);
            }
        });

        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(MainActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                intentMyPage.putExtra("nickname", nickname);
                startActivity(intentMyPage);
            }
        });
    }

    private void getFirebaseDatabaseUserInfo() {
        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                me = dataSnapshot.child("user_list/" + id).getValue(UserInfo.class);
                nickname = me.nickname;
                userNickname.setText("안녕 " + nickname + "!\n여행하고 싶은 나라를 골라보자!");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
