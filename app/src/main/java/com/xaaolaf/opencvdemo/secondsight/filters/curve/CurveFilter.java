package com.xaaolaf.opencvdemo.secondsight.filters.curve;

import com.xaaolaf.opencvdemo.secondsight.filters.Filter;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;

/**
 * Created by xupingwei on 2017/6/23.
 */
//该类所体现的滤镜可向图像中的个颜色通道应用独立的曲线转换
public class CurveFilter implements Filter {
    //查找表
    private final Mat mLUT = new MatOfInt();

    public CurveFilter(
            double[] vValIn, double[] vValOut,
            double[] rValIn, double[] rValOut,
            double[] gValIn, double[] gValOut,
            double[] bValIn, double[] bValOut) {
        UnivariateFunction vFunc = newFunc(vValIn, vValOut);
        UnivariateFunction rFunc = newFunc(rValIn, rValOut);
        UnivariateFunction gFunc = newFunc(gValIn, gValOut);
        UnivariateFunction bFunc = newFunc(bValIn, bValOut);
        mLUT.create(256, 1, CvType.CV_8UC4);
        //创建一个计算好的查询表
        for (int i = 0; i < 256; i++) {
            double v = vFunc.value(i);
            double r = rFunc.value(v);
            double g = gFunc.value(v);
            double b = bFunc.value(v);
            mLUT.put(i, 0, r, g, b, i);
        }
    }

    @Override
    public void apply(Mat src, Mat dst) {
        Core.LUT(src, mLUT, dst);
    }


    private UnivariateFunction newFunc(double[] valIn, double[] valOut) {
        UnivariateInterpolator interpolator;
        if (valIn.length > 2) {
            interpolator = new SplineInterpolator();
        } else {
            interpolator = new LinearInterpolator();
        }
        return interpolator.interpolate(valIn, valOut);
    }
}
