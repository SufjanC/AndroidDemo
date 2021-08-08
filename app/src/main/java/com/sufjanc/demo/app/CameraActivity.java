package com.sufjanc.demo.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sufjanc.demo.R;
import com.sufjanc.demo.utils.LogUtil;
import com.sufjanc.demo.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    // TODO: 1.拍照后的照片存储
    // TODO: 2.录制视频，保存后播放

    private TextureView textureView = null;
    private Button btnCapture, btnVideo;
    private CameraManager cameraManager = null;
    private CameraDevice cameraDevice = null;
    private CameraCaptureSession cameraCaptureSession = null;

    private Surface surface = null;
    private CaptureRequest.Builder builder = null;
    private CaptureRequest captureRequest = null;
    private ImageReader imageReader = null;

    private Size previewSize = new Size(1080, 720);//预览数据大小 1080 × 720
    private Size captureSize = new Size(1080, 720); //拍照数据图像大小 1080 × 720

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            LogUtil.logi("SurfaceTextureListener", "onSurfaceTextureAvailable");
            //获取CameraManager服务
            cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            try {
                //打开主摄(CameraID, CameraDevice.StateCallback, Handler)
                cameraManager.openCamera("0", stateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            LogUtil.logi("SurfaceTextureListener", "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            LogUtil.logi("SurfaceTextureListener", "onSurfaceTextureDestroyed");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
            LogUtil.logi("SurfaceTextureListener", "onSurfaceTextureUpdated");
        }
    };

    //设备回调
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            if(camera != null){

            }
            //相机开启后调用此回调，获取相机设备
            LogUtil.logi("CameraDevice.StateCallback", "onOpened");
            cameraDevice = camera;
            //开始预览
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            //断开连接时关闭相机
            LogUtil.logi("CameraDevice.StateCallback", "onDisconnected");
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            //发生错误时，关闭相机
            LogUtil.logi("CameraDevice.StateCallback", "onError");
            camera.close();
            cameraDevice = null;
        }
    };


    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            LogUtil.logi("CameraCaptureSession.CaptureCallback", "onCaptureStarted");
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            LogUtil.logi("CameraCaptureSession.CaptureCallback", "onCaptureProgressed");
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            LogUtil.logi("CameraCaptureSession.CaptureCallback", "onCaptureCompleted");
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            LogUtil.logi("CameraCaptureSession.CaptureCallback", "onCaptureFailed");
            super.onCaptureFailed(session, request, failure);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showToast(CameraActivity.this, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        textureView.setSurfaceTextureListener(textureListener);
        imageReader = ImageReader.newInstance(captureSize.getWidth(), captureSize.getHeight(), ImageFormat.JPEG,2);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                LogUtil.logi("OnImageAvailableListener", "onImageAvailable");
                Image image = reader.acquireNextImage();
                ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                Bitmap bitmap = Bitmap.createBitmap(captureSize.getWidth(), captureSize.getHeight(), Bitmap.Config.ARGB_8888);
                byteBuffer.position(0);
                bitmap.copyPixelsFromBuffer(byteBuffer);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/Camera/pic.jpeg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                image.close();
            }
        }, null);
    }

    private void initView(){
        setContentView(R.layout.activity_camera);
        textureView = findViewById(R.id.texture);
        btnCapture = findViewById(R.id.btn_camera_capture);
        btnVideo = findViewById(R.id.btn_camera_video);

        btnCapture.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera_capture:
                takePicture();
                break;
        }
    }

    private void startPreview(){
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        surface = new Surface(surfaceTexture);
//        SessionConfiguration sessionConfiguration =
        try {
            cameraDevice.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onConfigured");
                    try {
                        builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        builder.addTarget(surface);
                        captureRequest = builder.build();
                        cameraCaptureSession = session;
                        cameraCaptureSession.setRepeatingRequest(captureRequest, captureCallback, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onConfigureFailed");
                }

                @Override
                public void onReady(@NonNull CameraCaptureSession session) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onReady");
                    super.onReady(session);
                }

                @Override
                public void onActive(@NonNull CameraCaptureSession session) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onActive");
                    super.onActive(session);
                }

                @Override
                public void onClosed(@NonNull CameraCaptureSession session) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onClosed");
                    super.onClosed(session);
                }

                @Override
                public void onSurfacePrepared(@NonNull CameraCaptureSession session, @NonNull Surface surface) {
                    LogUtil.logi("cameraDevice.createCaptureSession", "onSurfacePrepared");
                    super.onSurfacePrepared(session, surface);
                }
            }
            , null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void takePicture(){
        try {
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(imageReader.getSurface());
            captureRequest = builder.build();
            cameraCaptureSession.capture(captureRequest, new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    ToastUtil.showToast(CameraActivity.this, "Picture capture successful!");
                    super.onCaptureCompleted(session, request, result);
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String doByte2JpegFile(byte[]... jpeg) {
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/", "photo-test.jpg");

        if (photo.exists()) {
            photo.delete();
        } else {
        }

        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(jpeg[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return(null);
    }
}