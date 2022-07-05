package screen

import Action
import Action.Type.START
import BaseScreen
import GameBoot.Companion.assets
import GameBoot.Companion.gameSizes
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import component.InputComponent
import component.PlayerComponent
import component.RenderComponent
import component.TargetBoxComponent
import component.TransformComponent
import system.FallingBoxSystem
import system.InputSystem
import system.MovementSystem
import system.RenderSystem

class GameScreen : BaseScreen() {
    private val player: Entity
    private val world = World {
        inject(batch)
        inject(camera)
        inject(assets)
        system<InputSystem>()
        system<FallingBoxSystem>()
        system<MovementSystem>()
        system<RenderSystem>()
    }

    init {
        buildControls()
        spawnTargetBox()

        world.apply {
            player = entity {
                add<PlayerComponent>()
                add<InputComponent>()
            }

            entity {
                add<TransformComponent>()
                add<RenderComponent> {
                    sprite = Sprite(assets.get<Texture>("space.png"))
                }
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

    private fun spawnTargetBox() {
        var button = assets.get<Texture>("button.png")
        var counter = 0
        val padding = 50f
        val gridSize = (gameSizes.worldWidthF() - padding) / 4
        val xPos = (gridSize / 2 - button.width / 2) + padding / 2
        val yPos = 70f

        mapOf(
            "F" to Color.RED,
            "G" to Color.YELLOW,
            "H" to Color.GREEN,
            "J" to Color.BLUE
        ).forEach { (letter, color) ->
            world.entity {
                add<TargetBoxComponent> {
                    label.apply {
                        setText(letter)
                        setColor(color)
                        setSize(button.width.toFloat(), button.height.toFloat())
                        setAlignment(Align.center)
                    }
                }
                add<TransformComponent> {
                    zIndex = 1f
                    position.set(xPos + (gridSize * counter), yPos)
                }
                add<RenderComponent> { sprite = Sprite(button) }
            }
            counter++
        }
    }

    private fun buildControls() {

        val table = Table().apply {
            setFillParent(true)
            pad(10f)
        }

        registerAction(Input.Keys.F, Action.Name.F)
        registerAction(Input.Keys.G, Action.Name.G)
        registerAction(Input.Keys.H, Action.Name.H)
        registerAction(Input.Keys.J, Action.Name.J)

        hudStage.addActor(table)
    }

    override fun doAction(action: Action) {
        val input = world.mapper<InputComponent>()[player]
        val isStarting = action.type == START
        when (action.name) {
            Action.Name.F -> input.f = isStarting
            Action.Name.G -> input.g = isStarting
            Action.Name.H -> input.h = isStarting
            Action.Name.J -> input.j = isStarting
        }
    }
}
