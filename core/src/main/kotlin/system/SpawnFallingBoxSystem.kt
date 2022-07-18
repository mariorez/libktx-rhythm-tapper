package system

import GameBoot.Companion.assets
import GameBoot.Companion.sizes
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.IntervalSystem
import component.FallingBoxComponent
import component.RenderComponent
import component.TransformComponent
import manager.SongManager

class SpawnFallingBoxSystem : IntervalSystem() {

    private val songManager = SongManager(assets["funky-junky.txt"])
    private val music = assets.get<Music>(songManager.songName)

    private var increasedZIndex = 2f
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

        music.play()

        if (songManager.finished() || music.position < songManager.currentTime()) return

        world.entity {
            add<FallingBoxComponent>()
            add<TransformComponent> {
                position.set(
                    xPositions[songManager.currentKey()]!!,
                    sizes.worldHeightF()
                )
                zIndex = increasedZIndex++
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
