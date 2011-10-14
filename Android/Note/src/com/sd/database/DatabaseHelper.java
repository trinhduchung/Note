package com.sd.database;

//import gnt.mobion.music.playmusic.SongInfo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sd.model.FolderInfo;

public class DatabaseHelper extends SQLiteOpenHelper {
	private Context _context;
	public static final String DBName = "note.db";
	public static final int DBVersion = 4;
	/*note table */
	public final static String KEY_ID = "id";
	public static final String NAME = "name";
	public static final String IMG_ID = "img_id";
	public static final String ICON = "icon";
	public static final String PASSWORD = "password";
	public static final String COUNT = "count";

	public static final String FOLDER_TABLE = "folder";
	public static final String NOTE_TABLE = "node";
	
	public DatabaseHelper(Context context) {
		super(context, DBName, null, DBVersion);
		_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " + FOLDER_TABLE + " (" + KEY_ID
				+ " integer PRIMARY KEY autoincrement ," + "" + NAME
				+ " text NOT NULL ," + IMG_ID + " integer ,"
				+ ICON + " integer ," + "" + PASSWORD
				+ " text NOT NULL ," + COUNT + " integer);";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("drop table if exists " + FOLDER_TABLE);
			this.onCreate(db);
		}
	}

	public ArrayList<FolderInfo> getAllFolder() {
		ArrayList<FolderInfo> list = new ArrayList<FolderInfo>();
		String[] selectArg = new String[] {KEY_ID, NAME, IMG_ID, ICON, PASSWORD, COUNT};
		Cursor cursor = this.getReadableDatabase().query(FOLDER_TABLE, selectArg , null, null, null, null, null);
		while(cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
			String name = cursor.getString(cursor.getColumnIndex(NAME));
			int img = cursor.getInt(cursor.getColumnIndex(IMG_ID));
			int icon = cursor.getInt(cursor.getColumnIndex(ICON));
			String password = cursor.getString(cursor.getColumnIndex(PASSWORD));
			int count = cursor.getInt(cursor.getColumnIndex(COUNT));
			
			FolderInfo info = new FolderInfo(id, name, img, icon, password, count);
			list.add(info);
		}
		cursor.close();
		this.getReadableDatabase().close();
		return list;
	}
	
	public long insertFolder(FolderInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, info.getName());
		values.put(IMG_ID, info.getImg());
		values.put(ICON, info.getIcon());
		values.put(PASSWORD, info.getPassword());
		values.put(COUNT, info.getCount());
		
		long id = db.insert(FOLDER_TABLE, null, values);
		db.close();
		return id;
	}
	
	public void deleteFolder(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String whereClause = KEY_ID + "=?";
		db.delete(FOLDER_TABLE, whereClause, new String[] { String.valueOf(id) });
		db.close();
	}
}
