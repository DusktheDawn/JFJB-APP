package com.JFJB.adapter;


import java.util.List;
import java.util.Map;

import com.JFJB.app.R;
import com.JFJB.helper.HttpClientHelper;
import com.JFJB.item.MyListItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ListItemAdapter1 extends BaseAdapter{
	protected Map<String, Bitmap> cacheImageMap = null;
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	
	
	public ListItemAdapter1(Context context,List<Map<String, Object>>  list,Map<String, Bitmap> cacheImageMap){
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
		    
		    //更新时间
		   /* holder.date = (TextView)convertView.findViewById(R.id.item_dotime);*/
		    
		    convertView.setTag(holder);	
	}   else{
		holder = (ViewHolder) convertView.getTag();
		}
		
		String title = (String) list.get(position).get("title");
		String num = (String) list.get(position).get("num");
		String likes = (String) list.get(position).get("likes");
		String dotime = (String) list.get(position).get("dotime");
		String imglink = (String) list.get(position).get("imglink");
		String date = (String) list.get(position).get("date");
	holder.title.setText(title);
	holder.num.setText(num);
	holder.date.setText(date);
	holder.likes.setText(likes);
	holder.dotime.setText(dotime);
	/*holder.imglink.setImageResource(R.drawable.junbaojizhe);*/
	if (cacheImageMap.get(imglink) == null) {
		/* 不存在，开始请求网络，并设置图片 */	
	new MyTask(holder.imglink,imglink).execute(imglink);
	}
	else {
		holder.imglink.setImageBitmap(cacheImageMap.get(imglink));
	}
	return convertView;

  }
	private static final class ViewHolder {
		private TextView title;
		private TextView num;		
		private TextView likes;
		private TextView date;
		private TextView dotime;
		private ImageView imglink;
	}
	
	
	class MyTask extends AsyncTask<String, Void, Object>{

		private ImageView image;
		/** 作为缓存下来的图片网址字符串 */
		private String urlStr;
		public MyTask (ImageView image,String urlStr){
			this.image = image;
			this.urlStr = urlStr;
		}
		@Override
		protected Object doInBackground(String... params) {
			Object object = null;
			byte[] bitmapByte = HttpClientHelper.loadByteFromURL(params[0]);
			object = bitmapByte;
			return object;
		}
		
		@Override
		protected void onPostExecute(Object object) {
			
			super.onPostExecute(object);
			byte[] result = (byte[]) object;
			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
					result.length);
			image.setImageBitmap(bitmap);
			cacheImageMap.put(urlStr,bitmap);
			
		}

		
		
	}
	
}