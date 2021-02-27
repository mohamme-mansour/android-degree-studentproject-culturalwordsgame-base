package com.barmej.culturalwords;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnswerActivity extends AppCompatActivity {

    TextView questionAnswer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        questionAnswer = findViewById(R.id.text_view_answer);

        questionAnswer.setText(getIntent().getStringExtra("AnswerPackage"));
    }
    public void Back(View view) {
        finish();
    }


}