package com.JFJB.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.JFJB.widget.XListView;
import com.JFJB.widget.XListView.IXListViewListener;

/*import org.json.JSONArray;*/
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
/*import android.R.integer;*/
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*import android.widget.ListView;*/
import android.widget.SimpleAdapter;
import com.JFJB.app.R;
import com.JFJB.helper.HttpClientHelper;
import com.JFJB.helper.SQLiteDatabaseHelper;

public class DownloadappTabFragment extends Fragment implements IXListViewListener {
	
	
//handler��Ҫ���յ�����Ϣ
/*	private final static int SUCCESS = 0;*/
	String Urlstr = "http://111.203.149.50/jfjb/home/app/index";
	private static Context mContext;
	protected View view;	
	private String[] names ;
	private int[] imageIds;
	List<Map<String, Object>> listItem = new ArrayList<Map<String,Object>>();
	SimpleAdapter simpleAdapter;
	private XListView mlistView;
	SQLiteDatabaseHelper sqLiteDatabaseHelper;
	SQLiteDatabase db;
	Calendar calendar ;
	String time;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		listItem = new ArrayList<Map<String,Object>>();
		names = new String[]{"�ٶ��ֻ�����","��ѶӦ�ñ�","360�ֻ�����","�㶹��","�йش�����",};
		imageIds = new int[] {R.drawable.image4,R.drawable.image1,R.drawable.image3,
				R.drawable.image5,R.drawable.zol};
		sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity(), "APP");
		db = sqLiteDatabaseHelper.getWritableDatabase();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.tab03, container, false);
	
		mlistView = (XListView)view.findViewById(R.id.xlistview2);
		/*MyThread thread = new MyThread(handler, Urlstr);		
		thread.start();	*/	
		mlistView.setPullLoadEnable(false);
		mlistView.setPullRefreshEnable(true);
		mlistView.setXListViewListener(this);
		new MyTask(mContext,1).execute(Urlstr) ;
	    return  view;
	}	
    
	

/**
 * ��ʾ����ĿΪ��������
 */	
	class MyTask extends AsyncTask<String, Void, String[]>{
		private Context context;
		private ProgressDialog progressDialog;		
		 /** ������ǣ�1�����һ�μ��أ�2����ˢ��**/
		private int flag;
		public MyTask (Context context,int flag) 
		{
			context=this.context;
			this.flag = flag;
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("�������");
			progressDialog.setMessage("���ڼ���...");			
		}		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			switch (flag) {
			case 1:
				progressDialog.show();		
				break;

			case 2:
				break;
			}
				
		}

		@Override
		protected String[] doInBackground(String... params) {
		
			String[] nums = new String[5];
		
			try {
				if(HttpClientHelper.loadTextFromURL(
						Urlstr, "UTF-8")!=null){
				JSONObject mJsonObject = new JSONObject(HttpClientHelper.loadTextFromURL(
						Urlstr, "UTF-8"));
				Log.e("JSONȡ����", mJsonObject.toString());
				 	
				nums[0]=mJsonObject.getString("baidu");
				nums[1]=mJsonObject.getString("tencent");
				nums[2]=mJsonObject.getString("360");
				nums[3]=mJsonObject.getString("wandou");
				nums[4]=mJsonObject.getString("zol");
				
				for(int i = 0;i<nums.length;i++)
				{
				ContentValues values = new ContentValues();			
				values.put("id",i+1);
				values.put("title","");
				values.put("num",nums[i]);		
				values.put("likes","");			
				values.put("dotime","");
				Cursor scCursor = db.rawQuery("select * from Jfjb_app where id =?", new String[]{i+1+""});
				Log.e("APP�е�cursor", "scCursor.moveToFirst()=="+scCursor.moveToFirst());
				if(scCursor.moveToFirst()==false){
					db.insert("Jfjb_app", null, values);
				}
				else {
					db.update("Jfjb_app", values, "id = ?", new String[]{i+1+""});
				}
				Log.e("�������ݳɹ���", db.getPath());
				}
				
				}
				else {				
						for (int i = 0;i<5;i++){
							nums[i]="������δȡ��";			
					}
					
				}
				Log.e("numsװ����ϣ�", nums.toString());						
			} catch (JSONException e) {
				e.printStackTrace();

			}
			
			return nums;
		}
		
		
		
		
		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			
			switch (flag) {
			case 1:
				if(result[0].equals("������δȡ��")){
					 Cursor cursor = db.rawQuery("select * from Jfjb_app", null);
					 Log.e("cursor",cursor.toString());
					 List<Map<String, String>> cursorlist= cursorToList(cursor);
					 Log.e("�����ݿ�ȡ������", cursorlist.toString());
					 for(int i =0;i<5;i++){
						 result[i]=cursorlist.get(i).get("num");
					 }
				}
				for(int i = 0;i<names.length;i++)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Num", "������Ϊ��"+result[i]);					
					map.put("header", imageIds[i]);
					map.put("Name", names[i]);				
					listItem.add(map);
				}
				Log.e("listItem", listItem.toString());
				simpleAdapter= new SimpleAdapter(mContext, listItem, R.layout.listitem , 
						new String[]{"Name","header","Num","Increase"},
						new int[]{R.id.name,R.id.image,R.id.num,R.id.increase});
				mlistView.setAdapter(simpleAdapter);
				progressDialog.dismiss();
				break;
		
			case 2:
				if(result[0].equals("������δȡ��")){
					 Cursor cursor = db.rawQuery("select * from Jfjb_app", null);
					 Log.e("cursor",cursor.toString());
					 List<Map<String, String>> cursorlist= cursorToList(cursor);
					 Log.e("�����ݿ�ȡ������", cursorlist.toString());
					 for(int i =0;i<5;i++){
						 result[i]=cursorlist.get(i).get("num");
					 }
				}
				for(int i = 0;i<names.length;i++)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Num", "������Ϊ��"+result[i]);							
					map.put("header", imageIds[i]);
					map.put("Name", names[i]);	
					listItem.set(i, map);
				}
				Log.e("listItem1", listItem.toString());
				simpleAdapter.notifyDataSetChanged();
				mlistView.setAdapter(simpleAdapter);
				progressDialog.dismiss();			
				break;
			}
			
			
		}
	}
	/*
	 * �رն���ˢ�½�����ʾ�ϴθ���ʱ��
	 */
	 private void  Onload() {
			mlistView.stopRefresh();
			mlistView.stopLoadMore();
			
		}
	@Override
	public void onRefresh() {
		new MyTask(mContext,2).execute(Urlstr);	
		Onload();
		mlistView.setRefreshTime(time);
		calendar = Calendar.getInstance();
		time =    formatTime(calendar.get(Calendar.MONTH)+1)+"-"+   //month��һ    //��
				  formatTime(calendar.get(Calendar.DAY_OF_MONTH))+" "+                    //��
				  formatTime(calendar.get(Calendar.HOUR_OF_DAY))+":"+                      //ʱ
				  formatTime(calendar.get(Calendar.MINUTE))+":"+                           //��
				  formatTime(calendar.get(Calendar.SECOND));
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	 
	/** ��calendarת��Ϊint��ʽ */
	private String formatTime(int t){
		  return t>=10? ""+t:"0"+t;//��Ԫ����� t>10ʱȡ ""+t
		 }
	
	  /**cursor ����ת��Ϊlist */
    private List<Map<String, String>> cursorToList(Cursor cursor) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] arrColumnName = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < arrColumnName.length; i++) {
				String cols_value = cursor.getString(i);
				map.put(arrColumnName[i], cols_value);
			}
			list.add(map);
		}
		Log.e("���ݿ��е�list", list.toString());
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
    
    
}



/**
 * ��ʾ����ĿΪ������������������
 */
/*class MyTask extends AsyncTask<String, Void, List<Map<String, Object>>>{
	private Context context;
	private ProgressDialog progressDialog;		
	 *//** ������ǣ�1�����һ�μ��أ�2����ˢ��**//*
	private int flag;
	public MyTask (Context context,int flag) 
	{
		context=this.context;
		this.flag = flag;
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("�������");
		progressDialog.setMessage("���ڼ���...");			
	}
	
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		switch (flag) {
		case 1:
			progressDialog.show();		
			break;

		case 2:
			break;
		}
			
	}



	@Override
	protected List<Map<String, Object>> doInBackground(String... params) {
	
		List<Map<String, Object>> list  = new ArrayList<Map<String,Object>>();
	
		try {
			if(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8")!=null){
			JSONArray array = new JSONArray(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8"));
			Log.e("JSONȡ����", array.toString());
			 
				for(int i = 0;i<array.length();i++)
				{
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject temp = (JSONObject)array.get(i);
				map.put("Num", "������Ϊ��"+temp.getInt("num"));
				map.put("Increase","����������"+temp.getInt("increase"));		
				list.add(map);
				}
			}
			else {
				for (int i = 0;i<5;i++){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Num", "������δȡ��");
					map.put("Increase", "��������δȡ��");
					list.add(map);
				}
			}
			Log.e("listװ����ϣ�", list.toString());						
		} catch (JSONException e) {
			e.printStackTrace();

		}
		
		return list;
	}
	
	
	
	
	@Override
	protected void onPostExecute(List<Map<String, Object>> result) {
		super.onPostExecute(result);
		
		switch (flag) {
		case 1:
			for(int i = 0;i<names.length;i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Num", result.get(i).get("Num"));
				map.put("Increase", result.get(i).get("Increase"));
				map.put("header", imageIds[i]);
				map.put("Name", names[i]);				
				listItem.add(map);
			}
			Log.e("listItem", listItem.toString());
			simpleAdapter= new SimpleAdapter(mContext, listItem, R.layout.listitem , 
					new String[]{"Name","header","Num","Increase"},
					new int[]{R.id.name,R.id.image,R.id.num,R.id.increase});
			mlistView.setAdapter(simpleAdapter);
			progressDialog.dismiss();
			break;
	
		case 2:
			for(int i = 0;i<names.length;i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Num", result.get(i).get("Num"));
				map.put("Increase", result.get(i).get("Increase"));
				map.put("header", imageIds[i]);
				map.put("Name", names[i]);	
				listItem.set(i, map);
			}
			Log.e("listItem1", listItem.toString());
			simpleAdapter.notifyDataSetChanged();
			mlistView.setAdapter(simpleAdapter);
			progressDialog.dismiss();			
			break;
		}
		
		
	}
}*/



/**
 * ����handler�������̵߳ķ�ʽʵ���첽�������磬������handler�����ݴ��ݸ����߳�̫�鷳������AsyncTask����
 */
/*	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case SUCCESS:
				
				Bundle b = msg.getData();
				Log.e("BUNDLEȡ����", b.toString());
				nums[0]= b.getInt("baidunum");
				nums[1]= b.getInt("d360num");
				nums[2]= b.getInt("yingyongbaonum");
				nums[3]= b.getInt("android");
				nums[4]= b.getInt("wandoujia");
				Log.e("��ֵ�ɹ���", nums[0]+"");				
				break;

			default:
				nums=null;
				break;
			 }	
			
			for(int i = 0 ;i<5;i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("header", imageIds[i]);
				map.put("Name", names[i]);
				map.put("Num", "������Ϊ:"+nums[i]);	
				listItem.add(map);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(mContext.getApplicationContext(), listItem, 
					R.layout.listitem, new String[]{"Name","header","Num"}, 
					new int[]{R.id.name,R.id.image,R.id.num});	
			ListView listView = (ListView)view.findViewById(R.id.lv);
			listView.setAdapter(simpleAdapter);
		}
	};*/

	
/**
 * ����̱߳���װ����MyThread��
 */
/*	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.e("TAG", "��ʼ��ȡ");
			int baidunum = 0;
			int d360num = 0;
			int yingyongbaonum = 0;
			int androidnum=0;
			int wandoujianum=0;
			try {
				JSONObject jsonObject = new JSONObject(HttpClientHelper.loadTextFromURL(
						Urlstr, "UTF-8"));
				Log.e("JSONȡ����", jsonObject.toString());
				baidunum = jsonObject.getInt("baidu");
				d360num = jsonObject.getInt("d360");
				yingyongbaonum = jsonObject.getInt("yingyongbao");
				androidnum = jsonObject.getInt("android");
				wandoujianum = jsonObject.getInt("wandoujia");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Message msg = handler.obtainMessage(SUCCESS);
			Bundle bundle = new Bundle();
			bundle.putInt("baidunum",baidunum);
			bundle.putInt("d360num",d360num);
			bundle.putInt("yingyongbaonum",yingyongbaonum);
			bundle.putInt("android",androidnum);
			bundle.putInt("wandoujia",wandoujianum);
			
			Log.e("bundle��װ��ϣ�", bundle.toString());
			msg.setData(bundle);
			msg.sendToTarget();
			
		}
	
	};*/
		




	