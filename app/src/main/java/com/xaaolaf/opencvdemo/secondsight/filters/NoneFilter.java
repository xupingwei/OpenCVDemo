package com.xaaolaf.opencvdemo.secondsight.filters;

import org.opencv.core.Mat;

/**
 * Created by xupingwei on 2017/6/23.
 * 当需要关闭滤镜，且仍需要某一对象遵守Filter规则时，可使用这个方法
 */

public class NoneFilter implements Filter {
    @Override
    public void apply(Mat src, Mat dst) {

    }
}
