package View.loader;

import Model.Csv;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class CsvLoader extends SynchronousAssetLoader<Csv, CsvLoader.CsvParameter> {

    public CsvLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Csv load(AssetManager assetManager, String fileName, FileHandle file, CsvParameter parameter) {
        return new Csv(file);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CsvParameter parameter) {
        return null;
    }

    public static class CsvParameter extends AssetLoaderParameters<Csv> {

    }
}
