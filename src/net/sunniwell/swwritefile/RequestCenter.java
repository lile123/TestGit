package net.sunniwell.swwritefile;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinamobile.middleware.okhttps.base.BaseRequestCenter;
import com.chinamobile.middleware.okhttps.listener.DisposeDataCodeListener;
import com.chinamobile.middleware.okhttps.listener.DisposeDataListener;
import com.chinamobile.middleware.okhttps.listener.DisposeDownloadListener;
import com.chinamobile.middleware.okhttps.listener.DisposeXmlDataListener;
import com.chinamobile.middleware.okhttps.request.RequestParams;
import com.chinamobile.middleware.okhttps.utils.L;

import android.R.integer;
import android.content.Context;
import android.os.Build;

import android.util.Log;

public class RequestCenter extends BaseRequestCenter {
	
	public static void sendRequest(Context context, DisposeDataListener listener, boolean isNetworkPriority) {
		String url = "http://bngt.itv.cmvideo.cn:8095/scspProxy";
		String url1 = "http://www.baidu.com";
		Log.d("访问get请求 url：", url);
//		RequestCenter.getRequest(isNetworkPriority, url, null, listener, null);
//		Log.d("访问post请求 url：", url);
//		RequestCenter.postRequest(isNetworkPriority, url, null, listener, null);
		Log.d("访问get请求 url1：", url1);
		RequestCenter.getRequest(context, isNetworkPriority, url1, null, listener, null);
//		Log.d("访问post请求 url1：", url);
//		RequestCenter.postRequest(isNetworkPriority, url1, null, listener, null);
	}

	public static void sendResultGetRequest(Context context, String url, DisposeDownloadListener listener,
			boolean isNetworkPriority,String path) {
		Log.d("访问get请求 url：", url);
		RequestCenter.downloadFile(context, url, path, listener);
	}

	public  int sendResultGetRequsert(Context context, String url, DisposeDownloadListener listener) {
		HttpResult httpResult = exeuct(context,url, listener);
		return httpResult.getStatusCode();
		
		
	}

	private synchronized HttpResult exeuct(Context context, String url, DisposeDownloadListener listener) {
			final HttpResult httpResult = new HttpResult(); 
			RequestCenter.getResultRequest(context, false, url, null, new DisposeDataCodeListener() {
				
				@Override
				public void onCodeCallback(int arg0) {
					Log.i("lile", "msg:        "+arg0);
					synchronized (RequestCenter.this) {
						httpResult.setStatusCode(arg0);
						RequestCenter.this.notify();
					}
				}
			});
			synchronized (this) {
				 try {
					 RequestCenter.this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return httpResult;
			}

	}
}
