package com.example.dungtt.dailyreport.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dungtt.dailyreport.Model.Report;
import com.example.dungtt.dailyreport.R;

public class AdapterRecycle extends RecyclerView.Adapter{
    private IData iData;
    private String TAG = "Adapter RecyclerView";

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setiData(IData iData) {
        this.iData = iData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            Report report = iData.getItem(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Report", report);
            Intent intent = new Intent(view.getContext(), Report_View_Activity.class);
            intent.putExtras(bundle);
            Toast.makeText(view.getContext(), "click itemt " + report.getReportName(), Toast.LENGTH_SHORT).show();
            view.getContext().startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the Action");
            menu.add(Menu.NONE, R.id.deleteReport, Menu.NONE,"Delete report");
            menu.add(Menu.NONE, R.id.changereports, Menu.NONE, "Change Report");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View messView = inflater.inflate(R.layout.item_report, parent, false);
        ViewHolder viewHolder = new ViewHolder(messView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Report report = iData.getItem(position);
        String title = report.getReportName();
        String content = report.getContentReport();
        String time = report.getTimeCreate();
        byte[] image = report.getImage();

        TextView titleText = holder.itemView.findViewById(R.id.Titlerp);
        TextView timeReport = holder.itemView.findViewById(R.id.dateCreate);
        TextView contentText = holder.itemView.findViewById(R.id.contentrp);
        ImageView imageView = holder.itemView.findViewById(R.id.imageIcon);

        titleText.setText(title);
        timeReport.setText(time);
        if (content.length() >= 10){
            contentText.setText(content.substring(0,10));
        }else contentText.setText(content);

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(bitmap);
        Log.i(TAG, "Binded View Holder");
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return iData.getCount();
    }


    public interface IData {
        int getCount();

        Report getItem(int pos);
    }
}
