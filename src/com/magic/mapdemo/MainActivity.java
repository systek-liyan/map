package com.magic.mapdemo;


import com.magic.mapdemo.DrawView;
import com.magic.mapdemo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.magic.map.IMapLoaderCallback;
import com.magic.map.MapManager;
import com.magic.map.resource.poi.PoiFilter;
import com.magic.map.util.MapData;
import com.magic.map.util.PoiData;
import com.magic.map.view.MapController;
import com.magic.map.widget.MapView;
import com.magic.map.widget.onMapListener;


@SuppressLint("NewApi") public class MainActivity extends Activity implements IMapLoaderCallback,
		onMapListener, android.view.View.OnClickListener {
	private static final String TAG = "地图";
	private MapManager mManager = null;
	private MapView mMapView = null;
	private PoiFilter mFilter = null;
	private TextView mPoiName = null;
	private TextView mPoiDescription = null;
	private TextView mPoiLocation = null;
	private ViewGroup mPoi = null;
	private Button mBtnStart = null;
	private Button mBtnEnd = null;
	private Button mBtnDel = null;
//	private Button mBtnRoute = null;
	public  PointF mTouchPoint = null;
	private long mPoiId ;
	private PoiData mStart = null;
	private PoiData mEnd = null;
	private PoiData mPoiData = null;
	
	private MapController mController;
	private ViewOverlay mapOverlay;
	private Context self;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = new MapManager(this);
		mManager.init();
		mManager.addOnMapListener(this);
		setContentView(R.layout.activity_main);
		initComponents();
		
		
		
		FrameLayout root = (FrameLayout)findViewById(R.id.root);	
		final DrawView draw = new DrawView(this);
		draw.setMinimumWidth(300); 
		draw.setMinimumHeight(500);
		root.addView(draw);
	}


	private void initComponents() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setClickable(true);	
		//mapOverlay=mMapView.getOverlay();
		//mapOverlay.add(drawable)
		mMapView.getTouchables();		
		mController = mMapView.getController(); 
		
		//GeoPoint cityLocPoint = new GeoPoint(39932002, 116430230);
		
		//缩放函数终于找到。缩放值为3到18.当值为3时，显示最近视角。	
		//mController.setzoomToAnimate(3);
		//实现直接缩放的函数，第一个参数应该是总视图大小，第二参数为0到6，6是最大放大倍数，继续往上将难以看到地图
				mController.setZoomAnimate(0,1);
		//地图旋转函数，参数值为旋转角度。
		//mController.rotateToAnimate(180);
		
		
		
		
		//实现坐标点的计算
		//mMapView.getLayoutDirection().toPixels(cityLocPoint, myScreen);
		//mMapData.calculateCenterLatLon();
		
		
		
		//mMapView.onScroll(null, null, 0, 3);
		//mMapView.onZoomStarted(mTouchPoint);
		//mMapView.refreshDrawableState();
		
		//mMapView.scrollBy(39958241, 116350579);
		//mMapView.scrollTo(800, 1160);
		//mMapView.setMinimumHeight(256);
		//mMapView.setMinimumWidth(256);	
		//Drawable drawable = this.getResources().getDrawable(  
        //        R.drawable.marker_gpsvalid);       
		//mMapView.getOverlay().add(drawable);
		
			
	
		mPoiName = (TextView) findViewById(R.id.tv_name);
		mPoiDescription = (TextView) findViewById(R.id.tv_description);
		mPoiLocation = (TextView) findViewById(R.id.tv_location);
		mPoi = (ViewGroup) findViewById(R.id.poi_info);
		mMapView.setMapManager(mManager);
		mFilter = new PoiFilter(mManager, 2);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnEnd = (Button) findViewById(R.id.btn_stop);
		mBtnDel = (Button) findViewById(R.id.btn_del);
	//	mBtnRoute = (Button) findViewById(R.id.btn_route);
		mBtnStart.setOnClickListener(this);
		mBtnEnd.setOnClickListener(this);
		mBtnDel.setOnClickListener(this);
	//	mBtnRoute.setOnClickListener(this);
		mMapView.setOnPoiListener(this);
		
		//信息点在初始化时便设置为可见
		mMapView.setFilter(mFilter);
		
		//理论上的地图拖拽，但实验不出效果
	              mController.isMapRotateEnabled();
				//mController.dragToAnimate(0, 0, 100,8);
				//mController.centerTo(10,10);
				//mController.moveTo(0,116);
				//mController.setLatLon(0,0);	
		          mController.setLatLonAnimate(39916616,116390791);
				
			
		
	}

	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onMapInit(MapData data) {
		Log.d(TAG, getString(R.string.map_init));
	}

	@Override
	public void onMapIndexFinish() {
		Log.d(TAG, getString(R.string.map_index_finish));
	}

	@Override
	public void onMapLoadFinish(int error) {
		Log.d(TAG, getString(R.string.map_load_finish));
	}

	@Override
	public void onMapFinish() {
		Log.d(TAG, getString(R.string.map_finish));
	}

	@Override
	public void onAmenityLoadFinish(int error) {
		Log.d(TAG, getString(R.string.amenity_load_finish));
	}

	@Override
	public void onPoiClick(PoiData data) {
		mPoiData = data;
		mPoiName.setText(data.getName());
		mPoiDescription.setText(data.getDescription());
		mPoiLocation.setText(String.format("Lat:%f, Lon:%f", data.getLat(),
				data.getLon()));
		mPoiId = data.getId();
		//此处原本为导览键设置初始不可用
	//	if (data.getType() == PoiFilter.TYPE_CUSTOM) {
	//		mBtnDel.setEnabled(true);
	//	} else {
	//		mBtnDel.setEnabled(false);
	//	}
		
		
		if (!mPoi.isShown()) {
			mPoi.setVisibility(View.VISIBLE);
		}
	}

	public void onNothingClick() {
		if (mPoi.isShown()) {
			mPoi.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLongPress(PointF point) {
		mTouchPoint = point;
	//
		//此处应添加传送消息功能，把mTouchPoint信息传送出去。
		//mTouchPoint的x，y参数
	//	if (mBtnPoiSwitcher.isChecked()) {
	//		onCreateDialog(0).show();
	//	}
	}

	
	@Override
	public void onMapLoadError(int arg0) {
		new AlertDialog.Builder(self)
		 .setTitle("标题") 
		 .setMessage("地图加载错误请重新加载")
		 	.setPositiveButton("确定", null)
		 	.show();
	 }

	//@Override
	
	
	//信息点描述面板三个按钮的点击事件
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_del:
			//mMapView.delPoi(mPoiId);
			//mPoi.setVisibility(View.INVISIBLE);
			//此处用mPoiId进行关联，此按键点击事件设为播放导览函数（参数：mPoiId）
			//
			//********************未完成***********************
			break;
			
			//此处留作之后的路径规划
		//case R.id.btn_start:
		//	mStart = mPoiData;
		//	mBtnRoute.setEnabled(checkRouteReadly());
		//	break;
		//case R.id.btn_stop:
		//	mEnd = mPoiData;
		//	mBtnRoute.setEnabled(checkRouteReadly());
		//	break;
		//case R.id.btn_route:
			
			//路径规划按钮的暂时取消功能
			//mMapView.getController().searchRoute(mStart, mEnd);
		//	break;
		}
	}
	//路径规划
	//private boolean checkRouteReadly(){
	//	return mStart != null && mEnd != null;
	//}

	
	
	
	//信息点显示隐藏转换按钮
	//public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	//	if (isChecked) {
	//		mMapView.setFilter(mFilter);
	//	} else {
	//		mMapView.setFilter(null);
	//	}
	//  }

	@Override
	public void onNewRouteCalculated(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
}
