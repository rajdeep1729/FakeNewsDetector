package com.example.rajdeep.fakenewsdetetor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajdeep.fakenewsdetector.R;

public class FirstActivity  extends AppCompatActivity {

    private
    SocketService mBoundService;
    private boolean mIsBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final EditText t=findViewById(R.id.editText);
        Button btn=findViewById(R.id.button);
        startService(new Intent(FirstActivity.this,SocketService.class));
        doBindService();
        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                SocketService.tosend=t.getText().toString();
                while(SocketService.received==null)
                {
                    System.out.println();
                }
                Toast.makeText(getApplicationContext(), SocketService.received, Toast.LENGTH_LONG).show();
                SocketService.received=null;
            }

        });

    }
    private ServiceConnection mConnection = new ServiceConnection() {
        //EDITED PART
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBoundService = ((SocketService.LocalBinder)service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBoundService = null;
        }

    };



    private void doBindService() {
        bindService(new Intent(FirstActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        if(mBoundService!=null){
            mBoundService.IsBoundable();
        }
    }


    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
