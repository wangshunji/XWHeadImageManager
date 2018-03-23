package wang.shunji.HeadImage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.xiweinet.xwpermission.Permission.PermissionCallback;
import com.xiweinet.xwpermission.Permission.PermissionChecker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wang.shunji.HeadImage.Callback.HeadImageCallback;
import wang.shunji.HeadImage.Utils.PathUtils;


/**
 * Created by shunjiwang on 2018/3/16.
 */

public class HeadImageActivity extends AppCompatActivity {
    private String mItemName; //用户选择的item

    private static final int PHOTO_CARMERA = 0;//Uri获取类型判断
    private static final int PHOTO_PICK = 1;
    private static HeadImageCallback sCallBack;
    private File mOutFile;
    private File mCameraFile;
    private Bundle optionsBundle;

    /**
     * 裁剪 需要的参数
     **/
    private float mAspectRatioX;
    private float mAspectRatioY;
    private boolean mUseSourceImageAspectRatio = false;
    private int mMaxResultImageSizeX;
    private int mMaxResultImageSizeY;
    private String mFormat; //Bitmap.CompressFormat
    private int mCompressQuality;
    private int[] mGestures;
    private boolean mFreeStyleCropEnabled;
    private float mMaxScaleMultiplier;
    private int mAnimTime;
    private int mMaxBitmapSize;
    private int mLayerColor;
    private boolean mIsOval;
    private boolean mShowCropFrame;
    private int mCropFrameColor;
    private int mCropFrameStrokeWidth;
    private int mRowCount;
    private int mColumnCount;
    private int mGridStrokeWidth;
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;
    private String mToolbarTitle;
    private int mLogoColor;
    private boolean mHideBottomControls;
    private boolean mShowCropGrid;
    private int mCropGridColor;
    private int mActiveWidgetColor;

    public static void setCallBack(HeadImageCallback callBack) {
        sCallBack = callBack;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatas();
        UserSelectAction();
    }

    /**
     * 获取跳转过来的data
     */
    private void getDatas() {
        Intent intent = getIntent();
        if (intent != null) {
            mItemName = intent.getStringExtra(ConstantValue.DATA_DIALOG_ITEM_NAME);
            optionsBundle = intent.getBundleExtra(ConstantValue.DATA_BUNDLE);
            mAspectRatioX = optionsBundle.getFloat(ConstantValue.DATA_ASPECTRATIOX, 1);
            mAspectRatioY = optionsBundle.getFloat(ConstantValue.DATA_ASPECTRATIOY, 1);
            mUseSourceImageAspectRatio = optionsBundle.getBoolean(ConstantValue.DATA_USESOURCEIMAGEASPECTRATIO, false);
            mMaxResultImageSizeX = optionsBundle.getInt(ConstantValue.DATA_MAXRESULTIMAGESIZEX, 1000);
            mMaxResultImageSizeY = optionsBundle.getInt(ConstantValue.DATA_MAXRESULTIMAGESIZEY, 1000);
            mFormat = optionsBundle.getString(ConstantValue.DATA_FORMAT, Bitmap.CompressFormat.PNG.name());

            mCompressQuality = optionsBundle.getInt(ConstantValue.DATA_COMPRESSQUALITY, 10);
            mGestures = optionsBundle.getIntArray(ConstantValue.DATA_GESTURES);
            mFreeStyleCropEnabled = optionsBundle.getBoolean(ConstantValue.DATA_FREESTYLECROPENABLED, false);

            mMaxScaleMultiplier = optionsBundle.getInt(ConstantValue.DATA_MAXSCALEMULTIPLIER, -1);
            mAnimTime = optionsBundle.getInt(ConstantValue.DATA_ANIMTIME, -1);
            mMaxBitmapSize = optionsBundle.getInt(ConstantValue.DATA_MAXBITMAPSIZE, -1);

            mLayerColor = optionsBundle.getInt(ConstantValue.DATA_LAYERCOLOR, 1);
            mIsOval = optionsBundle.getBoolean(ConstantValue.DATA_ISOVAL, false);
            mShowCropFrame = optionsBundle.getBoolean(ConstantValue.DATA_SHOWCROPFRAME, true);

            mCropFrameColor = optionsBundle.getInt(ConstantValue.DATA_CROPFRAMECOLOR, 1);
            mCropFrameStrokeWidth = optionsBundle.getInt(ConstantValue.DATA_CROPFRAMESTROKEWIDTH, -1);
            mRowCount = optionsBundle.getInt(ConstantValue.DATA_ROWCOUNT, -1);

            mColumnCount = optionsBundle.getInt(ConstantValue.DATA_COLUMNCOUNT, -1);
            mGridStrokeWidth = optionsBundle.getInt(ConstantValue.DATA_COLUMNCOUNT, -1);
            mToolbarColor = optionsBundle.getInt(ConstantValue.DATA_TOOLBARCOLOR, 1);

            mStatusBarColor = optionsBundle.getInt(ConstantValue.DATA_STATUSBARCOLOR, 1);
            mToolbarWidgetColor = optionsBundle.getInt(ConstantValue.DATA_TOOLBARWIDGETCOLOR, 1);
            mToolbarTitle = optionsBundle.getString(ConstantValue.DATA_TOOLBARTITLE);

            mLogoColor = optionsBundle.getInt(ConstantValue.DATA_LOGOCOLOR, 1);
            mHideBottomControls = optionsBundle.getBoolean(ConstantValue.DATA_HIDEBOTTOMCONTROLS, true);
            mShowCropGrid = optionsBundle.getBoolean(ConstantValue.DATA_SHOWCROPGRID, true);

            mCropGridColor = optionsBundle.getInt(ConstantValue.DATA_CROPGRIDCOLOR, 1);
            mActiveWidgetColor = optionsBundle.getInt(ConstantValue.DATA_ACTIVEWIDGETCOLOR, 1);

        }
    }


    /**
     * 用户选择了什么
     */
    private void UserSelectAction() {
        switch (mItemName) {
            case ConstantValue.DATA_USER_SELECT_CAMERA:
                startCamera();
                break;
            case ConstantValue.DATA_USER_SELECT_ALBUM:
                startAlbum();
                break;
            default:
                break;
        }
    }


    /**
     * 开启相机
     */
    private void startCamera() {
        PermissionChecker.create(this)
                .requestCount(3)
                .checkSinglePermission(Manifest.permission.CAMERA, new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onDeny(String s, int i) {
                        Toast.makeText(HeadImageActivity.this, "相机权限被禁止，无法打开相机", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGuarantee(String s, int i) {
                        if (!TextUtils.isEmpty(mItemName) && mItemName.equals(ConstantValue.DATA_USER_SELECT_CAMERA)) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri cametaUri = getMediaFileUri(intent);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, cametaUri);
                            startActivityForResult(intent, PHOTO_CARMERA);
                        }

                    }
                });

    }

    /**
     * 开启相册
     */
    private void startAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICK);

    }

    /**
     * 获取相机Uri
     *
     * @param intent
     * @return
     */
    public Uri getMediaFileUri(Intent intent) {

        mCameraFile = new File(PathUtils.getTailorImgPath());
        //根据版本设置
        Uri uriForFile;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //判断是否是AndroidN以及更高的版本
            uriForFile = FileProvider.getUriForFile(HeadImageActivity.this, "wang.shunji.HeadImage.fileProvider", mCameraFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uriForFile = Uri.fromFile(mCameraFile);
        }
        return uriForFile;
    }

    /**
     * 裁剪图片
     *
     * @param filePath
     */
    private void cropPicImage(String filePath) {
        File file = new File(filePath);
        if (file.length() == 0) {
            Toast.makeText(HeadImageActivity.this, "文件为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        @SuppressLint("SimpleDateFormat")
        String ImgName = "HeadImg_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
        String fileName = PathUtils.getPhotoPath() + File.separator + ImgName;
        mOutFile = new File(fileName);
        //检查文件是否存在
        if (!mOutFile.exists()) {
            if (!mOutFile.getParentFile().exists()) {
                mOutFile.getParentFile().mkdirs();
            }
            try {
                mOutFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri sourceUri = getImageContentUri(HeadImageActivity.this, file);
        Uri destinationUri = getImageContentUri(HeadImageActivity.this, mOutFile);

        if (sourceUri != null && destinationUri != null && optionsBundle != null) {
            UCrop.Options options = new UCrop.Options();
            UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(mAspectRatioX, mAspectRatioY)//默认1:1
                    .withMaxResultSize(mMaxResultImageSizeX, mMaxResultImageSizeY);//默认1000x1000

            if (mGestures != null && mGestures.length == 3) {//设置裁剪图片可操作的手势
                options.setAllowedGestures(mGestures[0], mGestures[1], mGestures[2]);
            } else {
                options.setAllowedGestures(CropActivity.SCALE, CropActivity.ROTATE, CropActivity.ALL);
            }
            options.setHideBottomControls(mHideBottomControls); //设置隐藏底部容器，默认隐藏
            options.setFreeStyleCropEnabled(mFreeStyleCropEnabled);//是否能调整裁剪框
            options.setCompressionQuality(mCompressQuality);//设置压缩质量
            options.setOvalDimmedLayer(mIsOval);
            options.setShowCropFrame(mShowCropFrame);
            options.setShowCropGrid(mShowCropGrid);
            options.setCompressionFormat(mFormat); //设置压缩格式
            if (mLogoColor != 1) {
                options.setLogoColor(mLogoColor);
            }
            if (mToolbarWidgetColor != 1) {
                options.setToolbarWidgetColor(mToolbarWidgetColor);
            }
            if (mUseSourceImageAspectRatio) {
                uCrop.useSourceImageAspectRatio();
            }
            if (mMaxScaleMultiplier > 0) {
                options.setMaxScaleMultiplier(mMaxScaleMultiplier);
            }
            if (mAnimTime > 0) {
                options.setImageToCropBoundsAnimDuration(mAnimTime);
            }
            if (mMaxBitmapSize > 0) {
                options.setMaxBitmapSize(mMaxBitmapSize);
            }
            if (mLayerColor != 1) {
                options.setDimmedLayerColor(mLayerColor);
            }
            if (mCropFrameColor != 1) {
                options.setCropFrameColor(mCropFrameColor);//边框颜色
            }
            if (mCropFrameStrokeWidth > 0) {
                options.setCropFrameStrokeWidth(mCropFrameStrokeWidth);
            }

            if (mRowCount > 0) {
                options.setCropGridRowCount(mRowCount);
            }
            if (mColumnCount > 0) {
                options.setCropGridColumnCount(mColumnCount);
            }
            if (mGridStrokeWidth > 0) {
                options.setCropGridStrokeWidth(mGridStrokeWidth);
            }
            if (mToolbarColor != 1) {
                options.setToolbarColor(mToolbarColor); //设置toolbar颜色
            }

            if (mStatusBarColor != 1) {
                options.setStatusBarColor(mStatusBarColor);
            }
            if (!TextUtils.isEmpty(mToolbarTitle)) {
                options.setToolbarTitle(mToolbarTitle);
            }

            if (mCropGridColor != 1) {
                options.setCropGridColor(mCropGridColor);//内框颜色
            }
            if (mActiveWidgetColor != 1) {
                options.setActiveWidgetColor(mActiveWidgetColor);
            }

            uCrop.withOptions(options);
            uCrop.start(HeadImageActivity.this);
        }
    }


    /**
     * 处理相册的图片
     *
     * @param data
     */
    private void cropAlbumPic(final Intent data) {
        PermissionChecker.create(HeadImageActivity.this)
                .checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String s, int i) {

                    }

                    @Override
                    public void onGuarantee(String s, int i) {
                        Uri uri = data.getData();
                        String imagePath;
                        // 判断手机系统版本号
                        if (Build.VERSION.SDK_INT >= 19) {// 4.4及以上系统使用这个方法处理图片
                            imagePath = uriToPath(uri);
                        } else {                           // 4.4以下系统使用这个方法处理图片
                            imagePath = getImagePath(uri, null);
                        }
                        cropPicImage(imagePath);
                    }
                });
    }


    /**
     * 获取 相册 Uri
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String uriToPath(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(HeadImageActivity.this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 删除空文件
     *
     * @param file
     */
    private void delNullFile(@Nullable File file) {
        if (file!=null)//使用模拟器时file有可能是null
        if (file.isFile() && file.exists()) {
            if (file.length() == 0) {
                boolean b = file.delete();
                file.deleteOnExit();
                System.gc();
//                if (b) {
//                    Toast.makeText(HeadImageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(HeadImageActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
//
//                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //当指定过Uri时data就返回null
        switch (resultCode) {
            //取消拍照
            case Activity.RESULT_CANCELED:
//                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                delNullFile(mOutFile);
                finish();
                break;
            // 照相成功
            case Activity.RESULT_OK:
                switch (requestCode) {
                    case PHOTO_CARMERA:// 将拍摄的照片进行裁剪
                        cropPicImage(mCameraFile.getPath());
                        break;
                    case PHOTO_PICK:// 将相册的照片进行裁剪
                        cropAlbumPic(data);
                        break;
                    case UCrop.REQUEST_CROP://裁剪成功 ，回调
                        sCallBack.headImagePath(mOutFile.getAbsolutePath());
                        finish();
                        break;
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(this, "图片裁剪失败", Toast.LENGTH_SHORT).show();
                break;
        }


    }


}
