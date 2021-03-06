package com.xiajue.browser.localwebbrowser.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xiajue.browser.localwebbrowser.R;
import com.xiajue.browser.localwebbrowser.model.manager.ImageLoaderManager;
import com.xiajue.browser.localwebbrowser.model.manager.Settings;
import com.xiajue.browser.localwebbrowser.model.utils.FileUtils;
import com.xiajue.browser.localwebbrowser.model.utils.L;
import com.xiajue.browser.localwebbrowser.view.activity.ImageActivity;
import com.xiajue.browser.localwebbrowser.view.activity.viewInterface.IImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * xiaJue 2017/9/21创建
 */
public class ImagePresenter {
    private Context mContext;
    private IImageView mIImage;
    private String url;

    public ImagePresenter(IImageView iImageView) {
        mIImage = iImageView;
        mContext = (Context) iImageView;
    }

    private boolean isRes = false;
    private int mRes;
    private String mName;

    public void loadImageRes(int res, String name) {
        mIImage.getImageView().setImage(ImageSource.resource(res));
        this.mRes = res;
        this.mName = name;
        isRes = true;
    }

    public void loadImageUrl(String url) {
        L.e("load...");
        this.url = url;
        ImageLoaderManager.getInstance(mContext).loadImage(url, new
                SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        mIImage.getImageView().setImage(ImageSource.resource(R.mipmap
                                .image_load_failure));
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        mIImage.getImageView().setImage(ImageSource.bitmap(loadedImage));
                        FadeInBitmapDisplayer.animate(mIImage.getImageView(), 800);
                    }
                });
    }

    /**
     * 当菜单选择
     */
    public void onMenuSelect(int id) {
        switch (id) {
            case R.id.menu_max_save:
                if (isRes) {
                    /**
                     * 保存res下的图片
                     */
                    saveImage(mRes, mName);
                } else {
                    /**
                     * 保存图片--因为图片在一开始已经被缓存到本地了
                     * 为了节省流量直接从缓存文件中获取图片
                     */
                    saveImage(url);
                }
                break;
            case R.id.menu_max_share:
                /**
                 * 分享图片
                 */
                shareImage(url);
                break;
            case android.R.id.home:
                /**
                 * 左上角返回按钮
                 */
                ((Activity) mContext).finish();
                break;
        }
    }

    public File shareTempFile;
    private boolean isShare;

    /**
     * 分享图片
     */
    private void shareImage(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        //获取临时文件的目录
        String path = Settings.getImageSavePath(mContext, "", "");
        if (!isRes) {
            //将缓存文件复制到临时文件夹
            File file = ImageLoader.getInstance().getDiskCache().get(url);
            FileUtils.copyFile(file, path, "temp" + file.getName(), new File[1]);
            //拿到新文件的路径
            shareTempFile = new File(path + FileUtils.changeSuff("temp" + file.getName(), ".jpg"));
        } else {
            isShare = true;
            saveImage(mRes, "temp" + mName);
            shareTempFile = new File(Settings.getImageSavePath(mContext, "temp" + mName, ".jpg"));
            isShare = false;
        }
        //分享
        Uri uri = Uri.fromFile(shareTempFile);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string
                .share_how)));
    }

    /**
     * 保存图片
     */
    private void saveImage(String url) {
        File[] outFile = new File[1];
        int resultCode = FileUtils.copyFile(ImageLoader.getInstance().getDiskCache().get(url),
                Settings.getImageSavePath(mContext, "", ""), outFile);
        if (resultCode == 0) {
            Toast.makeText(mContext, R.string.save_success, Toast.LENGTH_SHORT).show();
            //通知系统图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                    ("file://" + outFile[0])));
        } else if (resultCode == 1) {
            Toast.makeText(mContext, R.string.save_failure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, R.string.save_exist, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * res目录下面的一张图片保存到本地
     *
     * @param id 图片的id
     */
    private void saveImage(int id, String name) {
        File file = new File(Settings.getImageSavePath(mContext, name, ".jpg"));
        if (file.exists()) {
            if (!isShare) {
                Toast.makeText(mContext, R.string.save_exist, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        //使用BitmapFactory把res下的图片转换成Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
        FileOutputStream fos = null;
        try {
            //获得一个可写的输入流
            fos = new FileOutputStream(file);
            //使用图片压缩对图片进行处理  压缩的格式  可以是JPEG、PNG、WEBP
            //第二个参数是图片的压缩比例，第三个参数是写入流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (!isShare) {
                Toast.makeText(mContext, R.string.save_success, Toast.LENGTH_SHORT).show();
                //通知系统图库更新
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse
                        ("file://" + file)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!isShare) {
                Toast.makeText(mContext, R.string.save_failure, Toast.LENGTH_SHORT).show();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_iv:
                ((ImageActivity) mContext).animationFinish();
                break;
        }
    }
}
