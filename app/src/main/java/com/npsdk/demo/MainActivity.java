package com.npsdk.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.npsdk.LibListener;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.PaymentMethod;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.utils.Actions;
import com.npsdk.module.utils.Flavor;
import com.npsdk.module.utils.JsHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivityLOG";
    TextView userInfo;
    TextView txtMoney;
    WebView webViewGate;
    LinearLayout layoutGate;
    boolean isShow = false;
    Toolbar toolbar;
    EditText edtUrlPaygate;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llRutTien = findViewById(R.id.ll_quet_ma);
        LinearLayout llNapTien = findViewById(R.id.ll_nap_tien);
        LinearLayout llChuyenTien = findViewById(R.id.ll_chuyen_tien);
        LinearLayout llTienIch = findViewById(R.id.ll_lich_su);
        LinearLayout llMuaTheDt = findViewById(R.id.ll_thanh_toan_hoa_don);
        LinearLayout llNapTheDT = findViewById(R.id.ll_nap_tien_dt);
        LinearLayout llNapData = findViewById(R.id.ll_mua_the_game);
        LinearLayout ll_mua_the_dich_vu = findViewById(R.id.ll_mua_the_dich_vu);
        LinearLayout ll_mua_the_dt = findViewById(R.id.ll_mua_the_dt);
        LinearLayout ll_nap_data = findViewById(R.id.ll_nap_data);
        LinearLayout testClick = findViewById(R.id.test_click);
        LinearLayout layout_sdv = findViewById(R.id.layout_sdv);
        RelativeLayout rlInfo = findViewById(R.id.rl_info);
        ImageView btn_eyes = findViewById(R.id.btn_eyes);
        Button btn_bank_link_manage = findViewById(R.id.btn_bank_link_manage);
        Button btn_bank_link_add = findViewById(R.id.btn_bank_link_add);
        edtUrlPaygate = findViewById(R.id.edt_url_paygate);
        Button btnThanhToan = findViewById(R.id.btn_thanh_toan);
        Button btnThanhToan2 = findViewById(R.id.btn_thanh_toan2);
        Button btnThanhToan3 = findViewById(R.id.btn_thanh_toan3);
        Button btnThanhToan4 = findViewById(R.id.btn_thanh_toan4);
        View btnClose = findViewById(R.id.btnClose);
        txtMoney = findViewById(R.id.txt_money);
        userInfo = findViewById(R.id.txt_name);
        webViewGate = findViewById(R.id.webView_gate);
        layoutGate = findViewById(R.id.layout_web_gate);
        toolbar = findViewById(R.id.toolbar);
        btnClose = findViewById(R.id.btnClose);

        txtMoney.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        llRutTien.setOnClickListener(this);
        llNapTien.setOnClickListener(this);
        llChuyenTien.setOnClickListener(this);
        llTienIch.setOnClickListener(this);
        llMuaTheDt.setOnClickListener(this);
        llNapTheDT.setOnClickListener(this);
        llNapData.setOnClickListener(this);
        ll_mua_the_dich_vu.setOnClickListener(this);
        ll_mua_the_dt.setOnClickListener(this);
        ll_nap_data.setOnClickListener(this);
        btn_bank_link_manage.setOnClickListener(this);
        btn_bank_link_add.setOnClickListener(this);
        layout_sdv.setOnClickListener(this);
        btn_eyes.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnThanhToan.setOnClickListener(this);
        btnThanhToan2.setOnClickListener(this);
        btnThanhToan3.setOnClickListener(this);
        btnThanhToan4.setOnClickListener(this);
        testClick.setOnClickListener(this);
        // Create flavor by packagen name test
        String flavorEnv = Flavor.setEnvTest(this);
        Bundle bundle = getIntent().getExtras();
        String mcCode = ""; // uymvnd
        String colorCode = ""; // 15AE62
        String secretKey = "vIPldW/y/VJuy8qKEQUoH9ypHTTt9W/8ufvn3BFFTBU="; // vIPldW/y/VJuy8qKEQUoH9ypHTTt9W/8ufvn3BFFTBU=
        if (bundle != null) {
            colorCode = bundle.getString("color_code");
            mcCode = bundle.getString("merchant_code");
            secretKey = bundle.getString("secret_key");
        }
        SdkConfig sdkConfig = new SdkConfig.Builder(this).merchantCode(mcCode).secretKey(secretKey).uid(null).env(flavorEnv).brandColor(colorCode).build();
        initSdk(sdkConfig);
    }


    private void initSdk(SdkConfig sdkConfig) {
        NPayLibrary.getInstance().init(MainActivity.this, sdkConfig, new LibListener() {

            @Override
            public void getInfoSuccess(String jsonData) {
                System.out.println(jsonData);
            }

            @Override
            public void onError(int errorCode, String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogoutSuccessful() {
                Toast.makeText(MainActivity.this, "Logout success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseSDK() {
                Toast.makeText(MainActivity.this, "onCloseSDK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void sdkDidComplete(String name, Object status, @Nullable Object params) {
                Toast.makeText(MainActivity.this, name + " " + status, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void backToAppFrom(String screen) {
                System.out.println(screen);
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String url = edtUrlPaygate.getText().toString();
        if (url.isEmpty() && NPayLibrary.getInstance().sdkConfig.getEnv().contains("staging"))
            url = "https://stg-api.pgw.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6IlZNNzE0RyIsInRpbWUiOjE2NzcxMjM3ODcsImludm9pY2Vfbm8iOiJCb29raW5nTHZ5cmpScnQiLCJhbW91bnQiOjIwMDAwLCJkZXNjcmlwdGlvbiI6IlRoYW5oIHRvYW4gZG9uIGhhbmcgQm9va2luZ0x2eXJqUnJ0IiwicmV0dXJuX3VybCI6Imh0dHBzOi8vcXAuc3Bob3Rvbi5jb20vYXBpL3YxL3BheW1lbnQvY29tcGxldGUtdHJhbnNhY3Rpb24iLCJiYWNrX3VybCI6Imh0dHA6Ly9xcC50ZXN0L2FwaS92My9jdXN0b21lci9ib29raW5nIiwibGFuZyI6ImVuIiwic2F2ZV90b2tlbiI6MCwiaXNfY3VzdG9tZXJfcGF5X2ZlZSI6MX0%3D&signature=eUtKetwGRFgoIJ5zwADzU7KjuIwlPK4RKq9IO5fL6so%3D";
        switch (v.getId()) {
            case R.id.ll_quet_ma:
                Log.d(TAG, "onClick: ll_rut_tien");
                NPayLibrary.getInstance().openSDKWithAction(Actions.QR);
                break;
            case R.id.ll_nap_tien:
                Log.d(TAG, "onClick: ll_nap_tien");
                NPayLibrary.getInstance().openSDKWithAction(Actions.DEPOSIT);
                break;
            case R.id.ll_chuyen_tien:
                NPayLibrary.getInstance().openSDKWithAction(Actions.TRANSFER);
                Log.d(TAG, "onClick: ll_chuyen_tien");
                break;
            case R.id.ll_lich_su:
                Log.d(TAG, "onClick: ll_lich_su");
                NPayLibrary.getInstance().openSDKWithAction(Actions.HISTORY);
                break;
            case R.id.ll_thanh_toan_hoa_don:
                Log.d(TAG, "onClick: ll_thanh_toan_hoa_don");
                NPayLibrary.getInstance().openSDKWithAction(Actions.BILLING);
                break;
            case R.id.ll_nap_tien_dt:
                Log.d(TAG, "onClick: ll_nap_tien_dt");
                NPayLibrary.getInstance().openSDKWithAction(Actions.TOPUP);
                break;
            case R.id.ll_mua_the_game:
                Log.d(TAG, "onClick: ll_mua_the_game");
                NPayLibrary.getInstance().openSDKWithAction(Actions.GAME);
                break;
            case R.id.ll_mua_the_dich_vu:
                Log.d(TAG, "onClick: ll_mua_the_dich_vu");
                NPayLibrary.getInstance().openSDKWithAction(Actions.SERVICE_CARD);
                break;
            case R.id.ll_mua_the_dt:
                Log.d(TAG, "onClick: ll_mua_the_dt");
                NPayLibrary.getInstance().openSDKWithAction(Actions.PHONE_CARD);
                break;
            case R.id.ll_nap_data:
                Log.d(TAG, "onClick: ll_nap_data");
                NPayLibrary.getInstance().openSDKWithAction(Actions.DATA_CARD);
                break;
            case R.id.btn_bank_link_manage:
                Log.d(TAG, "onClick: btn_bank_link_manage");
//                NPayLibrary.getInstance().openWallet(Actions.BANK_LINK_MANAGE);
                break;
            case R.id.btn_bank_link_add:
                Log.d(TAG, "onClick: btn_bank_link_add");
//                NPayLibrary.getInstance().openWallet(Actions.ADD_LINK_BANK);
                break;
            case R.id.btn_eyes:
                Log.d(TAG, "onClick: btn_eyes");
                if (isShow) {
                    txtMoney.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    txtMoney.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
                isShow = !isShow;
                break;
            case R.id.layout_sdv:
            case R.id.rl_info:
                Log.d(TAG, "onClick: layout_sdv");
                NPayLibrary.getInstance().openSDKWithAction(Actions.OPEN_WALLET);
                break;
            case R.id.btnClose:
                layoutGate.setVisibility(View.GONE);
                break;
            case R.id.btn_thanh_toan:
                //paste url thanh toán vào hàm pay

                NPayLibrary.getInstance().openPaymentOnSDK(url, PaymentMethod.WALLET, DataOrder.Companion.isShowResultScreen());
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan2:
                NPayLibrary.getInstance().openPaymentOnSDK(url, PaymentMethod.ATM_CARD, DataOrder.Companion.isShowResultScreen());
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan3:
                //paste url thanh toán vào hàm pay
                NPayLibrary.getInstance().openPaymentOnSDK(url, PaymentMethod.CREDIT_CARD, DataOrder.Companion.isShowResultScreen());
                break;
            case R.id.btn_thanh_toan4:
                //paste url thanh toán vào hàm pay
                NPayLibrary.getInstance().openPaymentOnSDK(url, PaymentMethod.DEFAULT, DataOrder.Companion.isShowResultScreen());
                break;
            case R.id.test_click:
//                NPayLibrary.getInstance().getUserInfo();
//                NPayLibrary.getInstance().openWallet(url);
//                String old = Preference.getString(this, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
//                Preference.save(this, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN, old + "a");
//                Intent i = new Intent(this, WebviewComposeActivity.class);
//                i.putExtra("url", "https://zing.vn");
//                startActivity(i);

//                Intent i = new Intent(this, TestWebviewActivity.class);
//                startActivity(i);

//                new JsHandler(this).getClipboardData();
//                NPayLibrary.getInstance().openSDKWithAction("https://stg-sdk.9pay.mobi/v1/viet-qr");
                NPayLibrary.getInstance().openSDKWithAction(Actions.WITHDRAW);
                break;
        }
        edtUrlPaygate.setText("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private final CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}