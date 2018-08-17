package com.example.rajdeep.fakenewsdetetor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajdeep.fakenewsdetector.R;

public class FirstActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final EditText t=findViewById(R.id.editText);
        Button btn=findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                String message=t.getText().toString();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

        });

    }
}
