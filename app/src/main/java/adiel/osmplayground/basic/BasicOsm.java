package adiel.osmplayground.basic;

import android.content.Context;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import adiel.osmplayground.R;

public class BasicOsm extends AppCompatActivity implements View.OnClickListener ,LocationListener{

    public final static String MapServerUrl="http://tile.wishtrip.com/osm_tiles/";

    MapView map;
    ImageView ivGoToMyLocation;
    GpsMyLocationProvider mGpsLocationProvider;
    Location myLocation;
    LocationManager locationManager = null;
    IMapController mapController;
    int zoom = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_osm);
        RntMapResourceProxy resourceProxy = new RntMapResourceProxy(getApplicationContext());

        setUpMap();

        goToMyLOcation();
        setGesture();
        map.setMultiTouchControls(true);
        mapController.setZoom(zoom);


    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSConfig.MIN_TIME, GPSConfig.MIN_DISTANCE, this);
    }

    private void setGesture() {
        RotationGestureDetector mRotationGestureDetector = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            public void OnRotation(float angle, PointF anchor) {
                map.setMapOrientation(map.getMapOrientation() + angle);
            }
            @Override
            public void OnRotationEnd() {
                //if(usersOverlay != null)
                //usersOverlay.onRotate();
            }
        }, getApplicationContext());
        map.getOverlays().add(mRotationGestureDetector);
    }

    private void setUpMap(){
        DisplayMetrics d = getResources().getDisplayMetrics();
        int tileSize = (int) (d.density * 256);
        map = (MapView) findViewById(R.id.fragment_record_map_mv);
        mapController = map.getController();
        map.setTileSource(new XYTileSource("RNTTile", null, 3, 18, tileSize, ".png", new String[]{MapServerUrl}));
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mGpsLocationProvider = new GpsMyLocationProvider(this);
    }

    private void goToMyLOcation() {
        ivGoToMyLocation = (ImageView)findViewById(R.id.fragment_record_gotomylocation_iv);
        ivGoToMyLocation.setOnClickListener(this);
    }
    public Location getLastLocationFix(LocationManager locationManager) {
        Location location = myLocation;
        if(location == null) {
            if(mGpsLocationProvider != null) {
                location = mGpsLocationProvider.getLastKnownLocation();
            }
        }
        if(location == null) {
            for (String provider : locationManager.getProviders(true)) {
                location = locationManager.getLastKnownLocation(provider);
            }
        }
        if (location == null) {
            location = new Location(LocationManager.GPS_PROVIDER);
            Toast.makeText(this, "location_not_available", Toast.LENGTH_SHORT).show();
        }
        return location;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_record_gotomylocation_iv:
                Location currentLocationPoint = getLastLocationFix(locationManager);
                mapController.animateTo(new GeoPoint(currentLocationPoint));
                break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation =location;
        Log.d("adiel","location:"+location.toString());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void zoomIn(View view) {
        zoom++;
        mapController.setZoom(zoom);
    }

    public void zoomOut(View view) {
        zoom--;
        mapController.setZoom(zoom);
    }
}
