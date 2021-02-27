package com.barmej.culturalwords;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_CONSTANT_1 = "app pref";
    private static final String SHARED_PREFERENCES_CONSTANT_2 = "Keep Language";
    private ImageView imageViewQuestion;
    private String randomImagesNames;
    ImageButton imageAnswerPage;
    private ImageView languageChange;
    private ImageView shareButton;
    private int randomImageQuestion;
    private int randomImage;

    int[] imagesQuestion = {
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

    String[] imagesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_CONSTANT_1, MODE_PRIVATE);
        String appLang = sharedPreferences.getString(SHARED_PREFERENCES_CONSTANT_2, "");
        if (!appLang.equals("")) LocaleHelper.setLocale(this, appLang);

        imagesNames = getResources().getStringArray(R.array.answers);
        imageViewQuestion = findViewById(R.id.image_view_question);
        imageAnswerPage = findViewById(R.id.button_open_answer);
        languageChange = findViewById(R.id.button_change_language);
        shareButton = findViewById(R.id.button_share_question);

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sharePage();
            }
        });

        languageChange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showLanguageDialog();
            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.langueageMenu)
        {
            showLanguageDialog();
            return true;

        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showLanguageDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.change_lang_text).setItems(R.array.Languages, new DialogInterface.OnClickListener()
        {
        @Override
        public void onClick(DialogInterface dialogInterface, int which)
        {
            String language = "en";
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
    }

    public void saveLanguage(String lang)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_CONSTANT_2, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("app lang", lang);
        editor.apply();
    }

    private void sharePage()
    {
        if (imageViewQuestion.getDrawable() == null)
        {
            Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            intent.putExtra("QuestionShare", randomImageQuestion);
            startActivity(intent);
        }
    }

    public void randomImageQuestion(View view)
    {
        Random random = new Random();
        randomImage = random.nextInt(imagesQuestion.length);
        randomImageQuestion = imagesQuestion[randomImage];
        randomImagesNames = imagesNames[randomImage];
        imageViewQuestion.setImageResource(randomImageQuestion);

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
            intent.putExtra("AnswerPackage", randomImagesNames);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // outState is used to put the thing we need to save in... it needs a key and a resource
        outState.putInt("thing" , randomImageQuestion);
        outState.putString("thingName" , randomImagesNames);
        System.out.println("Current Image Index: "+randomImageQuestion + "Current Image Name: " + randomImagesNames);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        // randomImageQuestion = savedInstanceState.getInt(); this is used to make RandomImageQuestion value the same as what we last saved at onSaveInstance
        randomImageQuestion = savedInstanceState.getInt("thing");
        randomImagesNames = savedInstanceState.getString("thingName");
        System.out.println("onRestoreInstance: " + randomImageQuestion + " This is the current index. " + randomImagesNames + " This is the current index name");
        // to apply the current value of the randomImageQuestion to the ImageView we have...
        imageViewQuestion.setImageResource(randomImageQuestion);

    }
}