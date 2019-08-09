package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MyPageActivity extends AppCompatActivity {
    Intent intent, intentType;
    String id;
    Button enter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        intent = getIntent();
        id = intent.getStringExtra("id");
        enter = findViewById(R.id.friendList);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String find_id = id;
                Intent test2 = new Intent(MyPageActivity.this, ScriptActivity.class);
                test2.putExtra("id", find_id);
                startActivity(test2);
            }
        });

    }


}
