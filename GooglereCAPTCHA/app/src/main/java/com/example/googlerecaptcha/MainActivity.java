package com.example.googlerecaptcha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import static com.google.android.gms.safetynet.SafetyNetApi.*;

//Google 機器人驗證
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private Button btn_robot;
    private TextView test_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_robot = (Button)findViewById(R.id.btn);
        test_msg = (TextView)findViewById(R.id.tv);

        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        btn_robot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(0);
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, "6LdXMd0ZAAAAANzU4x4SjdNRzb-HS4FhDSZWdNoE")
                        .setResultCallback(new ResultCallbacks<SafetyNetApi.RecaptchaTokenResult>() {
                            @Override
                            public void onSuccess(@NonNull RecaptchaTokenResult recaptchaTokenResult) {
                                final Status status = recaptchaTokenResult.getStatus();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if ((status != null) && status.isSuccess()){
                                            setText(1);
                                        }else{
                                            setText(0);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@NonNull Status status) {

                            }
                        });
            }
        });

}

    private void setText(int i) {
        if (i == 1){
            test_msg.setText("驗證通過");
            test_msg.setTextColor(Color.GREEN);
        }else{
            test_msg.setText("驗證失敗");
            test_msg.setTextColor(Color.RED);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Google Services 連線成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Google Services 連線中斷，連線中斷代號 :" + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Services 連線失敗，原因 :" + connectionResult.getErrorMessage(),
                Toast.LENGTH_SHORT).show();
    }
}