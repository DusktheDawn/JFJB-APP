package com.JFJB.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
/*import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;*/
import android.view.View;
import android.view.View.OnClickListener;
/*import android.view.ViewGroup.LayoutParams;*/
import android.view.Window;
/*import android.widget.ImageView;
import android.widget.LinearLayout;*/
import android.widget.TextView;

import com.JFJB.fragment.DownloadappTabFragment;
/*import com.JFJB.fragment.WeiboTabFragment;*/
import com.JFJB.fragment.WeixinTabFragment;
import com.JFJB.app.R;

public class MainActivity extends FragmentActivity implements OnClickListener
{
	private ViewPager mViewPager;
	//viewpager适配器
	private FragmentPagerAdapter mAdapter;
	//fragment数据源
	private List<Fragment> mDatas;
    //三个页面的tab
	private TextView weixinTextView;
	//下面被注释的这一行代码是第二个页面，本来准备做3个页面的，最后由于数据的原因只做2个页面，下文和mFriendTextView相关的被注释的代码都是第二个页面的代码
/*	private TextView mFriendTextView;*/
	private TextView appDownloadTextView;
	
	/*private LinearLayout mChatLinearLayout;
	
	private ImageView mTabline;
	private int mScreen1_2;

	//当前页面标签
	private int mCurrentPageIndex;*/

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		/*initTabLine();*/
		initView();
	}
	
	
	/** 初始化指示线 **/
/*	private void initTabLine()
	{
		mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		mScreen1_2 = outMetrics.widthPixels / 2;
		LayoutParams lp = mTabline.getLayoutParams();
		lp.width = mScreen1_2;
		mTabline.setLayoutParams(lp);
	}
*/
	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		weixinTextView = (TextView) findViewById(R.id.id_tv_chat);
	/*	mFriendTextView = (TextView) findViewById(R.id.id_tv_friend);*/
		appDownloadTextView = (TextView) findViewById(R.id.id_tv_contact);
	/*	mChatLinearLayout = (LinearLayout) findViewById(R.id.id_ll_chat);*/
		//给tab上的标签设置监听器
		weixinTextView.setOnClickListener(this);
		appDownloadTextView.setOnClickListener(this);
		
		mDatas = new ArrayList<Fragment>();
		WeixinTabFragment tab01 = new WeixinTabFragment();
		/*WeiboTabFragment tab02 = new WeiboTabFragment();*/
		DownloadappTabFragment tab03 = new DownloadappTabFragment();
        //将3个fragment加入到数据源中去
		mDatas.add(tab01);
		/*mDatas.add(tab02);*/
		mDatas.add(tab03);
		initAdapter();
		
		//给viewpager设置缓冲区，使得各个界面的状态能够保存
		mViewPager.setOffscreenPageLimit(2);
		
	}
	
	//监听器：点击标签名，跳转到相应界面
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tv_chat:
			mViewPager.setCurrentItem(0);
			
			//其他按钮回复到为普通状态，自己为按下状态
			setweixinTextSelectedState();
			break;
       
		case R.id.id_tv_contact:
			mViewPager.setCurrentItem(1);
			setappTextSelectedState();
			/*LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
					.getLayoutParams();
			lp.rightMargin = 0; */
			break;
		default:
			break;
		}
		
	}
	
	//选中微信时，设置为按下状态
	private void setweixinTextSelectedState() {
		weixinTextView.setBackgroundResource(R.drawable.iask_bottom_tab_ask_pressed);
		appDownloadTextView.setBackgroundResource(R.drawable.iask_bottom_tab_answer_bg);	
	}
	
	private void setappTextSelectedState() {
		weixinTextView.setBackgroundResource(R.drawable.iask_bottom_tab_answer_bg);
		appDownloadTextView.setBackgroundResource(R.drawable.iask_bottom_tab_ask_pressed);	
	}
	
	
	/**
	 * 设置viewpagerAdapter
	 */
	private void initAdapter() {
		// TODO Auto-generated method stub
		mAdapter = new FragmentPagerAdapter(	getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mDatas.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mDatas.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				weixinTextView.setTextColor(Color.BLACK);
			/*	mFriendTextView.setTextColor(Color.BLACK);*/
				appDownloadTextView.setTextColor(Color.BLACK);
				switch (position)
				{
				case 0:
					weixinTextView.setTextColor(Color.parseColor("#008000"));
					setweixinTextSelectedState();
					break;
				/*case 1:
					mFriendTextView.setTextColor(Color.parseColor("#008000"));
					break;*/
				case 1:
					appDownloadTextView.setTextColor(Color.parseColor("#008000"));
					setappTextSelectedState();
					break;

				}

				/*mCurrentPageIndex = position;*/

			}

			/**
			 * 监听viewpager的滑动，并同时改变mTabline的位置
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPx)
			{/*
				Log.e("TAG", position + " , " + positionOffset + " , "
						+ positionOffsetPx);
				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
						.getLayoutParams();*/
				/**
		         * 利用position和currentIndex判断用户的操作是哪一页往哪一页滑动
		         * 然后改变根据positionOffset动态改变TabLine的leftMargin
		         */
			/*	if (mCurrentPageIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (positionOffset * mScreen1_2 + mCurrentPageIndex
							* mScreen1_2);
				} else if (mCurrentPageIndex == 1 && position == 0)// 1->0
				{
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + (positionOffset - 1)
							* mScreen1_2);
				}*/ /*else if (mCurrentPageIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + positionOffset
							* mScreen1_2);
				} else if (mCurrentPageIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + ( positionOffset-1)
							* mScreen1_2);
				}*/
				/*mTabline.setLayoutParams(lp);*/

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	

}
