package com.net.finditnow;

import java.math.BigDecimal;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.*;
import android.app.Dialog;


public class UWOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public UWOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public UWOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		GeoPoint itemLocation = item.getPoint();
		
		BigDecimal distance = FINMap.distanceTo(itemLocation);

	    String[] floor = FINMap.getFloors(itemLocation);
	    String name = FINMap.getLocationName(itemLocation);	    
		String buildingName = FINMenu.getBuilding(itemLocation).getName();
		
	    Dialog popUp = new PopUpDialogVer2(mContext, floor, buildingName, name, distance);
	  
	    popUp.show();
	    return true;
	}

}
