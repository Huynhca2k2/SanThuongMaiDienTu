package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.chh.shoponline.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class SalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        setupLineChart();
    }

    private void setupLineChart() {
        LineChartView lineChartView = findViewById(R.id.chart);

        // Dữ liệu thống kê của bạn (số liệu tháng 1 đến 12)
        //Du liệu thống kê doanh thu
        int[] monthlyData = {50, 60, 70, 80, 100, 100, 80, 120, 80, 70, 50, 120, 50, 90, 100, 130, 70, 50, 200, 400, 250, 260, 270, 80, 290, 190, 310, 600, 330, 200, 350};

        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < monthlyData.length; i++) {
            values.add(new PointValue(i, monthlyData[i]));
            axisValues.add(new AxisValue(i).setLabel(i+1+""));
            // Label là số tháng
        }

        Line line = new Line(values);
        List<Line> lines = new ArrayList<>();
        line.setColor(getResources().getColor(R.color.lavendar));
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        lineChartView.setLineChartData(data);
    }
    private String getMonthName(int monthNumber) {
        // Tùy thuộc vào yêu cầu, bạn có thể sử dụng một mảng nhãn hoặc một phương thức chuyển đổi khác
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (monthNumber >= 1 && monthNumber <= 12) {
            return monthNames[monthNumber - 1];
        }
        return "";
    }
}