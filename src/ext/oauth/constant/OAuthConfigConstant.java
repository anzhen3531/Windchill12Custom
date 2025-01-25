package ext.oauth.constant;

import ext.common.constant.CommonConfigConstants;
import ext.oauth.config.cache.PropertiesManager;

/**
 * OAuth 配置常量
 *
 * @author anzhen
 * @date 2023/12/28
 */
public interface OAuthConfigConstant {
	/**
	 * 客户端 ID
	 */
	String CLIENT_ID = PropertiesManager.getProperty("oauth.client.id");
	/**
	 * 访问类型
	 */
	String GRANT_TYPE = "code";
	/**
	 * 客户端密码
	 */
	String CLIENT_SECRET = PropertiesManager.getProperty("oauth.client.secret");

	/**
	 * 重定向页面 URI
	 */
	String REDIRECT_PAGE_URI = PropertiesManager.getProperty("oauth.redirect.uri");
	/**
	 * 重定向 URI
	 */
	String REDIRECT_URI = CommonConfigConstants.HOST_URL + REDIRECT_PAGE_URI;
	/**
	 * 访问令牌 URL github
	 */
	String ACCESS_TOKEN_URL = PropertiesManager.getProperty("oauth.access.token.url");
	/**
	 * 获取用户信息 URL
	 */
	String GET_USER_INFO_URL = PropertiesManager.getProperty("oauth.access.user.info.url");

	/**
	 *
	 */
	String OAUTH2_LOGIN_PAGE_FILE = "/Windchill/netmarkets/jsp/gwt/login.jsp";
	/**
	 * OAuth2 登录页面
	 */
	String OAUTH2_LOGIN_PAGE = CommonConfigConstants.HOST_URL + OAUTH2_LOGIN_PAGE_FILE;
}
