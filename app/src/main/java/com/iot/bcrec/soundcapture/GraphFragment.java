package com.iot.bcrec.soundcapture;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import static com.iot.bcrec.soundcapture.MainActivity.TAG;

/**
 * Created by Joseph on 03-May-18.
 */

public class GraphFragment extends Fragment {

        private LineChart mChart;
        private RelativeLayout graphlayout;

        BroadcastReceiver qReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s1 = intent.getStringExtra("Data");
                float flag = Float.parseFloat(s1);
                Log.i(TAG,"fragment = "+s1);
                addEntry(flag);
            }
        };

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_layout, container, false);

            mChart= (LineChart) view.findViewById(R.id.chart1);

            mChart.setHighlightPerDragEnabled(false);
            mChart.setTouchEnabled(false);
            mChart.setDragEnabled(false);
            mChart.setScaleEnabled(true);
            mChart.setDrawGridBackground(true);
            mChart.setPinchZoom(true);
            mChart.setBackgroundColor(Color.LTGRAY);

            LineData data = new LineData();
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);

            Legend i = mChart.getLegend();
            i.setForm(Legend.LegendForm.LINE);
            i.setTextColor(Color.WHITE);

            XAxis x1 = mChart.getXAxis();
            x1.setTextColor(Color.WHITE);
            x1.setAvoidFirstLastClipping(true);
            x1.setDrawGridLines(false);

            YAxis y1 = mChart.getAxisLeft();
            y1.setTextColor(Color.WHITE);
            y1.setAxisMaximum(120f);
            y1.setDrawGridLines(true);

            YAxis y2 = mChart.getAxisRight();
            y2.setEnabled(false);

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        }

        private void addEntry(float x){
            LineData data = mChart.getData();

            if(data != null){
                ILineDataSet set = data.getDataSetByIndex(0);

                if(set == null){
                    set = createSet();
                    data.addDataSet(set);
                }
                data.addEntry(
                        new Entry(set.getEntryCount(),x),0
                );
                mChart.notifyDataSetChanged();
                mChart.setVisibleXRangeMaximum(20);
                mChart.moveViewToX(data.getEntryCount());
            }
        }

        private LineDataSet createSet(){
            LineDataSet set = new LineDataSet(null,"");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setLineWidth(1f);
            set.setColor(Color.MAGENTA);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0);
            return set;

        }

        @Override
        public void onResume() {
            IntentFilter filter = new IntentFilter("SoundData");
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(qReciever,filter);
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(qReciever);
        }
    }

