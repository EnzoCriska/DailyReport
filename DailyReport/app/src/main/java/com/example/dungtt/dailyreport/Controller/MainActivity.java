package com.example.dungtt.dailyreport.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dungtt.dailyreport.Model.Report;
import com.example.dungtt.dailyreport.Model.SQLiteHandler;
import com.example.dungtt.dailyreport.R;

import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static SQLiteHandler database;
    FloatingActionButton floatingActionButton;
    public static AdapterRecycle adapter;
    RecyclerView recyclerView;
    ArrayList<Report> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        database= new SQLiteHandler(this);
        adapter = new AdapterRecycle();

        database = new SQLiteHandler(this);
        list = database.getListReport();
        recyclerView = findViewById(R.id.recycler);
        recyclerUp();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

            }


        }
    }

    public void recyclerUp(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter.setiData(new AdapterRecycle.IData() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Report getItem(int pos) {
                return list.get(pos);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatingActionButton:
                Intent intent = new Intent(MainActivity.this, Report_Create.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = (adapter.getPosition());
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.deleteReport:
                Report report = list.get(position);
                database.deleteReport(report);
                list = database.getListReport();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Deleted Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changereports:
                Report reports = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Report", reports);
                Intent intent = new Intent(MainActivity.this, Report_Update_Activity.class);
                intent.putExtras(bundle);

                Toast.makeText(this, "click itemt " + reports.getReportName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
