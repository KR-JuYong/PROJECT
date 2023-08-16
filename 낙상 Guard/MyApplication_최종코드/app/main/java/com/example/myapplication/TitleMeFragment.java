package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TitleMeFragment extends Fragment {

    private ListView listViewDevices;
    private List<BluetoothDevice> pairedDevicesList;
    private ArrayAdapter<String> deviceAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private String deviceAddress;
    private SerialService service;
    private MyApp myApp;

    private boolean initialStart = true;
    private static final int REQUEST_CALL_PHONE = 1;

    @Nullable
    @Override
    //프래그먼트의 레이아웃을 초기화하고 이벤트 리스너를 설정하는 부분입니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title_me, container, false);
        //페어링된 블루투스 기기 목록을 표시하는 ListView입니다. 클릭 시 기기와 연결됩니다.
        listViewDevices = rootView.findViewById(R.id.listViewDevices);
        listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 기기 목록 항목을 클릭했을 때의 이벤트 처리
                BluetoothDevice device = pairedDevicesList.get(position);
                connectToDevice(device);

            }
        });

        // 블루투스 어댑터 가져오기
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 기기 목록을 표시할 ListView 초기화
        listViewDevices = rootView.findViewById(R.id.listViewDevices);

        // 블루투스를 지원하는지 체크
        if (bluetoothAdapter == null) {
            // 블루투스를 지원하지 않는 경우, 사용자에게 알림 표시
            Toast.makeText(getActivity(), "이 기기는 블루투스를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        } else {
            // 블루투스를 지원하는 경우, 페어링된 기기 목록을 가져와서 표시
            showPairedBluetoothDevices();
        }


        // 프래그먼트 인자로 전달된 기기 주소를 가져옵니다.
        Bundle args = getArguments();
        if (args != null) {
            deviceAddress = args.getString("device");
        }

        // 시작하기 버튼 클릭 리스너를 설정합니다.
        Button btnStart = rootView.findViewById(R.id.btnstart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "시작하기" 버튼이 클릭되면 메뉴 화면으로 이동합니다.
                openMenuActivity();
            }
        });

        // 정보 입력 버튼 클릭 리스너를 설정합니다.
        Button btnEnterInfo = rootView.findViewById(R.id.btnEnterInfo);
        btnEnterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "정보 입력" 버튼이 클릭되면 정보 입력 화면으로 이동하는 메서드를 호출합니다.
                openEditInfoActivity();
            }
        });
        // 레이아웃 뷰(rootView)를 반환합니다
        return rootView;
    }

    // 페어링된 블루투스 기기 목록을 표시하는 메소드
    @SuppressLint("MissingPermission")
    private void showPairedBluetoothDevices() {
        // 표시할 기기 목록과 페어링된 기기 목록을 초기화합니다.
        List<String> deviceList = new ArrayList<>();
        pairedDevicesList = new ArrayList<>();

        // 블루투스 어댑터로부터 페어링된 기기 목록을 가져옵니다.
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // 페어링된 기기가 있을 경우 처리합니다.
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesList.add(device);
                deviceList.add(device.getName() + "\n" + device.getAddress());
            }
        }
         // 기기 목록을 표시하는 ArrayAdapter를 생성하고 ListView에 설정합니다.
        deviceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, deviceList);
        listViewDevices.setAdapter(deviceAdapter);
    }

    class MyThread extends Thread {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        public boolean goOn = true;

        // 블루투스 소켓으로부터 입력 및 출력 스트림을 설정하는 메서드
        public void setSocket(BluetoothSocket socket) {
            try {
                tmpIn = socket.getInputStream();// 입력스트림
                tmpOut = socket.getOutputStream();// 출력스트림
            } catch (IOException e) {
                // 예외가 발생한 경우 로그에 메시지와 예외 내용을 기록합니다.
                Log.e("test", "temp sockets not created", e);
            }
        }


        // 데이터를 블루투스 소켓으로 전송하는 메서드
        public void sendData(String data) {
            if (tmpOut != null) {
                try {
                    tmpOut.write(data.getBytes()); // 데이터를 바이트 배열로 변환하여 전송
                    tmpOut.flush(); // 출력 버퍼를 비움 (필요한 경우)
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 블루투스 기기와의 연결을 시도하는 메소드
        @SuppressLint("MissingPermission")
        private void connectToDevice(BluetoothDevice device) {
            if (myApp.connectBluetoothSocket(device)) {
                // 기기와 연결 성공 시
                Toast.makeText(getActivity(), "기기와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                Log.i("test","connect");
                startCommunicationThread();
            } else {
                // 기기와 연결 실패 시
                Toast.makeText(getActivity(), "기기와 연결하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.i("test","connect false");
            }
        }

        // 데이터 통신 스레드를 시작하는 메소드
        public void startCommunicationThread() {
            CommunicationThread communicationThread = new CommunicationThread();
            communicationThread.start();
        }

        // 데이터 통신을 수행하는 내부 클래스
        private class CommunicationThread extends Thread {
            private InputStream inputStream;

            // 생성자: 데이터 입력 스트림 초기화
            public CommunicationThread() {
                inputStream = myApp.getInputStream();
            }



            @Override
            public void run() {
                super.run();

                Log.i("test", "thread started");
                byte[] buffer = new byte[1024];
                int bytes;

                // 데이터 통신 스레드 동작
                while (goOn) {
                    try {
                        // 입력스트림으로부터 읽기
                        bytes = inputStream.read(buffer);
                        String str = new String(buffer, 0, bytes, "UTF-8");
                        Log.i("test", "bytes=" + bytes + ", msg=" + str);

                        // 데이터 처리
                        if (str.contains("w")) { // 데이터에 "w"가 포함되어 있을 경우
                            // UI 스레드에서 토스트 메시지 및 화면 전환을 처리합니다.
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "!!낙상이 감지되었습니다!!", Toast.LENGTH_SHORT).show();
                                    openDangerActivity();
                                }
                            });
                        } else {
                            // 다른 데이터에 대한 처리
                        }

                        // 읽은 메시지를 UI 액티비티로 전송
                    } catch (IOException e) {
                        Log.e("test", "disconnected", e);
                        break;// 예외 발생 시 스레드 종료
                    }
                }
                Log.i("test", "Thread stopped");
            }
        }
    }



        MyThread thread = null;

    // 블루투스 기기와의 연결을 시도하는 메소드
    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        if (myApp.connectBluetoothSocket(device)) {
            Toast.makeText(getActivity(), "기기와 연결되었습니다.", Toast.LENGTH_SHORT).show();
            Log.i("test","connect");
            startCommunicationThread(); // MyThread 클래스의 인스턴스를 생성하지 않고 바로 호출
        } else {
            Toast.makeText(getActivity(), "기기와 연결하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            Log.i("test","connect false");
        }
    }

    // startCommunicationThread() 메서드를 TitleMeFragment 클래스 내에 정의
    private void startCommunicationThread() {
        MyThread thread = new MyThread();
        thread.startCommunicationThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            myApp.disconnectBluetoothSocket();
        } catch (Exception e) { // 예외 추가
            e.printStackTrace();
            // 예외 발생 시 로그에 예외 내용 기록
        }
    }


    // 권한 요청 결과를 처리하는 메서드입니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 전화 걸기 권한이 승인된 경우 메뉴 화면으로 이동
                openMenuActivity();
            } else {
                // 전화 걸기 권한이 거부된 경우 처리 (메시지 또는 사용자 안내 등)
                showToast("전화 걸기 권한이 거부되었습니다.");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void checkAndRequestPermissions() {
        openMenuActivity();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    // 정보 입력 액티비티로 이동하는 메서드
    private void openEditInfoActivity() {
        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        startActivity(intent);
    }

    // 메뉴 액티비티로 이동하는 메서드
    private void openMenuActivity() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }

    private void openDangerActivity() {
        Intent intent = new Intent(getActivity(), dangerActivity.class);
        startActivity(intent);
    }
}
