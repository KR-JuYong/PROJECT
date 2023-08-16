package com.example.myapplication;

import android.content.ComponentName;
import android.os.IBinder;

import java.util.ArrayDeque;

interface SerialListener {
    void onServiceConnected(ComponentName name, IBinder binder);

    void onSerialConnect      ();
    void onSerialConnectError (Exception e);
    void onSerialRead         (byte[] data);                // socket -> service
    void onSerialRead         (ArrayDeque<byte[]> datas);   // service -> UI thread
    void onSerialIoError      (Exception e);
}