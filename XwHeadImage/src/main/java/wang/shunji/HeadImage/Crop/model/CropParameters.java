package wang.shunji.HeadImage.Crop.model;

import android.graphics.Bitmap;

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class CropParameters {

    private int mMaxResultImageSizeX, mMaxResultImageSizeY;

    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private String mImageInputPath, mImageOutputPath;
    private wang.shunji.HeadImage.Crop.model.ExifInfo mExifInfo;

    private CropParameters() {

    }

    public CropParameters(int maxResultImageSizeX, int maxResultImageSizeY, Bitmap.CompressFormat compressFormat,
                          int compressQuality, String imageInputPath, String imageOutputPath, ExifInfo exifInfo) {
        mMaxResultImageSizeX = maxResultImageSizeX;
        mMaxResultImageSizeY = maxResultImageSizeY;
        mCompressFormat = compressFormat;
        mCompressQuality = compressQuality;
        mImageInputPath = imageInputPath;
        mImageOutputPath = imageOutputPath;
        mExifInfo = exifInfo;
    }

    public static CropParameters crate(){
        return new CropParameters();
    }
    public  CropParameters maxResultImageSizeX(int maxResultImageSizeX) {
        mMaxResultImageSizeX = maxResultImageSizeX;
        return this;
    }

    public CropParameters maxResultImageSizeY(int maxResultImageSizeY) {
        mMaxResultImageSizeY = maxResultImageSizeY;
        return this;
    }

    public CropParameters compressForma(Bitmap.CompressFormat compressForma) {
        mCompressFormat = compressForma;
        return this;
    }

    public CropParameters compressQuality(int compressQuality) {
        mCompressQuality = compressQuality;
        return this;
    }

    public CropParameters imageInputPath(String imageInputPath) {
        mImageInputPath = imageInputPath;
        return this;
    }

    public CropParameters imageOutputPath(String imageOutputPath) {
        mImageOutputPath = imageOutputPath;
        return this;
    }

    public CropParameters exifInfo(ExifInfo exifInfo) {
        mExifInfo = exifInfo;
        return this;
    }

    public int getMaxResultImageSizeX() {
        return mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return mCompressFormat;
    }

    public int getCompressQuality() {
        return mCompressQuality;
    }

    public String getImageInputPath() {
        return mImageInputPath;
    }

    public String getImageOutputPath() {
        return mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return mExifInfo;
    }

}
