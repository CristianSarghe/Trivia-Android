package com.example.didyouknow.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.didyouknow.models.CategoryModel;
import com.example.didyouknow.models.InformationModel;
import com.example.didyouknow.services.api.TriviaService;

import java.util.InvalidPropertiesFormatException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class TriviaIntentService extends IntentService {
    private static final String GET_INFORMATION = "com.trivia.didyouknow.service.action.GET_INFORMATION";
    private static final String GET_CATEGORIES = "com.trivia.didyouknow.service.action.GET_CATEGORIES";
    private static final String POST_NEW_INFORMATION = "com.trivia.didyouknow.service.action.POST_NEW_INFORMATION";

    private static final String RECEIVER = "com.trivia.didyouknow.service.action.RESULT_RECEIVER";

    private static final String CATEGORY = "com.trivia.didyouknow.service.action.models.CATEGORY";
    private static final String INFORMATION = "com.trivia.didyouknow.service.action.models.INFORMATION";

    public static final String RESULT_RECEIVER_CATEGORY = "com.trivia.didyouknow.service.action.receivers.CATEGORY";
    public static final String RESULT_RECEIVER_INFORMATION = "com.trivia.didyouknow.service.action.receivers.INFORMATION";

    public TriviaIntentService() {
        super("TriviaIntentService");
    }

    public static void getInformation(Context context, long category, ResultReceiver receiver) {
        Intent intent = new Intent(context, TriviaIntentService.class);
        intent.setAction(GET_INFORMATION);
        intent.putExtra(CATEGORY, category);
        intent.putExtra(RECEIVER, receiver);
        context.startService(intent);
    }

    public static void getCategories(Context context, ResultReceiver receiver) {
        Intent intent = new Intent(context, TriviaIntentService.class);
        intent.setAction(GET_CATEGORIES);
        intent.putExtra(RECEIVER, receiver);
        context.startService(intent);
    }

    public static void postNewInformation(Context context, InformationModel model) {
        Intent intent = new Intent(context, TriviaIntentService.class);
        intent.setAction(POST_NEW_INFORMATION);
        intent.putExtra(INFORMATION, model);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (GET_INFORMATION.equals(action)) {
                final long category = intent.getLongExtra(CATEGORY, -1);
                final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);

                getApiInformation(category, receiver);

            }

            if(GET_CATEGORIES.equals(action)) {
                final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
                getApiCategories(receiver);
            }

            if(POST_NEW_INFORMATION.equals(action)) {
                final InformationModel model = intent.getParcelableExtra(INFORMATION);
                postApiNewInformation(model);
            }
        }
    }

    private void getApiInformation(long categoryId, final ResultReceiver receiver) {
        TriviaService service = getTriviaApiService();
        Call<InformationModel> informationCall = service.getInformation(categoryId);

        informationCall.enqueue(new Callback<InformationModel>() {
            @Override
            public void onResponse(Call<InformationModel> call, Response<InformationModel> response) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(RESULT_RECEIVER_INFORMATION, response.body());
                receiver.send(RESULT_OK, bundle);
            }

            @Override
            public void onFailure(Call<InformationModel> call, Throwable t) { }
        });
    }

    private void getApiCategories(final ResultReceiver receiver) {
        TriviaService service = getTriviaApiService();
        Call<CategoryModel[]> categoriesCall = service.getCategories();

        categoriesCall.enqueue(new Callback<CategoryModel[]>() {
            @Override
            public void onResponse(Call<CategoryModel[]> call, Response<CategoryModel[]> response) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArray(RESULT_RECEIVER_CATEGORY, response.body());
                receiver.send(RESULT_OK, bundle);
            }

            @Override
            public void onFailure(Call<CategoryModel[]> call, Throwable t) { }
        });
    }

    private void postApiNewInformation(InformationModel model) {
        TriviaService service = getTriviaApiService();
        Call<InformationModel> postUserInformation = service.postUserInformation(model);

        postUserInformation.enqueue(new Callback<InformationModel>() {
            @Override
            public void onResponse(Call<InformationModel> call, Response<InformationModel> response) { }

            @Override
            public void onFailure(Call<InformationModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed uploading your suggestion", Toast.LENGTH_LONG).show();
            }
        });
    }

    private TriviaService getTriviaApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cristiansarghe.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TriviaService.class);
    }
}
