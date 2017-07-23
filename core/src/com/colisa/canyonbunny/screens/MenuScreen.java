package com.colisa.canyonbunny.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.colisa.canyonbunny.game.Assets;
import com.colisa.canyonbunny.screens.transitions.ScreenTransition;
import com.colisa.canyonbunny.screens.transitions.ScreenTransitionFade;
import com.colisa.canyonbunny.util.AudioManager;
import com.colisa.canyonbunny.util.CharacterSkin;
import com.colisa.canyonbunny.util.Constants;
import com.colisa.canyonbunny.util.GamePreferences;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

public class MenuScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    // Debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;

    private Stage stage;
    private Skin skinCanyonBunny;
    private Skin skinLibgdx;
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
    private SelectBox<CharacterSkin> selectCharacterSkin;
    private CheckBox checkShowFpsCounter;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen(DirectedGame game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }

        stage.act(deltaTime);
        stage.draw();
        stage.setDebugAll(debugEnabled);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT));
        rebuildStage();
    }

    @Override
    public void hide() {
        stage.dispose();
        skinLibgdx.dispose();
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

        skinLibgdx = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)
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
        imageCoins.setOrigin(imageCoins.getWidth() / 2, imageCoins.getHeight() / 2);
        imageCoins.addAction(sequence(
                moveTo(135, -20),
                scaleTo(0, 0),
                fadeOut(0),
                delay(2.5f),
                parallel(moveBy(0, 100, 0.5f, Interpolation.swingOut)),
                scaleTo(1, 1, 0.25f, Interpolation.linear),
                alpha(1.0f, 0.7f, Interpolation.linear)

        ));
        //imageCoins.setPosition(135, 80);
        // +bunny
        imageBunny = new Image(skinCanyonBunny, "bunny");
        layer.addActor(imageBunny);
        imageBunny.addAction(sequence(
                moveTo(655, 510),
                delay(4.0f),
                moveBy(-70, -100, 0.5f, Interpolation.fade),
                moveBy(-100, -50, 0.5f, Interpolation.fade),
                moveBy(-150, -300, 1.0f, Interpolation.elasticIn)
        ));
        //imageBunny.setPosition(355, 40);
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


    private void onPlayClicked() {
        ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        game.setScreen(new GameScreen(game), transition);
    }

    private void onOptionsClicked() {
        loadSettings();
        showMenuButtons(false);
        showOptionsWindow(true, true);
    }

    private Table buildOptionsWindowAudioLayer() {
        Table tbl = new Table();
        // + Title: "Audio"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox, "Sound" label, sound volume slider
        checkSound = new CheckBox("", skinLibgdx);
        tbl.add(checkSound);
        tbl.add(new Label("Sound", skinLibgdx));
        sliderSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sliderSound);
        tbl.row();
        // + Checkbox, "Music" label, music volume slider
        checkMusic = new CheckBox("", skinLibgdx);
        tbl.add(checkMusic);
        tbl.add(new Label("Music", skinLibgdx));
        sliderMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
        tbl.add(sliderMusic);
        tbl.row();
        return tbl;

    }

    private Table buildOptionSkinSelection() {
        Table tbl = new Table();
        //+ Title: "Character Skin"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Character Skin", skinLibgdx, "default-font", Color.ORANGE)).colspan(2);
        tbl.row();

        // + Dropdown box filled with skin items
        selectCharacterSkin = new SelectBox<CharacterSkin>(skinLibgdx);

        // This code doesn't work on HTML
        // selectCharacterSkin.setItems(CharacterSkin.values());
        Array<CharacterSkin> items = new Array<CharacterSkin>();
        CharacterSkin[] arr = CharacterSkin.values();
        for (CharacterSkin anArr : arr) {
            items.add(anArr);
        }
        selectCharacterSkin.setItems(items);
        selectCharacterSkin.addListener(new ChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void changed(ChangeEvent event, Actor actor) {
                onCharSkinSelected(((SelectBox<CharacterSkin>) actor).getSelectedIndex());
            }
        });
        tbl.add(selectCharacterSkin).width(120).padRight(20);
        // + Skin preview image
        imageCharacterSkin = new Image(Assets.instance.bunnyAssets.head);
        tbl.add(imageCharacterSkin).width(50).height(50);
        return tbl;
    }

    private Table buildOptWinDebug() {
        Table tbl = new Table();
        // + Title: "Debug"
        tbl.pad(10, 10, 0, 10);
        tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        // + Checkbox "Show FPS Counter" label

        checkShowFpsCounter = new CheckBox("", skinLibgdx);
        tbl.add(new Label("Show FPS Counter", skinLibgdx));
        tbl.add(checkShowFpsCounter);
        tbl.row();
        return tbl;
    }

    private Table buildOptWinButton() {
        Table tbl = new Table();
        // + Separator
        Label lbl = null;
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.75f, 0.75f, 0.75f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
        tbl.row();
        lbl = new Label("", skinLibgdx);
        lbl.setColor(0.5f, 0.5f, 0.5f, 1);
        lbl.setStyle(new Label.LabelStyle(lbl.getStyle()));
        lbl.getStyle().background = skinLibgdx.newDrawable("white");
        tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
        tbl.row();

        // + Save Button with event handle
        buttonWindowOptionSave = new TextButton("Save", skinLibgdx);
        tbl.add(buttonWindowOptionSave).padRight(30);
        buttonWindowOptionSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });

        // + Cancel Button woth event handler
        buttonWindowOptionSCancel = new TextButton("Cancel", skinLibgdx);
        tbl.add(buttonWindowOptionSCancel);
        buttonWindowOptionSCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });

        return tbl;
    }

    private Table buildOptionsWindowLayer() {
        winOptions = new Window("Options", skinLibgdx);
        // + Audio Settings: Sound/Music checkbox and volume slider
        winOptions.add(buildOptionsWindowAudioLayer()).row();
        // + Character Skin: Select Box (White, Gray, Brown)
        winOptions.add(buildOptionSkinSelection()).row();
        // + Debug show FPS Counter
        winOptions.add(buildOptWinDebug()).row();
        // + Separators and Buttons (Save, Cancel)
        winOptions.add(buildOptWinButton()).pad(10, 0, 10, 0);

        // Make options window slight transparent
        winOptions.setColor(1, 1, 1, 0.8f);
        // Hide options windows bu default
        showOptionsWindow(false, false);
        if (debugEnabled) winOptions.debug();

        // Let table recalculate widget sizes and position
        winOptions.pack();
        // Move options window to bottom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
        return winOptions;
    }

    private void loadSettings() {
        GamePreferences preferences = GamePreferences.instance;
        preferences.load();
        // Sound
        checkSound.setChecked(preferences.sound);
        sliderSound.setValue(preferences.volumeSound);
        // Music
        checkMusic.setChecked(preferences.music);
        sliderMusic.setValue(preferences.volumeMusic);
        // Character Skin
        selectCharacterSkin.setSelectedIndex(preferences.characterSkin);
        onCharSkinSelected(preferences.characterSkin);
        // FPS
        checkShowFpsCounter.setChecked(preferences.showFPSCounter);
    }

    private void saveSettings() {
        GamePreferences prefs = GamePreferences.instance;
        prefs.sound = checkSound.isChecked();
        prefs.volumeSound = sliderSound.getValue();
        prefs.music = checkMusic.isChecked();
        prefs.volumeMusic = sliderMusic.getValue();
        prefs.characterSkin = selectCharacterSkin.getSelectedIndex();
        prefs.showFPSCounter = checkShowFpsCounter.isChecked();
        prefs.save();
    }

    private void onCharSkinSelected(int index) {
        CharacterSkin skin = CharacterSkin.values()[index];
        imageCharacterSkin.setColor(skin.getColor());
    }

    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
        AudioManager.instance.onSettingsUpdated();
    }

    private void onCancelClicked() {
        showMenuButtons(true);
        showOptionsWindow(false, true);
        AudioManager.instance.onSettingsUpdated();
    }

    private void showMenuButtons(boolean visible) {
        float moveDuration = 1.0f;
        Interpolation moveEasing = Interpolation.swing;
        float delayOptionsButton = 0.25f;

        float moveX = 300 * (visible ? -1 : 1);
        float moveY = 0;
        final Touchable touchEnabled = (visible ? Touchable.enabled : Touchable.disabled);
        buttonMenuPlay.addAction(
                moveBy(moveX, moveY, moveDuration, moveEasing)
        );

        buttonMenuOptions.addAction(sequence(
                delay(delayOptionsButton),
                moveBy(moveX, moveY, moveDuration, moveEasing)
        ));

        SequenceAction seq = new SequenceAction();
        seq.addAction(delay(delayOptionsButton + moveDuration));
        seq.addAction(run(new Runnable() {
            @Override
            public void run() {
                buttonMenuPlay.setTouchable(touchEnabled);
                buttonMenuOptions.setTouchable(touchEnabled);
            }
        }));

        stage.addAction(seq);
    }

    private void showOptionsWindow(boolean visible, boolean animated) {
        float alphaTo = visible ? 0.8f : 0.0f;
        float duration = animated ? 1.0f : 0.0f;
        Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
        winOptions.addAction(sequence(
                touchable(touchEnabled),
                alpha(alphaTo, duration)
        ));
    }

}
