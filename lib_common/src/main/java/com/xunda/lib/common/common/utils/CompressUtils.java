package com.xunda.lib.common.common.utils;

import android.content.Context;

import com.nanchen.compresshelper.CompressHelper;

import java.io.File;

public class CompressUtils {

    public static File compressFile(int times, File oldFile, Context mContext) {
        L.e("原图的大小是"+FileUtil.getReadableFileSize(oldFile.length()));
        if (oldFile.length() >= times * 1024) {
            File newFile = new CompressHelper.Builder(mContext)
                    .setQuality(80)
                    .build()
                    .compressToFile(oldFile);
            L.e("压缩后的图的大小是"+FileUtil.getReadableFileSize(newFile.length()));
            return newFile;
        } else {
            return oldFile;
        }
    }
}
