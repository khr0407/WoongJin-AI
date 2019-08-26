package edu.skku.woongjin_ai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShowQuestionActivity extends AppCompatActivity {
    public DatabaseReference mPostReference;
    Intent intent;
    String script_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);

        intent = getIntent();
        script_name = intent.getStringExtra("scriptnm");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("quiz_list");
    }
}