package ext.oauth.config.cache;

import java.rmi.RemoteException;

import wt.cache.CacheManager;

/**
 * 配置文件缓存
 */
public class PropertiesCache extends CacheManager {
	private static final long serialVersionUID = 1L;

	public PropertiesCache() throws RemoteException {
		super();
	}
}
