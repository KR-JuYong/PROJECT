package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int REQUEST_SMS_PERMISSION = 2;
    private static final int REQUEST_PHONE_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 블루투스 권한 요청
        requestBluetoothPermission();
    }

    private void requestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            // 블루투스 권한이 이미 허용되었으면 SMS 권한 요청
            requestSmsPermission();
        } else {
            // 블루투스 권한이 허용되지 않았으면 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    REQUEST_BLUETOOTH_PERMISSION);
        }
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // SMS 권한이 이미 허용되었으면 전화 권한 요청
            requestPhonePermission();
        } else {
            // SMS 권한이 허용되지 않았으면 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }
    }

    private void requestPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // 전화 권한이 이미 허용되었으면 Fragment를 추가하여 블루투스 기기 목록을 표시
            addTitleMeFragment();
        } else {
            // 전화 권한이 허용되지 않았으면 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 블루투스 권한이 허용되었으면 SMS 권한 요청
                requestSmsPermission();
            } else {
                showPermissionDeniedDialog("블루투스");
            }
        } else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS 권한이 허용되었으면 전화 권한 요청
                requestPhonePermission();
            } else {
                showPermissionDeniedDialog("SMS");
            }
        } else if (requestCode == REQUEST_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 전화 권한이 허용되었으면 Fragment를 추가하여 블루투스 기기 목록을 표시
                addTitleMeFragment();
            } else {
                showPermissionDeniedDialog("전화");
            }
        }
    }

    private void showPermissionDeniedDialog(String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 거부")
                .setMessage(permission + " 권한이 거부되었습니다. 앱을 종료합니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    // TitleMeFragment를 추가하는 메소드
    private void addTitleMeFragment() {
        TitleMeFragment titleMeFragment = new TitleMeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, titleMeFragment); // 프래그먼트를 RelativeLayout에 추가
        fragmentTransaction.commit();
    }
}
