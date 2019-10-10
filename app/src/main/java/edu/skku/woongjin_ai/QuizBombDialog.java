package edu.skku.woongjin_ai;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizBombDialog extends Dialog {
    private DatabaseReference mPostReference, bPostReference;
    ArrayList<String> examiners;
    ArrayList<String> questions;
    ArrayList<String> answers;
    ArrayList<String> receivers;
    ArrayAdapter<String> arrayAdapter;

    Intent intent, intentType;
    String id;
    Button left ,right;
    String room_key_1, room_key_2;
    String roomname;
    TextView sender, receiver, answer, quizContent, bombCount;
    Context mContext;

    public QuizBombDialog(Context context, String room_name, String my_name ,String chat_with){
        super(context);
        this.mContext=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.activity_seebomb);     //다이얼로그에서 사용할 레이아웃입니다.

        mPostReference= FirebaseDatabase.getInstance().getReference().child("chatroom_list");

        examiners = new ArrayList<String>();
        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        receivers = new ArrayList<String>();

        room_key_1=my_name+"-"+chat_with+":"+room_name;
        room_key_2=chat_with+"-"+my_name+":"+room_name;

        Log.d("room_key_cal_1", room_key_1);
        Log.d("room_key_cal_2", room_key_2);

////        intentType = new Intent(mContext, SelectTypeActivity.class);
////        intentType.putExtra("id", id);
////        id_key=id;
//
        left=(Button)findViewById(R.id.previous);
        right=(Button)findViewById(R.id.next);
//
//
//
        getFirebaseDatabase();   //여기서 오류가 존나떠요!!
//
//
//        left.setOnClickListener(new View.OnClickListener() { //previous 질문 있다면 보기
//            public void onClick(View view) {
//
//            }
//        });
//
//        right.setOnClickListener(new View.OnClickListener() { //next 질문 있다면 보기
//            public void onClick(View view) {
//
//            }
//        });


        //팝업창 내에서 하는 활동들.............selecttype액티비티 불러오면될듯.
        //context.startActivity(intentType);

    }


    public void getFirebaseDatabase() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    examiners.clear();
                    questions.clear();
                    answers.clear();
                    receivers.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        roomname = postSnapshot.getKey();
                        if (roomname.equals(room_key_1)) {
                            bPostReference = FirebaseDatabase.getInstance().getReference().child("bomb_list");
                            getFirebaseBomb();
                            break;
                        } else if (roomname.equals(room_key_2)) {
                            bPostReference = FirebaseDatabase.getInstance().getReference().child("bomb_list");
                            getFirebaseBomb();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPostReference.addValueEventListener(postListener);
        }catch(java.lang.NullPointerException e){
        }
    }


    public void getFirebaseBomb() {
        try {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String question = postSnapshot.child("question").getValue().toString();
                        String answer = postSnapshot.child("answer").getValue().toString();
                        String receiver = postSnapshot.child("receiver").getValue().toString();
                        String examiner = postSnapshot.child("examiner").getValue().toString();

                        Log.d("질문받아온거?", question);


                        questions.add(question);
                        answers.add(answer);
                        receivers.add(receiver);
                        examiners.add(examiner);

                    }
                    Log.d("성공?", "ㅇㅇ ㅠㅠ");

//                    sender.setText(examiners.get(0));
//                    receiver.setText(receivers.get(0));
//                    answer.setText(answers.get(0));
//                    quizContent.setText(questions.get(0));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            bPostReference.addValueEventListener(postListener);
        }catch(java.lang.NullPointerException e){
        }
    }

}
