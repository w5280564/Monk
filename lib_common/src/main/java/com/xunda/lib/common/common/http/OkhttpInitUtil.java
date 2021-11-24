package com.xunda.lib.common.common.http;

import android.annotation.SuppressLint;
import android.content.Context;

import com.xunda.lib.common.common.utils.RxUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;


/**
 * Okhttp初始化类
 */
public class OkhttpInitUtil {

    private static OkhttpInitUtil mOkhttpUtilInstance;
    private Context mContext;


    public OkhttpInitUtil(Context mContext) {
        this.mContext = mContext;
        initOkHttpSender();
    }


    /**
     * 初始化 Okhttp.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mOkhttpUtilInstance == null) {
            synchronized (OkhttpInitUtil.class) {

                if (mOkhttpUtilInstance == null) {
                    mOkhttpUtilInstance = new OkhttpInitUtil(context);
                }
            }
        }

    }


    /**
     * 初始化OKHTTP
     */
    private void initOkHttpSender() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //其他配置
                .connectionSpecs(Arrays.asList(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.CLEARTEXT))
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)//请求超时30秒
                .readTimeout(30000L, TimeUnit.MILLISECONDS)//读写超时30秒
                .writeTimeout(30000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(RxUtils.createSSLSocketFactory())
                .hostnameVerifier(new RxUtils.TrustAllHostnameVerifier())
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}
