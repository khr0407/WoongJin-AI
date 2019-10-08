package edu.skku.woongjin_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecordActivity extends AppCompatActivity implements  ShowHoonjangCriteriaFragment.OnFragmentInteractionListener {

    public DatabaseReference mPostReference;
    Intent intent;
    String id;
    TextView userGrade, userSchool, userName, userCoin;
    TextView attend, script, myQnum, solveQnum, bombNum, bucketNum;
    Button Hoonjang;
    ImageButton goHome;
    UserInfo me;
    Button graph_attend, graph_made, graph_correct, graph_level, graph_like;
    int total_week;
    LineChart lineChart;
    int MAX_SIZE=100;
    int f1=0, f2=0, f3=0, f4=0, f5=0;
    ShowHoonjangCriteriaFragment showHoonjang;

    Intent intentGoHome;

    ArrayList<String> week_made, week_correct, week_level, week_like, week_attend;
    ArrayList<Entry> entries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecord);

        intent=getIntent();
        id=intent.getStringExtra("id");

        mPostReference = FirebaseDatabase.getInstance().getReference();


        userName = (TextView) findViewById(R.id.userName1);
        userSchool = (TextView) findViewById(R.id.userSchool);
        userGrade = (TextView) findViewById(R.id.userGrade1);
        userCoin = (TextView) findViewById(R.id.userCoin);
        attend=(TextView)findViewById(R.id.attendDays);
        script=(TextView)findViewById(R.id.scriptNum);
        myQnum=(TextView)findViewById(R.id.myQnum);
        solveQnum=(TextView)findViewById(R.id.solveQnum);
        bombNum=(TextView)findViewById(R.id.bombNum);
        bucketNum=(TextView)findViewById(R.id.bucketNum);
        goHome=(ImageButton)findViewById(R.id.home);

        graph_attend=(Button)findViewById(R.id.graph_attend);
        graph_made=(Button)findViewById(R.id.graph_made);
        graph_correct=(Button)findViewById(R.id.graph_correct);
        graph_level=(Button)findViewById(R.id.graph_level);
        graph_like=(Button)findViewById(R.id.graph_like);

        Hoonjang=(Button)findViewById(R.id.showHoonjang);

        lineChart=(LineChart)findViewById(R.id.chart);

        entries=new ArrayList<Entry>();

        showHoonjang=new ShowHoonjangCriteriaFragment();

        
        //week_attend=new long[MAX_SIZE];
        //week_cnt=new int[MAX_SIZE];
        //week_correct=new int[MAX_SIZE];
        //week_level=new float[MAX_SIZE];
       // week_like=new int[MAX_SIZE];

        week_attend=new ArrayList<String>();
        week_made=new ArrayList<String>();
        week_correct=new ArrayList<String>();
        week_level=new ArrayList<String>();
        week_like=new ArrayList<String>();

        getFirebaseDatabaseWeekInfo();
        getFirebaseDatabaseUserInfo();

        goHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intentGoHome=new Intent(MyRecordActivity.this, MainActivity.class);
                intentGoHome.putExtra("id", id);
                startActivity(intentGoHome);
            }
        });

        graph_attend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_attend.setBackgroundResource(R.drawable.rounded_yellow);
                f1=1;
                if(f2==1||f3==1||f4==1||f5==1){
                    graph_made.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_correct.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_level.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_like.setBackgroundResource(R.drawable.rounded_white_transparent);
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_attend.get(j))));
                }
                LineDataSet dataset = new LineDataSet(entries, "주간 출석일 수");
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.animateY(5000);
            }
        });

        graph_made.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_made.setBackgroundResource(R.drawable.rounded_yellow);
                f2=1;
                if(f1==1||f3==1||f4==1||f5==1){
                    graph_attend.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_correct.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_level.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_like.setBackgroundResource(R.drawable.rounded_white_transparent);
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_made.get(j))));
                }
                LineDataSet dataset = new LineDataSet(entries, "주간 만든 문제 수");
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.animateY(5000);
            }
        });

        graph_correct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_correct.setBackgroundResource(R.drawable.rounded_yellow);
                f3=1;
                if(f2==1||f1==1||f4==1||f5==1){
                    graph_made.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_attend.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_level.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_like.setBackgroundResource(R.drawable.rounded_white_transparent);
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_correct.get(j))));
                }
                LineDataSet dataset = new LineDataSet(entries, "주간 맞춘 문제 수");
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.animateY(5000);
            }
        });

        graph_level.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_level.setBackgroundResource(R.drawable.rounded_yellow);
                f4=1;
                if(f2==1||f3==1||f1==1||f5==1){
                    graph_made.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_correct.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_attend.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_like.setBackgroundResource(R.drawable.rounded_white_transparent);
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j, Float.parseFloat(week_level.get(j))));
                }
                LineDataSet dataset = new LineDataSet(entries, "주간 평균 레벨");
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.animateY(5000);
            }
        });

        graph_like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                graph_like.setBackgroundResource(R.drawable.rounded_yellow);
                f5=1;
                if(f2==1||f3==1||f4==1||f1==1){
                    graph_made.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_correct.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_level.setBackgroundResource(R.drawable.rounded_white_transparent);
                    graph_attend.setBackgroundResource(R.drawable.rounded_white_transparent);
                }
                entries.clear();
                for(int j=0; j<total_week ; j++){
                    entries.add(new Entry(j,Float.parseFloat(week_like.get(j))));
                }
                LineDataSet dataset = new LineDataSet(entries, "주간 좋아요 수");
                LineData data = new LineData(dataset);
                lineChart.setData(data);
                lineChart.animateY(1000);
            }
        });

        Hoonjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.commit();
                showHoonjang=new ShowHoonjangCriteriaFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.BiggestFrame, showHoonjang);
                transaction.commit();
            }
        });

    }

    private void getFirebaseDatabaseWeekInfo(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                week_like.clear();
                week_level.clear();
                week_correct.clear();
                week_attend.clear();
                week_made.clear();
                total_week=0;
                DataSnapshot snapshot=dataSnapshot.child("user_list").child(id).child("my_week_list");
                for(DataSnapshot snapshot1:snapshot.getChildren()){ //week껍데기
                    long attendCnt=snapshot1.child("attend_list").getChildrenCount();
                    week_attend.add(Long.toString(attendCnt));
                    week_made.add(snapshot1.child("made").getValue().toString());
                    week_correct.add(snapshot1.child("correct").getValue().toString());
                    week_level.add(snapshot1.child("level").getValue().toString());
                    week_like.add(snapshot1.child("like").getValue().toString());
                    total_week++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    private void getFirebaseDatabaseUserInfo() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key.equals("user_list")) {
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
                                        }
                                        script.setText(readcnt+" 개");
                                    }
                                    else if(key2.equals("my_week_list")){
                                        int attendcnt=0;
                                        int madecnt=0;
                                        for(DataSnapshot snapshot3:snapshot2.getChildren()){
                                            for(DataSnapshot snapshot4:snapshot3.getChildren()){
                                                String key5=snapshot4.getKey();
                                                if(key5.equals("attend_list")){
                                                    attendcnt+=snapshot4.getChildrenCount();
                                                    break;
                                                }else if(key5.equals("made")){
                                                    attendcnt+=Integer.parseInt(snapshot4.getValue().toString());
                                                    break;
                                                }
                                            }
                                        }
                                        attend.setText(attendcnt+" 일");
                                        myQnum.setText(madecnt+" 개");
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
