package info.bfly.archer.config.service;

/**
 *
 *
 * Description:
 */
public interface ConfigService {
    /**
     * 通过配置编号得到配置的值
     *
     * @param configId
     * @return
     */
    String getConfigValue(String configId);

    /**
     * @param configId
     * @param defaultVlaue
     * @return
     */
    String getConfigValue(String configId, String defaultVlaue);
}
