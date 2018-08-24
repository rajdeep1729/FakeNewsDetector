package com.example.rajdeep.fakenewsdetetor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rajdeep.fakenewsdetector.R;
import com.github.ybq.android.spinkit.style.FadingCircle;

public class MainActivity extends AppCompatActivity {
    private int SPLASH_DISPLAY_LENGTH = 10000;
    ProgressBar progressBar;
    int counter=0;
    boolean flag=false;
    private SocketService mBoundService;
    private boolean mIsBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageView imageView  = findViewById(R.id.splashscreen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        imageView.startAnimation(animation);
        ImageView imageView1  = findViewById(R.id.brand);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        imageView1.startAnimation(animation1);

        progressBar = findViewById(R.id.spinkit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        startService(new Intent(MainActivity.this,SocketService.class));
        doBindService();

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
               /* if (SocketService.socket==null)
                {
                   // counter++;
                    //System.out.println(counter);
                    //if(counter>=1000)
                   // {
                       // Toast.makeText(getApplicationContext(),"Check your internet Connection",Toast.LENGTH_LONG).show();
                         //flag=1;
                         //break;
                    buildDialog(MainActivity.this).show();

                        //System.exit(1);

                }
               else {*/
                    Intent mainIntent = new Intent(MainActivity.this, FirstActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                //}
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Connecting to Server Failed.");
        builder.setMessage("Please Check your internet connection to use features of this APP");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<1000000;i++){}
                flag=true;
                dialog.dismiss();
            }
        });

        return builder;
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
        bindService(new Intent(MainActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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