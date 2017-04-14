package kr.co.moon.aeds;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder geocoder;
    ArrayList<String> address;
    String address1;
    String address2;
    DbHelper mHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        openDb();
        geocoder = new Geocoder(this);

        Cursor cursor = db.rawQuery(
                "SELECT address FROM list", null);
        cursor.moveToFirst();
        do {
            Log.d("주소", cursor.getString(0));
//            if (grade == 0 || grade == 9) {
//                positions.add(cursor.getString(1).replaceAll("[0-9]", ""));
//                m_Adapter.add(cursor.getString(1).replaceAll("[0-9]", "") + "  " + cursor.getString(2) + " \n " + cursor.getString(3));
//            } else {
            List<Address> list = null;
            try {
                list = geocoder.getFromLocationName(
                        cursor.getString(0), // 지역 이름
                        1); // 읽을 개수
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
            }

            if (list != null) {
                if (list.size() == 0) {
                    Log.d("dddd", "해당되는 주소 정보는 없습니다");
                } else {
                    // 해당되는 주소로 인텐트 날리기
                    Address addr = list.get(0);
                    double lat = addr.getLatitude();
                    double lon = addr.getLongitude();
                    Log.d("dddd", lat + " " + lon);

//                    String sss = String.format("geo:%f,%f", lat, lon);
//
//                    LatLng sydney = new LatLng(lat, lon);
//                    mMap.addMarker(new MarkerOptions().position(sydney).title("제세동기"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
                }
            }
        } while (cursor.moveToNext());
        cursor.close();

        address1 = "경상남도 거제시 양정동 208-4";
        address2 = "경상남도 거제시 고현동 293-3 계룡중학교";



        // Add a marker in Sydney and move the camera

    }

    public void openDb() {

        mHelper = new DbHelper(MapsActivity.this);
        db = mHelper.getReadableDatabase();
    }

}
