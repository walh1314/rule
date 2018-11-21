package com.foxconn.core.pro.server.rule.engine.action.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author liupingan
 *
 */
@Slf4j
public class HttpClientUtil
{
	private static PoolingHttpClientConnectionManager clientConnectionManager = null;
	private static CloseableHttpClient httpClient = null;
	private static RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
	private final static Object syncLock = new Object();
	static
	{
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = null;
		try
		{
			sslcontext = createIgnoreVerifySSL();
		} catch (KeyManagementException e)
		{
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e)
		{
			log.error(e.getMessage());
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register(CommonConstant.HTTPS, new SSLConnectionSocketFactory(sslcontext))
				.register(CommonConstant.HTTP, PlainConnectionSocketFactory.getSocketFactory()).build();
		clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		clientConnectionManager.setMaxTotal(50);
		clientConnectionManager.setDefaultMaxPerRoute(25);
	}

	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException
	{
		SSLContext sc = SSLContext.getInstance(CommonConstant.SSLV3);

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager()
		{
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException
			{
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException
			{
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}
		};

		sc.init(null, new TrustManager[]
		{ trustManager }, null);
		return sc;
	}

	public static CloseableHttpClient getHttpClient()
	{
		if (httpClient == null)
		{
			synchronized (syncLock)
			{
				if (httpClient == null)
				{
					BasicCookieStore cookieStore = new BasicCookieStore();
					BasicClientCookie cookie = new BasicClientCookie(CommonConstant.SESSION_ID, CommonConstant.WELL_NUMBER_SIX);
					cookie.setDomain(CommonConstant.WELL_NUMBER_FIVE);
					cookie.setPath(CommonConstant.DOUBLE_SLASH);
					cookieStore.addCookie(cookie);
					httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager)
							.setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();
				}
			}
		}
		return httpClient;
	}

	@SuppressWarnings(CommonConstant.FIN_ALLY)
	public static HttpEntity httpGet(String url, Map<String, Object> headers)
	{
		CloseableHttpClient httpClient = getHttpClient();
		HttpRequest httpGet = new HttpGet(url);
		if (headers != null && !headers.isEmpty())
		{
			httpGet = setHeaders(headers, httpGet);
		}
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try
		{
			response = httpClient.execute((HttpGet) httpGet);
			entity = response.getEntity();
		} catch (Exception e)
		{
			log.error(e.getMessage());
		} finally
		{
			return entity;
		}
	}
	
	@SuppressWarnings(CommonConstant.FIN_ALLY)
	public static HttpEntity httpGet(String url,Map<String, Object> params, Map<String, Object> headers)
	{
		HttpEntity entity = null;
		try
		{
			CloseableHttpClient httpClient = getHttpClient();
			String paStr ="";
			if (params != null && !params.isEmpty())
			{
				List<NameValuePair> nameValuePairs = new ArrayList<>();
				for (Map.Entry<String, Object> entry : params.entrySet())
				{
					 nameValuePairs.add(new BasicNameValuePair(entry.getKey(), (String)entry.getValue()));
					 
				}
				paStr = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));
			}
			
			
			HttpRequest httpGet = new HttpGet(url+(url.indexOf(CommonConstant.QUESTION_MARK)!=-1?CommonConstant.ADD_OPERATOR:CommonConstant.QUESTION_MARK)+paStr);
			
			if (headers != null && !headers.isEmpty())
			{
				httpGet = setHeaders(headers, httpGet);
			}
			CloseableHttpResponse response = null;
			
		
			response = httpClient.execute((HttpGet) httpGet);
			entity = response.getEntity();
		} catch (Exception e)
		{
			log.error(e.getMessage());
		} finally
		{
			return entity;
		}
	}


	/**
	 * 使用表单键值对传参
	 */
	public static HttpEntity httpPost(String url, Map<String, Object> headers, List<NameValuePair> data)
	{
		CloseableHttpClient httpClient = getHttpClient();
		HttpRequest request = new HttpPost(url);
		if (headers != null && !headers.isEmpty())
		{
			request = setHeaders(headers, request);
		}
		CloseableHttpResponse response = null;
		UrlEncodedFormEntity uefEntity;
		try
		{
			HttpPost httpPost = (HttpPost) request;
			uefEntity = new UrlEncodedFormEntity(data, StandardCharsets.UTF_8);
			httpPost.setEntity(uefEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (IOException e)
		{
			log.error(e.getMessage());

		}
		return null;
	}

	/**
	 * 使用表单键值对传参
	 */
	public static HttpEntity httpPost(String url, Map<String, Object> headers, String data)
	{
		CloseableHttpClient httpClient = getHttpClient();
		HttpRequest request = new HttpPost(url);
		if (headers != null && !headers.isEmpty())
		{
			request = setHeaders(headers, request);
		}
		CloseableHttpResponse response = null;
		try
		{
			HttpPost httpPost = (HttpPost) request;
			StringEntity stringEntity = new StringEntity(data, StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (IOException e)
		{
			log.error(e.getMessage());

		}
		return null;
	}

	/**
	 * 设置请求头信息
	 * 
	 * @param headers
	 * @param request
	 * @return
	 */
	private static HttpRequest setHeaders(Map<String, Object> headers, HttpRequest request)
	{
		for (Map.Entry<String, Object> entry : headers.entrySet())
		{
			request.addHeader((String) entry.getKey(), (String) entry.getValue());
		}
		return request;
	}
	
	
}
