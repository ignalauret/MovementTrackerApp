package com.example.movementtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movementtracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    TextView mTotalTimeText;
    TextView mWorkedTimeText;
    RelativeLayout mReturnBtn;
    LineChart mLineChart;
    Fragment mMapFragment;

    String mTotalTime;
    String mWorkedTime;
    ArrayList<Integer> mGraphPoints;
    ArrayList<Double> mLongitudes;
    ArrayList<Double> mLatitudes;
    ArrayList<Date> mTimeStamps;
    List<Entry> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        bindUI();
        mTotalTime = getIntent().getStringExtra("total_time");
        mWorkedTime = getIntent().getStringExtra("worked_time");
        mGraphPoints = getIntent().getIntegerArrayListExtra("graph_points");
        mLongitudes = (ArrayList<Double>) getIntent().getSerializableExtra("longitudes");
        mLatitudes = (ArrayList<Double>) getIntent().getSerializableExtra("latitudes");
        mTimeStamps = (ArrayList<Date>) getIntent().getSerializableExtra("time_stamps");
        Log.d("Testing", "" + mLongitudes.get(0));
        String[] mTotalTimeArray = mTotalTime.split(":");
        if(mTotalTimeArray[0].startsWith("0")) mTotalTimeArray[0] = mTotalTimeArray[0].substring(1);
        if(mTotalTimeArray[1].startsWith("0")) mTotalTimeArray[1] = mTotalTimeArray[1].substring(1);
        String totalTime = "Tiempo total: \n" + mTotalTimeArray[0] + " min " + mTotalTimeArray[1] + " seg";
        String workedTime = "Tiempo trabajado: \n" + mWorkedTime + " seg";
        mTotalTimeText.setText(totalTime);
        mWorkedTimeText.setText(workedTime);
        setChart();
        saveLocationLog();

    }


    private void bindUI() {
        mTotalTimeText = findViewById(R.id.totalTime);
        mWorkedTimeText = findViewById(R.id.workedTime);
        mReturnBtn = findViewById(R.id.returnBtn);
        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mLineChart = findViewById(R.id.graph);
    }

    /* *************************** Chart Methods ********************************** */
    private void setChart() {
        float x = 0, y;
        points = new ArrayList<>();
        for(int i = 0; i < mGraphPoints.size(); i++) {
            x = x + 2f;
            y = mGraphPoints.get(i);
            points.add(new Entry(x, y));
        }
        LineDataSet dataSet = new LineDataSet(points, "Tiempo de trabajo");
        dataSet.setValueTextColor(Color.TRANSPARENT);
        LineData data = new LineData(dataSet);
        mLineChart.setData(data);
        charConfig();
        mLineChart.invalidate();

    }

    private void charConfig() {
        mLineChart.getAxisLeft().setTextColor(Color.WHITE);
        mLineChart.getXAxis().setTextColor(Color.WHITE);
        mLineChart.getAxisRight().setTextColor(Color.WHITE);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.getLegend().setTextColor(Color.WHITE);
    }

    /* ************************* Map methods ***************************** */

    private void saveLocationLog() {
        File logFile = new File(Environment.getExternalStorageDirectory(), "LogFile.txt");
        try {
            FileWriter fileWriter = new FileWriter(logFile);
            for(int i = 0; i < mLatitudes.size(); i++) {
                String location = mTimeStamps.get(i) + " " + mLatitudes.get(i) + ", " + mLongitudes.get(i) + "\n";
                fileWriter.append(location);

            }
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
