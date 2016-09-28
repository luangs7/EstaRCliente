package br.com.tads.estarcliente.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "estar.db";
	private final Context context;
	
	public OpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        version1(db);
	}

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       String drop = "DROP TABLE veiculos";
        db.execSQL(drop);
       onCreate(db);
	}

    private void version1(SQLiteDatabase db) {
        String table = "create table veiculos " +
                "( json varchar(500)," +
                "_id integer primary key autoincrement)";

        db.execSQL(table);
    }
}
