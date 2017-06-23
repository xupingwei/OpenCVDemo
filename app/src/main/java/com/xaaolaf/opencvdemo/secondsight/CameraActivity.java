package com.xaaolaf.opencvdemo.secondsight;

import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xaaolaf.opencvdemo.R;
import com.xaaolaf.opencvdemo.secondsight.filters.Filter;
import com.xaaolaf.opencvdemo.secondsight.filters.NoneFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.convolution.StrokeEdgedFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.CrossProcessCurveFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.PortraCurveFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.ProviaCurveFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.RecolorCMVFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.RecolorRCFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.RecolorRGVFilter;
import com.xaaolaf.opencvdemo.secondsight.filters.curve.VelviaCurveFilter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * Created by xupingwei on 2017/6/23.
 */

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {


    private static final String TAG = "CameraActivity";
    private static final String STATE_CAMERA_INDEX = "cameraindex";
    private int mCameraIndex;
    /**
     * 相机活动是否可见，如果可见，相机view应该被镜像
     */
    private boolean mIsCameraFrontFacing;

    private int mNumCameras;  //设备上的相机数量

    /**
     * 照片获取的下一帧是否作为图片保存
     */
    private boolean mIsPhotoPending;
    /**
     * 保存图片的时候被使用
     */
    private Mat mBgr;

    private boolean mIsMenuLocked;

    private CameraBridgeViewBase mCameraView;


    //滤镜操作
    private static final String STATE_CURVE_FILTER_INDEX = "curveFilterIndex";
    private static final String STATE_MIXER_FILTER_INDEX = "curveFilterIndex";
    private static final String STATE_CONVOLUTION_FILTER_INDEX = "convolutionFilterIndex";

    private Filter[] mCurveFilters;
    private Filter[] mMixerFilters;
    private Filter[] mConvolutionFilters;

    private int mCurveFilterIndex;
    private int mMixerFilterIndex;
    private int mConvolitionFilterIndex;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.e(TAG, "OpenCV loaded successfully");
                    mCameraView.enableView();
                    mBgr = new Mat();
                    mCurveFilters = new Filter[]{
                            new NoneFilter(),
                            new PortraCurveFilter(),
                            new ProviaCurveFilter(),
                            new VelviaCurveFilter(),
                            new CrossProcessCurveFilter()
                    };
                    mMixerFilters = new Filter[]{
                            new NoneFilter(),
                            new RecolorRCFilter(),
                            new RecolorRGVFilter(),
                            new RecolorCMVFilter()
                    };
                    mConvolutionFilters = new Filter[]{
                            new NoneFilter(),
                            new StrokeEdgedFilter()
                    };
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (null != savedInstanceState) {
            mCameraIndex = savedInstanceState.getInt(STATE_CAMERA_INDEX, 0);
            mCurveFilterIndex = savedInstanceState.getInt(STATE_CURVE_FILTER_INDEX, 0);
            mMixerFilterIndex = savedInstanceState.getInt(STATE_MIXER_FILTER_INDEX, 0);
            mConvolitionFilterIndex = savedInstanceState.getInt(STATE_CONVOLUTION_FILTER_INDEX, 0);
        } else {
            mCameraIndex = 0;
            mCurveFilterIndex = 0;
            mMixerFilterIndex = 0;
            mConvolitionFilterIndex = 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraIndex, cameraInfo);
            mIsCameraFrontFacing = (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
            mNumCameras = Camera.getNumberOfCameras();
        } else {
            mIsCameraFrontFacing = false;
            mNumCameras = 1;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.javaCameraView);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CAMERA_INDEX, mCameraIndex);
        outState.putInt(STATE_CURVE_FILTER_INDEX, mCurveFilterIndex);
        outState.putInt(STATE_MIXER_FILTER_INDEX, mMixerFilterIndex);
        outState.putInt(STATE_CONVOLUTION_FILTER_INDEX, mConvolitionFilterIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        mIsMenuLocked = false;
    }

    @Override
    protected void onPause() {
        if (null != mCameraView) {
            mCameraView.disableView();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != mCameraView) {
            mCameraView.disableView();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera, menu);
        if (mNumCameras < 2) {
            menu.removeItem(R.id.menu_next_camera);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mIsMenuLocked) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_next_curve_filter:
                mCurveFilterIndex++;
                if (mCurveFilterIndex == mCurveFilters.length) {
                }
                return true;
            case R.id.menu_next_mixer_filter:
                mMixerFilterIndex++;
                if (mMixerFilterIndex == mMixerFilters.length) {
                    mMixerFilterIndex = 0;
                }
                return true;
            case R.id.menu_next_convolution_filter:
                mConvolitionFilterIndex++;
                if (mConvolitionFilterIndex == mConvolutionFilters.length) {
                    mConvolitionFilterIndex = 0;
                }
                return true;
            case R.id.menu_next_camera:
                mIsMenuLocked = true;
                mCameraIndex++;
                if (mCameraIndex == mNumCameras) {
                    mCameraIndex = 0;
                }
                recreate();
                return true;
            case R.id.menu_take_photo:
                mIsMenuLocked = true;
                mIsPhotoPending = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        mCurveFilters[mCurveFilterIndex].apply(rgba, rgba);
        mMixerFilters[mMixerFilterIndex].apply(rgba, rgba);
        mConvolutionFilters[mConvolitionFilterIndex].apply(rgba, rgba);
        if (mIsPhotoPending) {
            mIsPhotoPending = false;
            takePhoto(rgba);
        }
        if (mIsCameraFrontFacing) {
            /**
             * d对图片执行镜像操作，参数为：数据源Mat，目标Mat，以及一个整数。
             * 其中整数表示翻转模式：0:垂直方向，1：水平方向，-1：二者兼顾
             */
            Core.flip(rgba, rgba, 1);
        }
        return rgba;
    }

    private void takePhoto(Mat rgba) {
        final long currentTimeMillis = System.currentTimeMillis();
        final String appName = getString(R.string.app_name);
        final String galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        final String albumPath = galleryPath + "/" + appName;
        final String photoPath = albumPath + "/" + currentTimeMillis + ".png";
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, photoPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, LabActivity.PHOTO_MIME_TYPE);
        values.put(MediaStore.Images.Media.DESCRIPTION, appName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);

        File album = new File(albumPath);
        if (!album.isDirectory() && !album.mkdir()) {
            Log.e(TAG, "Failed to create album directory at " + albumPath);
            onTakePhotoFailed();
            return;
        }

        Imgproc.cvtColor(rgba, mBgr, Imgproc.COLOR_RGB2BGR, 3);
        if (!Imgcodecs.imwrite(photoPath, mBgr)) {
            Log.e(TAG, "Failed to save photo to  " + photoPath);
            onTakePhotoFailed();
        }

        Uri uri;
        try {
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (final Exception e) {
            Log.e(TAG, "Failed to insert photo into MediaStore");
            e.printStackTrace();

            File photo = new File(photoPath);
            if (!photo.delete()) {
                Log.e(TAG, "Failed to delete non-inserted photo");
            }
            onTakePhotoFailed();
            return;
        }
        Intent intent = new Intent(this, LabActivity.class);
        intent.putExtra(LabActivity.EXTRA＿PHOTO_URI, uri);
        intent.putExtra(LabActivity.EXTRA_PHOTO_DATA_PATH, photoPath);
        startActivity(intent);
    }

    private void onTakePhotoFailed() {
        mIsMenuLocked = false;
        final String errorMessage = getString(R.string.photo_error_message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CameraActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
