package com.tpad.common.model.guidemodel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.change.unlock.common.R;
import com.tpad.common.utils.PhoneScreenAdpt;
import com.tpad.common.utils.PhoneUtils;


public class homeDialog extends Dialog {

	private TextView home_show_title_txt, home_show_title_id;
	private ImageView show_image;
	private Button hButton_one, hButton_two;
	private PhoneUtils pu;
	private int show_title_id;
	private int show_text_id;
	private int show_image_id;
	private int botton_text1_id;
	private int botton_text2_id;
	private android.view.View.OnClickListener mOnClickListener;

	private final int w = 480;

	private final int show_title_top = 12;
	private final int show_title_left = 16;
	private final int show_title_right = 16;

	private final int imageTopMargin = 12;
	private final int imageLeftMargin = 30;
	private final int imageRightMargin = 30;

	private Context mcontext;

	public homeDialog(Context context, int theme, PhoneUtils pu, int title, int mes, int imageid,
                      int po, int neu, android.view.View.OnClickListener listener) {
		super(context, theme);
		mcontext = context;
		this.pu = pu;
		show_title_id = title;
		show_text_id = mes;
		show_image_id = imageid;
		botton_text1_id = po;
		botton_text2_id = neu;
		mOnClickListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.guidemodel_homeshowdialog);
		Window dialogWindow = getWindow();
		LayoutParams lp = dialogWindow.getAttributes();
		lp.width = PhoneScreenAdpt.getInstance(mcontext).getdialog_width();
		dialogWindow.setAttributes(lp);

		home_show_title_id = (TextView) findViewById(R.id.build_dialog_title_id);
		home_show_title_txt = (TextView) findViewById(R.id.home_bulid_dialog_title_txt);
		show_image = (ImageView) findViewById(R.id.home_build_dialog_image);
		hButton_one = (Button) findViewById(R.id.home_dialog_button_one);
		hButton_two = (Button) findViewById(R.id.home_dialog_button_two);

		LinearLayout build_dialog_title_logo = (LinearLayout) findViewById(R.id.build_dialog_title_logo);
		build_dialog_title_logo.setBackgroundResource(R.drawable.guidemodel_title_bg_color);

		LinearLayout.LayoutParams show_title_idParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		show_title_idParams.topMargin = PhoneScreenAdpt.getInstance(mcontext).getTitle_Margin_top();
		show_title_idParams.bottomMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getTitle_Margin_top();
		home_show_title_id.setLayoutParams(show_title_idParams);
		home_show_title_id.setGravity(Gravity.CENTER);
		home_show_title_id.setTextSize(PhoneScreenAdpt.getInstance(mcontext).getTitleFontSize());
		// home_show_title_id.setTextColor(PhoneScreenAdpt.getInstance(mcontext).getTitleFontColor());
		home_show_title_id.setText(show_title_id);

		LinearLayout.LayoutParams show_title_txtParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		show_title_txtParams.topMargin = (int) (show_title_top * pu.getWScale(w));
		show_title_txtParams.leftMargin = (int) (show_title_left * pu.getWScale(w));
		show_title_txtParams.rightMargin = (int) (show_title_right * pu.getWScale(w));
		home_show_title_txt.setLayoutParams(show_title_txtParams);
		home_show_title_txt.setGravity(Gravity.CENTER | Gravity.LEFT);
		home_show_title_txt.setTextSize(PhoneScreenAdpt.getInstance(mcontext).getTextFontSize());
		// home_show_title_txt.setTextColor(PhoneScreenAdpt.getInstance(mcontext).getTextFontColor());
		home_show_title_txt.setText(show_text_id);

		LinearLayout.LayoutParams ImageshowParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageshowParams.topMargin = (int) (imageTopMargin * pu.getWScale(w));
		ImageshowParams.leftMargin = (int) (imageLeftMargin * pu.getWScale(w));
		ImageshowParams.rightMargin = (int) (imageRightMargin * pu.getWScale(w));
		show_image.setLayoutParams(ImageshowParams);
		show_image.setScaleType(ScaleType.CENTER);
		show_image.setImageResource(show_image_id);

		LinearLayout.LayoutParams botton_oneParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		botton_oneParams.leftMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getButton_two_botton1_left();
		botton_oneParams.topMargin = PhoneScreenAdpt.getInstance(mcontext).getButton_margin_top();
		botton_oneParams.bottomMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getButton_margin_top();

		hButton_one.setLayoutParams(botton_oneParams);
		hButton_one.setHeight(PhoneScreenAdpt.getInstance(mcontext).getButton_two_botton_heigth());
		hButton_one.setWidth(PhoneScreenAdpt.getInstance(mcontext).getButton_two_botton_width());
		hButton_one.setText(botton_text1_id);
		hButton_one.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		hButton_one.setPadding(0, 0, 0, 0);
		// hButton_one.setTextColor(PhoneScreenAdpt.getInstance(mcontext).getButton_text_color());
		hButton_one.setTextSize(PhoneScreenAdpt.getInstance(mcontext).getButton_text_size());
		hButton_one.setOnClickListener(mOnClickListener);
		hButton_one.setBackgroundResource(R.drawable.guidemodel_dialog_bt_left_color);

		LinearLayout.LayoutParams botton_twoParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		botton_twoParams.leftMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getButton_two_botton3_left();
		botton_twoParams.rightMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getButton_two_botton3_right();
		botton_twoParams.topMargin = PhoneScreenAdpt.getInstance(mcontext).getButton_margin_top();
		botton_twoParams.bottomMargin = PhoneScreenAdpt.getInstance(mcontext)
				.getButton_margin_top();

		hButton_two.setLayoutParams(botton_twoParams);
		hButton_two.setHeight(PhoneScreenAdpt.getInstance(mcontext).getButton_two_botton_heigth());
		hButton_two.setWidth(PhoneScreenAdpt.getInstance(mcontext).getButton_two_botton_width());
		hButton_two.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		hButton_two.setPadding(0, 0, 0, 0);
		hButton_two.setText(botton_text2_id);
		// hButton_two.setTextColor(PhoneScreenAdpt.getInstance(mcontext).getButton_no_text_color());
		hButton_two.setTextSize(PhoneScreenAdpt.getInstance(mcontext).getButton_text_size());
		hButton_two.setOnClickListener(mOnClickListener);
		hButton_two.setBackgroundResource(R.drawable.guidemodel_dialog_bt_right_color);

	}

	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
