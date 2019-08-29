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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements MainQuizTypeFragment.OnFragmentInteractionListener{
    public DatabaseReference mPostReference;


    Intent intent, intentBook, intentChatlist, intentMyPage;
    String id;
    ImageButton bookButton, quizButton, gameButton, myPageButton;
    TextView userNickname;
    MainQuizTypeFragment mainQuizTypeFragment;
    UserInfo me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        bookButton = (ImageButton) findViewById(R.id.ReadActivity);
        quizButton = (ImageButton) findViewById(R.id.QuizActivity);
        gameButton = (ImageButton) findViewById(R.id.GameActivity);
        myPageButton = (ImageButton) findViewById(R.id.myPage);
        userNickname = (TextView) findViewById(R.id.main);
        mainQuizTypeFragment = new MainQuizTypeFragment();

        intent = getIntent();
        id = intent.getStringExtra("id");

        //textView.setText("안녕 " + id + "!\n여행하고 싶은 나라를 골라보자!");

        getFirebaseDatabaseUserInfo();

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBook = new Intent(MainActivity.this, NationBookActivity.class);
                intentBook.putExtra("id", id);
                startActivity(intentBook);
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainQuizSelect, mainQuizTypeFragment);
                Bundle bundle = new Bundle(1);
                bundle.putString("id", id);
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
                startActivity(intentChatlist);
            }
        });

        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(MainActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                startActivity(intentMyPage);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals("kakaouser_list") || key.equals("user_list")) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if(key1.equals(id)) {
                                me = snapshot1.getValue(UserInfo.class);
                                //userNickname.setText(me.name);
                                userNickname.setText("안녕 " + me.nickname + "!\n여행하고 싶은 나라를 골라보자!");

                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
