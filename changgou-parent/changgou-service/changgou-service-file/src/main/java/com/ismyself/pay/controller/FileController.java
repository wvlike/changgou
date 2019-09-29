package com.ismyself.pay.controller;

import com.ismyself.file.FastDFSFile;
import com.ismyself.util.FastDFSClient;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * package com.ismyself.pay.controller;
 *
 * @auther txw
 * @create 2019-08-25  17:19
 * @description：
 */
@RestController
@CrossOrigin
public class FileController {

    /**
     * 文件上传
     * Content-Type=multipart/form-data
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {

        //封装一个FastDFSFile
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), //文件名字
                file.getBytes(),    //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename())  //文件扩展名
        );
        //文件上传
        String[] uploadMsgs = FastDFSClient.upload(fastDFSFile);
        for (String msg : uploadMsgs) {
            System.out.println(msg);
        }
        //组装文件上传地址
        return "http://192.168.211.132:8080/" + uploadMsgs[0] + "/" + uploadMsgs[1];
    }

    /**
     * 下载文件
     *  {
     * 	"groupName" : "group1",
     * 	"fileName" : "M00/00/00/wKjThF1iWc-AYd_cAAAgRqSstX4261.png"
     * }
     * Content-Type=application/json
     * @param response
     * @throws Exception
     */
    @PostMapping("/download")
    public void download(HttpServletResponse response, @RequestBody Map map) throws Exception {
        String groupName = "group1";
        String fileName = "M00/00/00/wKjThF1iWc-AYd_cAAAgRqSstX4261.png";
        groupName = (String) map.get("groupName");
        fileName = (String) map.get("fileName");

        InputStream is = FastDFSClient.download(groupName, fileName);
        //uuidname
        fileName = UUID.randomUUID().toString().replace("-", "") + ".png";
        ServletOutputStream os = response.getOutputStream();
        //设置响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] b = new byte[8192];
        int len;
        while ((len = is.read(b)) != -1) {
            os.write(b, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    /**
     * 删除文件
     *
     * @return 信号0成功，其他失败
     */
    @DeleteMapping("/delete")
    public String deleteFile(@RequestBody Map map) throws Exception {
        String groupName = "group1";
        String fileName = "M00/00/00/wKjThF1iWc-AYd_cAAAgRqSstX4261.png";
        groupName = (String) map.get("groupName");
        fileName = (String) map.get("fileName");
        int code = FastDFSClient.deleteFile(groupName, fileName);
        return String.valueOf(code);
    }

    /**
     * 获取StorageServer的组的的个数
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/storage")
    public String getStorageServer() throws Exception {
        String groupName = "group1";
        StorageServer storageServer = FastDFSClient.getStorageServer(groupName);
        //为StorageServer的组的的个数
        int storePathIndex = storageServer.getStorePathIndex();
        return String.valueOf(storePathIndex);
    }

    /**
     * 获取serverInfo
     *
     * @throws Exception
     */
    @GetMapping("/serverInfo")
    public void getServerInfo() throws Exception {
        String groupName = "group1";
        String fileName = "M00/00/00/wKjThF1iWc-AYd_cAAAgRqSstX4261.png";
        ServerInfo[] serverInfos = FastDFSClient.getServerInfo(groupName, fileName);
        for (ServerInfo info : serverInfos) {
            System.out.println(info.getIpAddr());
            System.out.println(info.getPort());
        }
    }

    /**
     * 获取TrackerServer的信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/tracker")
    public String getTrackerServer() throws Exception {
        TrackerServer trackerServer = FastDFSClient.getTrackerServer();
        String hostString = trackerServer.getInetSocketAddress().getHostString();
        int port = trackerServer.getInetSocketAddress().getPort();
        return hostString + ":" + port;
    }
}
