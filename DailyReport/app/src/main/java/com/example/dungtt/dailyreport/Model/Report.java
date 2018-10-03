package com.example.dungtt.dailyreport.Model;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class Report implements Parcelable {
    private int id;
    private String reportName, writer, timeCreate, contentReport;
    private byte[] image;

    public Report(int id, String reportName, String writer, String timeCreate, String contentReport, byte[] image) {
        this.id = id;
        this.reportName = reportName;
        this.writer = writer;
        this.timeCreate = timeCreate;
        this.contentReport = contentReport;
        this.image = image;
    }

    public Report(String reportName, String writer, String timeCreate, String contentReport, byte[] image) {
        this.reportName = reportName;
        this.writer = writer;
        this.timeCreate = timeCreate;
        this.contentReport = contentReport;
        this.image = image;
    }

    protected Report(Parcel in) {
        id = in.readInt();
        reportName = in.readString();
        writer = in.readString();
        timeCreate = in.readString();
        contentReport = in.readString();
        image = in.createByteArray();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getContentReport() {
        return contentReport;
    }

    public void setContentReport(String contentReport) {
        this.contentReport = contentReport;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(reportName);
        parcel.writeString(writer);
        parcel.writeString(timeCreate);
        parcel.writeString(contentReport);
        parcel.writeByteArray(image);
    }
}
