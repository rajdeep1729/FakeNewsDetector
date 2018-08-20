package com.example.rajdeep.fakenewsdetetor;


import java.io.*;

import java.net.Socket;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.KeyFactory;

import java.security.spec.EncodedKeySpec;

import java.security.spec.X509EncodedKeySpec;
import java.util.*;


import android.app.Service;
import android.content.Intent;


import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SocketService extends Service {

    public static String tosend;
    public static String received=null;
    public static String selected_group_name = null;
    public static String selected_alias_name = null;
    public static String query = null;
    private DataOutputStream sOutput;
    private DataInputStream sInput;
    PublicKey publicKey2;
    private Socket socket;
    private Cipher cipher1;
    private Cipher cipher2;
    int i = 1;
    public static int j = 0;
    public static int k = 0;
    SecretKey AESkey;
    static String IV = "AAAAAAAAAAAAAAAA";
    public static String Result = null;
    public static String voice = null;
    public static String callID = null;
    private static int read;
    public static short flag = 0;
    public static short flag2 = 0;
    int r = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("I am in Ibinder onBind method");
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketService getService() {
            System.out.println("I am in Localbinder ");
            return SocketService.this;

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("I am in on create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        socket = null;
    }

    public void IsBoundable() {
        Toast.makeText(this, "I bind like butter", Toast.LENGTH_LONG).show();
    }

    public void sendMessage(String message) {
        try {

            Log.i("sendmsg", message);
            if (message.equals("ID"))
                j = 20;
            else if (message.equals("GROUP"))
                j = 21;
            else if (message.equals("EXIT"))
                j = 22;
            else if (message.equals("CNCL"))
                j = 23;
            else if (message.equals("CNCL1"))
                j = 24;
            else {
                j = 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("I am in on start");
        Runnable connect = new connectSocket();
        new Thread(connect).start();
        return START_STICKY;
    }


    class connectSocket extends Thread implements Runnable {
        @Override
        public void run() {
            try {
                String IP = "127.0.0.1";
                //socket = new Socket("10.2.83.196", 50142);
                socket=new Socket("192.168.43.122",50142);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // the following loop performs the exchange of
                // information between client and client handler
                while (true) {
                    System.out.println(dis.readUTF());

                    while (SocketService.tosend==null)
                    {
                        System.out.println();
                    }
                    dos.writeUTF(tosend);

                    // If client sends exit,close this connection
                    // and then break from the while loop
                    if (tosend.equals("Exit")) {
                        System.out.println("Closing this connection : " + socket);
                        socket.close();
                        System.out.println("Connection closed");
                        break;
                    }
                    tosend=null;
                    // printing date or time as requested by client
                    received = dis.readUTF();
                    System.out.println(received);
                }

                // closing resources

                dis.close();
                dos.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}


