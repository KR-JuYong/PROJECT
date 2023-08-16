package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MenuActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_EDIT_INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button infoButton = findViewById(R.id.view_info_btn);
        Button emergencyCallButton = findViewById(R.id.eme_btn);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "정보 보기 / 수정" 버튼을 눌렀을 때 DataActivity로 이동하는 메서드
                openDataActivity();
            }
        });

        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "119전화" 버튼을 눌렀을 때 권한 설정을 요청하고, 권한이 허용된 경우 119로 전화를 걸도록 구현
                checkAndRequestCallPhonePermission();
            }
        });
    }

    private void openDataActivity() {
        // "정보 보기 / 수정" 버튼을 눌렀을 때 DataActivity로 이동하는 메서드
        Intent intent = new Intent(this, DataActivity.class);
        startActivityForResult(intent, REQUEST_EDIT_INFO);
    }

    private void checkAndRequestCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // 전화 걸기 권한이 이미 허용됨, 전화를 바로 걸 수 있습니다.
            makeEmergencyCall();
        } else {
            // 전화 걸기 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 전화 걸기 권한이 승인된 경우 119로 전화를 걸 수 있습니다.
                makeEmergencyCall();
            } else {
                // 전화 걸기 권한이 거부된 경우, 이에 대한 처리를 수행합니다. 예를 들어 권한이 필요함을 알리는 메시지를 표시하거나 사용자에게 수동으로 전화를 걸도록 안내할 수 있습니다.
                Toast.makeText(this, "전화 걸기 권한이 거부되어 119 전화를 걸 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeEmergencyCall() {
        // 전화 걸기
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:119"));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_INFO) {
            // DataActivity에서 돌아온 경우, 처리할 내용을 추가할 수 있습니다.
            if (resultCode == RESULT_OK) {
                // 예를 들어 정보가 수정되었을 때 처리할 작업을 추가할 수 있습니다.
            }
        }
    }
}
