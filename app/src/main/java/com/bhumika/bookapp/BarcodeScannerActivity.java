package com.bhumika.bookapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class BarcodeScannerActivity extends AppCompatActivity implements Detector.Processor<Barcode> {

    SurfaceView cameraView;
    TextView codeResult;

    CameraSource cameraSource;

    public static final int SCAN_SUCCESSFUL = 1;
    public static final String SCAN_RESULT = "scan_result";
    public static final int PERMISSION_NOT_GRANTED = 2;
    public static final int SCAN_CANCELLED = 3;
    public static final int PLAY_SERVICES_ERROR = 4;
    public static final int UNKNOWN_ERROR = 5;

    private int PLAY_SERVICES_RESULT = 555;
    public static final int PERMISSION_ACCESS_CAMERA = 453;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if(checkCameraPermission()) {
            setupCamera();
        }

    }

    void setupCamera() {

        setContentView(R.layout.activity_barcode_scanner);

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        codeResult = (TextView) findViewById(R.id.codeResult);

        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE | Barcode.EAN_8 | Barcode.EAN_13 | Barcode.UPC_A
                        | Barcode.UPC_E | Barcode.CODE_39 | Barcode.CODE_93 |
                        Barcode.CODE_128 | Barcode.CODABAR | Barcode.ALL_FORMATS)
                .build();
        if (!detector.isOperational()) {
            checkPlayServices();
            finish();
            return;
        }

        detector.setProcessor(this);

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(300, 300)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(mSurfaceCallback);
    }

    SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                if(checkCameraPermission()) {
                    cameraSource.start(cameraView.getHolder());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            cameraSource.stop();
        }
    };

    boolean checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(BarcodeScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BarcodeScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_ACCESS_CAMERA);
            return false;
        } else {
            return true;
        }
    }

    void checkPlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            setResult(UNKNOWN_ERROR);
            finish();
        } else {
            api.getErrorDialog(this, code, PLAY_SERVICES_RESULT, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    setResult(PLAY_SERVICES_ERROR);
                    finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setupCamera();


                } else {
                    setResult(PERMISSION_NOT_GRANTED);
                    finish();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PLAY_SERVICES_RESULT)
        {
            Log.d("PLAY_SERVICES_RESULT", data.getDataString());
        }
    }

    /**
     * Detector.Processor Interface Method
     */
    @Override
    public void release() {

    }

    /**
     * Detector.Processor Interface Method
     */
    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if(barcodes.size() > 0){
            codeResult.post(new Runnable() {
                @Override
                public void run() {
                    cameraSource.stop();//
                    //codeResult.setText( barcodes.valueAt(0).displayValue );//
                    Intent intent = new Intent();
                    intent.putExtra(SCAN_RESULT, barcodes.valueAt(0).displayValue);//
                    setResult(SCAN_SUCCESSFUL, intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        setResult(SCAN_CANCELLED);
        finish();
    }
}
