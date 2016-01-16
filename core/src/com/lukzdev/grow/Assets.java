package com.lukzdev.grow;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

public class Assets extends AssetManager {


    public static class Textures {
        public static final String Explosion = "sfx/guns/explosion";
    }

    private FileHandleResolver resolver;


    public Assets() {
        super();

        enqueueAssets();
    }

    public void enqueueAssets() {


    }

}
