package com.JFJB.helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class SQLiteDatabaseHelper extends SQLiteOpenHelper{

	
	public SQLiteDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);		
	}

	private static final int VERSION = 1;	
	public SQLiteDatabaseHelper(Context context, String name,
			 int version) {
		this(context, name, null, version);	
	}
	
	public SQLiteDatabaseHelper(Context context, String name
			) {
		this(context, name, null, VERSION);
		
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("Êý¾Ý¿âÃû", getDatabaseName());
		db.execSQL("CREATE TABLE Jfjb_app(id int , title varchar(5000),num varchar(5000)," +
				"likes varchar(5000),dotime varchar(5000))");
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
}