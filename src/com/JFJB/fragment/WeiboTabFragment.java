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
		super.onCreate(savedInstanceState);//super�������뱻����
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
				listItem.setTitle("΢�����±���" + i);
				listItem.setNum("�Ķ�����100");
				listItem.setLikes("ת������50");
				
				listItem.setDotime("2015��2��9��");
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
			mListView.setRefreshTime("1��ǰ");
		}
	 
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					MyListItem listItem =new MyListItem();
					listItem.setTitle("����ˢ�³���΢�����±���");
					listItem.setNum("��������200");
					listItem.setLikes("ת������300");
				
					listItem.setDotime("2015��2��9��");
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
						listItem.setTitle("����ˢ�³���΢�����±���"+j);
						listItem.setNum("��������200");
						listItem.setLikes("ת������300");
						
						listItem.setDotime("2015��2��10��");
						
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
