package com.barmej.culturalwords;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_KEEP_LANGUAGE = "Keep Language";
    private static final String SHARED_PREFERENCES_STRING_LANGUAGE = "app pref";

    private ImageView imageViewQuestion;
    private String answerName;
    private int answerImage;
    private int randomIndex;
    String language;


    int[] answersImages = {
            R.drawable.icon_1,
            R.drawable.icon_2,
            R.drawable.icon_3,
            R.drawable.icon_4,
            R.drawable.icon_5,
            R.drawable.icon_6,
            R.drawable.icon_7,
            R.drawable.icon_8,
            R.drawable.icon_9,
            R.drawable.icon_10,
            R.drawable.icon_11,
            R.drawable.icon_12,
            R.drawable.icon_13
    };

    String[] answers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEEP_LANGUAGE, MODE_PRIVATE);
        String appLang = sharedPreferences.getString(SHARED_PREFERENCES_STRING_LANGUAGE , "No Language Defined");
        if (!appLang.equals("No Language Defined")) LocaleHelper.setLocale(this, appLang);
        setContentView(R.layout.activity_main);

        answers = getResources().getStringArray(R.array.answers);

        imageViewQuestion = findViewById(R.id.image_view_question);

    }

    public void showLanguageDialog(View view)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.change_lang_text).setItems(R.array.Languages, new DialogInterface.OnClickListener()
        {
        @Override
        public void onClick(DialogInterface dialogInterface, int which)
        {
            language = "";
            switch (which)
            {
                case 0:
                    language = "arabic";
                    break;
                case 1:
                    language = "en";
                    break;
            }

            saveLanguage(language);
            LocaleHelper.setLocale(MainActivity.this, language);
            recreate();


        }
        }).create();

        alertDialog.show();

    }

    @Override
    public void recreate()
    {
        super.recreate();
        answersOnChangeLanguage();
    }

    public String answersOnChangeLanguage(){
        if (randomIndex >= 0){
            answers = getResources().getStringArray(R.array.answers);
            return answers[randomIndex];
        }
        return null;
    }

    public void saveLanguage(String lang)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEEP_LANGUAGE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_STRING_LANGUAGE, lang);
        editor.apply();
    }

    public void sharePage(View view)
    {
        if (imageViewQuestion.getDrawable() == null)
        {
            Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            intent.putExtra("QuestionShare", answerImage);
            startActivity(intent);
        }
    }

    public void randomValue(View view)
    {
        Random random = new Random();
        randomIndex = random.nextInt(answersImages.length);

        answerImage = answersImages[randomIndex];
        answerName = answers[randomIndex];

        imageViewQuestion.setImageResource(answerImage);

    }

    public void answerPage(View view)
    {

        if (imageViewQuestion.getDrawable() == null)
        {
            Toast.makeText(this, R.string.toast_message2, Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
            intent.putExtra("AnswerPackage", answerName);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("randomImage" , randomIndex);
        outState.putString("language" , language);
        outState.putInt("thing" , answerImage);
        outState.putString("thingTest" , answersOnChangeLanguage());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        randomIndex = savedInstanceState.getInt("randomImage");
        language = savedInstanceState.getString("language");
        answerName = savedInstanceState.getString("thingTest");
        answerImage = savedInstanceState.getInt("thing");
        imageViewQuestion.setImageResource(answerImage);
    }
}