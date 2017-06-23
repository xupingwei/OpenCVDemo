package com.xaaolaf.opencvdemo.secondsight.filters.convolution;

import com.xaaolaf.opencvdemo.secondsight.filters.Filter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

/**
 * Created by xupingwei on 2017/6/23.
 * 黑色轮廓边效果
 */
//滤镜将于上方边区域绘制黑色直线
public class StrokeEdgedFilter implements Filter {


    private Mat mKernel = new MatOfInt(
            0, 0, 10, 0, 0,
            0, 1, 2, 1, 0,
            1, 2, -16, 2, 1,
            0, 1, 2, 1, 0,
            0, 0, 1, 0, 0
    );

    private Mat mEdges = new Mat();

    @Override
    public void apply(Mat src, Mat dst) {
        Imgproc.filter2D(src, mEdges, -1, mKernel);
        Core.bitwise_not(mEdges, mEdges);
        Core.multiply(src, mEdges, dst, 1.0 / 255.0);
    }
}
