package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class NationQuizActivity extends AppCompatActivity {

    Intent intent, intentHome;
    String id, quizType;
    TextView textView;
    ImageButton homeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
        quizType = intent.getStringExtra("quizType");

        textView = (TextView) findViewById(R.id.nationQuiz);
        homeButton = (ImageButton) findViewById(R.id.home);

        if(quizType.equals("me")) {
            textView.setText(id + "가 읽은 책 목록이야~\n추가로 문제를 내고 싶은 책을 클릭하면 문제를 만들 수 있어!\n연필 아이콘 개수는 " + id + "가 낸 문제 개수와 같아");
        } else if(quizType.equals("friend")) {
            textView.setText(id + "가 읽은 책 목록이야~\n책을 클릭하면 다른 친구들이 낸 문제를 풀어보고 평가할 수 있어!");
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(NationQuizActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }
}
