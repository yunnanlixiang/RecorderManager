package com.mingyuechunqiu.recordermanagerproject;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.mingyuechunqiu.recordermanager.record.RecorderManager;
import com.mingyuechunqiu.recordermanager.record.RecorderManagerable;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView svVideo;

    private RecorderManagerable mManager;
    private Camera mCamera;
    private boolean isRecording;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        svVideo = findViewById(R.id.sv_video);
        AppCompatButton btnRecord = findViewById(R.id.btn_video_record);
        AppCompatButton btnStop = findViewById(R.id.btn_video_stop);
        AppCompatButton btnPlay = findViewById(R.id.btn_video_play);
        btnRecord.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        svVideo.getHolder().addCallback(this);
        svVideo.getHolder().setKeepScreenOn(true);
//        svVideo.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_video_record:
                recordVideos();
                break;
            case R.id.btn_video_stop:
                stopRecordVideo();
                break;
            case R.id.btn_video_play:

                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mManager == null) {
            mManager = RecorderManager.newInstance();
        }
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mManager.releaseCamera(mCamera);
        mCamera = null;
    }

    private void startPreview() {
        if (mCamera == null) {
            mCamera = mManager.initCamera();
        }
        try {
            mCamera.setPreviewDisplay(svVideo.getHolder());
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordVideos() {
        Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
        isRecording = mManager.recordVideo(mCamera, svVideo.getHolder().getSurface(),
                getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() +
                        File.separator + "test.mp4");
    }

    private void stopRecordVideo() {
        if (isRecording && mManager != null) {
            Toast.makeText(this, "录制结束", Toast.LENGTH_SHORT).show();
            mManager.release();
            mManager.releaseCamera(mCamera);
            mCamera = null;
        }
    }
}
