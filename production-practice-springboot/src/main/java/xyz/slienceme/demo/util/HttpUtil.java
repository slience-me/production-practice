package xyz.slienceme.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpUtil {

	/**
	 * 返回成功状态码
	 */
	private static final int SUCCESS_CODE = 200;

	/**
	 * 发送GET请求
	 * 
	 * @param url
	 *            请求url
	 * @param nameValuePairList
	 *            请求参数
	 * @return JSON或者字符串
	 * @throws Exception
	 */
	public static Object sendGet(String url,
			List<NameValuePair> nameValuePairList) throws Exception {
		JSONObject jsonObject = null;
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			/**
			 * 创建HttpClient对象
			 */
			client = HttpClients.createDefault();
			/**
			 * 创建URIBuilder
			 */
			URIBuilder uriBuilder = new URIBuilder(url);
			/**
			 * 设置参数
			 */
			uriBuilder.addParameters(nameValuePairList);
			/**
			 * 创建HttpGet
			 */
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			/**
			 * 设置请求头部编码
			 */
			httpGet.setHeader(new BasicHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8"));
			/**
			 * 设置返回编码
			 */
			httpGet.setHeader(new BasicHeader("Accept",
					"text/plain;charset=utf-8"));
			/**
			 * 请求服务
			 */
			response = client.execute(httpGet);
			/**
			 * 获取响应吗
			 */
			int statusCode = response.getStatusLine().getStatusCode();

			if (SUCCESS_CODE == statusCode) {
				/**
				 * 获取返回对象
				 */
				HttpEntity entity = response.getEntity();
				/**
				 * 通过EntityUitls获取返回内容
				 */
				String result = EntityUtils.toString(entity, "UTF-8");
				/**
				 * 转换成json,根据合法性返回json或者字符串
				 */
				try {
					jsonObject = JSONObject.parseObject(result);
					return jsonObject;
				} catch (Exception e) {
					return result;
				}
			} else {
				System.out.println("请求失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
			client.close();
		}
		return null;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 * @param nameValuePairList
	 * @return JSON或者字符串
	 * @throws Exception
	 */
	public static Object sendPost(String url,
			List<NameValuePair> nameValuePairList) throws Exception {
		JSONObject jsonObject = null;
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			/**
			 * 创建一个httpclient对象
			 */
			client = HttpClients.createDefault();
			/**
			 * 创建一个post对象
			 */
			HttpPost post = new HttpPost(url);
			/**
			 * 包装成一个Entity对象
			 */
			StringEntity entity = new UrlEncodedFormEntity(nameValuePairList,
					"UTF-8");
			/**
			 * 设置请求的内容
			 */
			post.setEntity(entity);
			/**
			 * 设置请求的报文头部的编码
			 */
			post.setHeader(new BasicHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8"));
			/**
			 * 设置请求的报文头部的编码
			 */
			//	post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
			/**
			 * 执行post请求
			 */
			response = client.execute(post);
			/**
			 * 获取响应码
			 */
			int statusCode = response.getStatusLine().getStatusCode();
			if (SUCCESS_CODE == statusCode) {
				/**
				 * 通过EntityUitls获取返回内容
				 */
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				/**
				 * 转换成json,根据合法性返回json或者字符串
				 */
				try {
					jsonObject = JSONObject.parseObject(result);
					return jsonObject;
				} catch (Exception e) {
					return result;
				}
			} else {
				System.out.println("请求失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
			client.close();
		}
		return null;
	}

	/**
	 * 组织请求参数{参数名和参数值下标保持一致}
	 * 
	 * @param params
	 *            参数名数组
	 * @param values
	 *            参数值数组
	 * @return 参数对象
	 */
	public static List<NameValuePair> getParams(Object[] params, Object[] values) {
		/**
		 * 校验参数合法性
		 */
		boolean flag = params.length > 0 && values.length > 0
				&& params.length == values.length;
		if (flag) {
			List<NameValuePair> nameValuePairList = new ArrayList<>();
			for (int i = 0; i < params.length; i++) {
				nameValuePairList.add(new BasicNameValuePair(params[i]
						.toString(), values[i].toString()));
			}
			return nameValuePairList;
		} else {
			System.out.println("请求失败");
		}
		return null;
	}
}