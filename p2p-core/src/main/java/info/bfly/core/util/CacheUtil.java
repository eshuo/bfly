package info.bfly.core.util;

import net.sf.ehcache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheUtil {
    final static Logger log = LoggerFactory.getLogger(CacheUtil.class);
    private static CacheManager manager;
    private final static String CONFIG_FILE_PATH   = "ehcache.xml";
    private final static String DEFAULT_CACHE_NAME = "default_cache";

    static {
        try {
            CacheUtil.manager = CacheManager.getInstance();
            if (CacheUtil.manager == null) CacheUtil.manager = CacheManager.create(CacheUtil.CONFIG_FILE_PATH);
        } catch (CacheException e) {

            CacheUtil.log.error("Initialize cache manager failed.", e);
        }
    }

    /**
     * 清空指定缓存
     *
     * @param cacheName
     */
    public static void clear(String cacheName) {
        CacheUtil.getManager().getCache(cacheName).removeAll();
    }

    /**
     * 清除所有
     */
    public static void clearAll() {
        CacheUtil.getManager().clearAll();
    }

    /**
     * 按缺省配置创建缓存
     *
     * @param cacheName
     */
    public static void createCache(String cacheName) {
        CacheUtil.getManager().addCache(cacheName);
    }

    /**
     * 根据缓存名与key获取值
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key) {
        Ehcache cache = CacheUtil.getManager().getEhcache(cacheName);
        Element e = cache.get(key);
        return e == null ? null : e.getObjectValue();
    }

    /**
     * 获取指定名称的缓存
     *
     * @param arg0
     * @return
     * @throws IllegalStateException
     */
    public static Cache getCache(String name) throws IllegalStateException {
        if (name == null) {
            name = CacheUtil.DEFAULT_CACHE_NAME;
        }
        return CacheUtil.manager.getCache(name);
    }

    /**
     * 获取缓存名
     *
     * @return
     */
    public static String[] getCacheNames() {
        return CacheUtil.getManager().getCacheNames();
    }

    public static Element getElement(Object key) throws IllegalStateException, CacheException {
        return CacheUtil.getElement(null, key);
    }

    /**
     * 获取缓冲中的信息
     *
     * @param cache
     * @param key
     * @return
     * @throws IllegalStateException
     * @throws CacheException
     */
    public static Element getElement(String cache, Object key) {
        Cache cCache = CacheUtil.getCache(cache);
        return cCache.get(key);
    }

    /**
     * 获取缓存的Keys
     *
     * @param cacheName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getKeys(String cacheName) {
        Ehcache cache = CacheUtil.getManager().getEhcache(cacheName);
        return cache.getKeys();
    }

    public static CacheManager getManager() {
        return CacheUtil.manager;
    }

    /**
     * 获取缓存大小
     *
     * @param cacheName
     * @return
     */
    public static int getSize(String cacheName) {
        return CacheUtil.getManager().getCache(cacheName).getSize();
    }

    /**
     * 从默认的缓存配置中读取缓存的值 DEFAULT_CACHE_NAME = "default_cache";
     *
     * @param key
     * @return
     */
    public static Object getValue(Object key) {
        return CacheUtil.getValue(null, key);
    }

    public static Object getValue(String cache, Object key) {
        Element e = CacheUtil.getElement(cache, key);
        if (e == null) {
            return null;
        }
        return CacheUtil.getElement(cache, key).getObjectValue();
    }

    public static void main(String[] args) {
        CacheUtil.put("key", "123");
        System.out.println(CacheUtil.getValue("key"));
    }

    /**
     * 缓存对象放入默认的缓存中 DEFAULT_CACHE_NAME = "default_cache";
     *
     * @param key
     * @param value
     */
    public synchronized static void put(Object key, Object value) {
        CacheUtil.put(null, key, value);
    }

    /**
     * 把对象放入缓存中
     *
     * @param cache_name
     * @param key
     * @param value
     */
    public synchronized static void put(String cacheName, Object key, Object value) {
        Cache cache = CacheUtil.getCache(cacheName);
        if (cache != null) {
            try {
                cache.remove(key);
                Element elem = new Element(key, value);
                cache.put(elem);
            } catch (Exception e) {
                CacheUtil.log.error("put cache(" + cacheName + ") of " + key + " failed.", e);
            }
        }
    }

    /**
     * 添加缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        Ehcache cache = CacheUtil.getManager().getEhcache(cacheName);
        cache.put(new Element(key, value));
    }

    public static void remove(Object key) {
        CacheUtil.remove(null, key);
    }

    public static void remove(String cacheName, Object key) {
        CacheUtil.getCache(cacheName).remove(key);
    }

    /**
     * 删除指定对象
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static boolean remove(String cacheName, String key) {
        return CacheUtil.getManager().getCache(cacheName).remove(key);
    }

    /**
     * 停止缓存管理器
     */
    public static void shutdown() {
        if (CacheUtil.manager != null) CacheUtil.manager.shutdown();
    }
}
