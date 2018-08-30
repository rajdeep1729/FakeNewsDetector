package com.example.rajdeep.fakenewsdetetor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.github.ybq.android.spinkit.style.FadingCircle;
public class FirstActivity  extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private SocketService mBoundService;
    private boolean mIsBound;
    private String postUrl = "http://api.androidhive.info/webview/index.html";
    Set <String> url;
    ProgressBar progressBar;
    MyasyncTask myasynTask;
    AutoCompleteTextView t;
    String prevname;
    String s;
    SharedPreferences.Editor appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        t=findViewById(R.id.editText);
        final TextView t1=findViewById(R.id.editText1);
        myasynTask = new MyasyncTask();

        progressBar = findViewById(R.id.spinkit1);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        //progressBar.setVisibility(View.VISIBLE);

        t1.setVisibility(View.GONE);
        final Button btn=findViewById(R.id.button);

        Context context = FirstActivity.this;
        appPrefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
       // appPrefs.putString("url", "Elena");
        //appPrefs.apply();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         prevname = prefs.getString("url", "No name defined");
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

                    String [] notuniqueurls=TextUtils.split(prevname,",");
                    url=new HashSet<String>(Arrays.asList(notuniqueurls));

                    String [] urls=url.toArray(new String[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (FirstActivity.this, android.R.layout.select_dialog_item, urls);
                    //t.setThreshold(0);//will start working from first character
                    t.setAdapter(adapter);
                    t.showDropDown();

                }
            }
        });


        t.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    btn.performClick();
                }
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                System.out.println(SocketService.flag);
                if(SocketService.flag == 1)
                {
                    buildDialog(FirstActivity.this).show();
                    System.out.println("No connection");
                }
                else {
                    new MyasyncTask().execute();
                }

            }

        });

    }

   protected class MyasyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(progressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

                s = t.getText().toString();

                if (s.startsWith("http://") || s.startsWith("https://"))
                    SocketService.tosend = s;
                else {
                    SocketService.tosend = "http://" + s;

                }
                while (SocketService.received.equals("") || SocketService.j == 1) {
                    System.out.println();
                }
                // progressBar.setVisibility(View.INVISIBLE);

                //openInAppBrowser(postUrl);

            return null;
        }
        @Override
        protected void onPostExecute(Void a) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(getApplicationContext(), SocketService.received, Toast.LENGTH_LONG).show();
            SocketService.received = "";
            SocketService.tosend = null;
            SocketService.j = 1;
            postUrl = SocketService.tosend;
            if (prevname != null && !(url.contains(s)))
                appPrefs.putString("url", s + "," + prevname);
            else
                appPrefs.putString("url", s + ",");
            appPrefs.apply();
        }
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Connecting to Server Failed.");
        builder.setMessage("Please Check your internet connection to use features of this APP");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
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
