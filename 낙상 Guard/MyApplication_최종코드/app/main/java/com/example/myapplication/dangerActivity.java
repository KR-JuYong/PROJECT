package com.example.myapplication;

import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;

public class dangerActivity extends AppCompatActivity implements SerialListener{

    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    private boolean timerRunning = false;

    private DBHelper dbHelper; // DBHelper 변수 추가

    private boolean isWSignalReceived = false;
    private SerialService service;

    private BluetoothSocket bluetoothSocket;

    private TitleMeFragment.MyThread myThread;

    private MyApp myApp;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danger);

        myApp = (MyApp) getApplicationContext();
        outputStream = myApp.getOutputStream();

        timerTextView = findViewById(R.id.textViewTimer);


        // DBHelper 인스턴스 생성
        dbHelper = new DBHelper(this);

        // 타이머 설정 (60초, 1초 간격)
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 타이머가 감소할 때마다 남은 시간을 텍스트뷰에 표시
                timerTextView.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                // 타이머가 종료되었을 때의 동작
                timerTextView.setText("0");

                // 가족 전화번호(FAMILYPHONE) 가져오기
                String familyPhone = dbHelper.getFamilyPhone();
                String name = dbHelper.getName();
                String age = dbHelper.getAge();
                String address = dbHelper.getAddress(); // 예시로 사용한 이름입니다. 실제 코드에 맞게 수정하세요.
                String bloodtype = dbHelper.getBloodtype();
                Log.i("test","get phone number");

                // FAMILYPHONE 번호로 "위험합니다!" 메시지를 보냄
                sendSMS(familyPhone, "낙상이 감지되었습니다. 보호자를 확인하세요!", name, Integer.parseInt(age), bloodtype, address);
                Log.i("test","send msg");

                // 추가로 블루투스 데이터 전송도 수행
                sendDataToBluetooth("c");

                finish();
            }

        };

        // 타이머 시작
        countDownTimer.start();

        // 확인 버튼 클릭 리스너 설정
        Button btnConfirm = findViewById(R.id.btnOK);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test", "Confirm button clicked"); // 로그 출력 추가
                sendDataToBluetooth("c"); // "c" 데이터 전송

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                finish();
            }
        });
    }

    private void sendDataToBluetooth(String data) {
        if (outputStream != null) {
            try {
                outputStream.write(data.getBytes()); // 데이터 전송
                outputStream.flush();
                Log.i("test", "Data sent: " + data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // SMS를 보내는 메서드
    private void sendSMS(String phoneNumber, String message, String name, int age, String bloodtype, String address) {
        String additionalInfo = "이름: " + name + ", 나이: " + age + ", 혈액형: " + bloodtype + ", 주소: " + address;
        String completeMessage = message + "\n" + additionalInfo;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, completeMessage, null, null);
            Log.i("test", "SMS sent: " + completeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach((SerialListener) this);
    }


    @Override
    public void onSerialConnect() {
        // 시리얼 통신이 연결되었을 때의 동작
        runOnUiThread(() -> {
            Toast.makeText(this, "시리얼 통신이 연결되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onSerialConnectError(Exception e) {
        // 시리얼 통신 연결 오류가 발생했을 때의 동작
        runOnUiThread(() -> {
            Toast.makeText(this, "시리얼 통신 연결 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onSerialRead(byte[] data) {

    }

    @Override
    public void onSerialRead(ArrayDeque<byte[]> datas) {
        // 시리얼로부터 데이터를 읽었을 때의 동작
        // 읽은 데이터 처리 및 UI에 반영하는 작업을 수행
        // datas 변수에 읽은 데이터가 들어있습니다.
    }


    @Override
    public void onSerialIoError(Exception e) {
        // 시리얼 통신 중 오류가 발생했을 때의 동작
        runOnUiThread(() -> {
            Toast.makeText(this, "시리얼 통신 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
