package com.tpad.common.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.change.unlock.common.R;

public class AnyscHttpLoadingShow {

	private static Dialog mDialog;
	private static boolean canDismiss = true;


	/**
	 * 顯示自定义的loadingView
	 *
	 * @param mActivity
	 * @param msg
	 * @return
	 */
	public static void showLoadingDialog(final Activity mActivity, String msg) {
		showLoadingDialog(mActivity, msg, true);
	}

	public static void showLoadingDialog(final Activity mActivity, String msg, boolean isTouchDismiss) {
		dismissLoadingDialog();
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		View v = inflater.inflate(R.layout.anysc_http_loading, null);// 得到加载view
		TextView tipTextView = (TextView) v.findViewById(R.id.text_show);// 提示文字
		tipTextView.setText(msg);// 设置加载信息
		mDialog = new Dialog(mActivity, R.style.CustomProgressDialog);// 创建自定义样式dialog
		mDialog.setContentView(v);// 设置布局
		mDialog.show();
		if(isTouchDismiss){
			mDialog.setCancelable(false);// 不可以用“返回键”取消
			v.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					dismissLoadingDialog();


//				TTApplication.getPhoneUtils().DisplayToast(
//						mActivity.getString(R.string.AnyscHttpLoading_dialog_show));
					return false;
				}
			});
		}
	}
	public static void showLoadingDialog(final Activity mActivity, String msg, boolean isTouchDismiss,DialogInterface.OnCancelListener listener) {
		dismissLoadingDialog();
		LayoutInflater inflater = LayoutInflater.from(mActivity);
		View v = inflater.inflate(R.layout.anysc_http_loading, null);// 得到加载view
		TextView tipTextView = (TextView) v.findViewById(R.id.text_show);// 提示文字
		tipTextView.setText(msg);// 设置加载信息
		mDialog = new Dialog(mActivity, R.style.CustomProgressDialog);// 创建自定义样式dialog
		mDialog.setContentView(v);// 设置布局
		mDialog.show();
//		if(isTouchDismiss){
		mDialog.setOnCancelListener(listener);
		mDialog.setCancelable(true);// 不可以用“返回键”取消

//			v.setOnTouchListener(new OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//
//					dismissLoadingDialog();
//
//
////				TTApplication.getPhoneUtils().DisplayToast(
////						mActivity.getString(R.string.AnyscHttpLoading_dialog_show));
//					return false;
//				}
//			});
//		}
	}


    public static void showLoadingDialog(final Context mActivity, String msg) {
		dismissLoadingDialog();
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.anysc_http_loading, null);// 得到加载view
        TextView tipTextView = (TextView) v.findViewById(R.id.text_show);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        mDialog = new Dialog(mActivity, R.style.CustomProgressDialog);// 创建自定义样式dialog
        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setContentView(v);// 设置布局
        mDialog.show();
        v.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissLoadingDialog();
//				TTApplication.getPhoneUtils().DisplayToast(
//						mActivity.getString(R.string.AnyscHttpLoading_dialog_show));
                return false;
            }
        });

    }


    /**
	 * 取消自定義的loadingView
	 */
	public static void dismissLoadingDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

}
