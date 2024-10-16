package com.example.gallery.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.databinding.ActivityMain18Binding
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity18 : AppCompatActivity() {
    private lateinit var binding: ActivityMain18Binding
    val GRAPH_MAX_VERTICAL_VALUE = 120
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain18Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define your data points with Unix timestamps and their corresponding values
//        val series = LineGraphSeries(
//            arrayOf( // on below line we are adding
//                // each point on our x and y axis.
//                DataPoint(0.0, 1.0),
//                DataPoint(1.0, 3.0),
//                DataPoint(2.0, 4.0),
//                DataPoint(3.0, 9.0),
//                DataPoint(4.0, 6.0),
//                DataPoint(5.0, 3.0),
//                DataPoint(6.0, 6.0),
//                DataPoint(7.0, 1.0),
//                DataPoint(8.0, 2.0)
//            )
//        )
//
//
//        // Create a series with the data points
//     //   val series = LineGraphSeries(dataPoints)
//
//        // Set a date label formatter on the x-axis
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//        binding.idGraphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this, dateFormat)
//
//        // Set the number of horizontal labels (x-axis)
//        binding.idGraphView.gridLabelRenderer.numHorizontalLabels = 4
//
//        // Set manual x bounds based on the date range
////        binding.idGraphView.viewport.setMinX(dataPoints.first().x)
////        binding.idGraphView.viewport.setMaxX(dataPoints.last().x)
//        binding.idGraphView.viewport.isXAxisBoundsManual = true
//
//        // Enable scrolling and scaling for the graph view
//        binding.idGraphView.viewport.isScrollable = true
//        binding.idGraphView.viewport.isScalable = true
//        binding.idGraphView.viewport.setScalableY(true)
//        binding.idGraphView.viewport.setScrollableY(true)
//
//        // Customize the x-axis label color and size
//        binding.idGraphView.gridLabelRenderer.horizontalLabelsColor = resources.getColor(android.R.color.black)
//        binding.idGraphView.gridLabelRenderer.textSize = 40f
//        binding.idGraphView.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
//
//        // Add the series to the graph view
//        binding.idGraphView.addSeries(series)
//
//        // Optional: Enable animation for the graph
//        binding.idGraphView.animate()
    }

    // Function to convert Unix timestamp to Date object
//    private fun convertToDate(unixTime: Long): Date {
//        return Date(unixTime * 1000) // Multiply by 1000 to convert seconds to milliseconds
//    }
}
