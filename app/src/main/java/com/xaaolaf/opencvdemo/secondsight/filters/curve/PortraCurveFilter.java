package com.xaaolaf.opencvdemo.secondsight.filters.curve;

/**
 * Created by xupingwei on 2017/6/23.
 * 该滤镜可增强图片的亮度，使得阴影效果趋于冷色（更加蓝），
 * 并使高光趋于暖色（黄色），使图像外观更加阳光、整洁。此类滤镜通常用于肖像拍摄
 */

public class PortraCurveFilter extends CurveFilter {
    public PortraCurveFilter() {
        super(
                new double[]{0, 23, 157, 255},  //vValIn
                new double[]{0, 20, 173, 255},  //vValOut
                new double[]{0, 69, 213, 255},  //rValIn
                new double[]{0, 69, 218, 255},  //rValOut
                new double[]{0, 52, 189, 255},  //gValIn
                new double[]{0, 47, 196, 255},  //gValOut
                new double[]{0, 41, 231, 255},  //bValIn
                new double[]{0, 46, 228, 255}   //bValOut
        );
    }
}
