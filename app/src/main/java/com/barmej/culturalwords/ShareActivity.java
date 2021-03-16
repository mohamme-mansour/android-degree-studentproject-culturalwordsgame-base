package com.barmej.culturalwords;
import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class ShareActivity extends AppCompatActivity {

    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;
    private static final String SHARED_PREFERENCES_TITLE = "Share Title";
    ImageView sharedQuestion;
    EditText sharedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        sharedTitle = findViewById(R.id.edit_text_share_title);
        sharedQuestion = findViewById(R.id.image_view_question);
        sharedQuestion.setImageResource(getIntent().getIntExtra("QuestionShare" , 0));

        SharedPreferences sharedPreferences = getSharedPreferences("lastEditText", MODE_PRIVATE);
        String questionTitle = sharedPreferences.getString(SHARED_PREFERENCES_TITLE, "");
        sharedTitle.setText(questionTitle);
    }
    public void checkPermissionAndShare(View view)
    {
        // insertImage في النسخ من آندرويد 6 إلى آندرويد 9 يجب طلب الصلاحية لكي نتمكن من استخدام الدالة
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // هنا لا يوجد صلاحية للتطبيق ويجب علينا طلب الصلاحية منه عن طريك الكود التالي
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // بسبب عدم منح المستخدم الصلاحية للتطبيق فمن الأفضل شرح فائدتها له عن طريق عرض رسالة تشرح ذلك
                // هنا نقوم بإنشاء AlertDialog لعرض رسالة تشرح للمستخدم فائدة منح الصلاحية
                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.permission_title).setMessage(R.string.permission_explanation).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {@Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // requestPermissions عند الضغط على زر منح نقوم بطلب الصلاحية عن طريق الدالة
                    ActivityCompat.requestPermissions(ShareActivity.this, new String[] {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                            PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //  عند الضغط على زر منع نقوم بإخفاء الرسالة وكأن شيء لم يكن
                    dialogInterface.dismiss();
                }
                }).create();
                // نقوم بإظهار الرسالة بعد إنشاء alertDialog
                alertDialog.show();
            }
            else
            {
                // لا داعي لشرح فائدة الصلاحية للمستخدم ويمكننا طلب الصلاحية منه
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }

        }
        else
        {
            // الصلاحية ممنوحه مسبقا لذلك يمكننا مشاركة الصورة
            shareButton();
        }
    }

    private void shareButton()
    {
        int currentQuestion = getIntent().getIntExtra("QuestionShare" , 0);
        String questionTitle = sharedTitle.getText().toString();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, resourceToUri(currentQuestion));
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, questionTitle);
        startActivity(Intent.createChooser(intent, "Share images to.."));

        SharedPreferences sharedPreferences = getSharedPreferences("lastEditText", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TITLE, questionTitle);
        editor.apply();

    }

    private Uri resourceToUri(int currentQuestion){
        Resources resources = getApplicationContext().getResources();
        Uri imageUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(currentQuestion))
                .appendPath(resources.getResourceTypeName(currentQuestion))
                .appendPath(resources.getResourceEntryName(currentQuestion))
                .build();
        System.out.println(imageUri.toString());

        return imageUri;
    }
}