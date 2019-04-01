package io.jopen.web.core.security.access;

import io.jopen.core.common.util.MD5Util;
import io.jopen.web.core.model.ErrorEnum;
import io.jopen.web.core.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.jopen.web.core.model.ResponseModel.error;


/**
 * 接口访问加密验证
 * 1: 为每个客户端分配AppKey 和APPSecret   一个AppKey对应一个APPSecret  每个客户端对应一个AppKey？
 * 2： 在web端本地存储的secret明显可以被黑客拿到（不论加密算法何等异常复杂 也解决不了根本问题） 参数传递过程不传secret
 * <p>
 * <p>
 * 注意：这种借口加密方式只针对于公共访问接口
 */
public class APISignInterceptor extends HandlerInterceptorAdapter {

    @Value("${security.common.appKey}")
    private String appKey;

    @Value("${security.common.appSecret}")
    private String appSecret;

    @Value("${security.common.accessExpire}")
    private int accessExpire;

    private static final String SIGN_KEY = "sign";

    private static final String TIMESTAMP_KEY = "timestamp";

    private static final String ACCESS_KEY = "accessKey";

    private static final String ACCESS_SECRET = "accessSecret";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 参数校验
         */
        return verifyParams(request, response);
    }


    /**
     * 参数校验
     *
     * @param request
     * @return
     */
    private boolean verifyParams(HttpServletRequest request, HttpServletResponse response) {
        String timestamp = request.getParameter(TIMESTAMP_KEY);
        String accessKey = request.getParameter(ACCESS_KEY);
        long currentTimeMillis = System.currentTimeMillis();
        if (!StringUtils.isNumeric(timestamp) || StringUtils.isBlank(accessKey)) {
            WebUtils.write4Response(response, error(ErrorEnum.INVALID_TIMESTAMP));
            return false;
        } else if (currentTimeMillis - accessExpire > Long.valueOf(timestamp).longValue()) {
            WebUtils.write4Response(response, error(ErrorEnum.TIMESTAMP_TIMEOUT));
            return false;
        } else if (!verifySign(request)) {
            WebUtils.write4Response(response, error(ErrorEnum.INVALID_SIGNATURE));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证签名
     *
     * @param request
     * @return
     */
    private boolean verifySign(HttpServletRequest request) {
        /**
         * 除了sign参数之外  其他参数都放到map中
         */
        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        while (names.hasMoreElements()) {
            if (names.nextElement().equals(SIGN_KEY)) continue;
            String k = names.nextElement();
            map.put(k, request.getParameter(k));
        }
        String originSign = request.getParameter(SIGN_KEY);
        String sign = createSign(appSecret, map);
        return sign.equals(originSign);
    }

    /**
     * 所有参数进行签名处理
     *
     * @param appSecret
     * @param map
     * @return
     */
    private String createSign(String appSecret, Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        Object[] k = keySet.toArray();
        boolean first = true;
        StringBuilder temp = new StringBuilder();
        for (Object key : k) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object val = map.get(key);
            String valString = "";
            if (val == null) {
                temp.append(valString);
            } else {
                temp.append(val);
            }
        }
        temp.append("&").append(ACCESS_SECRET).append("=").append(appSecret);
        return MD5Util.MD5Encode(temp.toString()).toUpperCase();
    }
}
