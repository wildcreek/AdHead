package com.example.adhead;

import java.util.ArrayList;
import java.util.List;

 

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	//图片资源
	private int[] imgs = new int[]{
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
			R.drawable.e
	};
	
	private String[] infos = new String[]{
			"巩俐不低俗，我就不能低俗",
	        "扑树又回来啦！再唱经典老歌引万人大合唱",
	        "揭秘北京电影如何升级",
	        "乐视网TV版大派送",
	        "热血屌丝的反杀"
	};

	//初始化页面资源
	List<ImageView> list;
	private MyPagerAdapter adapter;
	private ViewPager vp;
	private TextView tv_info;
	private LinearLayout ll_point;
	
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		vp = (ViewPager) findViewById(R.id.vp);
		tv_info = (TextView) findViewById(R.id.tv_info);
		ll_point = (LinearLayout) findViewById(R.id.ll_point);
		tv_info.setText(infos[0]);
		
		//添加小圆点（黑色（可以点击   可用）  白色     没有提供 需要自己绘制  xml  shape）
		//可用  不可用  selector选择器
		for(int i = 0;i<imgs.length;i++){
			View view = new View(this);
			view.setEnabled(true);
			view.setBackgroundResource(R.drawable.point_bg);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
			params.rightMargin = 10;
			ll_point.addView(view, params);
			
			view.setOnClickListener(this);
			//给控件一个唯一标示
			view.setTag(i);
			view.setId(i);
		}
		//默认第一个点是不可用的
		ll_point.getChildAt(0).setEnabled(false);
		
		//初始化数据
		list = new ArrayList<ImageView>();
		for(int resid:imgs){
			ImageView iv = new ImageView(this);
			iv.setBackgroundResource(resid);
			list.add(iv);
		}
		
		adapter = new MyPagerAdapter();
		vp.setAdapter(adapter);
		
		//设置页面滑动监听
		vp.setOnPageChangeListener(new MyOnPagerChangeListener());
		
	}
	
	private class SwitchTask implements Runnable{

		//3s后该方法被调用
		@Override
		public void run() {
			//获取到当期显示的页面
			int item = vp.getCurrentItem();
			if(item == adapter.getCount() - 1){
				//已经是最后一页
				item = 0;
			}else{
				item++;
			}
			vp.setCurrentItem(item);
			
			mHandler.postDelayed(this, 3000);
		}
		
	}
	
	//在Activity显示在前台的时候就需要执行页面的滑动  到了后太页面的滑动需要停止
	//检测activity的生命周期
	@Override
	protected void onResume() {
		super.onResume();
		//准备滑动  3s滑动一页
//		mHandler.sendMessageDelayed(msg, delayMillis)  通过这样的方式来处理  是需要去重写Handler里面的handleMessage()方法
		mHandler.postDelayed(new SwitchTask(), 3000);//这里其实还是把Runnable封装成了Message里面的callback属性 最后直接调用run()方法
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		//停止   移除mHandler里面所有的消息 还有Runnable
		mHandler.removeCallbacksAndMessages(null);
	}
	
	
	
	
	
	private class MyOnPagerChangeListener implements OnPageChangeListener{

		//页面滑动状态的改变
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		//滑动完成
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		//页面被选择   int position 当前选择也的下标
		@Override
		public void onPageSelected(int position) {
			tv_info.setText(infos[position]);
			int childCount = ll_point.getChildCount();
			for(int i = 0;i<childCount;i++){
				View view = ll_point.getChildAt(i);
				if(i == position){
					//不可用
					view.setEnabled(false);
				}else{
					view.setEnabled(true);
				}
			}
				
			
		}
		
	}
	
	
	private class MyPagerAdapter extends PagerAdapter{
		//有多个个页面
		@Override
		public int getCount() {
			return list.size();
		}
		//判断要显示的页面是否为缓存也
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		//实例化页面
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = list.get(position);//获取要显示的页
			vp.addView(view);//自己手动的添加给ViewPager
			return view;
		}
		//销毁页面
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = list.get(position);//获取要销毁的页
			vp.removeView(view);
		}
		
	}


	//点被点击
	@Override
	public void onClick(View v) {
		int i = (Integer) v.getTag();
		// 页面需要切换到选择页
		vp.setCurrentItem(i);
	}


}
