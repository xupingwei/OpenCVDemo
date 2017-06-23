package com.xaaolaf.opencvdemo.secondsight.filters.curve;

/**
 * Created by xupingwei on 2017/6/23.
 * 该效果体在线了阴影中较为强烈的蓝。绿色调，以及高光中的黄、绿色调，
 * 用于生成模特、明星等不修边幅的外观
 */

public class CrossProcessCurveFilter extends CurveFilter {

    public CrossProcessCurveFilter() {
        super(
                new double[]{0, 255},  //vValIn
                new double[]{0, 255},  //vValOut
                new double[]{0, 56, 211, 255},  //rValIn
                new double[]{0, 22, 255, 255},  //rValOut
                new double[]{0, 56, 208, 255},  //gValIn
                new double[]{0, 39, 226, 255},  //gValOut
                new double[]{0, 255},  //bValIn
                new double[]{20, 235}   //bValOut
        );
    }
}
