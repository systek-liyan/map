package com.magic.mapdemo;







import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.magic.map.IMapLoaderCallback;
import com.magic.map.MapManager;
import com.magic.map.resource.poi.PoiFilter;
import com.magic.map.util.MapData;
import com.magic.map.util.PoiData;
import com.magic.map.view.MapController;
import com.magic.map.widget.MapView;
import com.magic.map.widget.onMapListener;

public class MainActivity extends Activity implements IMapLoaderCallback,
		onMapListener, android.view.View.OnClickListener,
		OnCheckedChangeListener {
	private static final String TAG = "Map1";
	private MapManager mManager = null;
	private MapView mMapView = null;
	private PoiFilter mFilter = null;
	private TextView mPoiName = null;
	private TextView mPoiDescription = null;
	private TextView mPoiPhone = null;
	private TextView mPoiUri = null;
	private TextView mPoiLocation = null;
	private ViewGroup mPoi = null;
//	private EditText mCustomPoiId = null;
	private EditText mCustomPoiName = null;
	private EditText mCustomPoiDescription = null;
	private EditText mCustomPoiPhone = null;
	private EditText mCustomPoiSite = null;
	private Button mBtnStart = null;
	private Button mBtnEnd = null;
	private Button mBtnDel = null;
	
	private PointF mTouchPoint = null;
	private long mPoiId = 0;
	private MapController mController;
	private double cc;
	private double dd;
	private double ee;
	private double ff;
	private float aaa;
	private float ccc;
	private int bbb;
	
	
	

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
		mController=mMapView.getController();
		mController.setZoomAnimate(0,1);
	        
		mPoiName = (TextView) findViewById(R.id.tv_name);
		mPoiDescription = (TextView) findViewById(R.id.tv_description);
		mPoiPhone = (TextView) findViewById(R.id.tv_phone);
		mPoiUri = (TextView) findViewById(R.id.tv_uri);
		mPoiLocation = (TextView) findViewById(R.id.tv_location);
		mPoi = (ViewGroup) findViewById(R.id.poi_info);
		mMapView.setMapManager(mManager);
		//mFilter = new PoiFilter(mManager, PoiFilter.TYPE_SHOP);
		mFilter = new PoiFilter(mManager, 2);
	   
		
		
		mBtnStart = (Button) findViewById(R.id.btn_start);
		mBtnEnd = (Button) findViewById(R.id.btn_stop);
		mBtnDel = (Button) findViewById(R.id.btn_del);
		
		mBtnStart.setOnClickListener(this);
		mBtnEnd.setOnClickListener(this);
		mBtnDel.setOnClickListener(this);
		
		mMapView.setOnPoiListener(this);
		
		
		//取消转换按钮功能，直接按照过滤结果显示。
		mMapView.setFilter(mFilter);
		
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
		mPoiName.setText(data.getName());
		mPoiDescription.setText(data.getDescription());
		//此处修改phone为显示id
		mPoiPhone.setText("Phone Num:" + data.getId());
		mPoiUri.setText("Site:" + data.getUri());
		mPoiLocation.setText(String.format("Lat:%f, Lon:%f", data.getLat(),
				data.getLon()));
		mPoiId = data.getId();
	//	if (data.getType() == PoiFilter.TYPE_CUSTOM) {
	//		mBtnDel.setEnabled(true);
	//	} else {.0.
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
		
		//mTouchPoint = point;
		Builder builder = new Builder(MainActivity.this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = null;
		layout = inflater.inflate(R.layout.custom_poi, null);
		//mCustomPoiId = (EditText) layout.findViewById(R.id.et_id);
		mCustomPoiName = (EditText) layout.findViewById(R.id.et_name);
		mCustomPoiDescription = (EditText) layout
				.findViewById(R.id.et_description);
		mCustomPoiPhone = (EditText) layout.findViewById(R.id.et_phone);
		mCustomPoiSite = (EditText) layout.findViewById(R.id.et_site);
		builder.setView(layout);
		PoiData data = new PoiData(111112345,
				mCustomPoiName.getText().toString(),
				mCustomPoiDescription.getText()
						.toString(), mCustomPoiPhone
						.getText().toString(),
				mCustomPoiSite.getText().toString(),
				PoiFilter.TYPE_SHOP);
//		PoiData data1 = new PoiData(111112346,
//				mCustomPoiName.getText().toString(),
//				mCustomPoiDescription.getText()
//						.toString(), mCustomPoiPhone
//						.getText().toString(),
//				mCustomPoiSite.getText().toString(),
//				PoiFilter.TYPE_SHOP);
//		PoiData data2 = new PoiData(111112347,
//				mCustomPoiName.getText().toString(),
//				mCustomPoiDescription.getText()
//						.toString(), mCustomPoiPhone
//						.getText().toString(),
//				mCustomPoiSite.getText().toString(),
//				PoiFilter.TYPE_SHOP);
		
		
		double x=116.356738,y=39.959162;
		//(570,810)是mMapView在任何情况下获取坐标对应的屏幕位置！！！
		PointF ppp=new PointF(300,400);
		mMapView.addPoi(ppp,data);
		cc=mMapView.getLatitude();
		dd=mMapView.getLongitude();
		ee=x-dd;
		ff=cc-y;
		aaa=mMapView.getDensity();
		bbb=mMapView.getZoom();
		ccc=mMapView.getZoomScale();
		//算式后的系数，一为17(0,1)倍率下，x轴参数，一为y轴参数。
		//float n=(float)(570+ee*186431.9);
		//float m=(float)(810+ff*243191.9);
		float n=(float)(570+ee*186431.9);
		float m=(float)(810+ff*243191.9);
		mTouchPoint=new PointF(800,1500);
		mMapView.addPoi(mTouchPoint, data);
		  
		
	}

	@Override
	public void onMapLoadError(int arg0) {
		onCreateDialog(1).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_del:
			mMapView.delPoi(mPoiId);
			mPoi.setVisibility(View.INVISIBLE);
			break;
		case R.id.btn_start:
			// BinaryMapIndexReader[] files = mManager.TestRoute();
			break;
		case R.id.btn_stop:
			break;
		
		}
	}
	


	@Override
	public void onNewRouteCalculated(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}
}
