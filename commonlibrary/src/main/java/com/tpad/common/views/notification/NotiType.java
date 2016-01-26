package com.tpad.common.views.notification;

/**
 * Created by loang.chen on 2015/5/12.
 */
public enum NotiType {
    TYPE_SHOW_L,// 同一个位置，不可清除
    TYPE_SHOW_S,// 非同一个位置，可清除
    TYPE_SHOW_S_FIXED_UPDATE_VERSION,
    TYPE_SHOW_S_FIXED// 临时展示，同一个位置，可清除
}
