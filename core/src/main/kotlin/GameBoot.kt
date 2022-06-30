import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.Platform
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import screen.GameScreen

class GameBoot : KtxGame<KtxScreen>() {

    companion object {
        val assets = AssetStorage()
        val gameSizes = GameSizes(
            windowWidth = 540,
            windowHeight = 960
        )
    }

    override fun create() {

        Gdx.input.inputProcessor = if (Platform.isMobile) InputMultiplexer()
        else InputMultiplexer(object : KtxInputAdapter {
            override fun keyDown(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, Action.Type.START)) }
                }
                return super.keyDown(keycode)
            }

            override fun keyUp(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, Action.Type.END)) }
                }
                return super.keyUp(keycode)
            }
        })

        KtxAsync.initiate()

        assets.apply {
            loadSync<Texture>("space.png").setFilter(Linear, Linear)
            loadSync<Texture>("box.png").setFilter(Linear, Linear)
        }

        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
