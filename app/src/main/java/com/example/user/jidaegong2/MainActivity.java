package com.example.user.jidaegong2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    ArrayList<ShelterData> shelterDataArrayList = new ArrayList<ShelterData>();
    private Button btnShowLocation;
    private TextView txtLat;
    private TextView txtLon;
    private GpsInfo gps;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. GPS 경도,위도 받아오기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Toast.makeText(
                    getApplicationContext(),
                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                    Toast.LENGTH_LONG).show();
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

        // 2. CSV 파일에서 값 읽어오기

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("Earthquake_data.csv")));
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                ShelterData temp = new ShelterData();
                temp.setId(nextLine[0]);
                temp.setLocationName(nextLine[1]);
                temp.setLocation(nextLine[2]);
                temp.setAvailableScale(nextLine[3]);
                temp.setVertical(nextLine[4]);
                temp.setHorizontal(nextLine[5]);
                shelterDataArrayList.add(temp);
            }
        } catch (IOException ie)

        {
            ie.printStackTrace();

        }

        // 3. 네이버 지도 API 받아오기
        Button btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment1 fragment1 = new Fragment1();
                fragment1.setArguments(new Bundle());
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.fragmentHere, fragment1);
                fragmentTransaction.commit();
            }
        });

    }
}
