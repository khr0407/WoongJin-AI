package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EndBombFragment extends AppCompatActivity {
    Intent intent, intent_gamelist;
    private DatabaseReference mPostReference;
    Button end;
    String id_key, nickname_key, user1_key, user2_key;
    int check1, check2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bombend);

        intent = getIntent();
        intent_gamelist = new Intent(EndBombFragment.this, GameListActivity.class);
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
        end = (Button) findViewById(R.id.end);
        id_key = intent.getStringExtra("id");
        nickname_key = intent.getStringExtra("nickname");
        user1_key = intent.getStringExtra("user1");
        user2_key = intent.getStringExtra("user2");
        check1 = 0;
        check2 = 0;

        final ValueEventListener findgamers = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String gamer_nickname = postSnapshot.child("nickname").getValue().toString();
                    String gamer_coin = postSnapshot.child("coin").getValue().toString();
                    if (check1 == 0 && gamer_nickname.equals(user1_key)) {
                        int coin = Integer.parseInt(gamer_coin) + 150;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child(key).child("coin").setValue(coin_convert);
                        check1 = 1;
                    }
                    if (check2 == 0 && gamer_nickname.equals(user2_key)) {
                        int coin = Integer.parseInt(gamer_coin) + 150;
                        String coin_convert = Integer.toString(coin);
                        mPostReference.child(key).child("coin").setValue(coin_convert);
                        check2 = 1;
                    }
                    if (check1 == 1 && check2 == 1) {
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mPostReference.addListenerForSingleValueEvent(findgamers);

        end.setOnClickListener(new View.OnClickListener() {
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