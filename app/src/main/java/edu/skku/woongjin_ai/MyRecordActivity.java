package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRecordActivity extends AppCompatActivity {

    public DatabaseReference mPostReference;
    Intent intent;
    String id;
    TextView userGrade, userSchool, userName, userCoin;
    TextView attend, script, myQnum, solveQnum, bombNum, bucketNum;
    ImageButton goHome;
    UserInfo me;

    Intent intentGoHome;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecord);

        intent=getIntent();
        id=intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();


        userName = (TextView) findViewById(R.id.userName);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade);
        userCoin = (TextView) findViewById(R.id.userCoin);
        attend=(TextView)findViewById(R.id.attendDays);
        script=(TextView)findViewById(R.id.scriptNum);
        myQnum=(TextView)findViewById(R.id.myQnum);
        solveQnum=(TextView)findViewById(R.id.solveQnum);
        bombNum=(TextView)findViewById(R.id.bombNum);
        bucketNum=(TextView)findViewById(R.id.bucketNum);
        goHome=(ImageButton)findViewById(R.id.home);


        getFirebaseDatabaseUserInfo();

        goHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intentGoHome=new Intent(MyRecordActivity.this, MainActivity.class);
                intentGoHome.putExtra("id", id);
                startActivity(intentGoHome);
            }
        });

    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("kakaouser_list") || key.equals("user_list")) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String key1 = snapshot1.getKey();
                            if (key1.equals(id)) {
                                me = snapshot1.getValue(UserInfo.class);
                                userName.setText(me.name+" 학생");
                                userGrade.setText(me.grade + "학년");
                                userCoin.setText(me.coin + " 코인");

                                for(DataSnapshot snapshot2:snapshot1.getChildren()){
                                    String key2=snapshot2.getKey();
                                    if(key2.equals("my_game_list")){
                                        String bomb, bucket;
                                        for(DataSnapshot snapshot3:snapshot2.getChildren()){
                                            String key3=snapshot3.getKey();
                                            if(key3.equals("bomb_cnt")){
                                                bomb=snapshot3.getValue().toString();
                                                bombNum.setText(bomb+" 회");
                                            }
                                            else{
                                                bucket=snapshot3.getValue().toString();
                                                bucketNum.setText(bucket+" 회");
                                            }
                                        }
                                    }
                                    else if(key2.equals("my_script_list")){
                                        long readcnt=0;
                                        long solvecnt=0;
                                        readcnt=snapshot2.getChildrenCount();
                                        for(DataSnapshot snapshot3:snapshot2.getChildren()){ //snapshot3에는 책 제목
                                            //친구 문제 풀어본 수
                                            for(DataSnapshot snapshot4:snapshot3.getChildren()){
                                                String key4=snapshot4.getKey();
                                                if(key4.equals("solved_list")){
                                                    solvecnt+=snapshot4.getChildrenCount();
                                                    solveQnum.setText(solvecnt+" 개");
                                                    break;
                                                }
                                            }
                                            //여기서 아예 지문 타고 들어가서 내가 만든 질문 개수도 가져올까용
                                        }
                                        script.setText(readcnt+" 개");
                                    }
                                    else if(key2.equals("my_week_list")){
                                        int attendcnt=0;
                                        for(DataSnapshot snapshot3:snapshot2.getChildren()){
                                            for(DataSnapshot snapshot4:snapshot3.getChildren()){
                                                String key5=snapshot4.getKey();
                                                if(key5.equals("attend_list")){
                                                    attendcnt+=snapshot4.getChildrenCount();
                                                    break;
                                                }
                                            }
                                        }
                                        attend.setText(attendcnt+" 일");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}
