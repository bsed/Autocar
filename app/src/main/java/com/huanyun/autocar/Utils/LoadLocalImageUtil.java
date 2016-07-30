package com.huanyun.autocar.Utils;

/**
 * Created by admin on 2015/12/15.
 */
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 异步加载本地图片工具类
 *
 * @author frankli
 *
 */
public class LoadLocalImageUtil {

    private LoadLocalImageUtil() {

    }

    private static LoadLocalImageUtil instance = null;

    public static DisplayImageOptions sendoptions = null;
    public static DisplayImageOptions dailyfines = null;
    public static DisplayImageOptions optionstop = null;

    public static synchronized LoadLocalImageUtil getInstance() {
        if (instance == null) {
            instance = new LoadLocalImageUtil();
        }
        return instance;
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public void displayFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card

        if (sendoptions == null) {
            sendoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }
        ImageLoader.getInstance().displayImage("file://" + uri, imageView,sendoptions);
    }

    /**
     * 从assets文件夹中异步加载图片
     *
     * @param imageName
     *            图片名称，带后缀的，例如：1.png
     * @param imageView
     */
    public void dispalyFromAssets(String imageName, ImageView imageView) {
        // String imageUri = "assets://image.png"; // from assets
        ImageLoader.getInstance().displayImage("assets://" + imageName,
                imageView);
    }



    public static void displayImageForNet(String url, ImageView imageView) {
        if (sendoptions == null) {
            sendoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }

        ImageLoader.getInstance().displayImage(url, imageView, sendoptions);
    }

    public static void displayImageForNet(String url, ImageView imageView,ImageLoadingListener listener) {
        if (sendoptions == null) {
            sendoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }

        ImageLoader.getInstance().displayImage(url, imageView, sendoptions,listener);
    }

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public void displayFromDrawable(int imageId, ImageView imageView) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        ImageLoader.getInstance().displayImage("drawable://" + imageId,
                imageView);
    }

    /**
     * 从内容提提供者中抓取图片
     */
    public void displayFromContent(String uri, ImageView imageView) {
        // String imageUri = "content://media/external/audio/albumart/13"; //
        // from content provider
        ImageLoader.getInstance().displayImage("content://" + uri, imageView);
    }

    	/**
	 * 显示本地文件
	 *
	 * @param path
	 * @param iv
	 */
	public void setBitmapFile(String path, ImageView iv) {
        if (sendoptions == null) {
            sendoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }
		String uri = "file://" + path;
        ImageLoader.getInstance().displayImage(uri, iv, sendoptions);
	}

    public void setBitmapFile(String path, ImageView iv,ImageLoadingListener listener) {
        if (sendoptions == null) {
            sendoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }
        String uri = "file://" + path;
        ImageLoader.getInstance().displayImage(uri, iv, sendoptions,listener);
    }


    public static void setdailyfineimg(String url, ImageView imageView,
                                       ImageLoadingListener listener) {
        if (dailyfines == null) {
            dailyfines = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0)
                    .showImageForEmptyUri(0)
                    .showImageOnFail(0)
                    .cacheInMemory(true)
//					.cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }
        ImageLoader.getInstance().displayImage(url, imageView, dailyfines,
                listener);
    }

    public static void settopimg(int dis, String url, ImageView imageView) {

        if (optionstop == null) {
            optionstop = new DisplayImageOptions.Builder()
                    .showImageOnLoading(0).showImageForEmptyUri(0)
                    .showImageOnFail(0).cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(dis)).build();
        }

        ImageLoader.getInstance().displayImage(url, imageView, optionstop);

    }

}