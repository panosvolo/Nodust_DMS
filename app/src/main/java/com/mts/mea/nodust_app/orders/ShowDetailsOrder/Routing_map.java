package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Routing_map  extends ActionBarActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private LatLng Current_loc;
    private Task task;
    private LatLng task_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing_map);
        Toolbar toolbar=(Toolbar)findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.Routing));
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        task = (Task) getIntent().getExtras().get("Task");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final double curr_lat = GetCurrentLocaion.CurrentLoc.getLatitude();
        final double curr_lng = GetCurrentLocaion.CurrentLoc.getLongitude();
        Current_loc = new LatLng(curr_lat, curr_lng);
        String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);

        if (Address!=null)
            mMap.addMarker(new MarkerOptions().position(Current_loc).title(Address));
        else
        mMap.addMarker(new MarkerOptions().position(Current_loc).title(getApplicationContext()
                .getResources().getString(R.string.currentloc)));
        task_loc = new LatLng(task.getY(), task.getX());
        mMap.addMarker(new MarkerOptions().position(task_loc).title(getApplicationContext()
                .getResources().getString(R.string.CardLoc)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(Current_loc);
        builder.include(task_loc);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,padding);
        googleMap.animateCamera(cu);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                      @Override
                                      public View getInfoWindow(Marker marker) {
                                          if(marker.getPosition().latitude!=curr_lat&&marker.getPosition().longitude!=curr_lng)
                                          {
                                              View popup = null;
                                              LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                                              if (popup == null)
                                                  popup = inflater.inflate(R.layout.infowindow, null);
                                              TextView tv_cardno=(TextView)popup.findViewById(R.id.tv_cardno);
                                              if(task.getCardNo()!=null)
                                              tv_cardno.setText(task.getCardNo());
                                              TextView tv_priority=(TextView)popup.findViewById(R.id.tv_priority);
                                              if(task.getPRIORITY()!=null)
                                                  tv_priority.setText(task.getPRIORITY());
                                              TextView tv_custName=(TextView)popup.findViewById(R.id.tv_custName);
                                              if(task.getClientName()!=null)
                                                  tv_custName.setText(task.getClientName());

                                              TextView tv_streetname=(TextView)popup.findViewById(R.id.tv_streetname);
                                              if(task.getStreet_name()!=null&&task.getHome_no()!=null)
                                                  tv_streetname.setText(task.getStreet_name() + " "
                                                          +getApplicationContext().getResources().getString(R.string.HOME_NO) + " " +
                                                          task.getHome_no());
                                              else if(task.getStreet_name()!=null)
                                                  tv_streetname.setText(task.getStreet_name()) ;
                                              else if(task.getHome_no()!=null)
                                                 tv_streetname.setText(
                                                         getApplicationContext().getResources().getString(R.string.HOME_NO) + " " +
                                                         task.getHome_no());
                                              else
                                                  tv_streetname.setText(" ");
                                              TextView tv_custAdd=(TextView)popup.findViewById(R.id.tv_custAdd);
                                              if(task.getFU_Note()!=null) {
                                                  String text=task.getFU_Note()+"\n";
                                                  if(task.getFrom_time()!=null)
                                                  {
                                                      text+=getApplicationContext().getResources().getString(R.string.FromTime)+" "+
                                                              task.getFrom_time();
                                                  }
                                                  if(task.getTo_time()!=null)
                                                  {
                                                      text+=" ";
                                                      text+=getApplicationContext().getResources().getString(R.string.ToTime)+" "+
                                                              task.getTo_time();

                                                  }

                                                  tv_custAdd.setText(text);
                                              }
                                              else {
                                                  String text=" ";
                                                  if(task.getFrom_time()!=null)
                                                  {
                                                      text+=getApplicationContext().getResources().getString(R.string.FromTime)+" "+
                                                              task.getFrom_time();
                                                  }
                                                  if(task.getTo_time()!=null)
                                                  {
                                                      text+=" ";

                                                      text+=getApplicationContext().getResources().getString(R.string.ToTime)+" "+
                                                              task.getTo_time();

                                                  }

                                                  tv_custAdd.setText(text);
                                              }

                                              return popup;
                                          }
                                          return null;
                                      }

                                      @Override
                                      public View getInfoContents(Marker marker) {
                                          return null;
                                      }
                                  });
                // draw route
        mMap.setOnInfoWindowClickListener(this);
                DrawRoute();
    }

    private void DrawRoute() {
        if (Current_loc.latitude == task_loc.latitude && Current_loc.longitude == task_loc.longitude) {
            Toast.makeText(getApplicationContext(), "Your Location is the Same Location of Card !", Toast.LENGTH_SHORT).show();
        } else {
            String url = getDirectionsUrl2(Current_loc, task_loc);

            DownloadTask2 downloadTask = new DownloadTask2(Current_loc, task_loc);
            downloadTask.execute(url);
        }
    }

    private String getDirectionsUrl2(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&Key=AIzaSyD6_diKszoj4kH8YIFzZ8J2-KPhCfTXTZw";

        return url;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getPosition().latitude!=Current_loc.latitude&&marker.getPosition().longitude!=
                Current_loc.longitude) {
            if(task.getASSIGNMENTS_TYPE()==4) {
                Intent intent = new Intent(Routing_map.this, CollectionOrderDetails.class);
                intent.putExtra("Task", (Serializable) task);

                startActivity(intent);

            }
            else
            {
                Intent intent = new Intent(Routing_map.this, OrderDetailsActivity.class);
                intent.putExtra("Task", (Serializable) task);

                startActivity(intent);
            }

        }

    }


    private class DownloadTask2 extends AsyncTask<String, Void, String> {
        LatLng mOrgin, mDest;

        public DownloadTask2(LatLng orgin, LatLng dest) {
            mOrgin = orgin;
            mDest = dest;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            final String[] data = {""};

            try {
                data[0] = downloadUrl2(url[0]);
                Log.d("Data", data[0]);
                Log.d("URL", url[0]);
                if (data[0].contains("error_message")) {
                    data[0] = "";
                }
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data[0];
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           if(!result.isEmpty()) {
               ParserTask2 parserTask = new ParserTask2(mOrgin, mDest);

               // Invokes the thread for parsing the JSON data
               parserTask.execute(result);
           }
            else
           {
               Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.Contactadmin),Toast.LENGTH_SHORT).show();
           }

        }
    }

    private String downloadUrl2(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //     Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask2 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        LatLng orgin, dest;

        public ParserTask2(LatLng mOrgin, LatLng mDest) {
            orgin = mOrgin;
            dest = mDest;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.i("routes", routes.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = new PolylineOptions();

            // Traversing through all the routes
            if (result != null)
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.add(orgin);
                    lineOptions.addAll(points);
                    lineOptions.add(dest);
                    lineOptions.width(6);
                    lineOptions.color(Color.BLUE);
                }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }

    }

    private class DirectionsJSONParser {
        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
            return routes;
        }


    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
