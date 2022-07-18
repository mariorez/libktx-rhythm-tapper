import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.TextAssetLoader
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import screen.GameScreen

class GameBoot : KtxGame<KtxScreen>() {

    companion object {
        val assets = AssetStorage()
        val sizes = Sizes(
            windowWidth = 800,
            windowHeight = 600
        )
    }

    override fun create() {

        Gdx.input.inputProcessor = InputMultiplexer(object : KtxInputAdapter {
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
            setLoader<Music> { MusicLoader(fileResolver) }
            setLoader<String> { TextAssetLoader(fileResolver) }
            setLoader<FreeTypeFontGenerator> { FreeTypeFontGeneratorLoader(fileResolver) }
            setLoader<BitmapFont>(".ttf") { FreetypeFontLoader(fileResolver) }

            loadSync<BitmapFont>("open-sans.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
                fontFileName = "open-sans.ttf"
                fontParameters.apply {
                    size = 32
                    color = Color.WHITE
                    borderColor = Color.BLACK
                    borderWidth = 2f
                    borderStraight = true
                    minFilter = Texture.TextureFilter.Linear
                    magFilter = Texture.TextureFilter.Linear
                }
            })
            loadSync<String>("funky-junky.txt")
            loadSync<Music>("funky-junky.mp3")
            loadSync<Texture>("space.png").setFilter(Linear, Linear)
            loadSync<Texture>("box.png").setFilter(Linear, Linear)
            loadSync<Texture>("button.png").setFilter(Linear, Linear)
        }

        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
