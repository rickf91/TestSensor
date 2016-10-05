package top.isense.gpssimulate;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import top.isense.demo.testsensor.R;

public class GPSSetting extends Activity implements LocationListener,
        BDLocationListener, OnMapClickListener,
        OnMarkerDragListener, OnGetGeoCoderResultListener {

    //Android Location related
    private LocationManager mlocationManager = null;

    private String mMockProviderName = LocationManager.GPS_PROVIDER;
    private String mMockProvidernetwork = LocationManager.NETWORK_PROVIDER;
    //private String mMockProviderPassive = LocationManager.PASSIVE_PROVIDER;
    private String[] mProviderList = {mMockProviderName, mMockProvidernetwork};

    //set default location
    private double mlatitude = 32.3029742;
    private double mlongitude = 120.6097126;   //江苏省南通市如皋市桃林路


    //Baidu Map related function
    private MapView     mMapView = null;
    private BaiduMap    mBaiduMap= null;
    private LocationClient mLocationClient = null;
    private MyLocationConfiguration.LocationMode mCurrentMode = null;
    private GeoCoder mGeoCoder = null;
    private BitmapDescriptor mLocationIcon = null;

    //Set Location Thread
    private Thread  mThread = null;
     private  boolean  mbExit  = false;

    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.i("gps", String.format("location: lat=%s lng=%s", lat, lng));
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }


    public void onProviderDisabled(String provider) {

    }

    private void initMockLocation() {
        //Simulate the Mock GPS
        mlocationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        for (int i = 0; i < mProviderList.length; i++) {
            LocationProvider provider = mlocationManager.getProvider(mProviderList[i]);
            mlocationManager.addTestProvider(provider.getName(), provider.requiresNetwork(),
                    provider.requiresSatellite(), provider.requiresCell(),
                    provider.hasMonetaryCost(), provider.supportsAltitude(),
                    provider.supportsSpeed(), provider.supportsBearing(),
                    provider.getPowerRequirement(), provider.getAccuracy());
            mlocationManager.setTestProviderEnabled(provider.getName(), true);

            setMockLocation(mlongitude, mlatitude);

            mlocationManager.requestLocationUpdates(provider.getName(), 500, 0.1f, this);
        }


    }

    private void setMockLocation(double longitude, double latitude) {
        //Simulate the Mock GPS
        for (int i = 0; i < mProviderList.length; i++) {
            Location location = new Location(mProviderList[i]);
            location.setTime(System.currentTimeMillis());
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAltitude(2.0f);
            location.setAccuracy(1.0f);
            location.setBearing(10.1f);
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            location.setSpeed(10);

            try {
                Location lc = mlocationManager.getLastKnownLocation(mProviderList[i]);
                mlocationManager.setTestProviderLocation(mProviderList[i], location);
                mlocationManager.setTestProviderStatus(mProviderList[i], LocationProvider.AVAILABLE,
                        null, System.currentTimeMillis());
            }catch (Exception e) {
                Log.w("SET MOCK LOCATION", e.getMessage());
            }
        }
    }


    //Set Location
    protected boolean LocationNow() {
        if(null == mBaiduMap) return false;
//
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//        // 构造定位数据
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(location.getLatitude())
//                .longitude(location.getLongitude()).build();
//        // 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
//        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration();
//        // 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);
            return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ensure the Baidu map SDK initialized
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_gpssetting);

        initialMapResource();

        //setContentView(R.layout.gps_settting);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.getBackground().setAlpha(100);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initialMapResource() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //Normal Map
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //Satelite Map
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);

        //Register the Map Listener
        mLocationClient.registerLocationListener(this);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerDragListener(this);
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

        initMockLocation();

        //setMockLocation(mlongitude, mlatitude);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 缩放
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

        mLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_camera);

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mLocationIcon));

        mLocationClient.setLocOption(option);
        mLocationClient.start();

        startSetLocationThread();
    }

    private void startSetLocationThread() {
        //avoid re-allocate thread resource
        if(null != mThread) return;

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mbExit) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setMockLocation(mlongitude, mlatitude);
                }
            }
        });
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        synchronized (this) {
            mbExit = true;
        }

        mThread = null;
        // 退出时销毁定位
        mLocationClient.stop();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mLocationIcon.recycle();
        //bd.recycle();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private boolean isFirstLoc = true;
    private double myGpslatitude;
    private double myGpslongitude;
    public void onReceiveLocation(BDLocation bdlocation) {
        if (null == bdlocation || null == mMapView) {
            return;
        }

        if (isFirstLoc) {
            isFirstLoc = false;
            myGpslatitude = bdlocation.getLatitude();
            myGpslongitude = bdlocation.getLongitude();
            LatLng pos = new LatLng(myGpslatitude, myGpslongitude);


            //mMarker.setPosition(arg0);

            // 设置地图中心点为这是位置
           // LatLng ll = new LatLng(arg0.latitude, arg0.longitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(pos);
            mBaiduMap.animateMapStatus(u);

            // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(pos));
        }
    }


    private void setMapCurrentLatiLongti(LatLng arg0) {
//        curLatlng = arg0;
//        mMarker.setPosition(arg0);
//
//        // 设置地图中心点为这是位置
//        LatLng ll = new LatLng(arg0.latitude, arg0.longitude);
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//        mBaiduMap.animateMapStatus(u);
//
//        // 根据经纬度坐标 找到实地信息，会在接口onGetReverseGeoCodeResult中呈现结果
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
    }


    //OnGetGeoCodeListener
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    //OnMarkerDragListener
    public void onMarkerDrag(Marker marker) {

    }

    public void onMarkerDragEnd(Marker marker) {

    }


    public void onMarkerDragStart(Marker marker) {

    }

    //OnMapClickListener
    public void onMapClick(LatLng latLng) {

    }

    public boolean onMapPoiClick(MapPoi mapPoi) {
        return true;
    }
}

