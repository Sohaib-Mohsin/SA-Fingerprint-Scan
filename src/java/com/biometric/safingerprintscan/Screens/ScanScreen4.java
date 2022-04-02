package com.biometric.safingerprintscan.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Scanner.Fingerprint;
import com.biometric.safingerprintscan.Scanner.Status;

public class ScanScreen4 extends AppCompatActivity {

    TextView Status_txt;
    Fingerprint fingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_screen4);

        Status_txt = findViewById(R.id.status);
        fingerprint = new Fingerprint();

    }
    @Override
    protected void onStart() {
        try{
            fingerprint.scan(ScanScreen4.this, printHandler, updateHandler);
        }
        catch(Exception ex){
            Status_txt.setText(ex.getLocalizedMessage());
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        fingerprint.turnOffReader();
        super.onStop();
    }

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int status = msg.getData().getInt("status");
            switch (status) {
                case Status.INITIALISED:
                    Status_txt.setText("Setting up reader");
                    break;
                case Status.SCANNER_POWERED_ON:
                    Status_txt.setText("Reader powered on");
                    break;
                case Status.READY_TO_SCAN:
                    Status_txt.setText("Ready to scan finger");
                    break;
                case Status.FINGER_DETECTED:
                    Status_txt.setText("Finger detected");
                    break;
                case Status.RECEIVING_IMAGE:
                    Status_txt.setText("Receiving image");
                    break;
                case Status.FINGER_LIFTED:
                    Status_txt.setText("Finger has been lifted off reader");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    Status_txt.setText("Reader is off");
                    break;
                case Status.SUCCESS:
                    Status_txt.setText("Fingerprint successfully captured");
                    break;
                case Status.ERROR:
                    Status_txt.setText("Error");
                    Status_txt.setText(msg.getData().getString("errorMessage"));
                    break;
                default:
                    Status_txt.setText(String.valueOf(status)+" "+msg.getData().getString("errorMessage"));
                    break;

            }
        }
    };

    Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            byte[] image;
            String errorMessage = "empty";
            int status = msg.getData().getInt("status");
            Intent intent = new Intent();
            intent.putExtra("status", status);
            if (status == Status.SUCCESS) {
                image = msg.getData().getByteArray("img");
                intent.putExtra("img", image);
            } else {
                errorMessage = msg.getData().getString("errorMessage");
                intent.putExtra("errorMessage", errorMessage);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    };

}