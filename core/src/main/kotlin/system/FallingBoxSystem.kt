package system

import Main.Companion.sizes
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.IntervalSystem
import component.FallingBoxComponent
import component.RenderComponent
import component.TransformComponent
import ktx.assets.async.AssetStorage
import manager.SongManager

class FallingBoxSystem(
    private val songManager: SongManager,
    private val assets: AssetStorage
) : IntervalSystem() {

    private var currentTime = 0f
    private val boxSize = 48f
    private val padding = 50f
    private val gridSize = (sizes.worldWidthF() - padding) / 4
    private val xPos = (gridSize / 2 - boxSize / 2) + padding / 2
    private val yPos = 70f
    private val noteSpeed = (sizes.worldHeightF() - yPos) / 3
    private val colors = mapOf(
        "F" to Color.RED,
        "G" to Color.YELLOW,
        "H" to Color.GREEN,
        "J" to Color.BLUE
    )

    override fun onTick() {

        currentTime += deltaTime

        if (songManager.finished() || currentTime < songManager.currentTime()) return

        world.entity {
            add<FallingBoxComponent>()
            add<TransformComponent> {
                position.set(xPos, sizes.worldHeightF())
                zIndex += 2
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
