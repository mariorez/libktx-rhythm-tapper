package system

import GameBoot.Companion.gameSizes
import manager.SongManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.IntervalSystem
import component.FallingBoxComponent
import component.RenderComponent
import component.TransformComponent
import ktx.assets.async.AssetStorage

class FallingBoxSystem(
    private val assets: AssetStorage
) : IntervalSystem() {

    private val songManager = SongManager()
    private var currentTime = 0f
    private val boxSize = 48f
    private val padding = 50f
    private val gridSize = (gameSizes.worldWidthF() - padding) / 4
    private val xPos = (gridSize / 2 - boxSize / 2) + padding / 2
    private val yPos = 70f
    private val noteSpeed = (gameSizes.worldHeightF() - yPos) / 3

    init {
        songManager.parseSongData(assets["funky-junky.txt"])
    }

    override fun onTick() {

        currentTime += deltaTime

        if (currentTime in 1.9f..2f) {
            world.entity {
                add<FallingBoxComponent>()
                add<TransformComponent> {
                    position.set(xPos, gameSizes.worldHeightF())
                    zIndex += 2
                    setSpeed(noteSpeed)
                    setMotionAngle(270f)
                }
                add<RenderComponent> {
                    sprite = Sprite(assets.get<Texture>("box.png")).apply {
                        setSize(boxSize, boxSize)
                        color = Color.RED
                    }
                }
            }
        }
    }
}
