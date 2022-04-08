package com.wjw.client;

import com.wjw.client.manager.AttachFuture;
import com.wjw.client.manager.AttachManager;
import com.wjw.storage.FileUploadRequest;
import com.wjw.storage.FileUploadResponse;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author wjw
 * @description: 客户端测试类
 * @title: StorageClient
 * @date 2022/4/1 14:15
 */
public class StorageClient {

    public static void main(String[] args) throws Exception {
        // file download
//        String path = "y5knpq.jpg";
//        FileDownloadRequest request = new FileDownloadRequest(path);
//        AttachFuture future = AttachManager.getInstance().sendRequest(request);
//        FileDownloadResponse response = (FileDownloadResponse) future.get();
//        FileOutputStream fos = new FileOutputStream("D:\\attach\\attach\\test.jpg");
//        fos.write(response.getFileBytes());
//        fos.close();

        // file upload
        String path = "C:\\Users\\wjw\\Desktop\\hotelQrCode.jpg";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        FileUploadRequest request = new FileUploadRequest(fis, "jpg", file.length(), false);
        AttachFuture future = AttachManager.getInstance().sendRequest(request);
        FileUploadResponse response = (FileUploadResponse) future.get();
        System.out.println(response.getPath());
    }
}
