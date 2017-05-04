package app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 23-Nov-16.
 */
public class LegionApplication extends Application implements Legion_Constants{

    private static LegionApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        instance = this;
        createAppDirectoryIfRequired();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static LegionApplication getInstance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(instance).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(instance).trimMemory(level);
    }

    private void createAppDirectoryIfRequired() {
        File dir = new File(LegionUtils.getAppDirPath(getApplicationContext()));
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

}
