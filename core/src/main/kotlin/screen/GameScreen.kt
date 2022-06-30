package screen

import Action
import Action.Type.START
import BaseScreen
import GameBoot.Companion.assets
import GameBoot.Companion.gameSizes
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import component.InputComponent
import component.RenderComponent
import component.TargetBoxComponent
import component.TransformComponent
import system.InputSystem
import system.MovementSystem
import system.RenderSystem

class GameScreen : BaseScreen() {
    private val player: Entity
    private val world = World {
        inject(batch)
        inject(camera)
        system<InputSystem>()
        system<MovementSystem>()
        system<RenderSystem>()
    }

    init {
        buildControls()

        world.apply {
            player = entity {
                add<InputComponent>()
            }

            entity {
                add<TransformComponent>()
                add<RenderComponent> {
                    sprite = Sprite(assets.get<Texture>("space.png"))
                }
            }

            val box = assets.get<Texture>("box.png")
            val padding = 100f
            val gridSize = (gameSizes.worldWidthF() - padding) / 4
            var boxPos = (gridSize / 2 - box.width / 2) + padding / 2

            (1..4).forEach {
                entity {
                    add<TargetBoxComponent>()
                    add<TransformComponent> {
                        zIndex = 1f
                        position.set(boxPos, 70f)
                    }
                    add<RenderComponent> { sprite = Sprite(box) }
                }
                boxPos += gridSize
            }
        }
    }

    override fun render(delta: Float) {
        world.update(delta)
        hudStage.draw()
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
    }

    private fun buildControls() {

        val table = Table().apply {
            setFillParent(true)
            pad(10f)
        }

        registerAction(Input.Keys.UP, Action.Name.UP)
        registerAction(Input.Keys.DOWN, Action.Name.DOWN)
        registerAction(Input.Keys.LEFT, Action.Name.LEFT)
        registerAction(Input.Keys.RIGHT, Action.Name.RIGHT)

        hudStage.addActor(table)
    }

    override fun doAction(action: Action) {
        val input = world.mapper<InputComponent>()[player]
        val isStarting = action.type == START
        when (action.name) {
            Action.Name.UP -> input.up = isStarting
            Action.Name.DOWN -> input.down = isStarting
            Action.Name.LEFT -> input.left = isStarting
            Action.Name.RIGHT -> input.right = isStarting
        }
    }
}
