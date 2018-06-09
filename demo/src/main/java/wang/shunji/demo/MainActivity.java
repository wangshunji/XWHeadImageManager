package wang.shunji.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import wang.shunji.HeadImage.Callback.HeadImageCallback;
import wang.shunji.HeadImage.Exception.DialogItemCountException;
import wang.shunji.HeadImage.HeadImageManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HeadImageManager.create(MainActivity.this)
                            .dialogTitle("选择上传的方式")
                            .dialogItemText("从相册上传", "从相机上传")
//                            .setItemPics(R.drawable.dialog_album_pic,R.drawable.dialog_camera_pic)
//                            .setHiddenItem(2)
                            .setToolbarColor(getResources().getColor(R.color.color_209764))
                            .setHideBottomControls(false)
                            .isShowItemPic(false)
                            .setOvalDimmedLayer(true)
                            .show(new HeadImageCallback() {
                                @Override
                                public void headImagePath(String path) {
                                    Toast.makeText(MainActivity.this, "path" + path, Toast.LENGTH_SHORT).show();
                                    FileInputStream fis = null;
                                    try {
                                        fis = new FileInputStream(path);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                                    ImageView imageView = findViewById(R.id.iv);
                                    imageView.setImageBitmap(bitmap);
                                }
                            });

                } catch (DialogItemCountException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
