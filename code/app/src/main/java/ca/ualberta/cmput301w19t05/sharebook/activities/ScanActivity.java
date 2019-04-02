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
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;

/**
 * A scan screen that open the camera to scan the ISBN of the book to search the information od the book.
 */

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

    /**
     * Use camera
     * Reference: https://camerakit.io/docs
     */
    public void confirmScanOnClick(View view) {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                processBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }

    /**
     * Detect the Barcode
     * Reference: https://firebase.google.com/docs/ml-kit/android/read-barcodes
     */
    private void processBitMap(Bitmap bitmap){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
        detector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcode) {
                        processResult(firebaseVisionBarcode);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Check the Barcode and return the ISBN
     */
    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcode) {
        // No Barcode was detected
        if (firebaseVisionBarcode.size() == 0){
            Toast.makeText(this, "Cannot detect the Barcode. Please try again", Toast.LENGTH_SHORT).show();
        }

        for (FirebaseVisionBarcode barcode : firebaseVisionBarcode){
            if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_ISBN){
                Intent intent = new Intent();
                intent.putExtra("ISBN", barcode.getDisplayValue());
                setResult(Activity.RESULT_OK, intent);
                cameraKitView.onStop();
                finish();
                return;
            }
        }

        // Other situation: The Barcode is not available
        Toast.makeText(this, "The Barcode is not available. Please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        cameraKitView.onStop();
        super.finish();
    }
}