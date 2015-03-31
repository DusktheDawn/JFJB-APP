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
	//viewpager������
	private FragmentPagerAdapter mAdapter;
	//fragment����Դ
	private List<Fragment> mDatas;
    //����ҳ���tab
	private TextView weixinTextView;
	//���汻ע�͵���һ�д����ǵڶ���ҳ�棬����׼����3��ҳ��ģ�����������ݵ�ԭ��ֻ��2��ҳ�棬���ĺ�mFriendTextView��صı�ע�͵Ĵ��붼�ǵڶ���ҳ��Ĵ���
/*	private TextView mFriendTextView;*/
	private TextView appDownloadTextView;
	
	/*private LinearLayout mChatLinearLayout;
	
	private ImageView mTabline;
	private int mScreen1_2;

	//��ǰҳ���ǩ
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
	
	
	/** ��ʼ��ָʾ�� **/
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
		//��tab�ϵı�ǩ���ü�����
		weixinTextView.setOnClickListener(this);
		appDownloadTextView.setOnClickListener(this);
		
		mDatas = new ArrayList<Fragment>();
		WeixinTabFragment tab01 = new WeixinTabFragment();
		/*WeiboTabFragment tab02 = new WeiboTabFragment();*/
		DownloadappTabFragment tab03 = new DownloadappTabFragment();
        //��3��fragment���뵽����Դ��ȥ
		mDatas.add(tab01);
		/*mDatas.add(tab02);*/
		mDatas.add(tab03);
		initAdapter();
		
		//��viewpager���û�������ʹ�ø��������״̬�ܹ�����
		mViewPager.setOffscreenPageLimit(2);
		
	}
	
	//�������������ǩ������ת����Ӧ����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_tv_chat:
			mViewPager.setCurrentItem(0);
			
			//������ť�ظ���Ϊ��ͨ״̬���Լ�Ϊ����״̬
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
	
	//ѡ��΢��ʱ������Ϊ����״̬
	private void setweixinTextSelectedState() {
		weixinTextView.setBackgroundResource(R.drawable.iask_bottom_tab_ask_pressed);
		appDownloadTextView.setBackgroundResource(R.drawable.iask_bottom_tab_answer_bg);	
	}
	
	private void setappTextSelectedState() {
		weixinTextView.setBackgroundResource(R.drawable.iask_bottom_tab_answer_bg);
		appDownloadTextView.setBackgroundResource(R.drawable.iask_bottom_tab_ask_pressed);	
	}
	
	
	/**
	 * ����viewpagerAdapter
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
			 * ����viewpager�Ļ�������ͬʱ�ı�mTabline��λ��
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
		         * ����position��currentIndex�ж��û��Ĳ�������һҳ����һҳ����
		         * Ȼ��ı����positionOffset��̬�ı�TabLine��leftMargin
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
