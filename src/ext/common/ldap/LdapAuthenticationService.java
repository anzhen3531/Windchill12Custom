package ext.common.ldap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.logging.log4j.Logger;

import wt.log4j.LogR;
import wt.util.WTProperties;

/**
 * 打开DJ密码服务
 *
 * @author anzhen
 * @date 2024/02/01
 *       <p/>
 *       ext.ziang.oauth.LdapAuthenticationService
 */
public class LdapAuthenticationService {
    private static final String LDAP_URL;
    private static final String LDAP_USERNAME;
    private static final String LDAP_PASSWORD;
    private static final String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final Map<String, String> cachedDNMap = new ConcurrentHashMap<>(64);

    private static final Logger logger = LogR.getLogger(LdapAuthenticationService.class.getName());

    static {
        try {
            LDAP_URL = WTProperties.getLocalProperties().getProperty("wt.federation.ie.ldapServer");
            String windchillHome = WTProperties.getLocalProperties().getProperty("wt.home");
            String ieStructPropertiesFilePath = windchillHome + File.separator + "codebase" + File.separator + "WEB-INF"
                + File.separator + "ieStructProperties.txt";
            File file = new File(ieStructPropertiesFilePath);
            Properties ieStructProperties = new Properties();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                ieStructProperties.load(br);
            }
            LDAP_USERNAME = ieStructProperties.getProperty("ie.ldap.managerDn");
            LDAP_PASSWORD = ieStructProperties.getProperty("ie.ldap.managerPw");
            logger.debug("LDAP_URL:" + LDAP_URL);
            logger.debug("LDAP_USERNAME:" + LDAP_USERNAME);
            logger.debug("LDAP_PASSWORD:" + LDAP_PASSWORD);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 查找用户的DN
     *
     * @param uid 用户id
     * @return
     */
    private static String getUserDN(String uid) {
        String userDN = "";
        String decryptPassword = "";
        // 从缓存中取
        if (cachedDNMap.containsKey(uid)) {
            return cachedDNMap.get(uid);
        }
        try {
            decryptPassword = com.ptc.windchill.keystore.WTKeyStoreUtil.decryptProperty(LDAP_PASSWORD,
                WTProperties.getLocalProperties().getProperty("wt.home"));
        } catch (IOException e) {
            logger.error("获取LDAP管理员密码异常", e);
        }

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put(Context.SECURITY_PRINCIPAL, LDAP_USERNAME);
        env.put(Context.SECURITY_CREDENTIALS, decryptPassword);
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
            if (ctx != null) {
                SearchControls constraints = new SearchControls();
                constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
                NamingEnumeration<SearchResult> en = ctx.search("", "uid=" + uid, constraints);
                // maybe more than one element
                while (en != null && en.hasMoreElements()) {
                    Object obj = en.nextElement();
                    if (obj instanceof SearchResult) {
                        SearchResult si = (SearchResult)obj;
                        userDN += si.getName();
                        // 加入缓存
                        cachedDNMap.put(uid, userDN);
                        break;
                    } else {
                        logger.debug(obj);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("查找用户时产生异常:" + e.getMessage(), e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    logger.error("ldap关闭异常，uid:{}", uid, e);
                }
            }
        }

        return userDN;
    }

    public static boolean isAuthenticate(String userName, String password) {
        long start = System.currentTimeMillis();
        boolean valid;
        String userDN = userName;

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        LdapContext ctx = null;
        try {
            userDN = getUserDN(userName);
            env.put(Context.SECURITY_PRINCIPAL, userDN);
            env.put(Context.SECURITY_CREDENTIALS, password);

            ctx = new InitialLdapContext(env, null);
            logger.debug(userDN + " 验证通过");
            valid = true;
            logger.debug("LDAPAuthenticationUtil::isAuthenticate 处理耗时：" + (System.currentTimeMillis() - start) + "ms");
        } catch (NamingException e) {
            logger.error(userDN + " 验证失败:" + e.getMessage(), e);
            valid = false;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    logger.error("ldap关闭异常，userName:{}", userName, e);
                }
            }
        }
        return valid;
    }

    public static void main(String[] args) {
        logger.debug(LdapAuthenticationService.isAuthenticate(args[0], args[1]));
    }
}
