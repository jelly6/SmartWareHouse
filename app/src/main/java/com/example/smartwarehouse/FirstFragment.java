package com.example.smartwarehouse;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirstFragment extends Fragment {

    private FirebaseFirestore db;
    ItemList items = new ItemList();
    private RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.check_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        final List<Item> items = new ArrayList<>();
        db.collection("checkList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " =>tables " + document.getData().keySet());
                                for (String key : document.getData().keySet()) {
                                    Log.d(TAG, "onComplete: tables"+key+"=>"+document.getBoolean(key));
                                    Item item = new Item();
                                    item.setItem_model(key);
                                    item.setChecked(document.getBoolean(key));
                                    items.add(item);

                                }

                                Collections.sort( items, new Comparator<Item>(){
                                    public int compare( Item l1, Item l2 ) {
                                        // 回傳值: -1 前者比後者小, 0 前者與後者相同, 1 前者比後者大
                                        return l1.getItem_model().toString().toLowerCase().compareTo(l2.getItem_model().toString().toLowerCase());
                                    }});



                                ItemAdapter itemAdapter = new ItemAdapter(items);
                                recyclerView.setAdapter(itemAdapter);
                            }
                            Log.d(TAG, "onComplete: "+items.size());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


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
