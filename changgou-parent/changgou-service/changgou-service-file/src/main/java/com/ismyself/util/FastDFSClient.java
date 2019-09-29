package com.ismyself.util;

import com.ismyself.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * package com.ismyself.util;
 *
 * @auther txw
 * @create 2019-08-27  16:18
 * @description：
 */
public class FastDFSClient {

    //加载配置文件
    static {
        try {
            //获取tracker的配置文件fdfs_client.conf的位置
            String path = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StorageClient getStorageClient() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 文件上传
     *
     * @param file ：要上传的文件信息封装
     * @return String[]
     * 1：文件上传所存储的组名
     * 2：文件存储路径
     */
    public static String[] upload(FastDFSFile file) throws Exception {
        //获取文件作者
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair(file.getAuthor());

        //获取StorageClient对象
        StorageClient storageClient = getStorageClient();
        /**
         * 文件上传后的返回值
         * uploadResults[0]:文件上传所存储的组名，例如:group1
         * uploadResults[1]:文件存储路径,例如：M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
         * 执行文件上传
         */
        String[] uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        return uploadResults;
    }

    /**
     * 文件下载
     *
     * @param groupName      组名
     * @param remoteFileName 文件完整名（包含路径）
     * @return
     * @throws Exception
     */
    public static InputStream download(String groupName, String remoteFileName) throws Exception {
        //获取StorageClient对象
        StorageClient storageClient = getStorageClient();
        //下载文件，得到字节数组
        byte[] fileBytes = storageClient.download_file(groupName, remoteFileName);
        //返回一个字节数组输入流，到内存中
        return new ByteArrayInputStream(fileBytes);
    }

    /**
     * 文件删除
     *
     * @param groupName      组名
     * @param remoteFileName 文件完整名（包含路径）
     * @return 0 for success, none zero for fail (error code)
     * @throws Exception
     */
    public static int deleteFile(String groupName, String remoteFileName) throws Exception {
        StorageClient storageClient = getStorageClient();
        int code = storageClient.delete_file(groupName, remoteFileName);
        return code;
    }

    /**
     * 获取StorageServer信息
     *
     * @param groupName
     * @return
     * @throws Exception
     */
    public static StorageServer getStorageServer(String groupName) throws Exception {
        //创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageServer对象
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer, groupName);
        return storeStorage;
    }

    /**
     * 根据文件组名和文件存储路径获取Storage服务的IP、端口信息
     *
     * @param groupName      :组名
     * @param remoteFileName ：文件存储完整名
     * @return
     * @throws Exception
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    /**
     * 获取TrackerServer服务信息
     *
     * @throws Exception
     */
    public static TrackerServer getTrackerServer() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }


    public static void main(String[] args) throws Exception {
/*        String groupName = "group1";
        String fileName = "M00/00/00/wKjThF1iWc-AYd_cAAAgRqSstX4261.png";
        InputStream is = FastDFSClient.download(groupName, fileName);
        FileOutputStream fos = new FileOutputStream(new File("e:/1.png"));
        byte[] b = new byte[8192];
        int len;
        while ((len = is.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        fos.flush();
        fos.close();
        is.close();*/


        String groupName = "group1";
        StorageServer storageServer = FastDFSClient.getStorageServer(groupName);
        //为StorageServer的组的的个数
        System.out.println(storageServer.getStorePathIndex());
    }
}
