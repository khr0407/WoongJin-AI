package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class TemplateActivity extends AppCompatActivity {

    Intent intent, intentHome;
    String id, scriptnm;
    TextView oxT, choiceT, shortwordT;
    public DatabaseReference mPostReference;
    ArrayList<QuizOXShortwordTypeInfo> quizListOX, quizListShortword;
    ArrayList<QuizChoiceTypeInfo> quizListChoice;
    int minLikeQuiz = 0, cnt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        oxT = (TextView) findViewById(R.id.quiz_ox_template);
        choiceT = (TextView) findViewById(R.id.quiz_choice_template);
        shortwordT = (TextView) findViewById(R.id.quiz_shortword_template);

        quizListOX = new ArrayList<QuizOXShortwordTypeInfo>();
        quizListChoice = new ArrayList<QuizChoiceTypeInfo>();
        quizListShortword = new ArrayList<QuizOXShortwordTypeInfo>();

        quizListOX.clear();
        quizListChoice.clear();
        quizListShortword.clear();

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");

        mPostReference = FirebaseDatabase.getInstance().getReference();

        final Random generator = new Random();

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(TemplateActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

        final ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if(key.equals(scriptnm)) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String type = snapshot1.getKey();
                            if(type.equals("type1")) {
                                for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    QuizOXShortwordTypeInfo getNew = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                    String getLike = getNew.like;

                                    if(cnt < 3) {
                                        quizListOX.add(getNew);
                                        for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
                                            int findMin = Integer.parseInt(findMinLike.like);
                                            if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListOX.indexOf(findMinLike);
                                        }
                                        cnt++;
                                    } else {
                                        int minLike = Integer.parseInt(quizListOX.get(minLikeQuiz).like);
                                        if(minLike < Integer.parseInt(getLike)) {
                                            quizListOX.remove(minLikeQuiz);
                                            quizListOX.add(minLikeQuiz, getNew);
                                            for(QuizOXShortwordTypeInfo findMinLike : quizListOX) {
                                                int findMin = Integer.parseInt(findMinLike.like);
                                                if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListOX.indexOf(findMinLike);
                                            }
                                        }
                                    }
                                }
                                cnt = 0;
                                minLikeQuiz = 0;
                            } else if(type.equals("type2")) {
                                for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    QuizChoiceTypeInfo getNew = snapshot2.getValue(QuizChoiceTypeInfo.class);
                                    String getLike = getNew.like;

                                    if(cnt < 3) {
                                        quizListChoice.add(getNew);
                                        for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
                                            int findMin = Integer.parseInt(findMinLike.like);
                                            if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListChoice.indexOf(findMinLike);
                                        }
                                        cnt++;
                                    } else {
                                        int minLike = Integer.parseInt(quizListChoice.get(minLikeQuiz).like);
                                        if(minLike < Integer.parseInt(getLike)) {
                                            quizListChoice.remove(minLikeQuiz);
                                            quizListChoice.add(minLikeQuiz, getNew);
                                            for(QuizChoiceTypeInfo findMinLike : quizListChoice) {
                                                int findMin = Integer.parseInt(findMinLike.like);
                                                if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListChoice.indexOf(findMinLike);
                                            }
                                        }
                                    }
                                }
                                cnt = 0;
                                minLikeQuiz = 0;
                            } else {
                                for(DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    QuizOXShortwordTypeInfo getNew = snapshot2.getValue(QuizOXShortwordTypeInfo.class);
                                    String getLike = getNew.like;

                                    if(cnt < 3) {
                                        quizListShortword.add(getNew);
                                        for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
                                            int findMin = Integer.parseInt(findMinLike.like);
                                            if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListShortword.indexOf(findMinLike);
                                        }
                                        cnt++;
                                    } else {
                                        int minLike = Integer.parseInt(quizListShortword.get(minLikeQuiz).like);
                                        if(minLike < Integer.parseInt(getLike)) {
                                            quizListShortword.remove(minLikeQuiz);
                                            quizListShortword.add(minLikeQuiz, getNew);
                                            for(QuizOXShortwordTypeInfo findMinLike : quizListShortword) {
                                                int findMin = Integer.parseInt(findMinLike.like);
                                                if(findMin < Integer.parseInt(getLike)) minLikeQuiz = quizListShortword.indexOf(findMinLike);
                                            }
                                        }
                                    }
                                }
                                cnt = 0;
                                minLikeQuiz = 0;
                            }
                        }
                        break;
                    }
                }

                int rand = generator.nextInt(3);
                QuizOXShortwordTypeInfo post1 = quizListOX.get(rand);
                String postS1 = "OX 퀴즈 예시\nQ. " + post1.question + "\nA. " + post1.answer + "\nLike: " + post1.like + "\n";
                oxT.setText(postS1);

                rand = generator.nextInt(3);
                QuizChoiceTypeInfo post2 = quizListChoice.get(rand);
                String postS2 = "객관식 퀴즈 예시\nQ. " + post2.question + "\nA. " + post2.answer + "\nA1. " + post2.answer1 + " A2. " + post2.answer2 + " A3. " + post2.answer3 + " A4. " + post2.answer4 + "\nLike: " + post2.like + "\n";
                choiceT.setText(postS2);

                rand = generator.nextInt(3);
                QuizOXShortwordTypeInfo post3 = quizListShortword.get(rand);
                String postS3 = "단답형 퀴즈 예시\nQ. " + post3.question + "\nA. " + post3.answer + "\nLike: " + post3.like;
                shortwordT.setText(postS3);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        };
        mPostReference.child("quiz_list/").addValueEventListener(postListner);
    }
}
