package com.example.metaballs

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils

class Application(width: Int, height: Int) : ApplicationAdapter() {

    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer

    private val boundingBox: Rectangle
    private var particles: List<Particle> = listOf()
    private var potential: (Vector2) -> Float = { 1f }

    override fun create() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()

        particles = generateRandomParticles(
            boundingBox,
            20f, 30f,
            50f, 100f,
            5
        )
        potential = getPotential(particles)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)

        for (particle in particles) {
            particle.update(Gdx.graphics.deltaTime, boundingBox)
        }

        batch.begin()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.setColor(1f, 0f, 0f, 1f)

        val lines = marchingSquares(
            potential,
            Rectangle(0f, 0f, 800f, 600f),
            100
        )
        for (line in lines) {
            shapeRenderer.rectLine(line.first, line.second, 3f)
        }
        shapeRenderer.end()
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }

    init {
        boundingBox = Rectangle(20f, 20f, width.toFloat() - 40, height.toFloat() - 40)
    }
}