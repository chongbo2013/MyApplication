package com.tpad.common.utils.phonebaseinfo;

import java.io.Serializable;

/**
 * 用户手机固件信息
 *
 * @author jone.sun
 */
public class Firmware implements Serializable {
    private static final long serialVersionUID = 010121L;

    private String clientVersion; //客户端版本(必须提供)
    private String imei; //imei号
    private String imsi; //当前SIM卡的imsi号
    private String fm; //渠道fm(必须提供)
    private String os; //操作系统
    private String model; //手机机型
    private String operators; //运营商：[CMCC|CUC|CNC]（注：即移动，联通，电信）
    private String resolution; //屏幕分辨率
    private String netEnv; //网络类型
    private String pkg; //包名
    private String mac;
    private String android_id;
    private String device_id;
    private String brand;//手机品牌
    //private String voltage;//电压
    //private String temperature;//温度


    public Firmware() {
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

//    public String getVoltage() {
//        return voltage;
//    }
//
//    public void setVoltage(String voltage) {
//        this.voltage = voltage;
//    }
//
//    public String getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(String temperature) {
//        this.temperature = temperature;
//    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getNetEnv() {
        return netEnv;
    }

    public void setNetEnv(String netEnv) {
        this.netEnv = netEnv;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }


    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
