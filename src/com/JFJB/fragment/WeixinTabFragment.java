package com.JFJB.fragment;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.JFJB.app.R;
import com.JFJB.helper.HttpClientHelper;
import com.JFJB.helper.SQLiteDatabaseHelper;
import com.JFJB.widget.XListView;
import com.JFJB.widget.XListView.IXListViewListener;




public class WeixinTabFragment extends Fragment implements IXListViewListener
{

	/** �����������listview���� */
	protected View view;
	String Urlstr = "http://111.203.149.50/jfjb/home/index/likes";
	private XListView mListView;
	private ListItemAdapter mAdapter;
	private Map<String, Bitmap> cacheImageMap;
	List<Map<String, Object>> urllist;
	List<Map<String, Object>> list;
	String path = Environment.getExternalStorageDirectory().getPath() + "/JFJB-app";
	SQLiteDatabaseHelper sqLiteDatabaseHelper;
	SQLiteDatabase db;
	Calendar calendar ;
	String time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//super�������뱻����
		list  = new ArrayList<Map<String,Object>>();
		cacheImageMap = new HashMap<String, Bitmap>();
		urllist = new ArrayList<Map<String,Object>>();
		sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getActivity(), "JFJB");
		db = sqLiteDatabaseHelper.getWritableDatabase();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view =inflater.inflate(R.layout.tab01, container, false);
		
		
		
		
		mListView = (XListView)view.findViewById(R.id.xlistview);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);		
		new MyTask(getActivity(),1).execute(Urlstr);
		return  view;
	}


/**
 * ��ȡ���ݲ�������װ�ص�adapter
 */

   class MyTask extends AsyncTask<String, Void, Object>{
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
	protected Object doInBackground(String... params) {
		Object object = null;	
		try {
			/** ����������� **/
			if(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8")!=null){
				JSONArray array = new JSONArray(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8"));
				Log.e("JSONȡ����", array.toString());
				for(int i = 0;i<array.length();i++)
				{
				ContentValues values = new ContentValues();
				JSONObject temp = (JSONObject)array.get(i);		
				values.put("id",i+1);
				values.put("title", temp.getString("title"));
				values.put("num","�Ķ�     "+temp.getString("num"));		
				values.put("likes","����     "+temp.getString("likes"));			
				values.put("dotime", "����ʱ�䣺"+temp.getString("dotime"));
				Log.e("valuesװ�����", values.toString());
				
				Cursor scCursor = db.rawQuery("select * from Jfjb_app where id =?", new String[]{i+1+""});
				Log.e("JFJB�е�cursor", "scCursor.moveToFirst()=="+scCursor.moveToFirst());
				if(scCursor.moveToFirst()==false){
					db.insert("Jfjb_app", null, values);
				}
				else {
					db.update("Jfjb_app", values, "id = ?", new String[]{i+1+""});
				}
				
				Log.e("�������ݳɹ���", db.getPath());
				}
				
				
			
			switch (flag) {
			case 1:
				for(int i = 0;i<array.length();i++)
				{
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject temp = (JSONObject)array.get(i);				
				map.put("title", temp.getString("title"));
				map.put("num","�Ķ�     "+temp.getString("num"));		
				map.put("likes","����     "+temp.getString("likes"));
				map.put("date","����ʱ�䣺"+ temp.getString("date"));
				map.put("dotime", "����ʱ�䣺"+temp.getString("dotime"));
				map.put("imglink",temp.getString("imglink"));
				map.put("contentUrl", temp.getString("url"));
				list.add(map);
				}
				break;

			case 2:
				for(int i = 0;i<array.length();i++)
				{
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject temp = (JSONObject)array.get(i);
				map.put("title", temp.getString("title"));
				map.put("num","�Ķ�     "+temp.getString("num"));		
				map.put("likes","����     "+temp.getString("likes"));
				map.put("date","����ʱ�䣺"+ temp.getString("date"));
				map.put("dotime", "����ʱ�䣺"+temp.getString("dotime"));
				map.put("imglink",temp.getString("imglink"));
				map.put("contentUrl", temp.getString("url"));
				list.set(i, map);
				}
				break;
			}
			}
			/** û����������*/
			else {
				switch (flag) {
				case 1:
					for(int i = 0;i<30;i++)
					{
					Map<String, Object> map = new HashMap<String, Object>();					
					map.put("title", "���±���δȡ��");
					map.put("num","�Ķ�      δȡ��");		
					map.put("likes","����   δȡ��");
					map.put("date","����ʱ�䣺 δȡ��");
					map.put("dotime", "����ʱ�䣺δȡ��");
					map.put("imglink","δȡ��");
					map.put("contentUrl","");
					list.add(map);
					}
					break;

				case 2:
					break;
				}
				
				
			}
		} catch (JSONException e) {		
			e.printStackTrace();
		}
			
		object = list;
		return object;
	}
	   
	@Override
		protected void onPostExecute(Object object) {
			super.onPostExecute(object);
			List<Map<String, Object>> list = (List<Map<String, Object>>) object;
			switch (flag) {
			case 1:
				mAdapter = new ListItemAdapter(getActivity(), list, cacheImageMap);
				mListView.setAdapter(mAdapter);
				break;

			case 2:
				mAdapter.notifyDataSetChanged();
				mListView.setAdapter(mAdapter);
				break;
			}
			
			
			/** ��list�е�url���ݸ�urllist */
			for(int i = 0;i<list.size();i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("contentUrl", list.get(i).get("contentUrl"));
				urllist.add(map);
			}
			
			/**�����ת��url */
			mListView.setOnItemClickListener(new OnItemClickListener() {
			
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri control_url = Uri.parse((String) urllist.get(position-1).get("contentUrl"));
					intent.setData(control_url);							
					getActivity().startActivity(intent);
				}
			});
			progressDialog.dismiss();
		}
	 
	 
   }
	
	
	
   class ListItemAdapter extends BaseAdapter{
		protected Map<String, Bitmap> cacheImageMap = null;
		private Context context;
		private List<Map<String, Object>> list;
		private LayoutInflater inflater;
				
		public ListItemAdapter(Context context,List<Map<String, Object>>  list,Map<String, Bitmap> cacheImageMap){
			this.cacheImageMap = cacheImageMap;
			this.context =context;
			this.list = list;
			this.inflater = LayoutInflater.from(context);
		}
		
		public void addList(List<Map<String, Object>> list) {
			this.list.addAll(list);
		}
		
		@Override
		public int getCount() {
			return this.list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
			
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = this.inflater.inflate(R.layout.list_item, null);
			    holder.title = (TextView)convertView.findViewById(R.id.item_title);
			    holder.num = (TextView)convertView.findViewById(R.id.item_readnum);			 
			    holder.likes = (TextView)convertView.findViewById(R.id.item_zannum);
			    holder.dotime = (TextView)convertView.findViewById(R.id.item_time);
			    holder.imglink = (ImageView)convertView.findViewById(R.id.user);
			    //����
			    /*holder.date = (TextView)convertView.findViewById(R.id.item_dotime);			*/    
			    convertView.setTag(holder);	
		}   else{
			holder = (ViewHolder) convertView.getTag();
			}
			
			String title = (String) list.get(position).get("title");
			String num = (String) list.get(position).get("num");
			String likes = (String) list.get(position).get("likes");
			String dotime = (String) list.get(position).get("dotime");
			String imglink = (String) list.get(position).get("imglink");
		/*	String date = (String) list.get(position).get("date");*/						 
		if(title.equals("���±���δȡ��")){
			  /** ��SQLiteDatebase ȡ���� */			
			 Cursor cursor = db.rawQuery("select * from Jfjb_app", null);
			 Log.e("cursor",cursor.toString());
			 List<Map<String, String>> cursorlist= cursorToList(cursor);
			 Log.e("�����ݿ�ȡ������", cursorlist.toString());
			 holder.title.setText(cursorlist.get(position).get("title"));
				/**  ���ļӴ�    */
				TextPaint tPaint = holder.title.getPaint();
				tPaint.setFakeBoldText(true);
				holder.num.setText(cursorlist.get(position).get("num"));
				/*holder.date.setText(date);*/
				holder.likes.setText(cursorlist.get(position).get("likes"));
				holder.dotime.setText(cursorlist.get(position).get("dotime"));
			
			
			
				/** ��SD����ȡ ͼƬ */
				Bitmap bm = useTheImage(position);
				if(bm!=null){
					holder.imglink.setImageBitmap(bm);			}
				else{
					holder.imglink.setImageResource(R.drawable.junbaojizhe);
				}
			
			
			
		}
		else {
			holder.title.setText(title);
			/**  ���ļӴ�    */
			TextPaint tPaint = holder.title.getPaint();
			tPaint.setFakeBoldText(true);
			holder.num.setText(num);
			/*holder.date.setText(date);*/
			holder.likes.setText(likes);
			holder.dotime.setText(dotime);
			if(cacheImageMap.get(imglink)!=null){				
			holder.imglink.setImageBitmap(cacheImageMap.get(imglink));
				Log.e("ִ���߳�","+++++++++++++++++++");			
			}
			else{
				/* �����ڣ���ʼ�������磬������ͼƬ */	
				new MyTask1(holder.imglink,imglink,position).execute(imglink);
			}	
		}		
		return convertView;

	  }
		private  final class ViewHolder {
			private TextView title;
			private TextView num;		
			private TextView likes;
	/*	����ʱ��	private TextView date;*/
			private TextView dotime;
			private ImageView imglink;
		}
			
		class MyTask1 extends AsyncTask<String, Void, Object>{

			private ImageView image;
			/** ��Ϊ����������ͼƬ��ַ�ַ��� */
			private String urlStr;
			int position;
			public MyTask1 (ImageView image,String urlStr,int position){
				this.image = image;
				this.urlStr = urlStr;
				this.position=position;
			}
			@Override
			protected Object doInBackground(String... params) {
				Object object = null;
				Log.e("��ʼ����", "============");
				byte[] bitmapByte = HttpClientHelper.loadByteFromURL(params[0]);
				Log.e("ͼƬ����", bitmapByte.toString());
				object = bitmapByte;
				return object;
			}	
			@Override
			protected void onPostExecute(Object object) {
				
				super.onPostExecute(object);
				byte[] result = (byte[]) object;		
				Log.e("ͼƬbyte", result.toString());
				
				Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,result.length);	
				Bitmap nBitmap = zoomImg(bitmap, 100, 100);
				image.setImageBitmap(nBitmap);				
				cacheImageMap.put(urlStr,nBitmap);						
				saveFile(nBitmap, position);
			}		
		}	
	}		
    private void  Onload() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		
	}
	@Override
	public void onRefresh() {
		new MyTask(getActivity(),2).execute(Urlstr);
		Onload();
		mListView.setRefreshTime(time);
		calendar = Calendar.getInstance();
		time = 	  formatTime(calendar.get(Calendar.MONTH)+1)+"-"+   //month��һ    //��
				  formatTime(calendar.get(Calendar.DAY_OF_MONTH))+" "+                    //��
				  formatTime(calendar.get(Calendar.HOUR_OF_DAY))+":"+                      //ʱ
				  formatTime(calendar.get(Calendar.MINUTE))+":"+                           //��
				  formatTime(calendar.get(Calendar.SECOND));
	}
	@Override
	public void onLoadMore() {
		Onload();
			
	}

	/** ��calendarת��Ϊint��ʽ */
	private String formatTime(int t){
		  return t>=10? ""+t:"0"+t;//��Ԫ����� t>10ʱȡ ""+t
		 }
	
	/**  
	 *  ����ͼƬ   
	 * @param bm ��Ҫת����bitmap  
	 * @param newWidth�µĿ�  
	 * @param newHeight�µĸ�    
	 * @return ָ����ߵ�bitmap  
	 */  
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){    
	    // ���ͼƬ�Ŀ��    
	    int width = bm.getWidth();    
	    int height = bm.getHeight();    
	    // �������ű���    
	    float scaleWidth = ((float) newWidth) / width;    
	    float scaleHeight = ((float) newHeight) / height;    
	    // ȡ����Ҫ���ŵ�matrix����    
	    Matrix matrix = new Matrix();    
	    matrix.postScale(scaleWidth, scaleHeight);    
	    // �õ��µ�ͼƬ    
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);    
	    return newbm;    
	}   
	 
	 /**
	     * ����ͼƬ��SD����
	     *
	     * @param bm
	     * @param fileName
	     *
	     */
	 public void saveFile(Bitmap bm , int position) {
	        try {	                        
	        	String fileNa =position+"";
	            File file = new File(path + "/image/" + fileNa);
	            // ����ͼƬ�����ļ���
	            boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
	            if (sdCardExist) {
	                File maiduo = new File(path);
	                File ad = new File(path + "/image");
	                // ����ļ��в�����
	                if (!maiduo.exists()) {
	                    // ����ָ����·�������ļ���
	                    maiduo.mkdir();
	                    // ����ļ��в�����
	                } else if (!ad.exists()) {
	                    // ����ָ����·�������ļ���
	                    ad.mkdir();
	                }
	                // ���ͼƬ�Ƿ����	                
	                    file.createNewFile();	                
	            }	           
	            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
	            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
	            Log.e("ͼƬ����", fileNa.toString());
	            bos.flush();
	            bos.close();
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	    }
		  
	 
	 /**
	     * ʹ��SD���ϵ�ͼƬ
	     *
	     */
	    public Bitmap useTheImage(int position) {
	        Bitmap bmpDefaultPic = null;
	        // ����ļ�·��
	        String imageSDCardPath = path + "/image/" +position;
	        File file = new File(imageSDCardPath);
	        // ���ͼƬ�Ƿ����
	        if (!file.exists()) {
	            return null;
	        }
	        bmpDefaultPic = BitmapFactory.decodeFile(imageSDCardPath, null);    
	        if (bmpDefaultPic != null || bmpDefaultPic.toString().length() > 3){
	        	  return bmpDefaultPic;
	        }
	          
	         else
	        {
	            return null;
	    	}
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


