package com.xaaolaf.opencvdemo.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xaaolaf.opencvdemo.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by xupingwei on 2017/6/26.
 * 图像灰度化
 */

public class GrayImagesActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnGray;
    private boolean isGray = false;

    private Bitmap srcBitmap;
    private Bitmap grayBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gray);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        btnGray = (Button) findViewById(R.id.btn_gray);
        btnGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGray) {
                    processSrc2Gray();
                    isGray = true;
                } else {
                    processGray2Src();
                    isGray = false;
                }
            }
        });
    }

    private void processGray2Src() {
        ivImage.setImageBitmap(srcBitmap);
        btnGray.setText("灰度化");
    }

    private void processSrc2Gray() {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.church_at_auvers_sur_oise);
        grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(grayMat, grayBitmap);
        ivImage.setImageBitmap(grayBitmap);
        btnGray.setText("还原图像");
    }
}
