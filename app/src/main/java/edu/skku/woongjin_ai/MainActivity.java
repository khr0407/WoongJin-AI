package edu.skku.woongjin_ai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Intent intent, intentBook, intentQuiz, intentGame, intentMyPage;
    String id;
    ImageButton bookButton, quizButton, gameButton, myPageButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookButton = (ImageButton) findViewById(R.id.ReadActivity);
        quizButton = (ImageButton) findViewById(R.id.QuizActivity);
        gameButton = (ImageButton) findViewById(R.id.GameActivity);
        myPageButton = (ImageButton) findViewById(R.id.myPage);
        textView = (TextView) findViewById(R.id.main);

        intent = getIntent();
        id = intent.getStringExtra("id");

        textView.setText("안녕 " + id + "!\n여행하고 싶은 나라를 골라보자!");

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBook = new Intent(MainActivity.this, NationBookActivity.class);
                intentBook.putExtra("id", id);
                startActivity(intentBook);
                finish();
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGame = new Intent(MainActivity.this, NationGameActivity.class);
                intentGame.putExtra("id", id);
                startActivity(intentGame);
                finish();
            }
        });

        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMyPage = new Intent(MainActivity.this, MyPageActivity.class);
                intentMyPage.putExtra("id", id);
                startActivity(intentMyPage);
                finish();
            }
        });
    }
}
