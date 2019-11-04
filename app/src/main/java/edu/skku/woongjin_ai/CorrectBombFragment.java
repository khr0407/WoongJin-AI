package edu.skku.woongjin_ai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CorrectBombFragment extends Fragment {
    private Context context;
    private DatabaseReference mPostReference;
    ImageButton send;
    String timestamp_key, id_key, nickname_key, user1_key, user2_key, roomname_key, script_key, state_key;
    TextView textCheckCorrect;

    public CorrectBombFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bombcorrect, container, false);

        context = container.getContext();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("user_list");
        send = (ImageButton) view.findViewById(R.id.send);
        textCheckCorrect = (TextView) view.findViewById(R.id.textCheckCorrect);
        timestamp_key = getArguments().getString("timestamp");
        id_key = getArguments().getString("id");
        nickname_key = getArguments().getString("nickname");
        user1_key = getArguments().getString("user1");
        user2_key = getArguments().getString("user2");
        roomname_key = getArguments().getString("roomname");
        script_key = getArguments().getString("scriptnm");
        state_key = getArguments().getString("state");

        textCheckCorrect.setText("우와!!! " + nickname_key + "이(가) 정답을 맞췄어!!!");

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

                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.beginTransaction().remove(CorrectBombFragment.this).commit();
                manager.popBackStack();
                Intent intent_makebombtype = new Intent(getActivity(), MakeBombTypeActivity.class);
                intent_makebombtype.putExtra("timestamp", timestamp_key);
                intent_makebombtype.putExtra("id", id_key);
                intent_makebombtype.putExtra("nickname", nickname_key);
                intent_makebombtype.putExtra("user1", user1_key);
                intent_makebombtype.putExtra("user2", user2_key);
                intent_makebombtype.putExtra("roomname", roomname_key);
                intent_makebombtype.putExtra("scriptnm", script_key);
                intent_makebombtype.putExtra("state", state_key);
                startActivity(intent_makebombtype);
            }
        });
        return view;
    }
}
