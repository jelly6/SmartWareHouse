package com.example.smartwarehouse;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qrScan = new IntentIntegrator(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission_check = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if(permission_check!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
                }else{
                    try{
                        NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment))
                                .navigate(R.id.action_FirstFragment_to_thirdFragment);
                    }catch (Exception e){
                        NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment))
                                .navigate(R.id.action_SecondFragment_to_thirdFragment);
                    }
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_new) {
            startActivity(new Intent(MainActivity.this,CameraActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 1");

        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(this,"Result not found",Toast.LENGTH_LONG).show();
            }else{
                try {
                    //converting the data to json
                    //JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    Log.d(TAG, "onActivityResult: "+result.getContents());
                    /*textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));*/
                    //textView.setText(result.getContents()+"");
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
