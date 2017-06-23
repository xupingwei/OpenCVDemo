package com.xaaolaf.opencvdemo.secondsight.filters.curve;

import com.xaaolaf.opencvdemo.secondsight.filters.Filter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;

/**
 * Created by xupingwei on 2017/6/23.
 */

/**
 * 该滤镜的效果是将绿色和蓝色转换为青色，并遗留有限的红色和青色调色板。该滤镜可模拟早起电影和计算机游戏的显示效果
 */
public class RecolorRCFilter implements Filter {

    private final ArrayList<Mat> mChannels = new ArrayList<>(4);

    @Override
    public void apply(Mat src, Mat dst) {
        //可将矩阵操作应用于独立通道上
        Core.split(src, mChannels);
        Mat g = mChannels.get(1);
        Mat b = mChannels.get(2);
        //计算两个通道的加权平均值，最后一个参数为目标矩阵
        Core.addWeighted(g, 0.5, b, 0.5, 0.0, g);
        mChannels.set(2, g);
        //可认为是Core.split方法的逆置方法，可根据独立通道重新生成多通道图像
        Core.merge(mChannels, dst);
    }
}
