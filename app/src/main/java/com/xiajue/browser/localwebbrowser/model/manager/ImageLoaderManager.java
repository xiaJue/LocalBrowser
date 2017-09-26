package com.xiajue.browser.localwebbrowser.model.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiajue.browser.localwebbrowser.R;

/**
 * xiaJue 2017/9/8创建
 */
public class ImageLoaderManager {
    public static ImageLoaderManager mImageLoaderManager;
    public Context mContext;

    public static ImageLoaderManager getInstance(Context context) {
        if (mImageLoaderManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (mImageLoaderManager == null) {
                    mImageLoaderManager = new ImageLoaderManager(context);
                }
            }
        }
        return mImageLoaderManager;
    }

    public ImageLoaderManager(Context context) {
        mContext = context;
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.image_loading_drawable).showImageOnFail(R.mipmap
                .image_load_failure).
                bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).
                displayer(new RoundedVignetteBitmapDisplayer(20, 800)).build();
    }

    private DisplayImageOptions options;

    public void displayImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    /**
     * 加载图片
     */
    public void displayImage(String url, final ImageView imageView, ImageLoadingListener
            imageLoadingListener) {
        ImageLoader.getInstance().displayImage(url, imageView, options, imageLoadingListener);
    }

    /**
     * 加载图片
     */
    public void loadImage(String url, ImageLoadingListener
            imageLoadingListener) {
        ImageLoader.getInstance().loadImage(url, imageLoadingListener);
    }
}
