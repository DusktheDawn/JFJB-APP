package com.JFJB.fragment;




import com.JFJB.adapter.ListItemAdapter1;
import com.JFJB.app.R;
import com.JFJB.item.MyListItem;


import java.util.ArrayList;
import java.util.List;

import com.JFJB.widget.XListView;
import com.JFJB.widget.XListView.IXListViewListener;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WeiboTabFragment extends Fragment implements IXListViewListener
{
	protected View view;
	private XListView mListView;
	private ListItemAdapter1 mAdapter;
	private List<MyListItem> items ;
	
	private android.os.Handler mHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//super方法必须被调用
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view= inflater.inflate(R.layout.tab02, container, false);
		items=geneItems();
		mListView = (XListView)view.findViewById(R.id.xlistview2);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);
		/*mAdapter = new ListItemAdapter(getActivity(), items);*/
		mListView.setAdapter(mAdapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		
		mListView.setXListViewListener(this);
		mHandler= new android.os.Handler();
		return  view;
	}

	private List<MyListItem> geneItems() {
		List<MyListItem> items= new ArrayList<MyListItem>(); 
		try {
			for (int i = 0; i != 30; ++i) {
				MyListItem listItem =new MyListItem();
				listItem.setTitle("微博文章标题" + i);
				listItem.setNum("阅读数：100");
				listItem.setLikes("转发数：50");
				
				listItem.setDotime("2015年2月9日");
				listItem.setId(R.drawable.junbaojizhe);
				items.add(listItem);	
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		return items;
	}
	 private void  Onload() {
			mListView.stopRefresh();
			mListView.stopLoadMore();
			mListView.setRefreshTime("1天前");
		}
	 
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					MyListItem listItem =new MyListItem();
					listItem.setTitle("上拉刷新出的微信文章标题");
					listItem.setNum("评论数：200");
					listItem.setLikes("转发数：300");
				
					listItem.setDotime("2015年2月9日");
					listItem.setId(R.drawable.junbaojizhe);
					items.add(listItem);
				/*	mAdapter=new ListItemAdapter(getActivity(), items);*/
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					Onload();
				} catch (ParseException e) {
					e.printStackTrace();
				}			
			}
		}, 2000);
	}

	@Override
public void onLoadMore() {
		
		
	    mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
						for (int j=0;j<=5;j++){
						MyListItem  listItem = new MyListItem();
						listItem.setTitle("下拉刷新出的微博文章标题"+j);
						listItem.setNum("评论数：200");
						listItem.setLikes("转发数：300");
						
						listItem.setDotime("2015年2月10日");
						
						listItem.setId(R.drawable.junbaojizhe);
						items.add(listItem);
						mAdapter.notifyDataSetChanged();
						Onload();}
				} catch (ParseException e) {
					e.getStackTrace();
				} 				
				
			}
		},2000);
			
	}
	
}
