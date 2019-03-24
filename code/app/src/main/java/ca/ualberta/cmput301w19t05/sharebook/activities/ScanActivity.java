package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;

public class ScanActivity extends AppCompatActivity {
    public static final int SCAN_BOOK = 2;

    private CameraKitView cameraKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraKitView = findViewById(R.id.scan_camera_view);
        cameraKitView.onStart();
    }

    public void confirmScanOnClick(View view) {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                processBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }

    private void processBitMap(Bitmap bitmap){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS).build();
        FirebaseVisionBarcodeDetector firebaseVisionBarcodeDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);
        firebaseVisionBarcodeDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size() == 0){
            Toast.makeText(this, "Cannot detect the Barcode. Please try again", Toast.LENGTH_SHORT).show();
        }

        for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes){
            if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_ISBN){
                Intent intent = new Intent();
                intent.putExtra("ISBN", barcode.getDisplayValue());
                setResult(Activity.RESULT_OK, intent);
                cameraKitView.onStop();
                finish();
                return;
            }
        }
        Toast.makeText(this, "The Barcode is not available. Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        cameraKitView.onStop();
        super.finish();
    }
}