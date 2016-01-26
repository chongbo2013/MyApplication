package com.tpad.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class PhotoUtil {
	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 0;
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 1;
	//获取背景的RequestCode
	public static final int INTENT_REQUEST_CODE_Background = 3;

	/**
	 * 通过手机照相获取图片
	 *
	 * @param activity
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Activity activity, String rootPath) {
		FileUtils.createDirFile(rootPath);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = rootPath + UUID.randomUUID().toString() + ".jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		return path;
	}

	/**
	 * 通过手机相册获取图片
	 *
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		activity.startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
	}

	/**
	 * 调用系统图片裁剪
	 * @param uri
	 * @param size
	 */
	public static final int PHOTO_REQUEST_CUT = 1011;
	public static void startPhotoZoom(Activity activity, Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
					1024 * 600);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is!=null){
					is.close();
				}
				if (baos!=null){
					baos.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.gc();
		}
		if (baos == null){
			return null;
		}
		return baos.toByteArray();
	}

	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	//循环压缩图片
	@SuppressLint("NewApi")
	public static Bitmap compressBmpFromBmp(Bitmap image) {
		if (Build.VERSION.SDK_INT < 11) {
			return image;
		}
		if (image.getByteCount() > 1024*1024*2) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int options = 100;
			image.compress(Bitmap.CompressFormat.PNG, 100, baos);
			while (baos.toByteArray().length / 1024 > 1024*2) {
				options -= 5;
				if (options <= 0)
					break;
				baos.reset();
				image.compress(Bitmap.CompressFormat.PNG, options, baos);
			}

			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
			return bitmap;
		}
		return image;
	}

	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(Context context, String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options,
				PhoneUtils.getInstance(context).getPhoneScreen().widthPixels,
				PhoneUtils.getInstance(context).getPhoneScreen().heightPixels);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	//把bitmap转换成String
	public static byte[] bitmapToBytes(Context context, String filePath) {
		Bitmap bitmap = getSmallBitmap(context, filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		return baos.toByteArray();
	}

	public static int getByteCount(Bitmap bitmap){
		int byteCount;
		if(Build.VERSION.SDK_INT < 12){
			byteCount = bitmap.getRowBytes() * bitmap.getHeight();
		}else {
			byteCount = bitmap.getByteCount();
		}
		return byteCount;
	}

}
