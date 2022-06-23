package info.bfly.api.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AppConstants {
    private static Properties props;
    /**返回信息 response messages*/

    public static Properties rMsgProps;
    static {
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("app.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("app.properties文件", e);
        } catch (IOException e) {
            throw new RuntimeException("app.properties文件出错", e);
        }
        rMsgProps = new Properties();
        try {
            rMsgProps.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("responseMessages.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("responseMessages.properties文件", e);
        } catch (IOException e) {
            throw new RuntimeException("responseMessages.properties文件出错", e);
        }
    }

    public static final class Config {
        public static final String RSA_BASE64_PUBLIC_KEY = props.getProperty("rsa_base64_public_key");
        //TODO 获取方式需要修改，不能写成静态变量
        public static final String RSA_BASE64_PRIVATE_KEY = props.getProperty("rsa_base64_private_key");
        public static final String SYSTEM_ERROR_JSON = "{}";
    }
}
