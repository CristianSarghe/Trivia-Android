package com.example.didyouknow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.didyouknow.models.CategoryModel;
import com.example.didyouknow.models.CategorySpinnerAdapter;
import com.example.didyouknow.models.InformationModel;
import com.example.didyouknow.services.TriviaIntentService;

public class SuggestionActivity extends Activity {

    private Spinner categorySpinner = null;
    private CategoriesReceiver receiver = new CategoriesReceiver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        categorySpinner = findViewById(R.id.spinner);

        final Button submitButton = findViewById(R.id.button_submit_suggestion);
        final EditText suggestionText = findViewById(R.id.edit_text_suggestion);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(TextUtils.isEmpty(suggestionText.getText().toString().trim())) {
                Toast.makeText(SuggestionActivity.this, "You may not send empty suggestions!", Toast.LENGTH_LONG).show();
            } else {
                InformationModel model = new InformationModel();
                model.categoryId = categorySpinner.getSelectedItemId();
                model.text = suggestionText.getText().toString();

                TriviaIntentService.postNewInformation(SuggestionActivity.this, model);

                applySubmitUiChanges();
                Toast.makeText(SuggestionActivity.this, "Suggestion submitted! Thank you!", Toast.LENGTH_LONG).show();

            }
            }
        });

        TriviaIntentService.getCategories(this, receiver);
    }

    private class CategoriesReceiver extends ResultReceiver {
        public CategoriesReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == RESULT_OK) {
                CategoryModel[] categories = (CategoryModel[])resultData.getParcelableArray(TriviaIntentService.RESULT_RECEIVER_CATEGORY);

                if(categorySpinner != null) {
                    categorySpinner.setAdapter(new CategorySpinnerAdapter(SuggestionActivity.this, categories));
                }
            }
        }
    }

    private void applySubmitUiChanges() {
        final Button submitButton = findViewById(R.id.button_submit_suggestion);
        final EditText suggestionText = findViewById(R.id.edit_text_suggestion);

        if(categorySpinner != null) {
            categorySpinner.setEnabled(false);
        }

        suggestionText.setEnabled(false);

        submitButton.setEnabled(false);
        submitButton.setText("Submitted");
        submitButton.setAlpha((float)0.4);
    }
}
