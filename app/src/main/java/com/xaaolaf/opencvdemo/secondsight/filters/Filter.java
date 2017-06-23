package com.xaaolaf.opencvdemo.secondsight.filters;

import org.opencv.core.Mat;

/**
 * Created by xupingwei on 2017/6/23.
 */

public interface Filter {
    void apply(Mat src, Mat dst);
}
