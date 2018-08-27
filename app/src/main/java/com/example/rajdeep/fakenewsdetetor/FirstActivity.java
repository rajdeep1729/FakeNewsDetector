package com.example.rajdeep.fakenewsdetetor;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rajdeep.fakenewsdetector.R;

public class FirstActivity  extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private SocketService mBoundService;
    private boolean mIsBound;
    private String postUrl = "http://api.androidhive.info/webview/index.html";
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    private ImageView imgHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final AutoCompleteTextView t=findViewById(R.id.editText);
        final TextView t1=findViewById(R.id.editText1);

        t1.setVisibility(View.GONE);
        Button btn=findViewById(R.id.button);

        Context context = FirstActivity.this;
        @SuppressLint("CommitPrefEdits")
        final SharedPreferences.Editor appPrefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
       // appPrefs.putString("url", "Elena");
        //appPrefs.apply();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String prevname = prefs.getString("url", "No name defined");
        //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item);
        doBindService();
        SocketService.j=1;
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setText("");
                t1.setVisibility(View.VISIBLE);
                if(prevname!=null) {
                    String [] urls=TextUtils.split(prevname,",");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (FirstActivity.this, android.R.layout.select_dialog_item,urls);
                    //t.setThreshold(0);//will start working from first character
                    t.setAdapter(adapter);
                    t.showDropDown();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                String s=t.getText().toString();
                if(s.startsWith("http://")|| s.startsWith("https://"))
                    SocketService.tosend=s;
                else
                {
                    SocketService.tosend="http://"+s;

                }
              while(SocketService.received.equals("") || SocketService.j==1)
                {
                    System.out.println();
                }
                Toast.makeText(getApplicationContext(), SocketService.received, Toast.LENGTH_LONG).show();
                SocketService.received="";
                SocketService.tosend=null;
                SocketService.j=1;
                postUrl=SocketService.tosend;
                if(prevname!=null)
                    appPrefs.putString("url", s+","+prevname);
                else
                    appPrefs.putString("url",s+",");
                appPrefs.apply();
                //openInAppBrowser(postUrl);

            }

        });

    }



    private void openInAppBrowser(String url) {
        Intent intent = new Intent(FirstActivity.this, BrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
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
