package com.example.amazons3_with_cognito;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketAccelerateStatus;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadTask obj=new UploadTask();
        obj.execute();
        /*AmazonS3 s3Client = new AmazonS3Client(new BasicSessionCredentials("AKIAYHKVHTY54LUDTGXT", "xvIDMvCylPCx27cHzjEE4/MjsCssDPTobG4PC/jg"," "));
        s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        TransferUtility transferUtility = new TransferUtility(s3Client, getApplicationContext());
        TransferObserver transferObserver = transferUtility.upload("snaptoktempuploads",
                " ",
                new File(Environment.getExternalStorageDirectory().toString()+"/what/hi.mp4"),
                CannedAccessControlList.PublicRead);

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                //Implement the code for handle the file status changed.
                if (state.equals(TransferState.COMPLETED)) {
                    //Success
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Log.d("Token","Success");
                } else if (state.equals(TransferState.FAILED)) {
                    //Failed
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("Token","Failed");
                }
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                //Implement the code to handle the file uploaded progress.
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                Log.d("progress", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception exception) {

                //Implement the code to handle the file upload error.
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.d("error","Failed");
            }
        });*/

    }

    class UploadTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext(),
                        "ap-south-1:71fba7a5-01b1-402a-9c3e-3280f6d916d1", // Identity pool ID
                        Regions.AP_SOUTH_1 // Region
                );

                AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                s3.setBucketAccelerateConfiguration("snaptoktempuploads", new BucketAccelerateConfiguration(BucketAccelerateStatus.Enabled));
                TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());
                final TransferObserver observer = transferUtility.upload(
                        "snaptoktempuploads",  //this is the bucket name on S3
                        "SnapTok By Vedant with accelerated configuration", //this is the path and name
                        new File(Environment.getExternalStorageDirectory().toString() + "/what/hi.mp4"), //path to the file locally
                        CannedAccessControlList.PublicRead //to make the file public
                );
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state.equals(TransferState.COMPLETED)) {
                            //Success
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d("Token", "Success");
                        } else if (state.equals(TransferState.FAILED)) {
                            //Failed
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Log.d("Token", "Failed");
                        }

                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                        int percentDone = (int) percentDonef;
                        Log.d("progress", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        Log.d("error", "Failed");
                    }
                });
            }catch(Exception e){
                Log.d("exception",e.getMessage());
            }
            return null;
        }
    }
}