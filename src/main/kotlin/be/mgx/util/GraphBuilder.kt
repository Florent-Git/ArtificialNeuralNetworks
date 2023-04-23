package be.mgx.util

import be.mgx.core.math.Matrix
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartFrame
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.ValueAxis
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Rectangle
import java.awt.geom.Ellipse2D

class GraphBuilder(
    private val graphType: GraphTypes,
    private val layers: List<Matrix>,
    private val inputs: MutableList<ArrayList<Double>>
) {
    private val dataset = XYSeriesCollection()
    private val weights
        get() = layers.map { it.array }

    fun drawGraph() {


        val chart: JFreeChart = when(graphType){
            GraphTypes.BASICANDPERCEPTRON -> drawSimpleANDPerceptron()
            GraphTypes.LINEARSEPARATION -> linearSeparation()
            GraphTypes.NONLINEARSEPARATION -> nonLinearSeparation()
            GraphTypes.LINEARREGRESSION -> linearRegression()
            GraphTypes.THREECLASSCLASSIFICATION -> threeClassClassification()
            GraphTypes.FOURCLASSCLASSIFICATION -> fourClassClassification()
            GraphTypes.XOROPERATOR -> operatorXOR()
            GraphTypes.TWOCLASSNONLINEAR -> twoClassNonLinear()
            GraphTypes.THREECLASSNONLINEAR -> threeClassNonLinear()
            GraphTypes.NONLINEARREGRESSION -> nonLinearRegression()
        }

        val chartFrame = ChartFrame("Neural Network Lab", chart)
        chartFrame.pack()
        chartFrame.isVisible = true
    }

    private fun drawSimpleANDPerceptron(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()
        var it = 1

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(pointsIt, Color.BLACK)
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 3.0, 3.0))
        }

        // Draw lines
        weights.forEach { layer ->
            addLine(layer[0], layer[1], layer[2], "Iteration $it")
            it++
        }

        return buildChart(renderer, "Simple Perceptron")
    }

    private fun drawInputs(renderer: XYLineAndShapeRenderer) {
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(pointsIt, Color.BLACK)
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 3.0, 3.0))
        }
    }

    private fun linearSeparation(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[3] == 1.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.GREEN)
                renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 3.0, 3.0))
            } else {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
                renderer.setSeriesShape(pointsIt, Rectangle(0, 0, 3, 3))
            }
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        //Draw lines
//        var it = 1
//        layers.forEach { layer ->
//            addLine(layer[0], layer[1], layer[2], "Iteration $it")
//            it++
//        }

        return buildChart(renderer, "Classification of separable linear data")
    }

    private fun nonLinearSeparation(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        //TODO

        return buildChart(renderer, "Classification of non-linearly separable data")
    }

    private fun linearRegression(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Linear Regression")
    }

    private fun threeClassClassification(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Three class classification")
    }

    private fun fourClassClassification(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Four class classification")
    }

    private fun operatorXOR(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        var points = 0
        var i = -10.0
        while (i < 10.0) {
            var j = -10.0
            while (j < 10.0) {
                addPoint("$i$j", i, j)
                renderer.setSeriesPaint(points, Color.BLACK)
                renderer.setSeriesStroke(points, BasicStroke(5.0f))
                renderer.setSeriesShape(points, Ellipse2D.Double(0.0,0.0, 3.0, 3.0))

                j += 0.2.toDouble()
                points++
            }
            i += 0.2
        }

        return buildChart(renderer, "XOR operator")
    }

    private fun twoClassNonLinear(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Two class non-linear classification")
    }

    private fun threeClassNonLinear(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Three class non-linear classification")
    }

    private fun nonLinearRegression(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        return buildChart(renderer, "Non linear regression")
    }

    private fun addLine(w0: Double, w1: Double, w2: Double, label: String) {
        val series = XYSeries(label)
        val coords = findIntersectionPoints(w0, w1, w2)
        series.add(coords[0][0], coords[0][1])
        series.add(coords[1][0], coords[1][1])
        dataset.addSeries(series)
    }

    private fun addPoint(label: String?, x: Double, y: Double) {
        val series = XYSeries(label)
        series.add(x, y)
//        series.add(x, y)
        dataset.addSeries(series)
    }

    private fun findIntersectionPoints(w0: Double, w1: Double, w2: Double): Array<DoubleArray> {
        return if (w1 == 0.0 && w2 == 0.0) {
            arrayOf(doubleArrayOf(10.0, 10.0), doubleArrayOf(10.0, 10.0))
        } else if (w1 == 0.0) {
            arrayOf(doubleArrayOf((-10.0).toDouble(), -w0 / w2), doubleArrayOf(10.0, -w0 / w2))
        } else if (w2 == 0.0) {
            arrayOf(doubleArrayOf(-w0 / w1, 10.0), doubleArrayOf(-w0 / w1, (-10.0).toDouble()))
        } else {
            arrayOf(
                doubleArrayOf(-10.0, ((-w0 - w1 * (-10.0)) / w2).toDouble()),
                doubleArrayOf(10.0, ((-w0 - w1 * 10.0) / w2).toDouble())
            )
        }
    }

    private fun buildChart(renderer: XYLineAndShapeRenderer, label: String): JFreeChart {
        val chart = ChartFactory.createXYLineChart(
            label,
            "x",
            "y",
            this.dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        )

        val plot: XYPlot = chart.xyPlot
        renderer.defaultStroke = BasicStroke(3.0f)
        renderer.autoPopulateSeriesStroke = false

        val domainAxis: ValueAxis = plot.domainAxis
        val rangeAxis: ValueAxis = plot.rangeAxis

        domainAxis.setRange(-10.0, 10.0)
        rangeAxis.setRange(-10.0, 10.0)

        plot.renderer = renderer

        return chart
    }
}