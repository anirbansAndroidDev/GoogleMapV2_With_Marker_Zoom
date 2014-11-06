package com.example.googlemapproto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapV2 extends Activity{
	
	private GoogleMap googleMap;
	LatLngBounds.Builder builder;
	ArrayList<String> numero;
	ArrayList<String> lat;
	ArrayList<String> lng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		numero = new ArrayList<String>();
		lat    = new ArrayList<String>();
		lng	   = new ArrayList<String>();
		
		loadMap();
		parseXml();
		//Log.i("TAG","Distance: " + measureDistance(Double.parseDouble(GPSLat), Double.parseDouble(GPSLng)));
	}
	
	private void parseXml() 
	{
		try 
		{
			InputStream is = getAssets().open("XmlFinal.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(is));
			doc.getDocumentElement().normalize();

			NodeList OT_n      = doc.getElementsByTagName("OT");
			NodeList geodata_n = doc.getElementsByTagName("geodata");
			

			for (int j = 0; j < OT_n.getLength(); j++) 
			{
				Node node = OT_n.item(j);
				Element fstElmnt = (Element) node;

				numero.add(fstElmnt.getAttribute("numero"));
				Log.i("TAG","numero: " + fstElmnt.getAttribute("numero"));
			}
			
			builder = new LatLngBounds.Builder();
			for (int j = 0; j < geodata_n.getLength(); j++) 
			{
				Node node = geodata_n.item(j);
				Element fstElmnt = (Element) node;

				lat.add(fstElmnt.getAttribute("latitud"));
				lng.add(fstElmnt.getAttribute("longiud"));
				Log.i("TAG","lat: " + fstElmnt.getAttribute("latitud") + " lng: " + fstElmnt.getAttribute("longiud"));
				
				builder.include(new LatLng(Double.parseDouble(fstElmnt.getAttribute("latitud").toString()), Double.parseDouble(fstElmnt.getAttribute("longiud").toString())));
				loadMarker(fstElmnt.getAttribute("latitud"), fstElmnt.getAttribute("longiud"));
			}
			googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
		} 
		catch (Exception e) 
		{
			System.out.println("XML Pasing Excpetion = " + e);
		}
	}
	

	public float measureDistance (double lat_b, double lng_b ) 
	{
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
		Location myLocation = locationManager.getLastKnownLocation(provider);

		double lat_a = myLocation.getLatitude();
		double lng_a = myLocation.getLongitude();
		
		Log.i("TAG"," Current Location: Lat: "+ lat_a + " Lng: " + lng_a);
	    double earthRadius = 3958.75;
	    double latDiff = Math.toRadians(lat_b-lat_a);
	    double lngDiff = Math.toRadians(lng_b-lng_a);
	    double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
	    Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
	    Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = earthRadius * c;

	    int meterConversion = 1609;

	    return new Float(distance * meterConversion).floatValue();
	}

	private void loadMarker(String GPSLat, String GPSLng) 
	{
		MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(GPSLat), Double.parseDouble(GPSLng)));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_small));
		googleMap.addMarker(marker);
	}

	public void loadMap() 
	{
		try 
		{
			initilizeMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setCompassEnabled(true);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			
			googleMap.setOnCameraChangeListener(new OnCameraChangeListener() 
			{
			    @Override
			    public void onCameraChange(CameraPosition arg0) 
			    {
			        // Move camera.
			    	googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
			        // Remove listener to prevent position reset on camera move.
			    	googleMap.setOnCameraChangeListener(null);
			    }
			});
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void initilizeMap() 
	{
		if (googleMap == null) 
		{
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			if (googleMap == null) 
			{
				Toast.makeText(getApplicationContext(),"Error on Load !!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
