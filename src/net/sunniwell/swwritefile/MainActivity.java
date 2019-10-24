package net.sunniwell.swwritefile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.NonReadableChannelException;
import java.nio.charset.Charset;

import com.chinamobile.middleware.okhttps.listener.DisposeDataListener;
import com.chinamobile.middleware.okhttps.listener.DisposeDownloadListener;
import com.chinamobile.middleware.okhttps.listener.DisposeXmlDataListener;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DataOutputStream mDataOutputStream;
	private Button btn_write,btn_network;
	private TextView tv_contenTextView;
	private RequestCenter mRequset=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_write = (Button) findViewById(R.id.btn_write);
		btn_network = (Button) findViewById(R.id.btn_network);
		tv_contenTextView = (TextView) findViewById(R.id.tv_content);
		mRequset = new RequestCenter();
		btn_write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "开始写入", Toast.LENGTH_LONG).show();
//				writeFile();
				promotePower();
			}
				
			}
		);
		btn_network.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  int a =	mRequset.sendResultGetRequsert(MainActivity.this,"http://www.baidu.com" , null);
			  Log.i("lile", "i:     "+a);
			}
		});
	}
	private void promotePower() {
		try {
			Process process = Runtime.getRuntime().exec("/system/xbin/su");		
			
			BufferedInputStream inputStream= new BufferedInputStream(process.getInputStream());
			BufferedInputStream errorStream=new BufferedInputStream(process.getErrorStream());
			

			
			
			mDataOutputStream = new DataOutputStream(process.getOutputStream());
			String order = "chmod 777 /dev/ttyGS2"+"\n";
			mDataOutputStream.write(order.getBytes(Charset.forName("utf-8")));
			mDataOutputStream.writeBytes("exit\n");
			mDataOutputStream.flush();
			try {
				process.waitFor();
				Toast.makeText(MainActivity.this, "结果展示"+process.waitFor(), Toast.LENGTH_LONG).show();
			
				byte[] buf=new byte[409600];
				int ret=inputStream.read(buf,0,buf.length);
				byte[] buf2=new byte[409600];
			    int ret2=errorStream.read(buf2,0,buf2.length);
			    String teString="123:";
			    if(ret>0)
			    	teString=new String(buf,0,ret);
			    if(ret2>0)
			    	teString+= "\n" + new String(buf2,0,ret2);
				tv_contenTextView.setText(teString);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mDataOutputStream.close();
//			Process process = Runtime.getRuntime().exec("su -c ");
//			 try {
//				 String commnd = "chmod 777" +" /dev/ttyGS0"+"\n";
//				 tv_contenTextView.setText(commnd);
//				 process.getOutputStream().write(commnd.getBytes(Charset.forName("utf-8")));
//				int waitFor = process.waitFor();
//				Toast.makeText(MainActivity.this, "结果展示"+waitFor, Toast.LENGTH_LONG).show();
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	/**
	 * apk的静默安装
	 */
	private void writeFile() {
		try {
			Process process = Runtime.getRuntime().exec("sh");		
			mDataOutputStream = new DataOutputStream(process.getOutputStream());
			String order = "pm install -r /storage/FC8CDDEB8CDDA086/test/SWTestApk.apk"+"\n";
			mDataOutputStream.write(order.getBytes(Charset.forName("utf-8")));
			mDataOutputStream.writeBytes("exit\n");
			mDataOutputStream.flush();
			process.waitFor();
			mDataOutputStream.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	DisposeDataListener disposeXmlDataListener = new DisposeDataListener() {
			

		@Override
		public void onFailure(Object reasonObj) {
			Log.d("fail", reasonObj.toString());
		}

		@Override
		public void onSuccess(Object arg0) {
			Log.d("success", arg0.toString());
			Toast.makeText(MainActivity.this, arg0.toString(), Toast.LENGTH_LONG).show();
			
		}
	};
	DisposeDownloadListener downloadListener = new DisposeDownloadListener() {
		
		@Override
		public void onSuccess(Object arg0) {
			// TODO Auto-generated method stub
			Log.d("下载完成", arg0.toString());
		}
		
		@Override
		public void onFailure(Object arg0) {
			// TODO Auto-generated method stub
			Log.d("下载失败", arg0.toString());
		}
		
		@Override
		public void onProgress(Object arg0) {
			// TODO Auto-generated method stub
			Log.d("下载进度", arg0.toString());
		}
	};
}
