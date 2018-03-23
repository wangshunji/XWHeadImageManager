package wang.shunji.head.HeadImage.Utils;

import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import wang.shunji.head.HeadImage.BaseApplication;

/**
 * @author zhouzd:
 * @date ：2017年11月8日
 */
public class PathUtils {
    private static final String TAG = "PathUtils";

    private static final String APPNAME = SystemUtils.getAppName(BaseApplication.getDefault());
    /**
     * 头像的文件夹
     */
    public static final String FOLDER_AVATAR = "avatar";
    private static File avatarDir;
    /**
     * 音频的文件夹
     */
    public static final String FOLDER_VOICE = "voice";
    /**
     * ecg data数据地址
     */
    public static final String FOLDER_DUDUDATA = "data";
    /**
     * 消息
     */
    public static final String FOLDER_MESSAGE = "file";
    /**
     * log文件夹
     */
    public static final String FOLDER_LOG = "log";

    /**
     * 存放临时文件
     */
    private static final String DIR_TEMP = APPNAME + "/temp";
    private static File tempDir;
    /**
     * 存放日志文件
     */
    private static final String DIR_LOG = APPNAME + "/log";
    public static File logDir;
    /**
     * 存放ecg数据的文件
     */
    private static final String DIR_DUDU = APPNAME + "/dudu";
    /**
     * 存放聊天拍摄的照片
     */
    public static final String DIR_PHOTO = APPNAME + "/photo";
    /**
     * 存放聊天拍摄的视频
     */
    private static final String DIR_VIDEO = APPNAME + "/video";
    public static File duduDir;
    public static File photoDir;
    public static File videoDir;

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tempDir = new File(Environment.getExternalStorageDirectory(), DIR_TEMP);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            logDir = new File(Environment.getExternalStorageDirectory(), DIR_LOG);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            duduDir = new File(Environment.getExternalStorageDirectory(), DIR_DUDU);
            if (!duduDir.exists()) {
                duduDir.mkdirs();
            }
            photoDir = new File(Environment.getExternalStorageDirectory(), DIR_PHOTO);
            if (!photoDir.exists()) {
                photoDir.mkdir();
            }
            videoDir = new File(Environment.getExternalStorageDirectory(), DIR_VIDEO);
            if (!videoDir.exists()) {
                videoDir.mkdir();
            }
            avatarDir = new File(Environment.getExternalStorageDirectory(), FOLDER_AVATAR);
            if (!avatarDir.exists()) {
                avatarDir.mkdirs();
            }
        }
    }

    public static String getDuduPath() {
        if (duduDir == null) {
            return null;
        }
        return duduDir.getPath();
    }

    public static String getPhotoPath() {
        if (photoDir == null) {
            return null;
        }
        return photoDir.getPath();
    }

    public static String getVideoPath() {
        if (videoDir == null) {
            return null;
        }
        return videoDir.getPath();
    }

    public static String getLocalImgPath() {
        if (tempDir == null) {
            return null;
        }
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dir = new File(tempDir, APPNAME + "_" + path + ".jpg");
        return dir.getPath();
    }

    public static String getSaveImgPath() {
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dir = new File(APPNAME + "_" + path + ".jpg");
        return dir.getPath();
    }

    /**
     * 获取裁剪图片保存位置
     *
     * @return
     */
    public static String getTailorImgPath() {
        if (tempDir == null) {
            return null;
        }
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dir = new File(tempDir, APPNAME + "_" + path + ".jpg");
        return dir.getPath();
    }

    public static String getDuDuDataFileName() {
        String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return name + ".dudu";
    }


    public static String getDuDuDataFilePath(String recordId) {
        if (duduDir == null) {
            return null;
        }
        File dir = new File(duduDir, APPNAME + "_" + recordId + ".ecg");
        return dir.getPath();
    }

    public static File getDuDuDataFile(String recordId) {
        String path = getDuDuDataFilePath(recordId);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        return file;
    }

    public static File[] getLogFiles() {
        if (logDir == null) return null;
        return logDir.listFiles();
    }

    public static String getLogPath() {
        if (logDir == null) {
            return null;
        }
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dir = new File(logDir, APPNAME + "_" + path + ".log");
        return dir.getPath();
    }

    public static String getApkPath() {
        if (tempDir == null) return null;
        //System.currentTimeMillis()+".amr"
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File dir = new File(tempDir, APPNAME + "_" + path + ".apk");
        return dir.getPath();
    }

    public static String getTempPath() {
        if (tempDir == null) {
            return null;
        }
        return tempDir.getPath();
    }

    /**
     * 上传到阿里云前本地生成随机地址
     *
     * @param fileName 本地文件名
     * @return
     */
    public static String getUploadFilePath(String fileName) {
        //扩展名
        String extensionName = null;
        if (!TextUtils.isEmpty(fileName)) {
            int lastIndex = fileName.lastIndexOf(".");
            if (lastIndex != -1) {
                extensionName = fileName.substring(lastIndex, fileName.length());
            }
        }
        String result = UUID.randomUUID().toString();
        return result + extensionName;
    }

    /**
     * 获取缓存长度
     *
     * @return
     */
    public static long getCacheSize() {
        long length = 0;
        length += getFolderLength(tempDir);
        length += getFolderLength(logDir);
        length += getFolderLength(duduDir);
        length += getFolderLength(photoDir);
        length += getFolderLength(avatarDir);
        return length;
    }

    /**
     * 获取文件夹下文件总的大小<只扫描一级目录></>
     *
     * @param file
     * @return
     */
    public static long getFolderLength(File file) {
        if (file == null || !file.exists()) return 0;
        int length = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                length += files[i].length();
            }
        }
        return length;
    }

    /**
     * 清空本地文件缓存
     */
    public static void clearCache() {
        clearFolder(tempDir);
        clearFolder(logDir);
        clearFolder(duduDir);
        clearFolder(photoDir);
        clearFolder(avatarDir);
    }

    /**
     * 清空文件夹
     *
     * @param file
     */
    public static void clearFolder(File file) {
        if (file == null || !file.exists()) ;
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }


}
