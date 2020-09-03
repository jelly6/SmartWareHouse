package com.example.smartwarehouse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwarehouse.model.Item;
import com.example.smartwarehouse.model.ItemList;
import com.example.smartwarehouse.model.Material;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class FirstFragment extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    ItemList items = new ItemList();
    private RecyclerView recyclerView;
    private Button btn_scan;
    private IntentIntegrator qrScan;
    private int REQUEST_CODE=100;
    private TextView textView;
    ItemAdapter itemAdapter;
    List<Item> listData;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        db = FirebaseFirestore.getInstance();
        //prepareDatas
        //prepareDatas();
        //viewUnitsSetup
        viewUnitsSetup(view);
        collectDataFromCloud();



    }
    private void collectDataFromCloud(){
        final List<Item>[] items = new List[]{new ArrayList<>()};
        CollectionReference docRef = db.collection("checkList");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }else{
                    items[0] = new ArrayList<>();
                    Log.d(TAG, "onEvent: YY"+queryDocumentSnapshots.getDocuments().get(0).getReference().getId());
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                            .putString("DOC",queryDocumentSnapshots.getDocuments().get(0).getReference().getId()).commit();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Log.d(TAG, document.getId() + " =>tables " + document.getData().keySet());
                        for (String key : document.getData().keySet()) {
                            Log.d(TAG, "onComplete: tables"+key+"=>"+document.getBoolean(key));
                            Item item = new Item();
                            item.setItem_model(key);
                            item.setChecked(document.getBoolean(key));
                            items[0].add(item);

                        }

                        Collections.sort(items[0], new Comparator<Item>(){
                            public int compare( Item l1, Item l2 ) {
                                // 回傳值: -1 前者比後者小, 0 前者與後者相同, 1 前者比後者大
                                return l1.getItem_model().toString().toLowerCase().compareTo(l2.getItem_model().toString().toLowerCase());
                            }});
                        itemAdapter=new ItemAdapter(items[0]);
                        recyclerView.setAdapter(itemAdapter);
                    }
                    Log.d(TAG, "onComplete: "+ items[0].size());
                }

            }
        });

    }

    private void checkItem() {
        ItemList itemList = new ItemList();
        final String doc = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("DOC",
                "");
        db.collection("tables")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " =>YY " + document.getData().keySet());
                                for (String key : document.getData().keySet()) {
                                    Log.d(TAG, "onComplete: YY"+key+"=>"+document.getString(key));
                                    if(document.getString(key).toString().equals(textView.getText())) {
                                        Map<String, Object> new_check = new HashMap<>();
                                        new_check.put(key, true);
                                        Log.d(TAG, "onComplete: YY"+db.collection("checkList").get().toString());
                                        db.collection("checkList")
                                                .document(doc)
                                                .update(new_check)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onSuccess: YY");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "onFailure: YY"+e);

                                                    }
                                                });



                                    }
                                }

                            }
                            Log.d(TAG, "onComplete: "+items.getKeys());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void viewUnitsSetup(@NonNull View view) {
        btn_scan = view.findViewById(R.id.button_scan);
        btn_scan.setOnClickListener(this);
        qrScan = new IntentIntegrator(getActivity());
        recyclerView = view.findViewById(R.id.check_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        textView = view.findViewById(R.id.textview_first);
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

    @Override
    public void onClick(View v) {
        int permission_check =ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA);
        if(permission_check== PackageManager.PERMISSION_GRANTED){
            IntentIntegrator.forSupportFragment(FirstFragment.this).initiateScan();
            //qrScan.initiateScan();
        }else{
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                qrScan.initiateScan();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(this.getContext(),"Result not found",Toast.LENGTH_LONG).show();
            }else{
                try {
                    //converting the data to json
                    //JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    //Log.d(TAG, "onActivityResult: "+result.getContents());
                    /*textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));*/
                    textView.setText(result.getContents()+"");
                    Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                    checkItem();
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this.getContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
