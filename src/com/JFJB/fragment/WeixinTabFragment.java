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

	/** 用来填充整个listview布局 */
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
		super.onCreate(savedInstanceState);//super方法必须被调用
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
 * 获取数据并将数据装载到adapter
 */

   class MyTask extends AsyncTask<String, Void, Object>{
	   	private Context context;
	   private ProgressDialog progressDialog;	
	   /** 操作标记，1代表第一次加载，2代表刷新**/
		private int flag;
		
		
		public MyTask (Context context,int flag) 
		{
			context=this.context;
			this.flag = flag;
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("网络访问");
			progressDialog.setMessage("正在加载...");			
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
			/** 如果请求到数据 **/
			if(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8")!=null){
				JSONArray array = new JSONArray(HttpClientHelper.loadTextFromURL(
					Urlstr, "UTF-8"));
				Log.e("JSON取到！", array.toString());
				for(int i = 0;i<array.length();i++)
				{
				ContentValues values = new ContentValues();
				JSONObject temp = (JSONObject)array.get(i);		
				values.put("id",i+1);
				values.put("title", temp.getString("title"));
				values.put("num","阅读     "+temp.getString("num"));		
				values.put("likes","点赞     "+temp.getString("likes"));			
				values.put("dotime", "更新时间："+temp.getString("dotime"));
				Log.e("values装载完毕", values.toString());
				
				Cursor scCursor = db.rawQuery("select * from Jfjb_app where id =?", new String[]{i+1+""});
				Log.e("JFJB中的cursor", "scCursor.moveToFirst()=="+scCursor.moveToFirst());
				if(scCursor.moveToFirst()==false){
					db.insert("Jfjb_app", null, values);
				}
				else {
					db.update("Jfjb_app", values, "id = ?", new String[]{i+1+""});
				}
				
				Log.e("更新数据成功！", db.getPath());
				}
				
				
			
			switch (flag) {
			case 1:
				for(int i = 0;i<array.length();i++)
				{
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject temp = (JSONObject)array.get(i);				
				map.put("title", temp.getString("title"));
				map.put("num","阅读     "+temp.getString("num"));		
				map.put("likes","点赞     "+temp.getString("likes"));
				map.put("date","发布时间："+ temp.getString("date"));
				map.put("dotime", "更新时间："+temp.getString("dotime"));
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
				map.put("num","阅读     "+temp.getString("num"));		
				map.put("likes","点赞     "+temp.getString("likes"));
				map.put("date","发布时间："+ temp.getString("date"));
				map.put("dotime", "更新时间："+temp.getString("dotime"));
				map.put("imglink",temp.getString("imglink"));
				map.put("contentUrl", temp.getString("url"));
				list.set(i, map);
				}
				break;
			}
			}
			/** 没有请求到数据*/
			else {
				switch (flag) {
				case 1:
					for(int i = 0;i<30;i++)
					{
					Map<String, Object> map = new HashMap<String, Object>();					
					map.put("title", "文章标题未取到");
					map.put("num","阅读      未取到");		
					map.put("likes","点赞   未取到");
					map.put("date","发布时间： 未取到");
					map.put("dotime", "更新时间：未取到");
					map.put("imglink","未取到");
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
			
			
			/** 将list中的url传递给urllist */
			for(int i = 0;i<list.size();i++)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("contentUrl", list.get(i).get("contentUrl"));
				urllist.add(map);
			}
			
			/**点击跳转到url */
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
			    //发布
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
		if(title.equals("文章标题未取到")){
			  /** 从SQLiteDatebase 取数据 */			
			 Cursor cursor = db.rawQuery("select * from Jfjb_app", null);
			 Log.e("cursor",cursor.toString());
			 List<Map<String, String>> cursorlist= cursorToList(cursor);
			 Log.e("从数据库取到数据", cursorlist.toString());
			 holder.title.setText(cursorlist.get(position).get("title"));
				/**  中文加粗    */
				TextPaint tPaint = holder.title.getPaint();
				tPaint.setFakeBoldText(true);
				holder.num.setText(cursorlist.get(position).get("num"));
				/*holder.date.setText(date);*/
				holder.likes.setText(cursorlist.get(position).get("likes"));
				holder.dotime.setText(cursorlist.get(position).get("dotime"));
			
			
			
				/** 从SD卡里取 图片 */
				Bitmap bm = useTheImage(position);
				if(bm!=null){
					holder.imglink.setImageBitmap(bm);			}
				else{
					holder.imglink.setImageResource(R.drawable.junbaojizhe);
				}
			
			
			
		}
		else {
			holder.title.setText(title);
			/**  中文加粗    */
			TextPaint tPaint = holder.title.getPaint();
			tPaint.setFakeBoldText(true);
			holder.num.setText(num);
			/*holder.date.setText(date);*/
			holder.likes.setText(likes);
			holder.dotime.setText(dotime);
			if(cacheImageMap.get(imglink)!=null){				
			holder.imglink.setImageBitmap(cacheImageMap.get(imglink));
				Log.e("执行线程","+++++++++++++++++++");			
			}
			else{
				/* 不存在，开始请求网络，并设置图片 */	
				new MyTask1(holder.imglink,imglink,position).execute(imglink);
			}	
		}		
		return convertView;

	  }
		private  final class ViewHolder {
			private TextView title;
			private TextView num;		
			private TextView likes;
	/*	发布时间	private TextView date;*/
			private TextView dotime;
			private ImageView imglink;
		}
			
		class MyTask1 extends AsyncTask<String, Void, Object>{

			private ImageView image;
			/** 作为缓存下来的图片网址字符串 */
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
				Log.e("开始下载", "============");
				byte[] bitmapByte = HttpClientHelper.loadByteFromURL(params[0]);
				Log.e("图片下载", bitmapByte.toString());
				object = bitmapByte;
				return object;
			}	
			@Override
			protected void onPostExecute(Object object) {
				
				super.onPostExecute(object);
				byte[] result = (byte[]) object;		
				Log.e("图片byte", result.toString());
				
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
		time = 	  formatTime(calendar.get(Calendar.MONTH)+1)+"-"+   //month加一    //月
				  formatTime(calendar.get(Calendar.DAY_OF_MONTH))+" "+                    //日
				  formatTime(calendar.get(Calendar.HOUR_OF_DAY))+":"+                      //时
				  formatTime(calendar.get(Calendar.MINUTE))+":"+                           //分
				  formatTime(calendar.get(Calendar.SECOND));
	}
	@Override
	public void onLoadMore() {
		Onload();
			
	}

	/** 将calendar转化为int格式 */
	private String formatTime(int t){
		  return t>=10? ""+t:"0"+t;//三元运算符 t>10时取 ""+t
		 }
	
	/**  
	 *  处理图片   
	 * @param bm 所要转换的bitmap  
	 * @param newWidth新的宽  
	 * @param newHeight新的高    
	 * @return 指定宽高的bitmap  
	 */  
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){    
	    // 获得图片的宽高    
	    int width = bm.getWidth();    
	    int height = bm.getHeight();    
	    // 计算缩放比例    
	    float scaleWidth = ((float) newWidth) / width;    
	    float scaleHeight = ((float) newHeight) / height;    
	    // 取得想要缩放的matrix参数    
	    Matrix matrix = new Matrix();    
	    matrix.postScale(scaleWidth, scaleHeight);    
	    // 得到新的图片    
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);    
	    return newbm;    
	}   
	 
	 /**
	     * 保存图片到SD卡上
	     *
	     * @param bm
	     * @param fileName
	     *
	     */
	 public void saveFile(Bitmap bm , int position) {
	        try {	                        
	        	String fileNa =position+"";
	            File file = new File(path + "/image/" + fileNa);
	            // 创建图片缓存文件夹
	            boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
	            if (sdCardExist) {
	                File maiduo = new File(path);
	                File ad = new File(path + "/image");
	                // 如果文件夹不存在
	                if (!maiduo.exists()) {
	                    // 按照指定的路径创建文件夹
	                    maiduo.mkdir();
	                    // 如果文件夹不存在
	                } else if (!ad.exists()) {
	                    // 按照指定的路径创建文件夹
	                    ad.mkdir();
	                }
	                // 检查图片是否存在	                
	                    file.createNewFile();	                
	            }	           
	            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
	            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
	            Log.e("图片保存", fileNa.toString());
	            bos.flush();
	            bos.close();
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	    }
		  
	 
	 /**
	     * 使用SD卡上的图片
	     *
	     */
	    public Bitmap useTheImage(int position) {
	        Bitmap bmpDefaultPic = null;
	        // 获得文件路径
	        String imageSDCardPath = path + "/image/" +position;
	        File file = new File(imageSDCardPath);
	        // 检查图片是否存在
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
	    
	    /**cursor 对象转化为list */
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
			Log.e("数据库中的list", list.toString());
			if (cursor != null) {
				cursor.close();
			}
			return list;
		}
		    
}


