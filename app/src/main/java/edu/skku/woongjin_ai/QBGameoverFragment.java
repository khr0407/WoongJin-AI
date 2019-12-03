package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QBGameoverFragment extends AppCompatActivity {
    Intent intent, intent_gamelist;
    private DatabaseReference mPostReference;
    Button quit;
    String id_key, nickname_key, user1_key, user2_key;
    int check1, check2;
    String loser_nickname, winner_nickname;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qbgameover);

        intent = getIntent();
        intent_gamelist = new Intent(QBGameoverFragment.this, QuizbucketMain.class);
        quit = (Button) findViewById(R.id.quit);
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        check1 = 0;
        check2 = 0;

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_gamelist.putExtra("id", id_key);
                intent_gamelist.putExtra("nickname", nickname_key);
                startActivity(intent_gamelist);
                finish();
            }
        });
    }
}