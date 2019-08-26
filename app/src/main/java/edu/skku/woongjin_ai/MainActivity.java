package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    ListView mListView;
    public DatabaseReference mPostReference;
    ArrayList<String> scriptList, backgroundList;
    ArrayAdapter<String> adapter;
    Intent intent, intentBook, intentQuiz, intentGame, intentMyPage;
    ArrayAdapter<String> scriptAdapter;
    String id;
    String check = "";
    ImageButton bookButton, quizButton, gameButton, myPageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookButton = (ImageButton) findViewById(R.id.ReadActivity);
        quizButton = (ImageButton) findViewById(R.id.QuizActivity);
        gameButton = (ImageButton) findViewById(R.id.GameActivity);
        myPageButton = (ImageButton) findViewById(R.id.myPage);

        intent = getIntent();
        id = intent.getStringExtra("id");

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

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
//                intentType.putExtra("scriptnm", scriptList.get(position));
//                intentType.putExtra("background", backgroundList.get(position));
//                intentReadScript.putExtra("scriptnm", scriptList.get(position));
//                intentReadScript.putExtra("background", backgroundList.get(position));
//                check = intentType.getStringExtra("scriptnm");
//            }
//        });
    }
}
