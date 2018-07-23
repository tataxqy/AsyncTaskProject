package com.example.xian.asynctaskproject;

import java.io.File;

/**
 * Name: DownloadHelper
 *
 * @Author: xian
 * Comment: //TODO
 * Date: 2018-07-20 17:34
 */

public class DownloadHelper {

    public static void download(String url,String localPath,OnDownloadListener listener)
    {

    }

    public interface onDownloadListener{
        void onStart();
        void onSuccess(int code,File file);
        void onFail(int code,File fie,String message);
        void onProgress(int progress);
    }
}
