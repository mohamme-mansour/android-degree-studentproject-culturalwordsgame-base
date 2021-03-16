package com.barmej.culturalwords;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnswerActivity extends AppCompatActivity {

    private final static String ANSWER_PACKAGE = "AnswerPackage";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionAnswer = findViewById(R.id.text_view_answer);

        String test = getIntent().getStringExtra(ANSWER_PACKAGE);

        questionAnswer.setText(test);
    }
    public void back(View view)
    {
        finish();
    }


}
