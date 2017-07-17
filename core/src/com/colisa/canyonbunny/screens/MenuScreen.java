package com.colisa.canyonbunny.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.colisa.canyonbunny.util.Constants;

public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    // Debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private Stage stage;
    private Skin skinCanyonBunny;
    // Menu
    private Image imageBackground;
    private Image imageLogo;
    private Image imageInfo;
    private Image imageCoins;
    private Image imageBunny;
    private Button buttonMenuPlay;
    private Button buttonMenuOptions;
    // Options
    private Window winOptions;
    private TextButton buttonWindowOptionSave;
    private TextButton buttonWindowOptionSCancel;
    private CheckBox checkSound;
    private Slider sliderSound;
    private CheckBox checkMusic;
    private Slider sliderMusic;
    private Image imageCharacterSkin;
    private SelectBox<Object> selectCharacterSkin;
    private CheckBox checkShowFpsCounter;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled){
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0){
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(debugEnabled);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinCanyonBunny.dispose();
    }


    @Override
    public void pause() {
    }

    private void rebuildStage() {
        skinCanyonBunny = new Skin(
                Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI)
        );

        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectLayer();
        Table layerLogos = buildLogoLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();

        // Assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stack.pack();
        stage.addActor(layerOptionsWindow);
    }

    private Table buildBackgroundLayer() {
        Table layer = new Table();
        // + background
        imageBackground = new Image(skinCanyonBunny, "background");
        layer.add(imageBackground);
        return layer;
    }

    private Table buildObjectLayer() {
        Table layer = new Table();
        // + coins
        imageCoins = new Image(skinCanyonBunny, "coins");
        layer.addActor(imageCoins);
        imageCoins.setPosition(135, 80);
        // +bunny
        imageBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imageBunny);
        imageBunny.setPosition(355,40);
        return layer;
    }

    private Table buildLogoLayer() {
        Table layer = new Table();
        layer.left().top();
        // + game logo
        imageLogo = new Image(skinCanyonBunny, "logo");
        layer.add(imageLogo);
        layer.row().expandY();
        // + info logo
        imageInfo = new Image(skinCanyonBunny, "info");
        layer.add(imageInfo).bottom();
        if (debugEnabled) layer.debug();
        return layer;
    }

    private Table buildControlsLayer() {
        Table layer = new Table();
        layer.right().bottom();
        buttonMenuPlay = new Button(skinCanyonBunny, "play");
        layer.add(buttonMenuPlay);
        buttonMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });

        layer.row();
        // + options button
        buttonMenuOptions = new Button(skinCanyonBunny, "options");
        layer.add(buttonMenuOptions);
        buttonMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });

        if (debugEnabled) layer.debug();
        return layer;
    }


    private void onPlayClicked(){
        game.setScreen(new GameScreen(game));
    }

    private void onOptionsClicked(){

    }
    private Table buildOptionsWindowLayer() {
        return new Table();
    }

}
