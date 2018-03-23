package wang.shunji.head.HeadImage.Exception;

/**
 * Created by shunjiwang on 2018/3/19.
 * 上传头像设置的item数组长度不对
 * 第一个item 是相册 第二个item是相机
 */

public class DialogItemCountException extends HeadImageException {

    public DialogItemCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public DialogItemCountException(String message) {
        super(message);
    }
}
