package com.JFJB.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;



public class HttpClientHelper {
	
	private static final int REQUEST_TIMEOUT = 8*1000;//设置请求超时10秒钟  
	private static final int SO_TIMEOUT = 8*1000;  //设置等待数据超时时间10秒钟 
	/**
	 * 下载流数据
	 */
		public static byte[] loadByteFromURL(String url) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response;
			try {
				response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity httpEntity = response.getEntity();
					return EntityUtils.toByteArray(httpEntity);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	/**
	 * 下载文件
	 * @param url
	 * @return
	 */
		public static InputStream loadFileFromURL(String url) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response;
			try {
				response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity httpEntity = response.getEntity();
					return httpEntity.getContent();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 下载文本数据
		 */
		public static String loadTextFromURL(String url, String charset) {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response;
			try {
				response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity httpEntity = response.getEntity();
					return EntityUtils.toString(httpEntity, charset);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	/**
	 * get请求
	 * @param url
	 * @return
	 */
		public byte[] doGetSubmit(String url) {
			return loadByteFromURL(url);
		}

		public static byte[] doPostSubmit(String url, List<NameValuePair> params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost();
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					return EntityUtils.toByteArray(entity);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * post请求
		 * @param url
		 * @return
		 */
		public static byte[] doPostSubmit(String url, Map<String, Object> params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost();
			try {

				List<NameValuePair> list = new ArrayList<NameValuePair>();

				for (Entry<String, Object> entry : params.entrySet()) {
					NameValuePair nameValuePair = new BasicNameValuePair(
							entry.getKey(), entry.getValue().toString());
					list.add(nameValuePair);
				}
				httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
				HttpResponse response = httpClient.execute(httpPost);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					return EntityUtils.toByteArray(entity);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
}
