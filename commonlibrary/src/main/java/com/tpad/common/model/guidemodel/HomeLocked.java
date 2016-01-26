package com.tpad.common.model.guidemodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.change.unlock.common.R;
import com.tpad.common.utils.PhoneUtils;

import java.util.List;

public abstract class HomeLocked extends Activity implements OnClickListener,
		OnTouchListener {
	private static final String SCHEME = "package";
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	private static final String APP_PKG_NAME_22 = "pkg";
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	private final String DEFPKG = "android";

	private boolean btn_Enabled;
	private Button btn_1, btn_2, btn_3;
	private ImageView mhomebuild_botom;
	private LinearLayout homelock_four, homelock_three, homelock_top_up;
	private TextView mtextView1, textView2, textView3, textView4;
	private PhoneUtils pu;
	List<ResolveInfo> apps = null;
	private boolean show;
	private int defhome;
	private String PKGSelf = "com.change.unlock";
    private String app_name = null;

	// Adaptation parameters 480*720
	private final int w = 480;
	private final int h = 800;
	private final int secondTitleFontSize = 18;
	private final float showFontSize = 20.0f;

	private final int textView1leftMargin = 43;
	private final int textView1topMargin = 17;

	private final int homelock_threeLeftMargin = 0;
	private final int homelock_threeRMargin = 0;
	private final int homelock_threeTopMargin = 17;
	private final int homelock_w = 480;

	private final int textView2topMargin = 21;
	private final int textView3topMargin = 23;

	private final int btn_1TopMargin = 48;

	private final int btn_2TopMargin = 42;


	private final int textView4topMargin = 45;
	private final int textView4LeftMargin = 58;

	private final int btn_3TopMargin = 44;


	private final int homelock_botom_TopMargin = 100;


	private final int secondTitleFontColor = 0xFFffffff;
	private final int showFontFontColor = 0xFF9c9b9b;

	private homeDialog mhomeDialog;
	private homeDialog homeDialog1;

    private GuideModelSave modelSave;
    private LinearLayout top;

    private View topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.guidemodel_homelocked);
		Intent intent = getIntent();
		Bundle extras = intent == null ? null : intent.getExtras();
		show = extras == null ? false : extras
				.getBoolean(getString(R.string.guidemodel_Alert_show_info));
        modelSave = getGuideModelSave();
        app_name = getAppName();
        PKGSelf = getPkG();
		initData();
		initControls();
		showControls();
        topView = getTopView();
        if (topView != null){
            top.addView(topView);
        }
		btn_1.setOnClickListener(this);
		btn_1.setOnTouchListener(this);
		btn_2.setOnClickListener(this);
		btn_2.setOnTouchListener(this);
		btn_3.setOnClickListener(this);
		btn_3.setOnTouchListener(this);
    }

    protected abstract String getPkG();

    protected abstract String getAppName();

    protected abstract GuideModelSave getGuideModelSave();

    protected abstract View getTopView();

    void initData() {
		pu = PhoneUtils.getInstance(this);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		apps = getPackageManager().queryIntentActivities(intent, 0);
		defhome = modelSave.getDefaultHome();
		if (show)
			showDetialHomes(getItems(apps), defhome);
	}

    public PhoneUtils getPhoneUtils(){
        return pu;
    };

	void initControls() {
		mtextView1 = (TextView) findViewById(R.id.textView1);
		homelock_three = (LinearLayout) findViewById(R.id.homelock_three);
		homelock_four = (LinearLayout) findViewById(R.id.homelock_four);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		mhomebuild_botom = (ImageView) findViewById(R.id.homebuild_botom);
		btn_1 = (Button) findViewById(R.id.button1);
		btn_2 = (Button) findViewById(R.id.button2);
		btn_3 = (Button) findViewById(R.id.button3);
        top = (LinearLayout)findViewById(R.id.top);
	}



    void showControls() {

		LinearLayout.LayoutParams mtextView1Params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mtextView1Params.topMargin = (int) (textView1topMargin * pu
				.getHScale(h));
		mtextView1Params.leftMargin = (int) (textView1leftMargin * pu
				.getWScale(w));
		mtextView1.setLayoutParams(mtextView1Params);
		mtextView1.setTextSize(pu.px2sp(showFontSize * pu.getWScale(w)));
		mtextView1.setTextColor(showFontFontColor);
		mtextView1.setText(getString(R.string.guidemodel_home_title));

		LinearLayout.LayoutParams homelock_threeParams = new LinearLayout.LayoutParams(
				(int) (homelock_w * pu.getWScale(w)), LayoutParams.WRAP_CONTENT);
		homelock_threeParams.leftMargin = (int) (homelock_threeLeftMargin * pu
				.getWScale(w));
		homelock_threeParams.rightMargin = (int) (homelock_threeRMargin * pu
				.getWScale(w));
		homelock_threeParams.topMargin = (int) (homelock_threeTopMargin * pu
				.getHScale(h));
		homelock_three.setLayoutParams(homelock_threeParams);
		homelock_three.setBackgroundResource(R.drawable.guidemodel_setting_item_bottom);

		homelock_four.setLayoutParams(homelock_threeParams);
		homelock_four.setBackgroundResource(R.drawable.guidemodel_setting_item_bottom);

		LinearLayout.LayoutParams textView2Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textView2Params.topMargin = (int) (textView2topMargin * pu.getHScale(h));
		textView2.setLayoutParams(textView2Params);
		textView2.setGravity(Gravity.CENTER);
		textView2.setTextSize(pu.px2sp(showFontSize * pu.getWScale(w)));
		textView2.setTextColor(showFontFontColor);
		textView2.setText(R.string.home_txt_clear_tip);

		LinearLayout.LayoutParams button1Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// button1Params.leftMargin = (int) (btn_1LeftMargin * pu.getWScale(w));
		// button1Params.rightMargin = (int) (btn_1LeftMargin *
		// pu.getWScale(w));
		button1Params.topMargin = (int) (btn_1TopMargin * pu.getHScale(h));
		btn_1.setLayoutParams(button1Params);
		btn_1.setBackgroundResource(R.drawable.guidemodel_homelocked_initsetting);
		btn_1.setGravity(Gravity.CENTER);
		btn_1.setTextSize(secondTitleFontSize);
		btn_1.setTextColor(secondTitleFontColor);
		btn_1.setText(R.string.guidemodel_homelocked_step_1);

		LinearLayout.LayoutParams textView3Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textView3Params.topMargin = (int) (textView3topMargin * pu.getHScale(h));
		textView3Params.bottomMargin = (int) ((textView3topMargin * 2) * pu
				.getHScale(h));
		textView3.setLayoutParams(textView3Params);
		textView3.setGravity(Gravity.CENTER);
		textView3.setTextSize(pu.px2sp(showFontSize * pu.getWScale(w)));
		textView3.setTextColor(showFontFontColor);
		textView3.setText(R.string.home_txt_lock_tip);

		LinearLayout.LayoutParams button2Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		button2Params.topMargin = (int) (btn_2TopMargin * pu.getHScale(h));
		btn_2.setLayoutParams(button2Params);

		btn_2.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);
		btn_2.setGravity(Gravity.CENTER);
		btn_2.setTextSize(secondTitleFontSize);
		btn_2.setTextColor(secondTitleFontColor);
		btn_2.setText(R.string.guidemodel_home_step_2);

		LinearLayout.LayoutParams textView4Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textView4Params.topMargin = (int) (textView4topMargin * pu.getWScale(w));
		textView4Params.leftMargin = (int) (textView4LeftMargin * pu
				.getWScale(w));
		textView4Params.rightMargin = (int) (textView4LeftMargin * pu
				.getWScale(w));

		textView4.setLayoutParams(textView4Params);
		textView4.setGravity(Gravity.LEFT);
		textView4.setTextSize(pu.px2sp(showFontSize * pu.getWScale(w)));
		textView4.setTextColor(showFontFontColor);
		textView4.setText(R.string.home_txt_select);

		LinearLayout.LayoutParams button3Params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// button3Params.leftMargin = (int) (btn_3LeftMargin * pu.getWScale(w));
		// button3Params.rightMargin = (int) (btn_3LeftMargin *
		// pu.getWScale(w));
		button3Params.topMargin = (int) (btn_3TopMargin * pu.getHScale(h));
		btn_3.setLayoutParams(button3Params);

		btn_3.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);
		btn_3.setGravity(Gravity.CENTER);
		btn_3.setTextSize(secondTitleFontSize);
		btn_3.setTextColor(secondTitleFontColor);
		btn_3.setText(R.string.home_btn_select);

		LinearLayout.LayoutParams homebuil_botom_params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		homebuil_botom_params.topMargin = (int) (homelock_botom_TopMargin * pu
				.getHScale(h));
		mhomebuild_botom.setLayoutParams(homebuil_botom_params);
	}

	@Override
	protected void onResume() {
		super.onResume();

		btn_Enabled = !getDefaultPkg().equals(DEFPKG);

		btn_1.setEnabled(btn_Enabled);

		btn_2.setEnabled(!btn_Enabled);

		if (!btn_Enabled) {
			btn_1.setBackgroundResource(R.drawable.guidemodel_button_homelock_normal);
		} else {

			btn_1.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);

		}
		if (btn_Enabled) {

			btn_2.setBackgroundResource(R.drawable.guidemodel_button_homelock_normal);
		} else {

			btn_2.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);

		}

		if (defhome != 0) {
			for (ResolveInfo app : apps) {
				if (app.activityInfo.packageName.equals(modelSave.getDefaultHomePKG())) {
					btn_3.setText(app.loadLabel(getPackageManager()));
				}
			}
		}
	}

	public int showInstalledAppDetails(String packageName) {
		int flag = 0;
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);

		} else {
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		startActivity(intent);
		flag = 1;
		return flag;
	}

	public String getDefaultPkg() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ComponentName name = intent.resolveActivity(getPackageManager());
		if (name == null) {
			return "";
		}
		String pkg = name.getPackageName();
		return pkg;
	}

	public void showDetialHomes(final String[] items, int defaultValue) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.home_dialog_select));
		builder.setSingleChoiceItems(items, defaultValue,
				new DialogInterface.OnClickListener() {

					// @Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case -1:
							break;
						case 0:
							modelSave.setDefaultHome(which);
							btn_3.setText(getString(R.string.Alert_unchoose));
							break;
						default:
							for (ResolveInfo app : apps) {
								if (app.loadLabel(getPackageManager())
										.toString().equals(items[which])) {
									btn_3.setText(app.loadLabel(
											getPackageManager()).toString());

									modelSave.setDefaultHome(which);
									modelSave.setDefaultHomePKG(
													app.activityInfo.packageName);
									modelSave.setDefaultHomeClass(
													app.activityInfo.name);
								}
							}
							if (getDefaultPkg().equals(PKGSelf)) {
								String pkg = modelSave.getDefaultHomePKG();
								String cls = modelSave.getDefaultHomeClass();
								Intent in = new Intent();
								in.setAction(Intent.ACTION_MAIN);
								in.addCategory(Intent.CATEGORY_HOME);
								in.setComponent(new ComponentName(pkg, cls));
								startActivity(in);
							}
							break;
						}
						defhome = modelSave.getDefaultHome();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	public String[] getItems(List<ResolveInfo> apps) {
		String[] strs = null;
		if (apps == null || apps.size() <= 0) {
			strs = new String[1];
			strs[0] = getString(R.string.Alert_unchoose);
			return strs;
		}
		strs = new String[apps.size()];
		strs[0] = getString(R.string.Alert_unchoose);
		for (int i = 0, j = 0; i < apps.size(); i++, j++) {
			String appName = apps.get(i).loadLabel(getPackageManager())
					.toString();
			if (appName.equals(app_name)) {
				j--;
				continue;
			}
			strs[j + 1] = appName;
		}
		return strs;
	}

	public OnClickListener shelp_use_ourlistener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.home_dialog_button_one ) {

                showInstalledAppDetails(getDefaultPkg());
                mhomeDialog.dismiss();
            }else if (v.getId() == R.id.home_dialog_button_two){
                mhomeDialog.dismiss();
            }
		}
	};

	public OnClickListener help_not_use_ourlistener = new OnClickListener() {

		public void onClick(View v) {

			if (v.getId() == R.id.home_dialog_button_one) {


                Intent i = new Intent();
                i.setAction(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                homeDialog1.dismiss();
                finish();
            }else if(v.getId() == R.id.home_dialog_button_two){
                homeDialog1.dismiss();
            }
		}
	};

	public void onClick(View v) {
        if (v.getId() == R.id.button1) {


            int res = R.drawable.guidemodel_home_use_our_cn;

            mhomeDialog = new homeDialog(this, R.style.Theme_showdialog, pu,
                    R.string.guide_tip, R.string.homelock_help_use_our, res,
                    R.string.homelock_help_btn_next,
                    R.string.homelock_help_btn_cancel, shelp_use_ourlistener);
            Window window = mhomeDialog.getWindow();
            window.setWindowAnimations(R.style.dilog_Animation);
            mhomeDialog.show();
        } else if (v.getId() == R.id.button2){
            int res2 = R.drawable.guidemodel_home_use_not_our_cn;
            if (!getDefaultPkg().equals(PKGSelf)) {
                homeDialog1 = new homeDialog(this, R.style.Theme_showdialog,
                    pu, R.string.guide_tip, R.string.homelock_help_not_use_our,
                    res2, R.string.homelock_help_btn_next,
                    R.string.homelock_help_btn_cancel,
                    help_not_use_ourlistener);
                Window window1 = homeDialog1.getWindow();
             window1.setWindowAnimations(R.style.dilog_Animation);
             homeDialog1.show();
        }
        }else if(v.getId() == R.id.button3) {
            showDetialHomes(getItems(apps), defhome);
        }
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
            if(v.getId() == R.id.button1) {

                btn_1.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);
            }else if (v.getId() == R.id.button2){
                btn_2.setBackgroundResource(R.drawable.guidemodel_button_homelock_press_down);
            }else if (v.getId() == R.id.button3){
                btn_3.setBackgroundResource(R.drawable.guidemodel_button_homelock_press_down);
            }
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(v.getId() == R.id.button1) {

                btn_1.setBackgroundResource(R.drawable.guidemodel_button_homelock_normal);
            }else if (v.getId() == R.id.button2){
                btn_2.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);
            }else if (v.getId() == R.id.button3){
                btn_3.setBackgroundResource(R.drawable.guidemodel_button_homelock_press);
            }
		}
		return false;
	}

}

