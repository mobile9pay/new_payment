package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsInland;
import com.npsdk.module.NPayLibrary;
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
            Call<CreateOrderCardModel> call = apiService.createOrderCardInland(param.getUrl(),
                    param.getCardNumber().replaceAll(" ", ""),
                    param.getCardName().trim(), param.getExpireMonth(), param.getExpireYear(),
                    param.getAmount(), param.getMethod(), param.getSaveToken());
            enqueue(call, new Callback<CreateOrderCardModel>() {
                @Override
                public void onResponse(Call<CreateOrderCardModel> call, Response<CreateOrderCardModel> response) {
                    if (response.code() == 200 && response.body() != null) {
                        updateUI(() -> {
                            callbackCreateOrder.onSuccess(response.body());
                        });
                    } else {
                        NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi xảy ra, code 1002");
                    }
                }

                @Override
                public void onFailure(Call<CreateOrderCardModel> call, Throwable t) {
                    NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi xảy ra, code 1002");
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}