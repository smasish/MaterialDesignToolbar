package com.efilx.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Formatter;
import android.util.Log;

public class InternetConnection {

	private static Context _context;

	public InternetConnection(Context context) {
		InternetConnection._context = context;
	}

	public boolean isConnectingToInternet() {
		
		try {
			ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				
				if (info != null)
					for (int i = 0; i < info.length; i++)
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							
							return true;
						}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}


