package info.bfly.pay.util;

import com.jcraft.jsch.*;
import info.bfly.core.annotations.Log;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by XXSun on 5/18/2017.
 */
@Service
public class SFTPUtil {

    @Log
    Logger log;


    @Value("#{refProperties['sinapay_sftp_address']}")
    private String host = "222.73.39.37";//sftp服务器ip

    @Value("#{refProperties['sinapay_sftp_Username']}")
    private String username = "200004595271";//用户名，即商户号

    @Value("#{refProperties['sinapay_sftp_privatekey']}")
    private Resource privateKey ;//密钥文件路径，联调环境密钥在网盘里key压缩包内
    @Value("#{refProperties['sinapay_sftp_port']}")
    private int      port       = 50022;//默认的sftp端口号是22

    @Value("#{refProperties['sinapay_sftp_passphrase']}")
    private String passphrase;//密钥口令

    @Value("#{refProperties['sinapay_sftp_password']}")
    private String      password;
    private ChannelSftp sftp;

    @Autowired
    private SinaUtils sinaUtils;

    /**
     * 获取连接
     *
     * @return channel
     */
    public ChannelSftp connectSFTP() {
        if (sftp == null) {
            initSession();
        }
        try {
            if (!sftp.getSession().isConnected()) {
                sftp.getSession().connect();
            }
            if (!sftp.isConnected()) {
                sftp.connect();
            }
            return sftp;
        } catch (JSchException e) {
            log.error("connect to Sftp failed {}", e);
            return null;
        }
    }

    private void initSession() {
        try {
            JSch jsch = new JSch();
            Session session;
            if (StringUtils.isEmpty(passphrase)) {
                jsch.addIdentity(privateKey.getFile().getPath());
            } else
                jsch.addIdentity(privateKey.getFile().getPath(), passphrase);
            session = jsch.getSession(username, host, port);
            if (StringUtils.isEmpty(password)) {
                session.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            session.setServerAliveInterval(92000);
            session.connect();
            //参数sftp指明要打开的连接是sftp连接
            Channel channel = session.openChannel("sftp");
            channel.connect();
            this.sftp = (ChannelSftp) channel;
            log.info("sftp connect success");
        } catch (JSchException | IOException e) {
            log.error("connect to Sftp failed {}", e);
        }
    }

    private void checkSession() throws JSchException {
        if (connectSFTP() == null) {
            throw new JSchException("sftp session is null");
        }
    }

    public boolean upload(String directory, String uploadFile) {
        return upload(directory, uploadFile,2);
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     */
    public boolean upload(String directory, String uploadFile, int retrytimes) {
        if (retrytimes < 0)
            return false;
        FileInputStream fileInputStream = null;
        try {

            checkSession();
            sftp.cd(directory);
            File file = new File(uploadFile);
            fileInputStream= new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
            log.info("sftp upload {} to {} success", uploadFile, directory);
            return true;

        } catch (Exception e) {
            log.error("sftp upload {} to {} failed {}", uploadFile, directory, e);
            retrytimes--;
            return upload(directory, uploadFile, retrytimes);
        }finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }


    public Vector ls(String directory) {
        try {
            checkSession();
            sftp.cd(directory);
            Vector v = sftp.ls("*.*");
            log.info("sftp ls {} with {} success", directory, v);
            return v;
        } catch (Exception e) {
            log.info("sftp ls {} fail", directory);
            return null;

        }

    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile,
                         String saveFile) {
        try {
            checkSession();
            sftp.cd(directory);
            sftp.get(downloadFile, saveFile);
            log.info("sftp get {}{} to {}  success", directory, downloadFile, saveFile);
        } catch (Exception e) {

            log.error("sftp get {}{} to {}  fail {}", directory, downloadFile, saveFile, e);
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
            checkSession();
            sftp.cd(directory);
            sftp.rm(deleteFile);
            log.info("sftp delete {}{} success", directory, deleteFile);
        } catch (Exception e) {

            log.error("sftp delete {}{} fail {}", directory, deleteFile, e);
        }
    }

    public void disconnected() {
        if (sftp != null) {
            try {
                checkSession();
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                log.error("sftp disconnect failed ");
            }
            sftp.disconnect();
        }
    }
}
