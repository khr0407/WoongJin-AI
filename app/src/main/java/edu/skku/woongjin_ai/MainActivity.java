package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainQuizTypeFragment.OnFragmentInteractionListener{

    Intent intent, intentBook, intentChatlist, intentMyPage;
    String id;
    ImageButton bookButton, quizButton, gameButton, myPageButton;
    TextView textView;
    MainQuizTypeFragment mainQuizTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookButton = (ImageButton) findViewById(R.id.ReadActivity);
        quizButton = (ImageButton) findViewById(R.id.QuizActivity);
        gameButton = (ImageButton) findViewById(R.id.GameActivity);
        myPageButton = (ImageButton) findViewById(R.id.myPage);
        textView = (TextView) findViewById(R.id.main);
        mainQuizTypeFragment = new MainQuizTypeFragment();

        intent = getIntent();
        id = intent.getStringExtra("id");

        textView.setText("안녕 " + id + "!\n여행하고 싶은 나라를 골라보자!");

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
}
