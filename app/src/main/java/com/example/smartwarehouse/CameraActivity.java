package com.example.smartwarehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.smartwarehouse.model.ItemList;
import com.example.smartwarehouse.model.Material;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class CameraActivity extends AppCompatActivity {
    private ImageCapture imageCapture;
    private FirebaseFirestore db;
    ItemList items = new ItemList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        db = FirebaseFirestore.getInstance();

        int permission_check = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
        if(permission_check!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this,new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }else{
            startCamera();
        }

        ImageView btn = findViewById(R.id.captureImg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CameraActivity.this).setTitle("完成新增")
                        .setMessage("工單輸入完成，回到checkList!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prepareDatas();
                                finish();
                            }
                        }).show();
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        PreviewView previewView = findViewById(R.id.camera);
        cameraProviderFuture.addListener(new Runnable() {
                                             @Override
                                             public void run() {
                                                 try {
                                                     // Camera provider is now guaranteed to be available
                                                     ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                                                     // Set up the view finder use case to display camera preview
                                                     Preview preview = new Preview.Builder().build();

                                                     // Set up the capture use case to allow users to take photos
                                                     imageCapture = new ImageCapture.Builder()
                                                             .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                                             .build();

                                                     // Choose the camera by requiring a lens facing
                                                     CameraSelector cameraSelector = new CameraSelector.Builder()
                                                             .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                                             .build();

                                                     // Attach use cases to the camera with the same lifecycle owner
                                                     Camera camera = cameraProvider.bindToLifecycle(
                                                             ((LifecycleOwner) CameraActivity.this),
                                                             cameraSelector,
                                                             preview,
                                                             imageCapture);

                                                     // Connect the preview use case to the previewView
                                                     preview.setSurfaceProvider(previewView.createSurfaceProvider());
                                                 } catch (InterruptedException | ExecutionException e) {
                                                     // Currently no exceptions thrown. cameraProviderFuture.get() should
                                                     // not block since the listener is being called, so no need to
                                                     // handle InterruptedException.
                                                 }
                                             }
                                         }

                , ContextCompat.getMainExecutor(this));
    }

    private void prepareDatas() {
        Map<String, Object> mm = getStringObjectMap();
        // Add a new document with a generated ID
        db.collection("tables")
                .add(mm)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        //step1
        db.collection("tables")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " =>tables " + document.getData().keySet());
                                for (String key : document.getData().keySet()) {
                                    Log.d(TAG, "onComplete: tables"+key+"=>"+document.getString(key));
                                    items.getItems().put(key,document.getString(key));
                                }

                            }
                            Log.d(TAG, "onComplete: "+items.getKeys());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //step2
        Map<String, Boolean> checkList = new HashMap<>();
        for (String key : mm.keySet()) {
            checkList.put(key,false);
        }
        Log.d(TAG, "checkList onViewCreated: "+mm.keySet());
        db.collection("checkList")
                .add(checkList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "checkList DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "checkList Error adding document", e);
                    }
                });
    }

    private Map<String, Object> getStringObjectMap() {
        List<Material> materials = new ArrayList<>();
        Material m1 = new Material("20200501001","LED-1","LED","bin_0","Philips",new Date());
        Material m2 = new Material("20200501002","LED-2","LED","bin_0","Philips",new Date());
        Material m3 = new Material("20200504003","R1","R","","Philips",new Date());
        Material m4 = new Material("20200504004","R2","R","","Philips",new Date());
        Material m5 = new Material("20200504005","R3","R","","Philips",new Date());
        Material m6 = new Material("20200504006","R4","R","","Philips",new Date());
        Material m7 = new Material("20200504007","R5","R","","Philips",new Date());
        Material m8 = new Material("20200504008","R6","R","","Philips",new Date());
        Material m9 = new Material("20200504009","R7","R","","Philips",new Date());
        Material m10 = new Material("20200504010","R8","R","","Philips",new Date());
        Material m11 = new Material("20200601003","R9","R","","Philips",new Date());
        Material m12 = new Material("20200601004","IC-1","IC","","Philips",new Date());
        Material m13 = new Material("20200601005","L1","L","","Philips",new Date());
        Material m14 = new Material("20200601006","C1","C","","Philips",new Date());
        materials.add(m1);
        materials.add(m2);
        materials.add(m3);
        materials.add(m4);
        materials.add(m5);
        materials.add(m6);
        materials.add(m7);
        materials.add(m8);
        materials.add(m9);
        materials.add(m10);
        materials.add(m11);
        materials.add(m12);
        materials.add(m13);
        materials.add(m14);

        Map<String, Object> mm = new HashMap<>();
        for (Material material : materials) {
            mm.put(material.getModel(), material.getDate_code());
        }
        return mm;
    }

}