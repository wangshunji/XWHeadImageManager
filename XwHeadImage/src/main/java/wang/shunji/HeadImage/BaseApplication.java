package wang.shunji.HeadImage;

import android.app.Application;

/**
 * Created by shunjiwang on 2018/3/20.
 */

public class BaseApplication extends Application {
    private static  BaseApplication mGlobleContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mGlobleContext=this;
    }

    public static BaseApplication getDefault() {
        return mGlobleContext;
    }
}
