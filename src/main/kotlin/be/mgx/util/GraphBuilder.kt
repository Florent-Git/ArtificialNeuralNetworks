package be.mgx.util

import be.mgx.core.NeuralNetwork
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
import java.awt.event.WindowAdapter
import java.awt.geom.Ellipse2D
import java.util.concurrent.CompletableFuture
import kotlin.math.pow

class GraphBuilder(
    private val graphType: GraphTypes,
    private val layers: List<Matrix>,
    private val inputs: MutableList<ArrayList<Double>>,
    private val network: NeuralNetwork
) {
    val isWindowClosed: CompletableFuture<Int> = CompletableFuture()

    private val dataset = XYSeriesCollection()
    private val chartDomain = arrayOf(
        doubleArrayOf(0.0, 10.0),
        doubleArrayOf(0.0, 10.0)
    )

    private val weights
        get() = layers.map { it.array }

    fun drawGraph() {
        val chart: JFreeChart = when(graphType){
            GraphTypes.BASICANDPERCEPTRON -> drawSimpleANDPerceptron()
            GraphTypes.ANDPERCEPTRONGRAD -> drawSimpleANDPerceptronGrad()
            GraphTypes.ANDPERCEPTRONADALINE -> drawSimpleANDPerceptronAda()
            GraphTypes.LINEARSEPARATIONGRAD -> linearSeparationGradient()
            GraphTypes.LINEARSEPARATIONADALINE -> linearSeparationAdaline()
            GraphTypes.NONLINEARSEPARATIONADALINE -> nonLinearSeparationAdaline()
            GraphTypes.NONLINEARSEPARATIONGRAD -> nonLinearSeparationGrad()
            GraphTypes.LINEARREGRESSIONGRAD -> linearRegressionGrad()
            GraphTypes.LINEARREGRESSIONADALINE -> linearRegressionAdaline()
            GraphTypes.THREECLASSCLASSIFICATION -> threeClassClassification()
            GraphTypes.XOROPERATOR -> operatorXOR()
            GraphTypes.TWOCLASSNONLINEAR -> twoClassNonLinear()
            GraphTypes.THREECLASSNONLINEAR -> threeClassNonLinear()
            GraphTypes.NONLINEARREGRESSION -> nonLinearRegression()
        }

        val chartFrame = ChartFrame("Neural Network Lab", chart)
        chartFrame.pack()
        chartFrame.isVisible = true

        chartFrame.addWindowListener(WindowAdapterImpl(isWindowClosed))
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
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")
//        weights.forEach { layer ->
//            addLine(layer[0], layer[1], layer[2], "Iteration $it")
//            it++
//        }

        chartDomain[0][0] = -5.0
        chartDomain[0][1] = 5.0
        chartDomain[1][0] = -5.0
        chartDomain[1][1] = 5.0

        return buildChart(renderer, "Simple Perceptron")
    }

    private fun drawSimpleANDPerceptronGrad(): JFreeChart {
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
//        weights.forEach { layer ->
//            addLine(layer[0], layer[1], layer[2], "Iteration $it")
//            it++
//        }
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = -5.0
        chartDomain[0][1] = 5.0
        chartDomain[1][0] = -5.0
        chartDomain[1][1] = 5.0

        return buildChart(renderer, "Simple Perceptron with Gradient Descent")
    }

    private fun drawSimpleANDPerceptronAda(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(pointsIt, Color.BLACK)
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 3.0, 3.0))
        }

        // Draw lines
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = -5.0
        chartDomain[0][1] = 5.0
        chartDomain[1][0] = -5.0
        chartDomain[1][1] = 5.0

        return buildChart(renderer, "Simple Perceptron with ADALINE")
    }

    private fun linearSeparationGradient(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 1.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.BLUE)
                renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            } else {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
                renderer.setSeriesShape(pointsIt, Rectangle(0, 0, 10, 10))
            }
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        //Draw lines
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 8.0
        chartDomain[1][0] = 3.0
        chartDomain[1][1] = 12.0

        return buildChart(renderer, "Classification of separable linear data with Gradient")
    }

    private fun linearSeparationAdaline(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 1.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.BLUE)
                renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            } else {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
                renderer.setSeriesShape(pointsIt, Rectangle(0, 0, 10, 10))
            }
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        // Draw points
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 8.0
        chartDomain[1][0] = 3.0
        chartDomain[1][1] = 12.0

        return buildChart(renderer, "Classification of separable linear data with Adaline")
    }

    private fun nonLinearSeparationAdaline(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 1.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.BLUE)
                renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 5.0, 5.0))
            } else {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
                renderer.setSeriesShape(pointsIt, Rectangle(0, 0, 5, 5))
            }
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        // Draw points
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 8.0
        chartDomain[1][0] = 0.0
        chartDomain[1][1] = 8.0

        return buildChart(renderer, "Classification of non-linearly separable data with ADALINE")
    }

    private fun nonLinearSeparationGrad(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 1.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.BLUE)
                renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 5.0, 5.0))
            } else {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
                renderer.setSeriesShape(pointsIt, Rectangle(0, 0, 5, 5))
            }
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        // Draw points
        addLine(weights.last()[0], weights.last()[1], weights.last()[2], "")

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 8.0
        chartDomain[1][0] = 0.0
        chartDomain[1][1] = 8.0

        return buildChart(renderer, "Classification of non-linearly separable data with Gradient")
    }

    private fun linearRegressionGrad(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(pointsIt, Color.BLACK)
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        // Draw lines
        drawRegressionLine(weights.last()[0], weights.last()[1])

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 35.0
        chartDomain[1][0] = 0.0
        chartDomain[1][1] = 15.0

        return buildChart(renderer, "Linear Regression")
    }

    private fun linearRegressionAdaline(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(pointsIt, Color.BLACK)
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            renderer.setSeriesStroke(pointsIt, BasicStroke(5.0f))
        }

        // Draw lines
        drawRegressionLine(weights.last()[0], weights.last()[1])

        chartDomain[0][0] = 0.0
        chartDomain[0][1] = 35.0
        chartDomain[1][0] = 0.0
        chartDomain[1][1] = 15.0

        return buildChart(renderer, "Linear Regression")
    }

    private fun threeClassClassification(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        var it = 0
        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 2.0) {
                // Class A
                renderer.setSeriesPaint(pointsIt, Color.BLUE)
            } else if (input[2] == 3.0) {
                // Class B
                renderer.setSeriesPaint(pointsIt, Color.RED)
            } else {
                // Class C
                renderer.setSeriesPaint(pointsIt, Color.GREEN)
            }
            renderer.setSeriesShape(pointsIt, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            it++
        }

        addLine(weights.last()[0], weights.last()[3], weights.last()[6], "1")
        addLine(weights.last()[1], weights.last()[4], weights.last()[7], "2")
        addLine(weights.last()[2], weights.last()[5], weights.last()[8], "3")

        renderer.setSeriesPaint(it, Color.BLUE)
        renderer.setSeriesPaint(it+1, Color.RED)
        renderer.setSeriesPaint(it+2, Color.GREEN)

        chartDomain[0][0] = -2.0
        chartDomain[0][1] = 4.0
        chartDomain[1][0] = 0.0
        chartDomain[1][1] = 8.0

        return buildChart(renderer, "Three class classification")
    }

    private fun operatorXOR(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        var points = 0
        var i = -0.5
        while (i < 1.5) {
            var j = -0.5
            while (j < 1.5) {
                addPoint("$i$j", i, j)

                val input = Matrix.createMatrix(1, 3)
                input[0, 0] = 1.0
                input[0, 1] = i
                input[0, 2] = j
                val output = network.fire(input)

                if (output[0, 0] >= 0.5) {
                    renderer.setSeriesPaint(points, Color.GREEN)
                } else {
                    renderer.setSeriesPaint(points, Color.ORANGE)
                }
                renderer.setSeriesShape(points, Rectangle(0, 0, 3, 3))

                j += 0.025.toDouble()
                points++
            }
            i += 0.025
        }

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if ((input[0].equals(1.0) && input[1].equals(0.0)) || (input[0].equals(0.0) && input[1].equals(1.0))) {
                renderer.setSeriesPaint(points+pointsIt, Color.BLUE)
                renderer.setSeriesShape(points+pointsIt, Ellipse2D.Double(0.0, 0.0, 7.0, 7.0))
                renderer.setSeriesStroke(points+pointsIt, BasicStroke(5.0f))
            } else {
                renderer.setSeriesPaint(points+pointsIt, Color.RED)
                renderer.setSeriesShape(points+pointsIt, Ellipse2D.Double(0.0, 0.0, 7.0, 7.0))
                renderer.setSeriesStroke(points+pointsIt, BasicStroke(5.0f))
            }
        }

        chartDomain[0][0] = -1.0
        chartDomain[0][1] = 2.0
        chartDomain[1][0] = -1.0
        chartDomain[1][1] = 2.0

        return buildChart(renderer, "XOR operator")
    }

    //TODO
    private fun twoClassNonLinear(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()
        var it = 0

        var points = 0
        var i = -2.0
        while (i < 2.0) {
            var j = -2.0
            while (j < 2.0) {
                addPoint("$i$j", i, j)

                val input = Matrix.createMatrix(1, 3)
                input[0, 0] = 1.0
                input[0, 1] = i
                input[0, 2] = j
                val output = network.fire(input)

                if (output[0, 0] >= 0.5) {
                    renderer.setSeriesPaint(points, Color.GREEN)
                } else {
                    renderer.setSeriesPaint(points, Color.ORANGE)
                }
                renderer.setSeriesShape(points, Rectangle(0, 0, 3, 3))

                j += 0.025.toDouble()
                points++
            }
            i += 0.05
        }

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 2.0) {
                // Class A
                renderer.setSeriesPaint(points, Color.BLUE)
            } else {
                // Class B
                renderer.setSeriesPaint(points, Color.RED)
            }
            renderer.setSeriesShape(points, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            points++
        }

        chartDomain[0][0] = -2.0
        chartDomain[0][1] = 2.0
        chartDomain[1][0] = -2.0
        chartDomain[1][1] = 2.0

        return buildChart(renderer, "Two class non-linear classification")
    }

    private fun threeClassNonLinear(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()
        val it = 0

        var points = 0
        var i = -2.5
        while (i < 2.5) {
            var j = -2.5
            while (j < 2.5) {
                addPoint("$i$j", i, j)

                val input = Matrix.createMatrix(1, 3)
                input[0, 0] = 1.0
                input[0, 1] = i
                input[0, 2] = j
                val output = network.fire(input)

                if (output[0, 0] >= 0.8 && output[0, 1] < 0.2 && output[0, 2] < 0.2) {
                    renderer.setSeriesPaint(points, Color.GREEN)
                } else if (output[0, 1] >= 0.8 && output[0, 0] < 0.2 && output[0, 2] < 0.2) {
                    renderer.setSeriesPaint(points, Color.ORANGE)
                } else if (output[0, 2] >= 0.8 && output[0, 1] < 0.2 && output[0, 1] < 0.2)
                    renderer.setSeriesPaint(points, Color.RED)
                else {
                    renderer.setSeriesPaint(points, Color.WHITE)
                }

                renderer.setSeriesShape(points, Rectangle(0, 0, 3, 3))

                j += 0.05.toDouble()
                points++
            }
            i += 0.05
        }

        // Draw points
        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            if (input[2] == 2.0) {
                // Class A
                renderer.setSeriesPaint(points, Color.BLUE)
            } else {
                // Class B
                renderer.setSeriesPaint(points, Color.RED)
            }
            renderer.setSeriesShape(points, Ellipse2D.Double(0.0, 0.0, 10.0, 10.0))
            points++
        }

        chartDomain[0][0] = -2.5
        chartDomain[0][1] = 2.5
        chartDomain[1][0] = -2.5
        chartDomain[1][1] = 2.5

        return buildChart(renderer, "Three class non-linear classification")
    }

    private fun nonLinearRegression(): JFreeChart {
        val renderer = XYLineAndShapeRenderer()

        var points = 0
        var i = -2.5
        while (i < 2.5) {
            val input = Matrix.createMatrix(1, 2)
            input[0, 0] = 1.0
            input[0, 1] = i
            //input[0, 2] = j
            val output = network.fire(input)

            addPoint("$i$points", i, output[0,0])
            renderer.setSeriesPaint(points, Color.RED)
            renderer.setSeriesShape(points, Ellipse2D.Double(0.0, 0.0, 5.0, 5.0))

            points++
            i += 0.05
        }

        for ((pointsIt, input) in inputs.withIndex()) {
            addPoint("Point $pointsIt", input[0], input[1])
            renderer.setSeriesPaint(points, Color.BLACK)
            renderer.setSeriesShape(points, Rectangle(0, 0, 5, 5))
            points++
        }

        chartDomain[0][0] = -2.0
        chartDomain[0][1] = 2.0
        chartDomain[1][0] = -2.0
        chartDomain[1][1] = 2.0

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

    private fun drawRegressionLine(b: Double, a: Double) {
        val coords = arrayOf(
            doubleArrayOf(-100.0, (a * (-100) + b).toDouble()),
            doubleArrayOf(100.0, ((a * (100) + b).toDouble()))
        )

        val series = XYSeries("")
        series.add(coords[0][0], coords[0][1])
        series.add(coords[1][0], coords[1][1])
        dataset.addSeries(series)
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

        domainAxis.setRange(chartDomain[0][0], chartDomain[0][1])
        rangeAxis.setRange(chartDomain[1][0], chartDomain[1][1])

        plot.renderer = renderer

        return chart
    }
}