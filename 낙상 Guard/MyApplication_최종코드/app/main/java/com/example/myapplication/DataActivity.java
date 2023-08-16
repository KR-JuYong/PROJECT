package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DataActivity extends AppCompatActivity {

    private ListView listViewData;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbHelper;
    private EditText editTextName, editTextAge, editTextAddress, editTextBloodType,
            editTextEmergencyContact, editTextPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        listViewData = findViewById(R.id.listViewData);
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewData.setAdapter(dataAdapter);

        dbHelper = new DBHelper(this);

        // DBHelper를 사용하여 저장된 모든 정보 조회하여 리스트에 추가
        refreshDataList();

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextBloodType = findViewById(R.id.editTextBloodType);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        Button btnSaveOrRefresh = findViewById(R.id.view_data_button);
        btnSaveOrRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '저장/새로고침 버튼' 클릭 시 데이터를 추가 또는 최신 데이터로 갱신
                saveOrRefreshData();
            }
        });

        Button btnOptimize = findViewById(R.id.btnOptimize);
        btnOptimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '최적화 버튼' 클릭 시 마지막 데이터를 제외하고 갱신
                optimizeData();
            }
        });
    }

    // 데이터를 추가하거나 최신 데이터로 갱신하는 메서드
    private void saveOrRefreshData() {
        String name = editTextName.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String bloodType = editTextBloodType.getText().toString().trim();
        String userPhone = editTextPhoneNumber.getText().toString().trim();
        String familyPhone = editTextEmergencyContact.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (!name.isEmpty() && !ageStr.isEmpty() && !bloodType.isEmpty()
                && !userPhone.isEmpty() && !familyPhone.isEmpty() && !address.isEmpty()) {
            int age = Integer.parseInt(ageStr);
            dbHelper.addInfo(name, age, bloodType, userPhone, familyPhone, address);
            clearEditTexts();
        } else {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        // 데이터 추가 또는 최신 데이터로 갱신 후 리스트뷰에 반영
        refreshDataList();
    }

    // 필드 초기화 메서드
    private void clearEditTexts() {
        editTextName.setText("");
        editTextAge.setText("");
        editTextBloodType.setText("");
        editTextPhoneNumber.setText("");
        editTextEmergencyContact.setText("");
        editTextAddress.setText("");
    }

    // 최적화 버튼 클릭 시 마지막 데이터를 제외하고 리스트뷰 갱신
    private void optimizeData() {
        try {
            List<String> infoList = dbHelper.getAllInfo();
            if (!infoList.isEmpty() && infoList.size() > 1) {
                // 마지막 데이터를 제외한 나머지 데이터를 삭제
                for (int i = 0; i < infoList.size() - 1; i++) {
                    dbHelper.deleteInfo(i + 1); // ID는 1부터 시작하므로 i + 1 사용
                }
                // 최신 데이터만 남겨서 리스트뷰에 추가
                String latestInfo = infoList.get(infoList.size() - 1);
                dataAdapter.clear();
                dataAdapter.add(latestInfo);

                // 최신 데이터를 EditText에 표시
                String[] infoArray = latestInfo.split(", ");
                for (String info : infoArray) {
                    String[] keyValue = info.split(": ");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];

                        // EditText에 해당 정보를 표시
                        if ("이름".equals(key)) {
                            editTextName.setText(value);
                        } else if ("나이".equals(key)) {
                            editTextAge.setText(value);
                        } else if ("주소".equals(key)) {
                            editTextAddress.setText(value);
                        } else if ("혈액형".equals(key)) {
                            editTextBloodType.setText(value);
                        } else if ("비상 연락처".equals(key)) {
                            editTextEmergencyContact.setText(value);
                        } else if ("전화번호".equals(key)) {
                            editTextPhoneNumber.setText(value);
                        }
                    }
                }
                Toast.makeText(this, "데이터를 최적화했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "최적화할 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "최적화 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 리스트뷰의 데이터를 갱신하여 최신 데이터만 보여주는 메서드
    private void refreshDataList() {
        dataAdapter.clear();
        try {
            List<String> infoList = dbHelper.getAllInfo();
            if (!infoList.isEmpty()) {
                // 최신 데이터를 리스트뷰에 추가
                String latestInfo = infoList.get(infoList.size() - 1);
                dataAdapter.add(latestInfo);

                // 최신 데이터를 EditText에 표시
                String[] infoArray = latestInfo.split(", ");
                for (String info : infoArray) {
                    String[] keyValue = info.split(": ");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];

                        // EditText에 해당 정보를 표시
                        if ("이름".equals(key)) {
                            editTextName.setText(value);
                        } else if ("나이".equals(key)) {
                            editTextAge.setText(value);
                        } else if ("주소".equals(key)) {
                            editTextAddress.setText(value);
                        } else if ("혈액형".equals(key)) {
                            editTextBloodType.setText(value);
                        } else if ("비상 연락처".equals(key)) {
                            editTextEmergencyContact.setText(value);
                        } else if ("전화번호".equals(key)) {
                            editTextPhoneNumber.setText(value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "정보 조회 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
