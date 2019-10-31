package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class NationBookActivity extends AppCompatActivity {

    Intent intent, intentHome, intentSelectBook;
    String id;
    ImageButton homeButton;
    ImageButton scienceButton, historyButton, newsButton, moralityButton, misteryButton, comicsButton, oldstoryButton, greatmanButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationbook);

        intent = getIntent();
        id = intent.getStringExtra("id");
        intentSelectBook = new Intent(NationBookActivity.this, SelectBookActivity.class);
        intentSelectBook.putExtra("id", id);

        homeButton = (ImageButton) findViewById(R.id.home);
        scienceButton = (ImageButton) findViewById(R.id.science);
        historyButton = (ImageButton) findViewById(R.id.history);
        newsButton = (ImageButton) findViewById(R.id.news);
        moralityButton = (ImageButton) findViewById(R.id.morality);
        misteryButton = (ImageButton) findViewById(R.id.mistery);
        comicsButton = (ImageButton) findViewById(R.id.comics);
        oldstoryButton = (ImageButton) findViewById(R.id.oldstory);
        greatmanButton = (ImageButton) findViewById(R.id.greatman);

        scienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "science");
                startActivity(intentSelectBook);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "history");
                startActivity(intentSelectBook);
            }
        });

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "news");
                startActivity(intentSelectBook);
            }
        });

        moralityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "morality");
                startActivity(intentSelectBook);
            }
        });

        misteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "mistery");
                startActivity(intentSelectBook);
            }
        });

        comicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "comics");
                startActivity(intentSelectBook);
            }
        });

        oldstoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "oldstory");
                startActivity(intentSelectBook);
            }
        });

        greatmanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSelectBook.putExtra("bookType", "greatman");
                startActivity(intentSelectBook);
            }
        });

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
