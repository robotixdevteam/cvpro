package com.example.upload;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.upload.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    //private static final int REQUEST_GALLERY = 200;
    private static final int REQUEST_FILE = 200;
    private static final int FILE_PICKER =201;
    private long downloadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // catching download complete events from android download manager which broadcast message

        Button download = findViewById(R.id.download);
        download.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/play/"); // Folder Name
                Log.d("path", String.valueOf(folder));
                if (!folder.exists())
                    folder.mkdir();
            }
        }));


        Button upload_file = findViewById(R.id.upload_file);

        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check permission greater than equal to marshmellow we used run time permission
                if(Build.VERSION.SDK_INT >= 23){

                    if(checkPermission()){
                        filePicker();

                    }
                    else{
                        requestPermission();
                    }
                }
                else{
                    filePicker();
                }
            }
        });
    }



        private void filePicker(){

         //Now permission working
        Toast.makeText(MainActivity.this,"File picker call", Toast.LENGTH_SHORT).show();
         //Let's pick file
        Intent opengallery=new Intent(Intent.ACTION_GET_CONTENT);
        opengallery.addCategory(Intent.CATEGORY_OPENABLE);

         //setting the type
        opengallery.setType("*/*"); //opengallery.setType("image/");
        startActivityForResult(opengallery,REQUEST_FILE);
    }
     //now checking if download complete

//    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
//            if(downloadID==id){
//                Toast.makeText(MainActivity.this,"Download completed",Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        unregisterReceiver(onDownloadComplete);
//    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "please give permission to upload file",Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this,"permission successfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"permission failed", Toast.LENGTH_SHORT).show();
                }

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST_FILE && resultCode== Activity.RESULT_OK){
//            String filePath=getRealPathFromUri(data.getData(), MainActivity.this);
//            Log.d("File Path:", " "+filePath);
//        }
//    }

//    public String getRealPathFromUri(Uri uri, Activity activity){
//        Cursor cursor=activity.getContentResolver().query(uri, null,null,null,null);
//        if(cursor==null){
//            return uri.getPath();
//        }
//        else{
//            cursor.moveToFirst();
//            int id = cursor.getColumnIndex(MediaStore.MediaColumns.DATA); //MediaStore.Images.ImageColumns.DATA
//            return cursor.getString(id);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_FILE && resultCode== Activity.RESULT_OK){
            String filePath= FileUtils.getPath(MainActivity.this,data.getData());

            Log.d("File Path:", " "+filePath);
        }
    }



}

