package com.example.dungtt.dailyreport.Controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dungtt.dailyreport.Model.Report;
import com.example.dungtt.dailyreport.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Report_Update_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView timeRp;
    private EditText nameRp, writerRp, contentRp;
    private ImageView previewImg, cameraImg, fileImg;
    private Button UpdateBtn;
    private int REQUEST_CODE = 100;
    private int REQUEST_CODE_FILE = 111;
    private String timepick, datepick, timenow;
    private Calendar cal;
    private Report changeReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_report);
        init();
    }

    public void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        changeReport = bundle.getParcelable("Report");

        nameRp = findViewById(R.id.reportTitle);
        writerRp = findViewById(R.id.reportWriter);
        timeRp = findViewById(R.id.timeInput);
        contentRp = findViewById(R.id.contentReport);
        previewImg = findViewById(R.id.previewImage);
        cameraImg = findViewById(R.id.camera);
        fileImg = findViewById(R.id.file);
        UpdateBtn = findViewById(R.id.btnUpdate);
        setDefauld();
        fileImg.setOnClickListener(this);
        cameraImg.setOnClickListener(this);
        UpdateBtn.setOnClickListener(this);
        timeRp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.file:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_CODE_FILE);
                break;
            case R.id.btnCreate:
                byte[] bytesImage = convertImageToByte(previewImg);
                String rpName = nameRp.getText().toString().trim();
                String rpWriter = writerRp.getText().toString().trim();
                String rptimeCreate = timeRp.getText().toString().trim();
                String rpContent = contentRp.getText().toString().trim();

                MainActivity.database.updateReport(new Report(rpName, rpWriter, rptimeCreate, rpContent, bytesImage));
                MainActivity.adapter.notifyDataSetChanged();
                startActivity(new Intent(Report_Update_Activity.this, MainActivity.class));
                break;
            case R.id.timeInput:
                timenow = timeRp.getText().toString();
                showHourPick();
                showDatePick();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            previewImg.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                previewImg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] convertImageToByte(ImageView view){
        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] hinhAnh = byteArrayOutputStream.toByteArray();
        return hinhAnh;
    }

    public void showDatePick(){
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String day, month;
                m = m+1;
                if (d<10) {
                    day = "0"+d;
                }else day = String.valueOf(d);
                if (m<10) {
                    month = "0"+m;
                }else month = String.valueOf(m);
                datepick = day+"/"+ month +"/"+y;
                timeRp.setText(datepick +" ");
                Log.i("REPORT_CREATE", "Set date pick");
            }
        };

        String s=timenow.substring(0, 10);
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1]) - 1;
        int nam=Integer.parseInt(strArrtmp[2]);

        DatePickerDialog pic=new DatePickerDialog(
                Report_Update_Activity.this,
                callBack, nam, thang, ngay);
        pic.setTitle("Chọn ngày hoàn thành");
        pic.show();
//        timeRp.setText(timepick + " " + datepick);
    }

    public void showHourPick(){
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String gio, phut;
                if (hour < 10){
                    gio = "0"+hour;
                }else gio = String.valueOf(hour);
                if (minute < 10){
                    phut = "0"+minute;
                }else phut = String.valueOf(minute);
                timepick = gio + ":" + phut;
                timeRp.append(timepick);
                Log.i("REPORT_CREATE", "Set time pick");
            }
        };
        String s = timenow.substring(11, 16);
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                Report_Update_Activity.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }
    public void setDefauld(){
        nameRp.setText(changeReport.getReportName());
        writerRp.setText(changeReport.getWriter());
        timeRp.setText(changeReport.getTimeCreate());
        contentRp.setText(changeReport.getContentReport());
        Bitmap bitmap = BitmapFactory.decodeByteArray(changeReport.getImage(), 0, changeReport.getImage().length);
        previewImg.setImageBitmap(bitmap);
    }
}
