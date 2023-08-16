package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class EditInfoActivity extends AppCompatActivity {

    private EditText editTextName, editTextAge, editTextAddress, editTextBloodType,
            editTextEmergencyContact, editTextPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextBloodType = findViewById(R.id.editTextBloodType);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        // 정보 저장_DB
        DBHelper dbHelper = new DBHelper(EditInfoActivity.this);

        // "저장하기" 버튼 클릭 시 정보를 저장하고 MenuActivity로 이동
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfoAndReturnToMenu();
            }
        });
    }

    private void saveInfoAndReturnToMenu() {
        // EditText에서 입력된 값 가져오기
        String name = editTextName.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());
        String address = editTextAddress.getText().toString();
        String bloodType = editTextBloodType.getText().toString();
        String emergencyContact = editTextEmergencyContact.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        // DBHelper를 사용하여 데이터베이스에 정보 저장
        DBHelper dbHelper = new DBHelper(EditInfoActivity.this);
        dbHelper.addInfo(name, age, bloodType, phoneNumber, emergencyContact, address);

        // 저장 완료 메시지 표시
        Toast.makeText(EditInfoActivity.this, "데이터를 성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show();

        // MenuActivity로 이동
        Intent intent = new Intent(EditInfoActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
