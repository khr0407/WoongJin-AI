package edu.skku.woongjin_ai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ShowQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);
    }

    public void btn1(View v){
        Intent intent001 = new Intent(this, LikeAndCommentsActivity.class);
        startActivity(intent001);
    }
}
