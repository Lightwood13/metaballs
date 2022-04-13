package com.example.metaballs

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

fun getPotential(particles: List<Particle>): (Vector2) -> Float = { point ->
    var potential = 0f
    for (particle in particles) {
        val l = (point - particle.position).len()
        potential += if (l == 0f) 1_000_000f else particle.radius / l
    }
    1 - potential
}

operator fun Vector2.plusAssign(other: Vector2) {
    this.x += other.x
    this.y += other.y
}

operator fun Vector2.minus(other: Vector2) = Vector2(this.x - other.x, this.y - other.y)
operator fun Vector2.times(value: Float) = Vector2(this.x * value, this.y * value)
operator fun Vector2.div(value: Float) = Vector2(this.x / value, this.y / value)

typealias Line = Pair<Vector2, Vector2>

private fun interpolateZero(
    point1: Vector2, point2: Vector2,
    value1: Float, value2: Float
): Vector2? = when {
    value1 > 0f && value2 > 0f -> null
    value1 < 0f && value2 < 0f -> null
    value1 == 0f && value2 == 0f -> point1
    else -> (point1 * value2 - point2 * value1) / (value2 - value1)
}

private fun processSquare(
    x1: Float, x2: Float, y1: Float, y2: Float,
    valDownLeft: Float, valDownRight: Float, valUpLeft: Float, valUpRight: Float
): List<Line> {
    val downLeft = Vector2(x1, y1)
    val downRight = Vector2(x2, y1)
    val upLeft = Vector2(x1, y2)
    val upRight = Vector2(x2, y2)

    val downPoint = interpolateZero(downLeft, downRight, valDownLeft, valDownRight)
    val upPoint = interpolateZero(upLeft, upRight, valUpLeft, valUpRight)
    val leftPoint = interpolateZero(downLeft, upLeft, valDownLeft, valUpLeft)
    val rightPoint = interpolateZero(downRight, upRight, valDownRight, valUpRight)

    val points = listOfNotNull(downPoint, upPoint, leftPoint, rightPoint)

    return when (points.size) {
        2 -> listOf(Line(points[0], points[1]))
        4 -> listOf(Line(points[0], points[1]), Line(points[2], points[3]))
        3 -> {
            val distinctPoints = points.distinct()
            listOf(Line(distinctPoints[0], distinctPoints[1]))
        }
        else -> listOf()
    }
}

fun marchingSquares(
    f: (Vector2) -> Float, region: Rectangle, gridSize: Int
): List<Line> {
    val res: MutableList<Line> = mutableListOf()

    val stepX = region.width / gridSize
    val stepY = region.height / gridSize

    val values: Array<Array<Float>> = Array(gridSize + 1) { i ->
        Array(gridSize + 1) { j ->
            f(
                Vector2(
                    region.x + j * stepX,
                    region.y + i * stepY
                )
            )
        }
    }

    for (i in 0 until gridSize) {
        for (j in 0 until gridSize) {
            res.addAll(
                processSquare(
                    region.x + j * stepX, region.x + (j + 1) * stepX,
                    region.y + i * stepY, region.y + (i + 1) * stepY,
                    values[i][j], values[i][j + 1], values[i + 1][j], values[i + 1][j + 1]
                )
            )
        }
    }

    return res
}