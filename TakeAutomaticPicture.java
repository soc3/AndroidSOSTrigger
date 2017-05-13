package com.example.sushantoberoi.catchyourtrain;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TakeAutoPicture extends Activity {

    private Camera camera; // camera object
    private TextView textTimeLeft; // time left field    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_auto_picture);
        //getActionBar().setTitle("Say Cheese...");
        try {
            textTimeLeft = (TextView) findViewById(R.id.textTimeLeft); // make time left object
            camera = Camera.open();
            Log.d("camera open", "open");
            SurfaceView view = new SurfaceView(this);
            Log.d("surf viw", "www");
            try {
                camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            camera.startPreview();
            Log.d("camera open", "open2");
        }
        catch(Exception e){
            Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();
        }
    }
    Camera.PictureCallback jpegCallBack=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                // set file destination and file name
                File destination = new File(Environment.getExternalStorageDirectory(), "myPicture.jpg");
                try {
                    Bitmap userImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // set file out stream
                    FileOutputStream out = new FileOutputStream(destination);
                    // set compress format quality and stream
                    userImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
            }

        }
    };

    public void startTimer(View v) {

        // 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
        try {
            new CountDownTimer(5000, 1000) {

                @Override
                public void onFinish() {
                    // count finished
                    textTimeLeft.setText("Picture Taken");
                    camera.takePicture(null, null, null, jpegCallBack);
                    SQLiteDatabase data = openOrCreateDatabase("hint", android.content.Context.MODE_PRIVATE, null); //nobody other can access
                    data.execSQL("create table if not exists contacts(name varchar, contact varchar);");
                    String query = "select * from contacts";
                    Cursor c = data.rawQuery(query, null);
                    int cnt = c.getCount();
                    String[] array_name = new String[cnt];
                    if (cnt > 0) {
                        c.moveToFirst();
                        array_name = new String[cnt];
                        int k = 0;
                        Log.d("Hello", "count : " + cnt);
                        while (!c.isAfterLast()) {
                            String s2 = c.getString(c.getColumnIndex("contact"));
                            array_name[k++] = s2;
                            c.moveToNext();
                        }
                    }
                    for (int j = 0; j < cnt; j++) {
                        String strPhone = array_name[j];
                        String strMessage = "Help me:";

/* Attach Url is local (!) URL to file which should be sent */
                        String strAttachUrl = "file:///sdcard/myPicture.jpg";

/* Attach Type is a content type of file which should be sent */
                        String strAttachType = "image/jpg";

                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setPackage("com.android.mms");
                        sendIntent.putExtra("address", strPhone);
                        sendIntent.putExtra("sms_body", strMessage);

/* Adding The Attach */
                        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(strAttachUrl));
                        sendIntent.setType(strAttachType);

                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(sendIntent);
                        Toast.makeText(getApplicationContext(), "message has been sent to" + array_name[j], Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    // every time 1 second passes
                    textTimeLeft.setText("Seconds Left: " + millisUntilFinished / 1000);
                }

            }.start();
        }
        catch(Exception e){
            Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();
        }
    }
}
