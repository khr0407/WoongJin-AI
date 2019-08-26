package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class NationQuizActivity extends AppCompatActivity {

    Intent intent, intentHome;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationquiz);

        intent = getIntent();
        id = intent.getStringExtra("id");
    }
}
