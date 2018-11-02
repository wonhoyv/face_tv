package com.visitor.tengli.facepadlygc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.visitor.tengli.facepadlygc.fs.SocketMessageBean;
import com.visitor.tengli.facepadlygc.util.IPHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Calendar;

/**
 * created by yangshaojie  on 2018/11/1
 * email: ysjr-2002@163.com
 */
public class UdpService extends Service {

    UdpThread mThread;

    DatagramSocket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("kaka", "service create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mThread =new UdpThread();
        mThread.start();

        Log.d("kaka", "start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class UdpThread extends Thread {
        boolean isstop = false;

        @Override
        public void run() {
            try {
                mSocket = new DatagramSocket(9875);
                while (isstop == false) {
                    byte data[] = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(data, 0, data.length);
                    mSocket.receive(datagramPacket);
                    String message = new String(data, 0, data.length, "utf-8");

                    message = message.replace('\0', ' ');
                    message = message.trim();
                    Gson gson = new Gson();
                    SocketMessageBean bean = gson.fromJson(message, SocketMessageBean.class);
                    EventBus.getDefault().post(bean);
                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void interrupt() {
            isstop = true;
            super.interrupt();
        }
    }

}
