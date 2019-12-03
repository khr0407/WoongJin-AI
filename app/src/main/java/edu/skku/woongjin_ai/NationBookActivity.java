package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/*
from MainActivity
독서나라
지문 카테고리 고르기
 */

public class NationBookActivity extends AppCompatActivity {

    Intent intent, intentHome, intentSelectBook;
    String id, nickname, thisWeek;
    ImageButton homeButton;
    ImageButton scienceButton, historyButton, newsButton, moralityButton, misteryButton, comicsButton, oldstoryButton, greatmanButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationbook);

        intent = getIntent();
        id = intent.getStringExtra("id");
        nickname = intent.getStringExtra("nickname");
        thisWeek = intent.getStringExtra("thisWeek");

        intentSelectBook = new Intent(NationBookActivity.this, SelectBookActivity.class);
        intentSelectBook.putExtra("id", id);
        intentSelectBook.putExtra("nickname", nickname);
        intentSelectBook.putExtra("thisWeek", thisWeek);

        homeButton = (ImageButton) findViewById(R.id.home);
        scienceButton = (ImageButton) findViewById(R.id.science);
        historyButton = (ImageButton) findViewById(R.id.history);
        newsButton = (ImageButton) findViewById(R.id.news);
        moralityButton = (ImageButton) findViewById(R.id.morality);
        misteryButton = (ImageButton) findViewById(R.id.mistery);
        comicsButton = (ImageButton) findViewById(R.id.comics);
        oldstoryButton = (ImageButton) findViewById(R.id.oldstory);
        greatmanButton = (ImageButton) findViewById(R.id.greatman);

        // 과학 카테고리 버튼 이벤트
        scienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "science");
                startActivity(intentSelectBook);
            }
        });

        // 역사 카테고리 버튼 이벤트
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "history");
                startActivity(intentSelectBook);
            }
        });

        // 시사 카테고리 버튼 이벤트
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "news");
                startActivity(intentSelectBook);
            }
        });

        // 도덕 카테고리 버튼 이벤트
        moralityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "morality");
                startActivity(intentSelectBook);
            }
        });

        // 미스터리 카테고리 버튼 이벤트
        misteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "mistery");
                startActivity(intentSelectBook);
            }
        });

        // 웃긴 이야기 카테고리 버튼 이벤트
        comicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "comics");
                startActivity(intentSelectBook);
            }
        });

        // 전래동화 카테고리 버튼 이벤트
        oldstoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "oldstory");
                startActivity(intentSelectBook);
            }
        });

        // 위인전 카테고리 버튼 이벤트
        greatmanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "greatman");
                startActivity(intentSelectBook);
            }
        });

        // 메인페이지 가기 버튼 이벤트
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(NationBookActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
            }
        });
    }
}
