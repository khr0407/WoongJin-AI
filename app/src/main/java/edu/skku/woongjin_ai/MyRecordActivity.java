package edu.skku.woongjin_ai;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class MyRecordActivity extends AppCompatActivity implements  ShowHoonjangCriteriaFragment.OnFragmentInteractionListener {

    public DatabaseReference mPostReference;
    Intent intent;
    String id;
    TextView userGrade, userSchool, userName, userCoin;
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

    MaterialCalendarView materialCalendarView;
    ArrayList<String> attendedDatesList;

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
        goHome=(ImageButton)findViewById(R.id.home);
        graph_attend=(Button)findViewById(R.id.graph_attend);
        graph_made=(Button)findViewById(R.id.graph_made);
        graph_correct=(Button)findViewById(R.id.graph_correct);
        graph_level=(Button)findViewById(R.id.graph_level);
        graph_like=(Button)findViewById(R.id.graph_like);
        Hoonjang=(Button)findViewById(R.id.showHoonjang);
        lineChart=(LineChart)findViewById(R.id.chart);

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.attendCalendar);
        attendedDatesList = new ArrayList<String>();

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OnDayDecorator());

        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int weekNum = 0;
                attendedDatesList.clear();

                for(DataSnapshot snapshot : dataSnapshot.child("user_list/" + id + "/my_week_list/").getChildren()) {
                    weekNum++;
                    for(DataSnapshot snapshot1 : snapshot.child("attend_list").getChildren()) {
                        String time = snapshot1.getValue().toString();
                        String[] date = time.split(" ");
                        attendedDatesList.add(date[0]);
                    }
                }

                new CheckAttendedDay(attendedDatesList).executeOnExecutor(Executors.newSingleThreadExecutor());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });


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
                for (DataSnapshot snapshot : dataSnapshot.child("user_list").getChildren()) {
                    if(snapshot.getKey().equals(id)) {
                        me = snapshot.getValue(UserInfo.class);
                        userName.setText(me.name+" 학생");
                        userGrade.setText(me.grade + "학년");
                        userCoin.setText(me.coin + " 코인");
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.addValueEventListener(postListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class CheckAttendedDay extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> attendedDatesList;

        CheckAttendedDay(ArrayList<String> attendedDatesList) {
            this.attendedDatesList = attendedDatesList;
        }

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for(String date : attendedDatesList) {
                CalendarDay calendarDay = CalendarDay.from(calendar);
                String[] time = date.split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);

                dates.add(calendarDay);
                calendar.set(year, month-1, day);

                Log.d("hereeeeeeeeee", date);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if(isFinishing()) return;

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, getApplicationContext()));
        }
    }

    private class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    private class OnDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OnDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
}

