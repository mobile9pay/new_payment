package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsInland;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateOrderInlandRepo extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void create(Context context, CreateOrderParamsInland param, CallbackCreateOrder callbackCreateOrder) {
        executor.execute(() -> {
            Call<CreateOrderCardModel> call = apiService.createOrderCardInland(param.getUrl(), param.getCardNumber().replaceAll(" ", ""), param.getCardName().trim(), param.getExpireMonth(), param.getExpireYear(), param.getAmount(), param.getMethod());
            enqueue(call, new Callback<CreateOrderCardModel>() {
                @Override
                public void onResponse(Call<CreateOrderCardModel> call, Response<CreateOrderCardModel> response) {
                    if (response != null && response.body() != null) {
                        updateUI(() -> {
                            callbackCreateOrder.onSuccess(response.body());
                        });
                    } else {
                        System.out.println("SERVER ERROR");
                    }
                }

                @Override
                public void onFailure(Call<CreateOrderCardModel> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(context, "Đã có lỗi xảy ra, code 1002", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}