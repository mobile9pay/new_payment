package com.npsdk.jetpack_sdk.repository;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.npsdk.jetpack_sdk.base.api.BaseApiClient;
import com.npsdk.jetpack_sdk.base.api.EncryptServiceHelper;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderCardModel;
import com.npsdk.jetpack_sdk.repository.model.CreateOrderParamsInland;
import com.npsdk.module.NPayLibrary;
import org.bouncycastle.util.encoders.DecoderException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateOrderInlandRepo extends BaseApiClient {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainThread = new Handler(Looper.getMainLooper());

    public void create(Context context, CreateOrderParamsInland param, CallbackCreateOrder callbackCreateOrder) {
        executor.execute(() -> {
            param.setCardNumber(param.getCardNumber().replaceAll(" ", ""));
            String key = EncryptServiceHelper.INSTANCE.getRandomkeyRaw();

            Map<String, String> mapEncrypt = new HashMap<>();
            mapEncrypt.put("data", param.getUrl());
            mapEncrypt.put("card_number", param.getCardNumber());
            mapEncrypt.put("card_name", param.getCardName());
            mapEncrypt.put("expire_month", param.getExpireMonth());
            mapEncrypt.put("expire_year", param.getExpireYear());
            mapEncrypt.put("amount", param.getAmount());
            mapEncrypt.put("method", param.getMethod());
            mapEncrypt.put("save_token", param.getSaveToken().toString());

            String jsonRaw = new Gson().toJson(mapEncrypt);
            System.out.println(jsonRaw);
            String jsonString = EncryptServiceHelper.INSTANCE.encryptKeyAesBase64(jsonRaw, key);

            Call<String> call = apiService.createOrderCardInland(key, jsonString);
            enqueue(call, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200 && response.body() != null) {

                        try {
                            String objectDecrypt = EncryptServiceHelper.INSTANCE.decryptAesBase64(
                                    response.body(),
                                    EncryptServiceHelper.INSTANCE.getRandomkeyRaw());
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            CreateOrderCardModel createOrderCardModel = gson.fromJson(objectDecrypt, CreateOrderCardModel.class);
                            updateUI(() -> {
                                callbackCreateOrder.onSuccess(createOrderCardModel);
                            });
                        } catch (JsonSyntaxException e) {
                            NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi phân tích cú pháp create payment.");
                        } catch (DecoderException e) {
                            NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi phân tích cú pháp json");
                        }
                    } else {
                        NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi xảy ra, code 1002");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    NPayLibrary.getInstance().callbackError(1002, "Đã có lỗi xảy ra, code 1002");
                }
            });
        });

    }

    private void updateUI(Runnable runnable) {
        mainThread.post(runnable);
    }
}