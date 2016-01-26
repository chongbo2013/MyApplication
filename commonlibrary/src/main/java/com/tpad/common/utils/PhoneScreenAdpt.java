package com.tpad.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class PhoneScreenAdpt {

	private int mScreeWidth;
	private int mScreeHeigth;
	private float mdensity = 0.0f; // density（0.75/1.0/1.5/2.0）
	private int densityDpi = 0;
	private static PhoneScreenAdpt mScreenAdpt;
	private Context context;

	public int normalWidth = 480;
	public int normalHeigth = 800;

	private float dialog_title_font_size = 25.0f;
	private int dialog_title_font_color = 0x7d7c7c;
	private int dialog_Margin_top = 16;
	private int dialog_list_Margin_rigth = 51;

	private float dialog_text_font_size = 23.0f;
	private int dialog_text_font_color = 0x7d7c7c;

	private int list_hight = 70;

	private int dialog_line_color = 0xFFd4d4d4;
	private float dialog_line_heigth = 1.0f;

	private int dialog_button_margin_top = 20;
	private int dialog_button_margin_botton = 20;

	private int dialog_one_button_width = 322;
	private int dialog_one_button_heigth = 53;
	private int button1_one_left = 31;
	private int button2_one_left = 31;
	private int button3_one_left = 31;
	private int button3_one_right = 31;

	private int dialog_two_button_width = 158;
	private int dialog_two_button_heigth = 53;
	private int button1_two_left = 23;
	private int button2_two_left = 22;
	private int button3_two_left = 23;
	private int button3_two_right = 23;

	private int dialog_three_button_width = 112;
	private int dialog_three_button_heigth = 53;
	private int button1_three_left = 14;
	private int button2_three_left = 10;
	private int button3_three_left = 10;
	private int button3_three_right = 14;

	private int dialog_text_margin_top = 40;
	private int dialog_text_margin_left = 30;
	private int dialog_text_margin_right = 30;

	private float dialog_button_text_font_size = 23.0f;
	private int dialog_button_text_font_color = 0xFFFFFF;
	private int dialog_button_text_font_colo_no = 0xFF4f4f4f;

	private float dialog_exit_text_font_size = 25.0f;
	private int dialog_exit_text_font_color = 0xFFffffff;
	private int dialog_exit_text_font_Margin_left = 24;

	private int dialog_progress_margin_top = 34;

	private int dialog_image_margin_left = 18;
	private int dialog_image_text_margin_left = 18;

	private int dialog_edit_margin_left = 20;
	private int dialog_edit_margin_top = 20;
	private int dialog_edit_margin_right = 20;
	private int dialog_edit_color_text_left = 10;
	private int dialog_edit_color_image_left = 6;
	private int dialog_edit_color_image_right = 8;
	private int dialog_edit_fontL_image_left = 60;
	private int dialog_edit_fontM_image_left = 23;

	private float dialog_edit_color_font_size = 25.0f;

	private int dialog_feedback_button_width = 328;
	private int dialog_feedback_button_heigth = 60;

	protected PhoneScreenAdpt(Context c) {
		this.context = c;
		DisplayMetrics infoScren = getPhoneScreen();
		mScreeWidth = infoScren.widthPixels;
		mScreeHeigth = infoScren.heightPixels;
		mdensity = infoScren.density;
		densityDpi = infoScren.densityDpi;


	}

	public static PhoneScreenAdpt getInstance(Context c) {
		if (mScreenAdpt == null) {
			mScreenAdpt = new PhoneScreenAdpt(c);
		}
		return mScreenAdpt;
	}

	private DisplayMetrics getPhoneScreen() {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WM.getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	public int getWelcome_width() {
		if (mScreeWidth * 800 / mScreeHeigth > 480) {
			return 480 * mScreeHeigth / 800;
		} else {
			return mScreeWidth;
		}
	}

	public int getWelcome_button_Top() {
		return getWelcome_width() * 600 / 480;

	}

	public float getScreenWidthScale() {
		float ret_size = 0.0f;
		ret_size = mScreeWidth * 1.0f / normalWidth;
		return ret_size;
	}

	public float getScreenHeightScale() {
		float ret_size = 0.0f;
		ret_size = mScreeHeigth * 1.0f / normalHeigth;
		return ret_size;
	}

	public Drawable getNewDrawable(int res) {
		TypedValue value = new TypedValue();
		context.getResources().openRawResource(res, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTargetDensity = value.density;
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res, opts);

		int width = bmp.getWidth();

		int height = bmp.getHeight();

		Matrix matrix = new Matrix();

		matrix.postScale(getScreenWidthScale(), getScreenWidthScale());

		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

		bitmap.setDensity(densityDpi);
		
		return new BitmapDrawable(bitmap);
	}

	public Drawable getNewDrawable(int res, float sw, float sh) {
		TypedValue value = new TypedValue();
		context.getResources().openRawResource(res, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTargetDensity = value.density;
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res, opts);

		int width = bmp.getWidth();

		int height = bmp.getHeight();

		Matrix matrix = new Matrix();

		matrix.postScale(sw, sh);

		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

		bitmap.setDensity(densityDpi);

//		if (bmp != null && !bmp.isRecycled()) {
//			bmp.recycle();
//			bmp = null;
//		}
		return new BitmapDrawable(bitmap);
	}

	public Drawable drawChoseDrawable(int resId, int maskId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		Bitmap img = BitmapFactory.decodeResource(context.getResources(), resId, opts);

		Bitmap mark = BitmapFactory.decodeResource(context.getResources(), maskId, opts);
		int W = mark.getWidth();
		int H = mark.getHeight();
		int w = img.getWidth();
		int h = img.getHeight();

		Bitmap bmp = Bitmap.createBitmap(mark.getWidth(), mark.getHeight(), Config.ARGB_8888);
		bmp.setDensity(160);
		Matrix matrix = new Matrix();
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(img, (W - w) / 2, (H - h) / 2, null);

		canvas.drawBitmap(mark, matrix, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);

		matrix.postScale(getScreenWidthScale(), getScreenWidthScale());

		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, W, H, matrix, true);

		return new BitmapDrawable(bitmap);
	}

	public Drawable getNewDrawableTwo(int res) {
		int width = 0;
		int height = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 不加载bitmap到内存中
		BitmapFactory.decodeResource(context.getResources(), res, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		options.inDither = false;
		options.inPreferredConfig = Config.ARGB_8888;
		options.inSampleSize = 1;
		width = (int) (getScreenWidthScale() * outWidth);
		height = (int) (outHeight * getScreenWidthScale());
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			options.inSampleSize = sampleSize;
		}
		options.inJustDecodeBounds = false;
		// options.inScaled=false;
		return new BitmapDrawable(
				BitmapFactory.decodeResource(context.getResources(), res, options));
	}

	public float getTitleFontSize() {
		float ret_size = 0.0f;
		ret_size = px2sp(getScreenWidthScale() * dialog_title_font_size);
		return ret_size;
	}

	public int getTitleFontColor() {
		return dialog_title_font_color;
	}

	public float getTextFontSize() {
		float ret_size = 0.0f;
		ret_size = px2sp(getScreenWidthScale() * dialog_text_font_size);
		return ret_size;
	}

	public int getTextFontColor() {
		return dialog_text_font_color;
	}

	public int getTitle_Margin_top() {
		int ret_size = 0;
		ret_size = (int) (dialog_Margin_top * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_image_margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_image_margin_left * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_image_text_margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_image_text_margin_left * getScreenWidthScale());
		return ret_size;
	}

	public int getList_Margin_rigth() {
		int ret_size = 0;
		ret_size = (int) (dialog_list_Margin_rigth * getScreenWidthScale());
		return ret_size;
	}

	public int getList_Margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_list_Margin_rigth * getScreenWidthScale());
		return ret_size;
	}

	public int getListItemHigth() {
		int ret_size = 0;
		ret_size = (int) (list_hight * getScreenWidthScale());
		return ret_size;
	}

	public int getLineColor() {

		return dialog_line_color;
	}

	public int getLineHigth() {

		return dip2px(dialog_line_heigth);
	}

	public int getButton_margin_top() {
        int ret_size = 0;
        ret_size = (int) (dialog_button_margin_top * getScreenWidthScale());
        return ret_size;
    }

	public int getButton_margin_botton() {
		int ret_size = 0;
		ret_size = (int) (dialog_button_margin_botton * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton_width() {
		int ret_size = 0;
		ret_size = (int) (dialog_one_button_width * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton_heigth() {
		int ret_size = 0;
		ret_size = (int) (dialog_one_button_heigth * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton1_left() {
		int ret_size = 0;
		ret_size = (int) (button1_one_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton2_left() {
		int ret_size = 0;
		ret_size = (int) (button2_one_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton3_left() {
		int ret_size = 0;
		ret_size = (int) (button3_one_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_one_botton3_right() {
		int ret_size = 0;
		ret_size = (int) (button3_one_right * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton_width() {
		int ret_size = 0;
		ret_size = (int) (dialog_two_button_width * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton_heigth() {
		int ret_size = 0;
		ret_size = (int) (dialog_two_button_heigth * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton1_left() {
		int ret_size = 0;
		ret_size = (int) (button1_two_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton2_left() {
		int ret_size = 0;
		ret_size = (int) (button2_two_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton3_left() {
		int ret_size = 0;
		ret_size = (int) (button3_two_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_two_botton3_right() {
		int ret_size = 0;
		ret_size = (int) (button3_two_right * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton_width() {
		int ret_size = 0;
		ret_size = (int) (dialog_three_button_width * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton_heigth() {
		int ret_size = 0;
		ret_size = (int) (dialog_three_button_heigth * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton1_left() {
		int ret_size = 0;
		ret_size = (int) (button1_three_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton2_left() {
		int ret_size = 0;
		ret_size = (int) (button2_three_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton3_left() {
		int ret_size = 0;
		ret_size = (int) (button3_three_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_three_botton3_right() {
		int ret_size = 0;
		ret_size = (int) (button3_three_right * getScreenWidthScale());
		return ret_size;
	}

	public int getdialog_text_margin_top() {
		int ret_size = 0;
		ret_size = (int) (dialog_text_margin_top * getScreenWidthScale());
		return ret_size;
	}

	public int getdialog_text_margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_text_margin_left * getScreenWidthScale());
		return ret_size;
	}

	public int getdialog_text_margin_right() {
		int ret_size = 0;
		ret_size = (int) (dialog_text_margin_right * getScreenWidthScale());
		return ret_size;
	}

	public float getButton_text_size() {
		float ret_size = 0.0f;
		ret_size = px2sp(getScreenWidthScale() * dialog_button_text_font_size);
		return ret_size;
	}

	public int getButton_text_color() {
		return dialog_button_text_font_color;
	}

	public int getButton_no_text_color() {
		return dialog_button_text_font_color;
	}

	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public int px2sp(float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;

		return (int) (pxValue / scale + 0.5f);
	}

	public float getexit_text_size() {
		float ret_size = 0.0f;

		ret_size = px2sp(getScreenWidthScale() * dialog_exit_text_font_size);

		return ret_size;
	}

	public int getexit_text_color() {

		return dialog_exit_text_font_color;
	}

	public int getexit_text_margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_exit_text_font_Margin_left * getScreenWidthScale());
		return ret_size;
	}

	public int getdialog_width() {
		int ret_size = 0;
		ret_size = (int) (384 * getScreenWidthScale());
		return ret_size;
	}

	public int getdialog_progress_margin_top() {
		int ret_size = 0;
		ret_size = (int) (dialog_progress_margin_top * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_margin_top() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_margin_top * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_margin_right() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_margin_right * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_margin_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_margin_left * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_color_text_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_color_text_left * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_color_image_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_color_image_left * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_color_image_right() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_color_image_right * getScreenWidthScale());
		return ret_size;
	}

	public float getedit_text_size() {
		float ret_size = 0.0f;
		ret_size = px2sp(getScreenWidthScale() * dialog_edit_color_font_size);
		return ret_size;
	}

	public int getDialog_edit_fontL_image_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_fontL_image_left * getScreenWidthScale());
		return ret_size;
	}

	public int getDialog_edit_fontM_image_left() {
		int ret_size = 0;
		ret_size = (int) (dialog_edit_fontM_image_left * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_feedback_botton_width() {
		int ret_size = 0;
		ret_size = (int) (dialog_feedback_button_width * getScreenWidthScale());
		return ret_size;
	}

	public int getButton_feedback_botton_heigth() {
		int ret_size = 0;
		ret_size = (int) (dialog_feedback_button_heigth * getScreenWidthScale());
		return ret_size;
	}

    public float getWScale(int w) {
        int widthPhone;
        float mscale_width = 0;
        widthPhone = getPhoneScreen().widthPixels;
        mscale_width = widthPhone * 1.0f / w;
        return mscale_width;
    }

    public float getHScale(int h) {
        int widthPhone;
        float mscale_height = 0;
        widthPhone = getPhoneScreen().heightPixels;
        mscale_height = widthPhone * 1.0f / h;
        return mscale_height;
    }

}
