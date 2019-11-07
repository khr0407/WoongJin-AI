package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CorrectBombFragment extends AppCompatActivity {
    Intent intent, intent_makebombtype, intent_gamelist;
    private DatabaseReference mPostReference;
    ImageButton send;
    TextView textCheckCorrect;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bombcorrect);

        intent = getIntent();
        intent_makebombtype = new Intent(CorrectBombFragment.this, MakeBombTypeActivity.class);
        intent_gamelist = new Intent(CorrectBombFragment.this, GameListActivity.class);

        send = (ImageButton) findViewById(R.id.send);
        textCheckCorrect = (TextView) findViewById(R.id.textCheck);

        timestamp_key = intent.getStringExtra("timestamp");
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        roomname_key = intent.getStringExtra("roomname");
        script_key = intent.getStringExtra("scriptnm");
        state_key = intent.getStringExtra("state");

        textCheckCorrect.setText("우와!!! " + id_key + "이(가) 정답을 맞췄어!!!");
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ValueEventListener findgamers = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            String gamer_coin = postSnapshot.child("coin").getValue().toString();
                            if (key.equals(id_key)) {
                                int coin = Integer.parseInt(gamer_coin) + 50;
                                String coin_convert = Integer.toString(coin);
                                mPostReference.child(key).child("coin").setValue(coin_convert);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                mPostReference.addListenerForSingleValueEvent(findgamers);

                intent_gamelist.putExtra("id", id_key);
                intent_gamelist.putExtra("nickname", nickname_key);
                startActivity(intent_gamelist);
                finish();

                /*intent_makebombtype.putExtra("timestamp", timestamp_key);
                intent_makebombtype.putExtra("id", id_key);
                intent_makebombtype.putExtra("nickname", nickname_key);
                intent_makebombtype.putExtra("user1", user1_key);
                intent_makebombtype.putExtra("user2", user2_key);
                intent_makebombtype.putExtra("roomname", roomname_key);
                intent_makebombtype.putExtra("scriptnm", script_key);
                intent_makebombtype.putExtra("state", state_key);
                startActivity(intent_makebombtype);
                finish();*/
            }
        });
    }
}
