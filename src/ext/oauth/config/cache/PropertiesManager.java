package ext.oauth.config.cache;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import wt.log4j.LogR;
import wt.method.MethodServerException;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.util.WTProperties;

/**
 * 配置文件缓存管理器
 * ext.oauth.config.cache.PropertiesManager
 */
public class PropertiesManager implements RemoteAccess, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogR.getLoggerInternal(PropertiesManager.class.getName());
    private static PropertiesCache cache = null;

    private static PropertiesCache getCache() {
        if (cache == null)
            createCache();
        return cache;
    }

    private static synchronized void createCache() {
        if (cache == null) {
            try {
                cache = new PropertiesCache();
            } catch (RemoteException e) {
                throw new MethodServerException("Unable to create integration properties cache", e);
            }
        }
    }

    public static String getProperty(String key) {
        PropertiesCache foo_cache = getCache();
        String value = (String)foo_cache.get(key);
        logger.debug("get cache key:" + key + ",value:" + value);
        if (StringUtils.isEmpty(value)) {
            updateProperty();
            value = (String)foo_cache.get(key);
        }
        return value;
    }

    public static Set<String> getPropertyValueSet(String key) {
        PropertiesCache propertiesCache = getCache();
        Set<String> valueSet = (Set<String>)propertiesCache.get(key);
        logger.debug("get cache key:" + key + ",value:" + valueSet);
        if (CollectionUtils.isEmpty(valueSet)) {
            updateProperty();
            valueSet = (Set<String>)propertiesCache.get(key);
        }
        return valueSet;
    }


    public static void updateProperty() {
        BufferedReader br = null;
        try {
            String homePath = WTProperties.getLocalProperties().getProperty("wt.home");
            // 获取配置文件
            String integrationPropertiesFileName = homePath + File.separator + "codebase" + File.separator + "ext"
                + File.separator + "oauth" + File.separator + "integration.properties";
            File file = new File(integrationPropertiesFileName);
            br = new BufferedReader(new FileReader(file));
            Properties integrationProperties = new Properties();
            integrationProperties.load(br);
            // Add to cache
            Set<String> whiteSet = new HashSet<>();
            Set<String> noSSOSet = new HashSet<>();
            for (Object obj : integrationProperties.keySet()) {
                String key = (String) obj;
                String value = integrationProperties.getProperty(key);
                getCache().put(obj, value);
                logger.info("put cache key: {} ,value: {}", obj, value);
                if (key.contains("white.list")) {
                    whiteSet.add(value);
                } else if (key.contains("no.sso.list")) {
                    noSSOSet.add(value);
                }
            }
            getCache().put("white.list", whiteSet);
            getCache().put("no.sso.list", noSSOSet);
        } catch (Exception e) {
            logger.error("updateProperty异常", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error("关闭流异常", e);
            }
        }
    }

    public static void main(String[] args) {
        RemoteMethodServer server = RemoteMethodServer.getDefault();
        server.setUserName("wcadmin");
        server.setPassword("123456");
        Class[] classes = {};
        Object[] objs = {};
        try {
            server.invoke("updateProperty", PropertiesManager.class.getName(), null, classes, objs);
        } catch (Exception e) {
            logger.error("main方法中服务调用异常", e);
        }
    }
}
