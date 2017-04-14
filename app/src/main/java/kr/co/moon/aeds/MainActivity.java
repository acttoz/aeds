package kr.co.moon.aeds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    ImageButton btn1;
    ImageButton btn2;
    Context mContext;
    DbHelper mHelper;
    SQLiteDatabase db;
    SharedPreferences idPrefs;
    SharedPreferences.Editor editor;boolean dbCopied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idPrefs = getSharedPreferences("id", MODE_PRIVATE);
        editor = idPrefs.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        dbCopied = idPrefs.getBoolean("DBCOPY", false);
//        if (!dbCopied)
            copySQLiteDB(this);
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
        btn1 = (ImageButton) findViewById(R.id.imageButton1);
        btn1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:119"));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:119"));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }
            }
        });
        btn2 = (ImageButton) findViewById(R.id.imageButton2);
        btn2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });


    }
    static public boolean checkGooglePlayService(Activity activity) {
        Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode == ConnectionResult.SUCCESS) {
            return true;
        }
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 0);
        if (dialog != null) {
            dialog.show();
        }
        return false;
    }
    private void copySQLiteDB(Context context) {

        mHelper = new DbHelper(MainActivity.this);
        db = mHelper.getReadableDatabase();


        Log.d("dddd", "db복사");

        AssetManager manager = context.getAssets();
        String filePath = "data/data/" + getApplicationContext().getPackageName() + "/databases/"
                + "db.db";
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            InputStream is = manager.open("db.db");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();
            editor.putBoolean("DBCOPY", true);
            editor.commit();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }

        db.close();
    }
}
