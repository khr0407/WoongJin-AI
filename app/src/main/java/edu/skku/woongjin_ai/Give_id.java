package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Give_id extends AppCompatActivity {
    EditText id;
    Button enter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveid);
        id = findViewById(R.id.name);
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String find_id = id.getText().toString();
                Intent test2 = new Intent(Give_id.this, ScriptActivity.class);
                test2.putExtra("id", find_id);

                // Toast.makeText(getApplicationContext(), "로그인되었습니다.",Toast.LENGTH_SHORT).show();

                startActivity(test2);
            }
        });
    }
}