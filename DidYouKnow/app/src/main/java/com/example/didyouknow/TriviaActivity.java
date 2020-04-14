package com.example.didyouknow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.didyouknow.models.CategoryModel;
import com.example.didyouknow.models.CategorySpinnerAdapter;
import com.example.didyouknow.models.InformationModel;
import com.example.didyouknow.receivers.NotificationsBroadcastReceiver;
import com.example.didyouknow.services.TriviaIntentService;

public class TriviaActivity extends Activity {

    public static final String CATEGORY_KEY = "TRIVIA_ACTIVITY_CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Button buttonPositive = findViewById(R.id.button_true);
        Button buttonNegative = findViewById(R.id.button_false);

        Intent intent = getIntent();
        final Long categoryId = intent.getLongExtra(CATEGORY_KEY, -1);

        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TriviaReceiver receiver = new TriviaReceiver(new Handler());
                TriviaIntentService.getInformation(TriviaActivity.this, categoryId, receiver);
            }
        });

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TriviaReceiver receiver = new TriviaReceiver(new Handler());
                TriviaIntentService.getInformation(TriviaActivity.this, categoryId, receiver);
            }
        });

        if(categoryId == -1) {
            Toast.makeText(this, "Invalid category!", Toast.LENGTH_SHORT);
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
        } else {
            TriviaReceiver receiver = new TriviaReceiver(new Handler());
            TriviaIntentService.getInformation(this, categoryId, receiver);
        }
    }

    private class TriviaReceiver extends ResultReceiver {
        public TriviaReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == RESULT_OK) {
                InformationModel triviaItem = (InformationModel) resultData.getParcelable(TriviaIntentService.RESULT_RECEIVER_INFORMATION);

                TextView textViewTitle = findViewById(R.id.textview_story_title);
                TextView textViewContent = findViewById(R.id.textview_story);

                textViewTitle.setText(triviaItem.title);
                textViewContent.setText(triviaItem.text);

                toggleContent(true);
            }
        }

        private void toggleContent(boolean isShown) {
            findViewById(R.id.textview_story_title).setVisibility(View.VISIBLE);
            findViewById(R.id.textview_story).setVisibility(View.VISIBLE);
            findViewById(R.id.scrollView2).setVisibility(View.VISIBLE);
            findViewById(R.id.button_true).setVisibility(View.VISIBLE);
            findViewById(R.id.button_false).setVisibility(View.VISIBLE);

            findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
        }
    }
}
