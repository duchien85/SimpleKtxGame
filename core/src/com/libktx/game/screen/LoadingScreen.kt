package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.libktx.game.Game
import com.libktx.game.asset.AtlasAssets
import com.libktx.game.asset.MusicAssets
import com.libktx.game.asset.SoundAssets
import com.libktx.game.asset.load
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(private val batch: Batch,
                    private val camera: Camera,
                    private val font: BitmapFont,
                    private val game: Game,
                    private val assets: AssetManager) : KtxScreen {
    override fun show() {
        // load all assets for the game
        AtlasAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }
        MusicAssets.values().forEach { assets.load(it) }
    }

    override fun render(delta: Float) {
        batch.use {
            font.draw(it, "Welcome to Drop!!!", 300f, 250f)
            // update asset manager
            if (assets.update()) {
                font.draw(it, "Tap anywhere to begin!", 300f, 200f)
            } else {
                font.draw(it, "Loading assets...", 300f, 200f)
            }
        }

        if (Gdx.input.isTouched && assets.isFinished) {
            // if the player touches the screen and all assets are loaded
            // then we can switch to the game screen
            game.addScreen(GameScreen(batch, camera, assets))
            game.setScreen<GameScreen>()
            // and remove the LoadingScreen as it is no longer needed
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }
}