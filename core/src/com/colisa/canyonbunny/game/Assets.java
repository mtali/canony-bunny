package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.colisa.canyonbunny.util.Constants;

@SuppressWarnings("WeakerAccess")
public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();
    private static final String TAG = Assets.class.getName();
    // Game assets
    public AssetBunny bunnyAssets;
    public AssetRock rockAssets;
    public AssetGoldCoin goldCoinAssets;
    public AssetFeather featherAssets;
    public AssetLevelDecoration levelDecorationAssets;
    public AssetFonts assetFonts;
    public AssetSounds assetSounds;
    public AssetMusic assetMusic;
    private AssetManager assetManager;


    // singleton prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

        // load sounds
        assetManager.load(Constants.JUMP, Sound.class);
        assetManager.load(Constants.JUMP_WITH_FEATHER, Sound.class);
        assetManager.load(Constants.PICKUP_COIN, Sound.class);
        assetManager.load(Constants.PICKUP_FEATHER, Sound.class);
        assetManager.load(Constants.LIVE_LOST, Sound.class);

        // load music
        assetManager.load(Constants.MUSIC_O1, Music.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "Assets loaded:- " + assetManager.hashCode());

        // Caching the textures
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        // Enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // Creating game resource objects
        assetFonts = new AssetFonts();
        bunnyAssets = new AssetBunny(atlas);
        rockAssets = new AssetRock(atlas);
        goldCoinAssets = new AssetGoldCoin(atlas);
        featherAssets = new AssetFeather(atlas);
        levelDecorationAssets = new AssetLevelDecoration(atlas);

        assetSounds = new AssetSounds(assetManager);
        assetMusic = new AssetMusic(assetManager);

    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load assets: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetFonts.dispose();
        assetManager.dispose();
        Gdx.app.debug(TAG, "Disposing all game assets");
    }

    public class AssetBunny {
        public final AtlasRegion head;

        public final Animation animNormal;
        public final Animation animCopterTransform;
        public final Animation animCopterTransformBack;
        public final Animation animCopterRotate;

        public AssetBunny(TextureAtlas atlas) {
            head = atlas.findRegion(Constants.BUNNY_HEAD);

            Array<AtlasRegion> regions = null;
            AtlasRegion region = null;

            // Animation: Bunny Normal
            regions = atlas.findRegions(Constants.ANIMATION_BUNNY_NORMAL);
            animNormal = new Animation<AtlasRegion>(1.0f / 10.0f, regions, Animation.PlayMode.LOOP_PINGPONG);

            // Animation: Bunny Copter - knot ears
            regions = atlas.findRegions(Constants.ANIMATION_BUNNY_COPTER);
            animCopterTransform = new Animation<AtlasRegion>(1.0f / 10.0f, regions);

            // Animation: Bunny Copter - un-knot ears
            regions = atlas.findRegions(Constants.ANIMATION_BUNNY_COPTER);
            animCopterTransformBack = new Animation<AtlasRegion>(1.0f / 10.0f, regions, Animation.PlayMode.REVERSED);

            // Animation: Copter rotate
            regions = new Array<AtlasRegion>();
            regions.add(atlas.findRegion(Constants.ANIMATION_BUNNY_COPTER, 4));
            regions.add(atlas.findRegion(Constants.ANIMATION_BUNNY_COPTER, 5));
            animCopterRotate = new Animation<AtlasRegion>(1.0f / 15.0f, regions);
        }
    }

    public class AssetRock {
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public AssetRock(TextureAtlas atlas) {
            edge = atlas.findRegion(Constants.ROCK_EDGE);
            middle = atlas.findRegion(Constants.ROCK_MIDDLE);
        }
    }

    public class AssetGoldCoin {
        public final AtlasRegion goldCoin;
        public final Animation animGoldIcon;

        public AssetGoldCoin(TextureAtlas atlas) {
            goldCoin = atlas.findRegion(Constants.GOLD_COIN);

            // Animation: gold icon
            Array<AtlasRegion> regions = atlas.findRegions(Constants.COIN_ANIMATION);
            AtlasRegion region = regions.first();
            for (int i = 0; i < 10; i++)
                regions.insert(0, region);
            animGoldIcon = new Animation<AtlasRegion>(1.0f / 20.0f, regions, Animation.PlayMode.LOOP_PINGPONG);
        }
    }

    public class AssetFeather {
        public final AtlasRegion feather;

        public AssetFeather(TextureAtlas atlas) {
            feather = atlas.findRegion(Constants.FEATHER);
        }
    }

    public class AssetLevelDecoration {
        public final AtlasRegion cloud1;
        public final AtlasRegion cloud2;
        public final AtlasRegion cloud3;
        public final AtlasRegion mountainLeft;
        public final AtlasRegion mountainRight;
        public final AtlasRegion waterOverlay;
        public final AtlasRegion carrot;
        public final AtlasRegion goal;

        public AssetLevelDecoration(TextureAtlas atlas) {
            cloud1 = atlas.findRegion(Constants.CLOUDS_1);
            cloud2 = atlas.findRegion(Constants.CLOUDS_2);
            cloud3 = atlas.findRegion(Constants.CLOUDS_3);
            mountainLeft = atlas.findRegion(Constants.MOUNTAIN_LEFT);
            mountainRight = atlas.findRegion(Constants.MOUNTAIN_RIGHT);
            waterOverlay = atlas.findRegion(Constants.WATER_OVERLAY);
            carrot = atlas.findRegion(Constants.CARROT);
            goal = atlas.findRegion(Constants.GOAL);
        }
    }

    public class AssetFonts {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts() {
            defaultSmall = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);
            defaultBig = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);

            setScaleAndFilter(defaultSmall, 0.75f, TextureFilter.Linear);
            setScaleAndFilter(defaultNormal, 1.0f, TextureFilter.Linear);
            setScaleAndFilter(defaultBig, 2.0f, TextureFilter.Linear);
        }

        private void setScaleAndFilter(BitmapFont font, float scaleXY, Texture.TextureFilter filter) {
            font.getData().setScale(scaleXY);
            font.getRegion().getTexture().setFilter(filter, filter);
        }

        protected void dispose() {
            defaultSmall.dispose();
            defaultNormal.dispose();
            defaultBig.dispose();
        }
    }

    public class AssetSounds {
        public final Sound jump;
        public final Sound jumpWithFeather;
        public final Sound pickupCoin;
        public final Sound pickupFeather;
        public final Sound liveLost;

        public AssetSounds(AssetManager manager) {
            jump = manager.get(Constants.JUMP, Sound.class);
            jumpWithFeather = manager.get(Constants.JUMP_WITH_FEATHER, Sound.class);
            pickupCoin = manager.get(Constants.PICKUP_COIN);
            pickupFeather = manager.get(Constants.PICKUP_FEATHER, Sound.class);
            liveLost = manager.get(Constants.LIVE_LOST, Sound.class);
        }
    }

    public class AssetMusic {
        public final Music song01;

        public AssetMusic(AssetManager manager) {
            song01 = manager.get(Constants.MUSIC_O1, Music.class);
        }
    }
}
