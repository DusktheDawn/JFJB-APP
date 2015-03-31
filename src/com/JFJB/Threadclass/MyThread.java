package com.JFJB.Threadclass;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.JFJB.helper.HttpClientHelper;

public class MyThread extends Thread {
   

	private Handler handler;
    private String Urlstr;
	public MyThread(Handler handler,String Urlstr)
    {
    	super();
    	this.handler = handler;
    	this.Urlstr=Urlstr;
    }
	 @Override
		public void run() {
		// TODO Auto-generated method stub
					Log.e("TAG", "开始获取");
					int baidunum = 0;
					int d360num = 0;
					int yingyongbaonum = 0;
					int androidnum=0;
					int wandoujianum=0;
					try {
						if(HttpClientHelper.loadTextFromURL(
								Urlstr, "UTF-8")!=null){
						JSONObject jsonObject = new JSONObject(HttpClientHelper.loadTextFromURL(
								Urlstr, "UTF-8"));
						Log.e("JSON取到！", jsonObject.toString());
						baidunum = jsonObject.getInt("baidu");
						d360num = jsonObject.getInt("d360");
						yingyongbaonum = jsonObject.getInt("yingyongbao");
						androidnum = jsonObject.getInt("android");
						wandoujianum = jsonObject.getInt("wandoujia");
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Message msg = handler.obtainMessage(0);
					Bundle bundle = new Bundle();
					bundle.putInt("baidunum",baidunum);
					bundle.putInt("d360num",d360num);
					bundle.putInt("yingyongbaonum",yingyongbaonum);
					bundle.putInt("android",androidnum);
					bundle.putInt("wandoujia",wandoujianum);
					
					Log.e("bundle封装完毕！", bundle.toString());
					msg.setData(bundle);
					msg.sendToTarget();
					
				}
			
		}
	

