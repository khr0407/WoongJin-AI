package edu.skku.woongjin_ai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class SelectTypeActivity extends AppCompatActivity {

    Intent intent, intentHome, intentOX, intentChoice, intentShortword;
    String id, scriptnm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttype);

        ImageView imageHome = (ImageView) findViewById(R.id.home);
        ImageView imageOX = (ImageView) findViewById(R.id.quiz_ox);
        ImageView imageChoice = (ImageView) findViewById(R.id.quiz_choice);
        ImageView imageShortword = (ImageView) findViewById(R.id.quiz_shortword);

        intent = getIntent();
        id = intent.getStringExtra("id");
        scriptnm = intent.getStringExtra("scriptnm");

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentHome = new Intent(SelectTypeActivity.this, MainActivity.class);
                intentHome.putExtra("id", id);
                startActivity(intentHome);
                finish();
            }
        });

        imageOX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentOX = new Intent(SelectTypeActivity.this, OXTypeActivity.class);
                intentOX.putExtra("id", id);
                intentOX.putExtra("scriptnm", scriptnm);
                startActivity(intentOX);
                finish();
            }
        });

        imageChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentChoice = new Intent(SelectTypeActivity.this, ChoiceTypeActivity.class);
                intentChoice.putExtra("id", id);
                intentChoice.putExtra("scriptnm", scriptnm);
                startActivity(intentChoice);
                finish();
            }
        });

        imageShortword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShortword = new Intent(SelectTypeActivity.this, ShortwordTypeActivity.class);
                intentShortword.putExtra("id", id);
                intentShortword.putExtra("scriptnm", scriptnm);
                startActivity(intentShortword);
                finish();
            }
        });
    }
}
