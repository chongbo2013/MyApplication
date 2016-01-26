package com.tpad.common.views.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.change.unlock.common.R;
import com.tpad.common.utils.PhoneUtils;

public class DialogManager implements OnClickListener {
    private Context mContext;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LinearLayout dialog_top_linearlayout,
            dialog_button_linearlayout;
    private RelativeLayout dialog_center_relaivelayout;
    private TextView dialog_title, dialog_button_left,
            dialog_button_center, dialog_button_right;
    private ImageView dialog_center_imageview;
    private int titlesize, centersize, buttonsize;
    private boolean ishasImage, ishasButton, ishasCenterbut, ishasleftbut;
    private TextView dialog_center_textview;
    private int button_left_background;
    private String button_left_text;
    private int button_center_background;
    private String button_center_text;
    private int button_right_background;
    private String button_right_text;
    private setOnButtonClick buttonClick;
    private setCenterImage centerImage;
    private String title;
    private String toastString;
    private int leftTextcolor, centerTextcolor, RightTextcolor, toasttextcolor;
    private int toastleft, toasttop, toastright, toastbottom;
    private int button_width, button_height, buttontop, buttonbottom;
    private PhoneUtils phoneUtils;
    public final static int TYPE_ONE_BUTTON = 1;
    public final static int TYPE_TWO_BUTTON = 2;
    public final static int TYPE_THREE_BUTTON = 3;
    public final static int TYPE_NO_BUTTON = 4;
    private int titleColor;
    private boolean canCancel = true;
    private DialogInterface.OnCancelListener cancelListener;
    public TextView getDialog_button_right(){
        return dialog_button_right;
    };

    /**
     * 此构造函数默认为底部两个按钮，没有中间图片
     *
     * @param context
     * @param buttonClick 实现此接口，监听按钮的点击事件，执行点击的操作
     *                    <p/>
     *                    需要实现方法 setToastRes及setButtonRes  设置dialog的资源
     */

    public DialogManager(Context context, int type, setOnButtonClick buttonClick) {
        this.mContext = context;
        builder = null;
        phoneUtils = PhoneUtils.getInstance(context);
        builder = new AlertDialog.Builder(mContext);
        this.buttonClick = buttonClick;
        switch (type) {
            case TYPE_ONE_BUTTON:

                ishasButton = true;
                ishasCenterbut = false;
                ishasleftbut = false;
                break;
            case TYPE_TWO_BUTTON:
                ishasButton = true;
                ishasCenterbut = false;
                ishasleftbut = true;
                break;
            case TYPE_THREE_BUTTON:
                ishasButton = true;
                ishasCenterbut = true;
                ishasleftbut = true;
                break;
            case TYPE_NO_BUTTON:
                ishasButton = false;
                ishasCenterbut = false;
                ishasleftbut = false;
                break;
            default:
                break;
        }
        isCache();
    }


    private void isCache() {
        ishasImage = false;
        //初始化字体大小及字体颜色
        titlesize = buttonsize = 36;
        centersize = 34;
        titleColor = 0xFF4F4D4D;
        leftTextcolor = 0xFFFFFFFF;
        RightTextcolor = 0xFFFFFFFF;
        centerTextcolor = 0xFFB5B4B4;
        toasttextcolor = 0xFF4F4D4D;
        button_right_background = 0xFFE64303;
        button_left_background = 0xFFCECECE;
        //初始化 提示词的边距
        toastleft = 0;
        toasttop = 30;
        toastright = 0;
        toastbottom = 30;
        //初始化 底部按钮的边距
        button_width = 520;
        buttontop = 0;
        button_height = 90;
        buttonbottom = 50;
    }


    /**
     * 设置字体大小
     *
     * @param titlesize
     * @param centersize
     * @param buttonsize
     * @return
     */
    public DialogManager setTextSize(int titlesize, int centersize, int buttonsize) {
        this.titlesize = titlesize;
        this.centersize = centersize;
        this.buttonsize = buttonsize;
        return this;
    }

    /**
     * 设置dialog的头部信息及提示词
     *
     * @param title
     * @param toastString
     */
    public DialogManager setToastRes(String title, String toastString) {
        this.title = title;
        this.toastString = toastString;
        return this;
    }


    /**
     * 设置单个按钮资源
     */
    public DialogManager setOneButtonRes(int rightRes, String rightString) {
        this.button_right_background = rightRes;
        this.button_right_text = rightString;
        return this;
    }

    /**
     * 设置两个按钮资源
     *
     * @param leftRes     左边按钮的背景色值
     * @param leftString  左边按钮的内容
     * @param rightRes    右边的背景色值
     * @param RightString 右边按钮内容
     */

    public DialogManager setTwoButtonRes(int leftRes, String leftString, int rightRes,
                                         String RightString) {
        this.button_left_background = leftRes;
        this.button_left_text = leftString;
        this.button_right_background = rightRes;
        this.button_right_text = RightString;
        return this;
    }

    /**
     * 设置按钮资源
     *
     * @param leftRes      左边按钮的背景色值
     * @param leftString   左边按钮的内容
     * @param rightRes     右边的背景色值
     * @param RightString  右边按钮内容
     * @param centerRes    中间的背景色值
     * @param centerString 中间按钮内容
     */
    public DialogManager setThreeButtonRes(int leftRes, String leftString, int rightRes,
                                           String RightString, int centerRes, String centerString) {
        this.button_left_background = leftRes;
        this.button_left_text = leftString;
        this.button_right_background = rightRes;
        this.button_right_text = RightString;
        this.button_center_background = centerRes;
        this.button_center_text = centerString;
        return this;
    }


    public DialogManager setToastTextColor(int toastTextColor) {
        this.toasttextcolor = toastTextColor;
        return this;
    }

    public DialogManager setTitleTextColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }


    public DialogManager setLeftTextColor(int leftTextColor) {
        this.leftTextcolor = leftTextColor;
        return this;
    }


    public DialogManager setCenterTextColor(int centerTextColor) {
        this.centerTextcolor = centerTextColor;
        return this;
    }

    public DialogManager setImageView(setCenterImage centerImage) {
        this.ishasImage = true;
        this.centerImage = centerImage;
        return this;
    }


    public DialogManager setRightTextColor(int rightTextColor) {
        RightTextcolor = rightTextColor;
        return this;
    }

    /**
     * 设置提示词的margins
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public DialogManager setToastMargins(int left, int top, int right, int bottom) {
        this.toastleft = left;
        this.toasttop = top;
        this.toastright = right;
        this.toastbottom = bottom;
        return this;
    }

    /**
     * 设置底部按钮布局的宽度及高度 设置margintop，marginbottom
     *
     * @param width  宽度
     * @param height 高度
     * @param top
     * @param bottom
     * @return
     */
    public DialogManager setButtonMargins(int width, int height, int top, int bottom) {
        this.button_width = width;
        this.buttontop = top;
        this.button_height = height;
        this.buttonbottom = bottom;
        return this;
    }


    public void showDialog() {
        // LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(mContext);
        // 把activity_login中的控件定义在View中
        View view = factory.inflate(R.layout.dialog_manager_base_layout, null);
        findviews(view);
        initviews(view);
        dialog_title.setText(title);
        dialog = null;
        dialog = builder.create();
        if (cancelListener!=null){
            dialog.setOnCancelListener(cancelListener);
        }
        dialog.setCancelable(canCancel);
        dialog.setCanceledOnTouchOutside(false);
        initOnKeyBackLis();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        //window.setWindowAnimations(R.style.myAnimationstyle); // 添加动画
        window.setWindowAnimations(R.style.dilog_Animation); // 添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = phoneUtils.get720WScale(614);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setContentView(view);
    }

    public void initOnKeyBackLis() {
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                return false;
//            }
//        });

    }

    private void initviews(View view) {
        //初始化title、提示词布局边距
        LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
                phoneUtils.get720WScale(614), LayoutParams.WRAP_CONTENT);
        topLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        LinearLayout.LayoutParams centerLayoutParams = new LinearLayout.LayoutParams(
                phoneUtils.get720WScale(520), LayoutParams.WRAP_CONTENT);
        centerLayoutParams.setMargins(0, phoneUtils.get720WScale(10), 0, phoneUtils.get720WScale(10));
        dialog_top_linearlayout.setLayoutParams(topLayoutParams);
        dialog_center_relaivelayout.setLayoutParams(centerLayoutParams);
        dialog_title.setTextSize(phoneUtils.px2sp(phoneUtils.get720WScale(titlesize)));
        dialog_title.setTextColor(titleColor);
        initcentertextview();

        if (ishasButton) {//有左边button

            //初始化底部按钮布局，并设置边距
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    phoneUtils.get720WScale(button_width), phoneUtils.get720WScale(button_height));
            buttonLayoutParams.setMargins(phoneUtils.get720WScale(0), phoneUtils.get720WScale(buttontop)
                    , phoneUtils.get720WScale(0), phoneUtils.get720WScale(buttonbottom));
            dialog_button_linearlayout.setLayoutParams(buttonLayoutParams);

            initbuttonright();

            if (ishasleftbut) {//有左边button及右边button
                initbuttonleft();
                if (ishasCenterbut) {//有所有button
                    initbuttoncenter();
                }
            } else {//只有左边button时

                dialog_button_left.setVisibility(View.GONE);
                //需要设置左边button的边距  字体大小 字体颜色
            }
        } else {
            dialog_button_linearlayout.setVisibility(View.GONE);
        }

        if (ishasImage) {//是否需要中间图片
            dialog_center_imageview.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams centerimageParams = centerImage.initImage(dialog_center_imageview);
            if (centerimageParams != null) {
                dialog_center_imageview.setLayoutParams(centerimageParams);
            }
        }

    }


    /**
     * 初始化中间按钮
     */
    private void initbuttoncenter() {
        dialog_button_center.setVisibility(View.VISIBLE);
        dialog_button_center.setTextSize(phoneUtils.px2sp(phoneUtils.get720WScale(buttonsize)));
        dialog_button_center.setBackgroundResource(button_center_background);
        dialog_button_center.setTextColor(centerTextcolor);
        dialog_button_center.setText(button_center_text);
        dialog_button_center.setOnClickListener(this);

    }

    /**
     * 初始化右边按钮
     */
    private void initbuttonright() {
        dialog_button_right.setBackgroundResource(button_right_background);
        dialog_button_right.setTextSize(phoneUtils.px2sp(phoneUtils.get720WScale(buttonsize)));
        dialog_button_right.setTextColor(RightTextcolor);
        dialog_button_right.setText(button_right_text);
        dialog_button_right.setOnClickListener(this);

    }

    /**
     * 初始化左边按钮
     */
    private void initbuttonleft() {
        dialog_button_left.setTextSize(phoneUtils.px2sp(phoneUtils.get720WScale(buttonsize)));
        dialog_button_left.setBackgroundResource(button_left_background);
        dialog_button_left.setTextColor(leftTextcolor);
        dialog_button_left.setText(button_left_text);
        dialog_button_left.setOnClickListener(this);

    }

    /*
     * dialog中间提示的适配
     */
    private void initcentertextview() {
        RelativeLayout.LayoutParams centertextviewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        centertextviewParams.setMargins((toastleft), phoneUtils.get720WScale(toasttop)
                , phoneUtils.get720WScale(toastright), phoneUtils.get720WScale(toastbottom));
        centertextviewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centertextviewParams.addRule(RelativeLayout.CENTER_VERTICAL);


        dialog_center_textview.setTextSize(phoneUtils.px2sp(phoneUtils.get720WScale(centersize)));
        dialog_center_textview.setTextColor(toasttextcolor);
        dialog_center_textview.setLayoutParams(centertextviewParams);
        dialog_center_textview.setText(toastString);

    }

    private void findviews(View view) {
        dialog_top_linearlayout = (LinearLayout) view
                .findViewById(R.id.dialog_top_linearlayout);
        dialog_center_relaivelayout = (RelativeLayout) view
                .findViewById(R.id.dialog_center_linearlayout);
        dialog_button_linearlayout = (LinearLayout) view
                .findViewById(R.id.dialog_button_linearlayout);

        dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        dialog_center_textview = (TextView) view
                .findViewById(R.id.dialog_center_textview);
        dialog_center_imageview = (ImageView) view
                .findViewById(R.id.dialog_center_imageview);

        dialog_button_left = (TextView) view
                .findViewById(R.id.dialog_button_left);
        dialog_button_center = (TextView) view
                .findViewById(R.id.dialog_button_center);
        dialog_button_right = (TextView) view
                .findViewById(R.id.dialog_button_right);

    }

    public DialogManager setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }

    public DialogManager setCancelListen(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        canCancel = true;
        return this;
    }


    /**
     * 监听点击事件
     *
     * @author Administrator
     */
    public interface setOnButtonClick {
        public void onLeftButtonClick(DialogManager dialog);

        public void onRightButtonClick(DialogManager dialog);

        public void onCenterButtonClick(DialogManager dialog);

        public void onImageViewClick(DialogManager dialog);
    }

    /**
     * 初始化中间图片的接口
     * 需要返回RelativeLayout.LayoutParams对象
     *
     * @author Administrator
     */
    public interface setCenterImage {
        public RelativeLayout.LayoutParams initImage(ImageView imageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_button_left) {
            buttonClick.onLeftButtonClick(DialogManager.this);
        } else if (v.getId() == R.id.dialog_button_center) {
            buttonClick.onCenterButtonClick(DialogManager.this);
        } else if (v.getId() == R.id.dialog_button_right) {
            buttonClick.onRightButtonClick(DialogManager.this);
        } else if (v.getId() == R.id.dialog_center_imageview) {
            buttonClick.onImageViewClick(DialogManager.this);
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
