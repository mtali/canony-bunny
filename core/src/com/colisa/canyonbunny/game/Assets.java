package com.colisa.canyonbunny.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
    private AssetManager assetManager;

    // singleton prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);

        // Caching the textures
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        // Enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()){
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // Creating game resource objects
        assetFonts = new AssetFonts();
        bunnyAssets = new AssetBunny(atlas);
        rockAssets = new AssetRock(atlas);
        goldCoinAssets = new AssetGoldCoin(atlas);
        featherAssets = new AssetFeather(atlas);
        levelDecorationAssets = new AssetLevelDecoration(atlas);

    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load assets: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class AssetBunny {
        public final AtlasRegion head;

        public AssetBunny(TextureAtlas atlas) {
            head = atlas.findRegion(Constants.BUNNY_HEAD);
        }
    }

    public class AssetRock {
        public final AtlasRegion edge;
        public final AtlasRegion middle;

        public AssetRock(TextureAtlas atlas){
            edge = atlas.findRegion(Constants.ROCK_EDGE);
            middle = atlas.findRegion(Constants.ROCK_MIDDLE);
        }
    }

    public class AssetGoldCoin {
        public final AtlasRegion goldCoin;

        public AssetGoldCoin(TextureAtlas atlas){
            goldCoin = atlas.findRegion(Constants.GOLD_COIN);
        }
    }

    public class AssetFeather{
        public final AtlasRegion feather;

        public AssetFeather(TextureAtlas atlas){
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

        public AssetLevelDecoration(TextureAtlas atlas){
            cloud1 = atlas.findRegion(Constants.CLOUDS_1);
            cloud2 = atlas.findRegion(Constants.CLOUDS_2);
            cloud3 = atlas.findRegion(Constants.CLOUDS_3);
            mountainLeft = atlas.findRegion(Constants.MOUNTAIN_LEFT);
            mountainRight = atlas.findRegion(Constants.MOUNTAIN_RIGHT);
            waterOverlay = atlas.findRegion(Constants.WATER_OVERLAY);
        }
    }

    public class AssetFonts{
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;

        public AssetFonts(){
            defaultSmall = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);
            defaultNormal = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);
            defaultBig = new BitmapFont(Gdx.files.internal(Constants.FONTS), true);

            setScaleAndFilter(defaultSmall, 0.75f, TextureFilter.Linear);
            setScaleAndFilter(defaultNormal, 1.0f, TextureFilter.Linear);
            setScaleAndFilter(defaultBig, 2.0f, TextureFilter.Linear);
        }

        private void setScaleAndFilter(BitmapFont font, float scaleXY, Texture.TextureFilter filter){
            font.getData().setScale(scaleXY);
            font.getRegion().getTexture().setFilter(filter, filter);
        }

        protected void dispose(){
            defaultSmall.dispose();
            defaultNormal.dispose();
            defaultBig.dispose();
        }
    }
}
