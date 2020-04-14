package com.example.didyouknow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        final TextView textViewPhone = findViewById(R.id.textview_phone);
        final TextView textViewEmail = findViewById(R.id.textview_email);

        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + textViewPhone.getText()));
                startActivity(intent);
            }
        });

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
                sendEmailIntent.setType("text/html");
                sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, textViewEmail.getText());
                startActivity(sendEmailIntent);
            }
        });
    }
}
