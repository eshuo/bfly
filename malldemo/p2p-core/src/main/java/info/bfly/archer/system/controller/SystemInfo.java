package info.bfly.archer.system.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.APPLICATION)
public class SystemInfo implements Serializable {
    private static final long serialVersionUID = -6837235120728149607L;

    public static void main(String[] args) {
    }

    @Log
    Logger log;
    @Resource
    HibernateTemplate ht;
    private String  javaVersion;
    /**
     * 操作系统信息
     */
    private String  os;
    /**
     * 服务器名称
     */
    private String  serverName;
    /**
     * 监听端口
     */
    private Integer serverPort;
    /**
     * 当前目录
     */
    private String  currentDir;

    public String getCurrentDir() {
        if (currentDir == null) {
            currentDir = FacesUtil.getAppRealPath();
        }
        return currentDir;
    }

    public String getJavaVersion() {
        if (javaVersion == null) {
            javaVersion = System.getProperty("java.version");
        }
        return javaVersion;
    }

    public String getOs() {
        if (os == null) {

            os = System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
        }
        return os;
    }

    public String getServerName() {
        if (serverName == null) {
            serverName = FacesUtil.getHttpSession().getServletContext().getServerInfo();
        }
        return serverName;
    }

    public int getServerPort() {
        if (serverPort == null) {
            serverPort = FacesUtil.getHttpServletRequest().getLocalPort();
        }
        return serverPort;
    }

    /**
     * 总内存(MB)
     */
    public Long getTotalRam() {
        return Runtime.getRuntime().totalMemory() / 1024 / 1024;
    }

    /**
     * 可用内存(MB)
     */
    public Long getUsedRam() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
    }
}
