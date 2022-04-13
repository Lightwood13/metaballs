package com.example.metaballs

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

data class Particle(val position: Vector2, val speed: Vector2, val radius: Float) {
    fun update(deltaTime: Float, box: Rectangle) {
        position += speed * deltaTime

        if (position.x - radius <= box.x) {
            speed.x = -speed.x
            position.x = box.x + radius
        }
        if (position.x + radius >= box.x + box.width) {
            speed.x = -speed.x
            position.x = box.x + box.width - radius
        }
        if (position.y - radius <= box.y) {
            speed.y = -speed.y
            position.y = box.y + radius
        }
        if (position.y + radius >= box.y + box.height) {
            speed.y = -speed.y
            position.y = box.y + box.height - radius
        }
    }
}

fun generateRandomParticle(
    boundingBox: Rectangle,
    minRadius: Float, maxRadius: Float,
    minSpeed: Float, maxSpeed: Float
): Particle {
    val radius = MathUtils.random(minRadius, maxRadius)
    val centerX = MathUtils.random(
        boundingBox.x + radius,
        boundingBox.x + boundingBox.width - radius
    )
    val centerY = MathUtils.random(
        boundingBox.y + radius,
        boundingBox.y + boundingBox.height - radius
    )

    val speed = MathUtils.random(minSpeed, maxSpeed)
    val speedAngle = MathUtils.random(0f, MathUtils.PI2)

    return Particle(
        Vector2(centerX, centerY),
        Vector2(speed * MathUtils.cos(speedAngle), speed * MathUtils.sin(speedAngle)),
        radius
    )
}

fun generateRandomParticles(
    boundingBox: Rectangle,
    minRadius: Float, maxRadius: Float,
    minSpeed: Float, maxSpeed: Float,
    number: Int
): List<Particle> {
    val result: MutableList<Particle> = mutableListOf()
    for (i in 1..number) {
        result.add(generateRandomParticle(boundingBox, minRadius, maxRadius, minSpeed, maxSpeed))
    }
    return result
}