package edu.uw.tcss450.nutrack.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import edu.uw.tcss450.nutrack.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void handleResult(Result result) {
        if (result.getText().length() == 12) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("barcode", result.getText());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            finish();
            Toast.makeText(this, "The barcode you want to scan is not working. Please try another barcode.", Toast.LENGTH_SHORT).show();
        }

    }
}
