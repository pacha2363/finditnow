package com.net.finditnow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
	
public class FINDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String REGIONS_TABLE_CREATE = "CREATE TABLE regions (rid INTEGER PRIMARY KEY, name TEXT, full_name TEXT, latitude INTEGER, longitude INTEGER, deleted INTEGER)";
    private static final String COLOR_TABLE_CREATE = "CREATE TABLE colors (rid INTEGER PRIMARY KEY, color1 TEXT, color2 TEXT)";
    private static final String CATEGORIES_TABLE_CREATE = "CREATE TABLE categories (cat_id INTEGER PRIMARY KEY, name TEXT, full_name TEXT, parent INTEGER, deleted INTEGER)";
    private static final String BUILDINGS_TABLE_CREATE = "CREATE TABLE buildings (bid INTEGER PRIMARY KEY, rid INTEGER, name TEXT, latitude INTEGER, longitude INTEGER, deleted INTEGER)";
    private static final String FLOORS_TABLE_CREATE = "CREATE TABLE floors (fid INTEGER PRIMARY KEY, bid INTEGER, fnum INTEGER, name TEXT, deleted INTEGER)";
    private static final String ITEMS_TABLE_CREATE = "CREATE TABLE items (item_id INTEGER PRIMARY KEY, rid INTEGER, latitude INTEGER, longitude INTEGER, special_info TEXT, fid INTEGER, not_found_count INTEGER, username TEXT, cat_id INTEGER, deleted INTEGER)";
    
    public FINDatabase(Context context) {
        super(context, "FIN_LOCAL", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REGIONS_TABLE_CREATE);
        db.execSQL(COLOR_TABLE_CREATE);
        db.execSQL(CATEGORIES_TABLE_CREATE);
        db.execSQL(BUILDINGS_TABLE_CREATE);
        db.execSQL(FLOORS_TABLE_CREATE);
        db.execSQL(ITEMS_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion == 2) {
			db.execSQL("DROP TABLE items");
			db.execSQL(ITEMS_TABLE_CREATE);
		}
		if (newVersion == 3) {
			db.execSQL("ALTER TABLE regions ADD deleted INTEGER");
			db.execSQL("UPDATE regions SET deleted = 0");
			
			db.execSQL("ALTER TABLE buildings ADD deleted INTEGER");
			db.execSQL("UPDATE buildings SET deleted = 0");
			
			db.execSQL("ALTER TABLE floors ADD deleted INTEGER");
			db.execSQL("UPDATE floors SET deleted = 0");
			
			db.execSQL("ALTER TABLE categories ADD deleted INTEGER");
			db.execSQL("UPDATE categories SET deleted = 0");
			
			db.execSQL("ALTER TABLE items ADD deleted INTEGER");
			db.execSQL("UPDATE items SET deleted = 0");
		}
	}
}
