package com.example.dungtt.dailyreport.Controller;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dungtt.dailyreport.Model.Report;
import com.example.dungtt.dailyreport.R;


public class Report_View_Activity extends AppCompatActivity {
    TextView title, writer, time, content;
    ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_view);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Report report = bundle.getParcelable("Report");
        init(report);
    }

    public void init(Report report){
        title = findViewById(R.id.title);
        writer = findViewById(R.id.writer);
        time = findViewById(R.id.timecreate);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);

        title.setText(report.getReportName());
        writer.setText("Writer: " +report.getWriter());
        time.setText("Time create: " + report.getTimeCreate());
        content.setText(report.getContentReport());
        image.setImageBitmap(BitmapFactory.decodeByteArray(report.getImage(), 0, report.getImage().length));
    }
}
