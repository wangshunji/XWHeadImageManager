package wang.shunji.head.HeadImage.Crop.callback;

import android.support.annotation.NonNull;
public interface BitmapCropCallback {

    void onBitmapCropped();

    void onCropFailure(@NonNull Exception bitmapCropException);

}