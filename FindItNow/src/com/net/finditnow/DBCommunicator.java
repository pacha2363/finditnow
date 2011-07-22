package com.net.finditnow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class DBCommunicator {

	// A Constant representing the location of FIN
	private static final String FIN_ROOT = "https://www.project-fin.org/fin/";
	private static final int CONNECTION_TIMEOUT = 8000;
	private static final int SOCKET_TIMEOUT = 8000;

	public static String create(String phone_id, String cat, String fid, String special_info, String latitude, String longitude, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));
		nameValuePairs.add(new BasicNameValuePair("cat", FINUtil.sendCategory(cat, context)));
		nameValuePairs.add(new BasicNameValuePair("fid", fid));
		nameValuePairs.add(new BasicNameValuePair("special_info", special_info));
		nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
		nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

		return Post("FINsert/createItem.php", nameValuePairs, context);
	}

	public static String delete(String phone_id, String item_id, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));
		nameValuePairs.add(new BasicNameValuePair("item_id", item_id));

		return Post("delete.php", nameValuePairs, context);
	}
	
	public static String getRegions(String lat, String lon, Context context) {
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));

		return Post("getRegions.php", nameValuePairs, context);
	}

	public static String getCategories(Context context) {
		return Get("getCategories.php", context);
	}

	public static String getBuildings(String rid, Context context) {
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("rid", rid));

		return Post("getBuildings.php", nameValuePairs, context);
	}

	public static String getLocations(String cat, String rid, String bid, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("cat", FINUtil.sendCategory(cat, context)));
		nameValuePairs.add(new BasicNameValuePair("rid", rid));
		nameValuePairs.add(new BasicNameValuePair("bid", bid));
				
		return Post("getLocations.php", nameValuePairs, context);
	}

	public static String login(String phone_id, String username, String userpass, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("userpass", userpass));
		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));

		return Post("login.php", nameValuePairs, context);
	}

	public static String loggedIn(String phone_id, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));

		return Post("loggedIn.php", nameValuePairs, context);
	}

	public static String logout(String phone_id, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));

		return Post("logout.php", nameValuePairs, context);
	}

	public static String searchLocations(String cat, String rid, String sString, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("cat", FINUtil.sendCategory(cat, context)));
		nameValuePairs.add(new BasicNameValuePair("rid", rid));
		nameValuePairs.add(new BasicNameValuePair("sString", sString));

		return Post("searchLocations.php", nameValuePairs, context);
	}

	public static String update(String phone_id, String item_id, Context context) {
		// Initialize the array of name value pairs
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("phone_id", phone_id));
		nameValuePairs.add(new BasicNameValuePair("item_id", item_id));

		return Post("update.php", nameValuePairs, context);
	}

	private static String Get(String suffix, Context context) {

		String data = "";

		HttpGet httpGet = new HttpGet(FIN_ROOT + suffix);

		// Process the response from the server
		HttpClient httpClient = createHttpClient();		
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpGet);  
			data = EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception e) {
			if (e != null) {
				Log.e("log_tag", e.getMessage());
			}
			return context.getString(R.string.timeout);
		}

		return data.trim();
	}

	private static String Post(String suffix, List<BasicNameValuePair> nameValuePairs, Context context) {

		// Initialize input stream and response variables
		InputStream iStream = null;
		String data = "";

		try {
			HttpPost httppost = new HttpPost(FIN_ROOT + suffix);

			// Process the response from the server
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpClient httpClient = createHttpClient();
			HttpResponse httpResponse = httpClient.execute(httppost);

			HttpEntity entity = httpResponse.getEntity();
			iStream = entity.getContent();
		} catch(Exception e) {
			if (e != null) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
		}

		// Convert server's response to a String
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			iStream.close();

			data = sb.toString();
		} catch(Exception e) {
			if (e != null) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			return context.getString(R.string.timeout);
		}

		if (data.trim().equals(context.getString(R.string.not_logged_in))) {
			FINHome.setLoggedIn(false);
		}

		return data.trim();
	}

	private static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}
}