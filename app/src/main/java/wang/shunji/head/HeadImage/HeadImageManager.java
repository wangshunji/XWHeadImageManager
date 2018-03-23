package wang.shunji.head.HeadImage;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import wang.shunji.head.HeadImage.Callback.HeadImageCallback;
import wang.shunji.head.HeadImage.Exception.DialogItemCountException;
import wang.shunji.head.R;

/**
 * @function 用来处理app个人头像 的类
 * Created by shunjiwang on 2018/3/15.
 */

public class HeadImageManager {

    private Context mContext;
    private Bundle optionsBundle;

    /**
     * popwindow 需要的参数
     **/
    private String mDialogTitle;
    private String[] mItems = new String[2];
    private HeadImageCallback mCallback;
    private boolean mIsShow = true;
    private int[] mItempics;
    private int mHiddenItem;


    /**********************构造函数**********************/

    private HeadImageManager() {
    }

    public HeadImageManager(Context context) {
        mContext = context;
        optionsBundle = new Bundle();
    }

    /*******************创建本类实例********************/

    public static HeadImageManager create(Context context) {
        return new HeadImageManager(context);
    }

    /*******************底部弹窗********************/
    /**
     * @param popTitle dialog 标题
     * @return
     */
    public HeadImageManager dialogTitle(String popTitle) {
        mDialogTitle = popTitle;
        return this;
    }

    /**
     * dialog选项文字
     */
    public HeadImageManager dialogItemText(String... items) throws DialogItemCountException {
        if (items.length != 2) {
            throw new DialogItemCountException("Dialog的Item数组个数设置不对，要么不设置，要设置必须是两个");
        }
        mItems = items;
        return this;
    }

    /**
     * 是否显示item图标
     *
     * @param isShow true 是显示 ，false是隐藏  默认显示
     * @return
     */
    public HeadImageManager isShowItemPic(boolean isShow) {
        mIsShow = isShow;
        return this;
    }

    /**
     * 设置item图标
     *
     * @param drawableResid 相册在前，相机在后, 没有的请传0
     * @return
     */
    public HeadImageManager setItemPics(@DrawableRes int... drawableResid) throws DialogItemCountException {
        if (drawableResid.length != 2) {
            throw new DialogItemCountException("Dialog的Item数组个数设置不对，要么不设置，要设置必须是两个");
        }
        mItempics = drawableResid;
        return this;
    }

    /**
     * 设置要隐藏的item
     *
     * @param hiddenItem 1 为隐藏相册，2为隐藏相机
     * @return
     */
    public HeadImageManager setHiddenItem(int hiddenItem) {
        mHiddenItem = hiddenItem;
        return this;
    }

    /**********************裁剪*************************/

    /**
     * 设置裁剪的宽高比  比如1:1或16:9 等等
     *
     * @param x 宽
     * @param y 高
     */
    public HeadImageManager setWithAspectRatio(float x, float y) {
//        mAspectRatioX = x;
//        mAspectRatioY = y;
//
        optionsBundle.putFloat(ConstantValue.DATA_ASPECTRATIOX, x);
        optionsBundle.putFloat(ConstantValue.DATA_ASPECTRATIOY, y);
        return this;
    }

    /**
     * 设置用源图像的宽高比为裁剪的宽高比
     */
    public HeadImageManager setUseSourceImageAspectRatio() {
//        mUseSourceImageAspectRatio =
        //true 代表用户设置了 ，默认为false
        optionsBundle.putBoolean(ConstantValue.DATA_USESOURCEIMAGEASPECTRATIO, true);
        return this;
    }

    /**
     * 设置裁剪后图像的最大尺寸
     *
     * @param width  图片裁剪后最大宽值
     * @param height 图片裁剪后最大高值
     */
    public HeadImageManager setWithMaxResultSize(@IntRange(from = 100) int width, @IntRange(from = 100) int height) {
        optionsBundle.putInt(ConstantValue.DATA_MAXRESULTIMAGESIZEX, width);
        optionsBundle.putInt(ConstantValue.DATA_MAXRESULTIMAGESIZEY, height);
//        mMaxResultImageSizeX = width;
//        mMaxResultImageSizeY = height;
        return this;
    }

    /**
     * 图片裁剪的格式,具体请查看 {@link android.graphics.Bitmap.CompressFormat}
     *
     * @param format 格式
     */
    public HeadImageManager setCompressionFormat(@NonNull Bitmap.CompressFormat format) {
        optionsBundle.putString(ConstantValue.DATA_FORMAT, format.name());
//        mFormat = format;
        return this;
    }

    /**
     * 设置图像压缩的质量
     *
     * @param compressQuality 值为0到100  值越高图像质量越好
     * @return
     */
    public HeadImageManager setCompressionQuality(@IntRange(from = 0) int compressQuality) {
//        mCompressQuality = compressQuality;
        optionsBundle.putInt(ConstantValue.DATA_COMPRESSQUALITY, compressQuality);
        return this;
    }

    /**
     * 设置裁剪图片可操作的手势
     *
     * @param tabScale
     * @param tabRotate
     * @param tabAspectRatio
     * @return
     */
    public HeadImageManager setAllowedGestures(@CropActivity.GestureTypes int tabScale,
                                               @CropActivity.GestureTypes int tabRotate,
                                               @CropActivity.GestureTypes int tabAspectRatio) {
//        mGestures = new int[]{tabScale, tabRotate, tabAspectRatio};
        optionsBundle.putIntArray(ConstantValue.DATA_GESTURES, new int[]{tabScale, tabRotate, tabAspectRatio});
        return this;
    }

    /**
     * 设置最多缩放的比例尺
     *
     * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
     */
    public HeadImageManager setMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false) float maxScaleMultiplier) {
//        mMaxScaleMultiplier = maxScaleMultiplier;
        optionsBundle.putFloat(ConstantValue.DATA_MAXSCALEMULTIPLIER, maxScaleMultiplier);
        return this;
    }

    /**
     * 设置图像的动画持续时间
     *
     * @param durationMillis 时间为秒
     * @return
     */
    public HeadImageManager setImageToCropBoundsAnimDuration(@IntRange(from = 100) int durationMillis) {
//        mAnimTime = durationMillis;
        optionsBundle.putInt(ConstantValue.DATA_ANIMTIME, durationMillis);
        return this;
    }

    /**
     * 设置图片压缩最大值
     *
     * @param maxBitmapSize 值为px
     * @return
     */
    public HeadImageManager setMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize) {
//        mMaxBitmapSize = maxBitmapSize;
        optionsBundle.putInt(ConstantValue.DATA_MAXBITMAPSIZE, maxBitmapSize);
        return this;
    }

    /**
     * 设置椭圆裁剪框阴影颜色
     *
     * @param color color资源id
     * @return
     */
    public HeadImageManager setDimmedLayerColor(@ColorInt int color) {
        optionsBundle.putInt(ConstantValue.DATA_LAYERCOLOR, color);
//        mLayerColor = color;
        return this;
    }

    /**
     * 是否显示裁剪框
     *
     * @param show
     * @return
     */
    public HeadImageManager setShowCropFrame(boolean show) {
//        mShowƒCropFrame = show;
        optionsBundle.putBoolean(ConstantValue.DATA_SHOWCROPFRAME, show);
        return this;
    }

    /**
     * 是否显示椭圆裁剪框阴影
     *
     * @param isOval
     * @return
     */
    public HeadImageManager setOvalDimmedLayer(boolean isOval) {
        optionsBundle.putBoolean(ConstantValue.DATA_ISOVAL, isOval);
//        mIsOval = isOval;
        return this;
    }

    /**
     * 裁剪框 边框颜色
     *
     * @param colorId
     * @return
     */
    public HeadImageManager setCropFrameColor(@ColorInt int colorId) {
//        mCropFrameColor = colorId;
        optionsBundle.putInt(ConstantValue.DATA_CROPFRAMECOLOR, colorId);
        return this;
    }

    /**
     * 设置裁剪框边的宽度
     *
     * @return
     */
    public HeadImageManager setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
//        mCropFrameStrokeWidth = width;
        optionsBundle.putInt(ConstantValue.DATA_CROPFRAMESTROKEWIDTH, width);
        return this;
    }

    /**
     * 是否显示裁剪框网格
     *
     * @param show
     * @return
     */
    public HeadImageManager setShowCropGrid(boolean show) {
//        mShowCropGrid = show;
        optionsBundle.putBoolean(ConstantValue.DATA_SHOWCROPGRID, show);
        return this;
    }

    /**
     * 设置裁剪网格的行数
     *
     * @param count
     * @return
     */
    public HeadImageManager setCropGridRowCount(@IntRange(from = 0) int count) {
//        mRowCount = count;
        optionsBundle.putInt(ConstantValue.DATA_ROWCOUNT, count);
        return this;
    }

    /**
     * 设置裁剪网格的列数
     *
     * @param count
     * @return
     */
    public HeadImageManager setCropGridColumnCount(@IntRange(from = 0) int count) {
//        mColumnCount = count;
        optionsBundle.putInt(ConstantValue.DATA_COLUMNCOUNT, count);
        return this;
    }

    /**
     * 设置裁剪框网格颜色
     *
     * @param color
     * @return
     */
    public HeadImageManager setCropGridColor(@ColorInt int color) {
//        mCropGridColor = color;
        optionsBundle.putInt(ConstantValue.DATA_CROPGRIDCOLOR, color);
        return this;
    }

    /**
     * 设置裁剪框网格线宽
     *
     * @param width
     * @return
     */
    public HeadImageManager setCropGridStrokeWidth(@IntRange(from = 0) int width) {
//        mGridStrokeWidth = width;
        optionsBundle.putInt(ConstantValue.DATA_GRIDSTROKEWIDTH, width);
        return this;
    }

    /**
     * 设置toolbar颜色
     *
     * @param colorid
     * @return
     */
    public HeadImageManager setToolbarColor(@ColorInt int colorid) {
        optionsBundle.putInt(ConstantValue.DATA_TOOLBARCOLOR, colorid);
//        mToolbarColor = colorid;
        return this;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorid
     * @return
     */
    public HeadImageManager setStatusBarColor(@ColorInt int colorid) {
        optionsBundle.putInt(ConstantValue.DATA_STATUSBARCOLOR, colorid);
//        mStatusBarColor = colorid;
        return this;
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorId
     * @return
     */
    public HeadImageManager setActiveWidgetColor(@ColorInt int colorId) {
//        mActiveWidgetColor = colorId;
        optionsBundle.putInt(ConstantValue.DATA_ACTIVEWIDGETCOLOR, colorId);
        return this;
    }

    /**
     * 设置toolbar颜色
     *
     * @param colorid
     * @return
     */
    public HeadImageManager setToolbarWidgetColor(@ColorInt int colorid) {
//        mToolbarWidgetColor = colorid;
        optionsBundle.putInt(ConstantValue.DATA_TOOLBARWIDGETCOLOR, colorid);
        return this;
    }

    /**
     * 设置toolbar标题
     *
     * @param title
     * @return
     */
    public HeadImageManager setToolbarTitle(@Nullable String title) {
//        mToolbarTiƒtle = title;
        optionsBundle.putString(ConstantValue.DATA_TOOLBARTITLE, title);
        return this;
    }

    /**
     * 设置logo颜色
     *
     * @param colorid
     * @return
     */
    public HeadImageManager setLogoColor(@ColorInt int colorid) {
//        mLogoColor = colorid;
        optionsBundle.putInt(ConstantValue.DATA_LOGOCOLOR, colorid);
        return this;
    }

    /**
     * 设置隐藏底部容器，默认显示
     *
     * @param hide true 为隐藏,false为显示
     */
    public HeadImageManager setHideBottomControls(boolean hide) {
//        mHideBottomControls = hide;
        optionsBundle.putBoolean(ConstantValue.DATA_HIDEBOTTOMCONTROLS, hide);
        return this;
    }

    /**
     * 是否能调整裁剪框
     *
     * @param enabled
     * @return
     */
    public HeadImageManager setFreeStyleCropEnabled(boolean enabled) {
//        mFreeStyleCropEnabled = enabled;
        optionsBundle.putBoolean(ConstantValue.DATA_FREESTYLECROPENABLED, enabled);
        return this;
    }


    /**
     * 开启
     */
    public void show(HeadImageCallback headImageCallback) {
        if (headImageCallback == null) {
            return;
        }
        mCallback = headImageCallback;

//        new DialogGetHeadImage((Activity) mContext, mDialogTitle, mItems, mItempics, mHiddenItem, mIsShow) {
//
//            @Override
//            public void Album() {
//                startActivity(ConstantValue.DATA_USER_SELECT_ALBUM);
//            }
//
//            @Override
//            public void Camera() {
//                startActivity(ConstantValue.DATA_USER_SELECT_CAMERA);
//            }
//        }.show();


        DialogFromBottom dialogFromBottom= new DialogFromBottom(mContext);
        dialogFromBottom.setContentView(R.layout.dialog_head_imgae);
        dialogFromBottom.show();

    }


    private void startActivity(String itemName) {
        HeadImageActivity.setCallBack(mCallback);
        Intent intent = new Intent();
        intent.setClass(mContext, HeadImageActivity.class);
        intent.putExtra(ConstantValue.DATA_DIALOG_ITEM_NAME, itemName);
        intent.putExtra(ConstantValue.DATA_BUNDLE, optionsBundle);
        mContext.startActivity(intent);
    }

}
