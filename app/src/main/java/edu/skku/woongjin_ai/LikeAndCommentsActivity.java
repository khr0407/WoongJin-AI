package edu.skku.woongjin_ai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LikeAndCommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_and_comments);
    }

    public void btn2(View v){
        Intent intent002 = new Intent(this, AnswerActivity.class);
        startActivity(intent002);
    }
}
