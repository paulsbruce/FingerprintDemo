package io.paulsbruce.dev.fingerprintdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.paulsbruce.dev.fingerprintdemo.R;
import io.paulsbruce.dev.fingerprintdemo.fingerprint.MyFingerprintManager;

/**
 * Created by paul on 6/19/17.
 */

public class FingerprintActivity extends AppCompatActivity {

    private TextView textView;

    public static boolean canFingerprint(Activity caller) {
        MyFingerprintManager mfm = new MyFingerprintManager(caller);
        return mfm.initFingerprint() == MyFingerprintManager.FingerprintInitResult.CAN_FINGERPRINT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        textView = (TextView) findViewById(R.id.errorText);

        MyFingerprintManager mfm = new MyFingerprintManager(this);
        MyFingerprintManager.FingerprintInitResult init = mfm.initFingerprint();

        switch(mfm.initFingerprint()) {
            case CAN_FINGERPRINT:

                mfm.startAuth();
                break;

            case HARDWARE_NOT_DETECTED:
                textView.setText("Your Device does not have a Fingerprint Sensor"); break;
            case FINGERPRINT_AUTH_NOT_PERMITTED:
                textView.setText("Fingerprint authentication permission not enabled"); break;
            case NO_FINGERPRINTS_ENROLLED:
                textView.setText("Register at least one fingerprint in Settings"); break;
            case LOCK_SCREEN_NOT_ENABLED:
                textView.setText("Lock screen security not enabled in Settings"); break;
            default:
                textView.setText("Unknown fingerprint subsystem");
        }
    }

}
