package component

import Main.Companion.assets
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle

data class TargetBoxComponent(
    var label: Label = Label("", LabelStyle().apply {
        font = assets.get<BitmapFont>("open-sans.ttf")
    })
)
