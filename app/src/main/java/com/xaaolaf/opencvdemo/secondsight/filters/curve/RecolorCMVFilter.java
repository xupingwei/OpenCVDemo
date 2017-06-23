package com.xaaolaf.opencvdemo.secondsight.filters.curve;

import com.xaaolaf.opencvdemo.secondsight.filters.Filter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupingwei on 2017/6/23.
 */

public class RecolorCMVFilter implements Filter {
    private List<Mat> mChannels = new ArrayList<>(4);

    @Override
    public void apply(Mat src, Mat dst) {
        Core.split(src, mChannels);
        Mat r = mChannels.get(0);
        Mat g = mChannels.get(1);
        Mat b = mChannels.get(2);
        Core.max(b, r, g);
        Core.max(b, g, b);
        mChannels.set(2, b);
        Core.merge(mChannels, dst);
    }
}
