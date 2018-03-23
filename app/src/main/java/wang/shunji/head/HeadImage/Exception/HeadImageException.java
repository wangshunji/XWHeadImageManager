package wang.shunji.head.HeadImage.Exception;

/**
 * Created by shunjiwang on 2018/3/19.
 *
 * 头像上传异常类 基类
 */

public class HeadImageException extends Exception {
    public HeadImageException() {
    }

    public HeadImageException(String message) {
        super(message);
    }

    public HeadImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeadImageException(Throwable cause) {
        super(cause);
    }

}
