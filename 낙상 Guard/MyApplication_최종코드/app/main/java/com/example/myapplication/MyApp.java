package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MyApp extends Application {
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    // 블루투스 소켓 연결 메서드
    @SuppressLint("MissingPermission")
    public boolean connectBluetoothSocket(BluetoothDevice device) {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 예시 UUID
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            Log.i("MyApp", "Bluetooth socket connected");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyApp", "Bluetooth socket connection failed");
            return false;
        }
    }

    public void setBluetoothSocket(BluetoothSocket socket) {
        this.bluetoothSocket = socket;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 블루투스 소켓 닫기 메서드
    public void disconnectBluetoothSocket() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                Log.i("MyApp", "Bluetooth socket disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }
    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}

