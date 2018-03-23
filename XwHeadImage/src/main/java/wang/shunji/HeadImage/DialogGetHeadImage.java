package wang.shunji.HeadImage;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 底部弹窗
 * Created by shunjiwang on 2018/3/19.
 */

public abstract class DialogGetHeadImage extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    private String mDialogTitle, mDialogAlbumText, mDialogCameraText;
    private boolean mIsShowItemPic; //是否显示item图标
    private int mDialogAlbumPic, mDialogCameraPic; //item图标
    private int mHiddenItem;


    public DialogGetHeadImage(@NonNull Activity activity, String dialogTitle, String[] dialogItemText, int[] itempics, int hiddenItem, boolean isShow) {
        super(activity, R.style.ActionSheetDialogStyle);
        mActivity = activity;
        mDialogTitle = dialogTitle;
        mHiddenItem = hiddenItem;
        mIsShowItemPic = isShow;
        //item文字
        if (dialogItemText != null && dialogItemText.length == 2) {
            mDialogAlbumText = dialogItemText[0];
            mDialogCameraText = dialogItemText[1];
        }
        //item 图片res id
        if (itempics != null && itempics.length == 2) {
            mDialogAlbumPic = itempics[0];
            mDialogCameraPic = itempics[1];
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_head_imgae);


        TextView tv_dialog_title = findView(R.id.tv_dialog_title);
        TextView tv_dialog_item_album = findView(R.id.tv_dialog_item_album);
        TextView tv_dialog_item_camera = findView(R.id.tv_dialog_item_camera);
        ImageView iv_dialog_item_album = findView(R.id.iv_dialog_item_album);
        ImageView iv_dialog_item_camera = findView(R.id.iv_dialog_item_camera);
        LinearLayout ll_album_upload = findView(R.id.ll_album_upload);
        LinearLayout ll_camera_upload = findView(R.id.ll_camera_upload);
        //是否显示图标
        if (!mIsShowItemPic) {
            iv_dialog_item_album.setVisibility(View.GONE);
            iv_dialog_item_camera.setVisibility(View.GONE);
        } else {
            if (mDialogAlbumPic>0)
                iv_dialog_item_album.setImageDrawable(mActivity.getResources().getDrawable(mDialogAlbumPic));
            if (mDialogCameraPic>0)
            iv_dialog_item_camera.setImageDrawable(mActivity.getResources().getDrawable(mDialogCameraPic));
        }
        //隐藏哪个
        if (mHiddenItem == 1) {//1 为隐藏相册，2为隐藏相机
            ll_album_upload.setVisibility(View.GONE);
        } else if (mHiddenItem == 2) {
            ll_camera_upload.setVisibility(View.GONE);
        }


        Button btn_cancel = findView(R.id.btn_cancel);
        if (!TextUtils.isEmpty(mDialogTitle)) {
            tv_dialog_title.setText(mDialogTitle);
        }
        if (!TextUtils.isEmpty(mDialogAlbumText)) {
            tv_dialog_item_album.setText(mDialogAlbumText);
        }
        if (!TextUtils.isEmpty(mDialogCameraText)) {
            tv_dialog_item_camera.setText(mDialogCameraText);
        }

        ll_album_upload.setOnClickListener(this);
        ll_camera_upload.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消

    }

    protected void setViewLocation() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.height = mActivity.getWindowManager().getDefaultDisplay().getHeight() - SystemUtils.checkoutDeviceHasNavigationBar(mActivity);
        // 设置显示位置
        onWindowAttributesChanged(lp);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_album_upload) {
            Album();
            this.cancel();

        } else if (i == R.id.ll_camera_upload) {
            Camera();
            this.cancel();

        } else if (i == R.id.btn_cancel) {
            this.cancel();

        }
    }

    public abstract void Album();

    public abstract void Camera();

    //
    private <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }
}
