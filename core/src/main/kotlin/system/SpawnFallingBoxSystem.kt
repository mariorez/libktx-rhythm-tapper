package system

import GameBoot.Companion.sizes
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.IntervalSystem
import component.FallingBoxComponent
import component.RenderComponent
import component.TransformComponent
import ktx.assets.async.AssetStorage
import manager.SongManager

class SpawnFallingBoxSystem(
    private val songManager: SongManager,
    private val assets: AssetStorage
) : IntervalSystem() {

    private var currentTime = 0f
    private val boxSize = 48f
    private val padding = 300f
    private val gridSize = (sizes.worldWidthF() - padding) / 4
    private val xPos = (gridSize / 2 - boxSize / 2) + padding / 2
    private val yPos = 32f
    private val noteSpeed = (sizes.worldHeightF() - yPos) / 3
    private val colors = mapOf(
        "F" to Color.RED,
        "G" to Color.YELLOW,
        "H" to Color.GREEN,
        "J" to Color.BLUE
    )
    private val xPositions = mapOf(
        "F" to xPos,
        "G" to xPos + (gridSize * 1),
        "H" to xPos + (gridSize * 2),
        "J" to xPos + (gridSize * 3)
    )

    override fun onTick() {

        currentTime += deltaTime

        if (songManager.finished() || currentTime < songManager.currentTime()) return

        world.entity {
            add<FallingBoxComponent>()
            add<TransformComponent> {
                position.set(
                    xPositions[songManager.currentKey()]!!,
                    sizes.worldHeightF()
                )
                zIndex = 2f
                setSpeed(noteSpeed)
                setMotionAngle(270f)
            }
            add<RenderComponent> {
                sprite = Sprite(assets.get<Texture>("box.png")).apply {
                    setSize(boxSize, boxSize)
                    color = colors[songManager.currentKey()] ?: Color.WHITE
                }
            }
        }

        songManager.advanceIndex()
    }
}
