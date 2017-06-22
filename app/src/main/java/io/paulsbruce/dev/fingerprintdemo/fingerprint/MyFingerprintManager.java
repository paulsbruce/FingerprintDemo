package io.paulsbruce.dev.fingerprintdemo.fingerprint;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import io.paulsbruce.dev.fingerprintdemo.fingerprint.FingerprintHandler;

/**
 * Created by paul on 6/19/17.
 */

public class MyFingerprintManager
{
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "fingerprintDemo";
    private Cipher cipher;
    private Activity activity;
    // Initializing both Android Keyguard Manager and Fingerprint Manager
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;

    public enum FingerprintInitResult {
        HARDWARE_NOT_DETECTED,
        FINGERPRINT_AUTH_NOT_PERMITTED,
        NO_FINGERPRINTS_ENROLLED,
        LOCK_SCREEN_NOT_ENABLED,
        CAN_FINGERPRINT
    }

    public MyFingerprintManager(Activity activity) {
        this.activity = activity;

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        keyguardManager = (KeyguardManager) activity.getSystemService(activity.KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) activity.getSystemService(activity.FINGERPRINT_SERVICE);
    }

    public FingerprintInitResult initFingerprint() {

        // Check whether the device has a Fingerprint sensor.
        if(fingerprintManager.isHardwareDetected())
        {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return FingerprintInitResult.FINGERPRINT_AUTH_NOT_PERMITTED;
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    return FingerprintInitResult.NO_FINGERPRINTS_ENROLLED;
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        return FingerprintInitResult.LOCK_SCREEN_NOT_ENABLED;
                    }else{
                        generateKey();

                        if (cipherInit()) {
                            return FingerprintInitResult.CAN_FINGERPRINT;
                        }
                    }
                }
            }
        }

        /**
         * An error message will be displayed if the device does not contain the fingerprint hardware.
         * However if you plan to implement a default authentication method,
         * you can redirect the user to a default authentication activity from here.
         * Example:
         * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
         * startActivity(intent);
         */
        return FingerprintInitResult.HARDWARE_NOT_DETECTED;
    }

    public void startAuth() {
        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
        FingerprintHandler helper = new FingerprintHandler(activity);
        helper.startAuth(fingerprintManager, cryptoObject);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
