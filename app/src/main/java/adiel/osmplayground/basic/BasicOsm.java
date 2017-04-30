package adiel.osmplayground.basic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.views.MapView;

import adiel.osmplayground.R;

public class BasicOsm extends AppCompatActivity {

    public final static String MapServerUrl="http://tile.wishtrip.com/osm_tiles/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_osm);
        RntMapResourceProxy resourceProxy = new RntMapResourceProxy(getApplicationContext());
        DisplayMetrics d = getResources().getDisplayMetrics();
        int tileSize = (int) (d.density * 256);
        MapView map = (MapView) findViewById(R.id.fragment_record_map_mv);
        map.setTileSource(new XYTileSource("RNTTile", null, 3, 18, tileSize, ".png", new String[]{MapServerUrl}));
    }
}
