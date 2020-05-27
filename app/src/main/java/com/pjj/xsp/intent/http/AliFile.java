package com.pjj.xsp.intent.http;


import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.pjj.xsp.PjjApplication;
import com.pjj.xsp.manage.XSPSystem;
import com.pjj.xsp.module.bean.AccessKeyBean;
import com.pjj.xsp.utils.FileUtils;
import com.pjj.xsp.utils.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XinHeng on 2018/12/19.
 * describe：
 */
public class AliFile {
    private static AliFile INSTANCE;
    private OSS oss;
    private OSSAsyncTask ossAsyncTask;
    private long timeOld;
    private String filePath;
    private String[] filePaths;
    private Map<String, String> fileResultMap = new HashMap<>();
    private int tag;
    private String bucketName;
    private String fileName;

    public static AliFile getInstance() {
        if (null == INSTANCE) {
            synchronized (AliFile.class) {
                if (null == INSTANCE) {
                    INSTANCE = new AliFile();
                }
            }
        }
        return INSTANCE;
    }

    private AliFile() {
    }

    public OSSAsyncTask getOssAsyncTask() {
        return ossAsyncTask;
    }

    public String getFilePath() {
        return filePath;
    }

    private OSS initOss(String accessKeyId, String secretKeyId, String securityToken) {
        timeOld = System.currentTimeMillis();
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, secretKeyId, securityToken);
        return new OSSClient(PjjApplication.application, endpoint, credentialProvider);
    }

    public void uploadFile(String fileName, String uploadFilePath, UploadResult uploadResult) {
        this.fileName = fileName;
        filePath = uploadFilePath;
        if (null == oss || System.currentTimeMillis() - timeOld > 3500 * 1000) {
            reGetPamars(uploadResult);
            return;
        }
        uploadFile_(uploadFilePath, uploadResult);
    }

    public void uploadFile(String uploadFilePath, UploadResult uploadResult) {
        this.fileName = null;
        filePath = uploadFilePath;
        if (null == oss || System.currentTimeMillis() - timeOld > 3500 * 1000) {
            reGetPamars(uploadResult);
            return;
        }
        uploadFile_(uploadFilePath, uploadResult);
    }

    public void uploadFile(UploadResult uploadResult, String[] uploadFilePaths) {
        fileResultMap.clear();
        this.fileName = null;
        tag = 0;
        filePath = null;
        filePaths = uploadFilePaths;
        if (null == uploadFilePaths || uploadFilePaths.length == 0) {
            uploadResult.fail("空文件");
            return;
        }
        if (null == oss || System.currentTimeMillis() - timeOld > 3500 * 1000) {
            reGetPamars(uploadResult);
            return;
        }
        for (int i = 0; i < uploadFilePaths.length; i++) {
            uploadFile_array(uploadFilePaths[i], uploadResult);
        }
    }

    private void uploadFile_(String uploadFilePath, UploadResult uploadResult) {
        /*String contentMD5 = null;
        try {
            contentMD5 = BinaryUtil.calculateBase64Md5(uploadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(contentMD5)) {
            Log.e("TAG", "uploadFile_: 获取 md5 失败");
            uploadResult.fail("获取 md5 失败");
            return;
        }*/
        String fileMD5 = FileUtils.getFileMD5(uploadFilePath);
        if (TextUtils.isEmpty(fileMD5)) {
            Log.e("TAG", "uploadFile_: 获取 md5 失败");
            uploadResult.fail("获取 md5 失败");
            return;
        }

        // 构造上传请求
        String fileName;
        if (null != this.fileName) {
            fileName = this.fileName;
        } else {
            int index = uploadFilePath.lastIndexOf(".");
            String foot = uploadFilePath.substring(index);
            fileName = fileMD5 + foot;
        }
        Log.e("TAG", "uploadFile_: fileName=" + fileName);
        PutObjectRequest put = new PutObjectRequest(bucketName, fileName, uploadFilePath);
        // 文件元信息的设置是可选的
        ObjectMetadata metadata = new ObjectMetadata();
        //metadata.setContentType("application/octet-stream"); // 设置content-type
        Log.e("TAG", "uploadFile_: contentMD5=" + ", fileMD5=" + fileMD5);
        //metadata.setContentMD5(contentMD5); // 校验MD5
        put.setMetadata(metadata);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //android.util.Log.e("TAG", "onProgress: " + (Looper.myLooper() == Looper.getMainLooper()));
                //Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                uploadResult.uploadProgress(currentSize, totalSize);
            }
        });

        ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //Log.e("PutObject", "UploadSuccess");
                //Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId() + fileName);
                uploadResult.success(fileName);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                uploadResult.fail("服务器异常");
            }
        });

        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待任务完成
    }

    private void reGetPamars(UploadResult uploadResult) {
        RetrofitService.getInstance().loadAccessKeyTask(XSPSystem.getInstance().getOnlyCode(), new RetrofitService.CallbackClassResult<AccessKeyBean>(AccessKeyBean.class) {

            @Override
            protected void resultSuccess(AccessKeyBean accessKeyBean) {
                AccessKeyBean.Cipher data = accessKeyBean.getData();
                new Thread() {
                    @Override
                    public void run() {
                        getAccessKeyMingWen(data, uploadResult);
                    }
                }.start();
            }

            @Override
            protected void fail(String error) {
                Log.e("TAG", "fail: " + error);
            }
        });
    }

    /**
     * 获取明文
     */
    private void getAccessKeyMingWen(AccessKeyBean.Cipher cipher, UploadResult uploadResult) {
        bucketName = cipher.getBucketName();
        if (!TextUtils.isEmpty(bucketName)) {
            oss = initOss(cipher.getAccessKeyId(), cipher.getAccessKeySecret(), cipher.getSecurityToken());
            if (null == filePath) {
                for (int i = 0; i < filePaths.length; i++) {
                    uploadFile_array(filePaths[i], uploadResult);
                }
            } else {
                uploadFile_(filePath, uploadResult);
            }
        } else {
            //Log.e("TAG", "successResult: 明文信息错误");
            uploadResult.fail("明文信息错误");
        }
    }

    private void uploadFile_array(String uploadFilePath, UploadResult uploadResult) {
        String contentMD5 = null;
        try {
            contentMD5 = BinaryUtil.calculateBase64Md5(uploadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(contentMD5)) {
            Log.e("TAG", "uploadFile_: 获取 md5 失败");
            uploadResult.fail("获取 md5 失败");
            return;
        }
        String fileMD5 = FileUtils.getFileMD5(uploadFilePath);
        if (TextUtils.isEmpty(fileMD5)) {
            Log.e("TAG", "uploadFile_: 获取 md5 失败");
            uploadResult.fail("获取 md5 失败");
            return;
        }
        int index = uploadFilePath.lastIndexOf(".");
        String foot = uploadFilePath.substring(index);
        // 构造上传请求
        String fileName = fileMD5 + foot;
        Log.e("TAG", "uploadFile_: fileName=" + fileName);
        PutObjectRequest put = new PutObjectRequest(bucketName, fileName, uploadFilePath);
        // 文件元信息的设置是可选的
        ObjectMetadata metadata = new ObjectMetadata();
        //metadata.setContentType("application/octet-stream"); // 设置content-type
        metadata.setContentMD5(contentMD5); // 校验MD5
        put.setMetadata(metadata);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //Log.e("PutObject", "UploadSuccess");
                //Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId() + ", " + fileName);
                //uploadResult.success(fileName);
                //fileResultList.add(fileName);
                fileResultMap.put(uploadFilePath, fileName);
                ++tag;
                checkResult(uploadResult);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                //uploadResult.fail("服务器异常");
                Log.e("RequestId", "onFailure" + ", " + fileName);
                ++tag;
                checkResult(uploadResult);
            }
        });

        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待任务完成
    }

    private void checkResult(UploadResult uploadResult) {
        Log.e("TAG", "tag=" + tag + ", " + filePaths.length);
        if (tag >= filePaths.length) {
            if (fileResultMap.size() == filePaths.length) {
                uploadResult.successMap(fileResultMap);
            } else {
                uploadResult.fail("上传失败");
            }
        }
    }

    public static class UploadResult {
        protected void success(String result) {
            Log.e("TAG", "AliFile success: " + result);
        }

        protected void successMap(Map<String, String> map) {
        }

        protected void uploadProgress(long currentSize, long totalSize) {
        }

        protected void fail(String error) {
            Log.e("TAG", "fail: " + error);
        }
    }
}
