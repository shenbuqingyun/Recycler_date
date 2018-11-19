package com.example.recycler_date;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


public class MarshmallowPermissions {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 103;
    private static final String[] CAMERA_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public static boolean checkPermissionForCamera(Activity activity){
        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result2 == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public static void requestPermissionForCamera(Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            Toast.makeText(activity, "请求权限.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, CAMERA_PERMS, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }
}
