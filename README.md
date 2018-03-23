
# XWHeadImageManager
本框架是在uCrop的基础的封装的，感谢uCrop的作者Yalantis，这是uCrop的地址：https://github.com/Yalantis/uCrop。

##### XWHeadImageManager的引用：
###### 在根目录的build.gradle里添加
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
######在moudle的build.gradle里添加
```java
dependencies {
	        compile 'com.github.wangshunji:XWHeadImageManager:v1.0.0'
	}
```
######XWHeadImageManager的调用：
```java
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
```
  每个方法具体的作用请查看HeadImageManager类里的注释