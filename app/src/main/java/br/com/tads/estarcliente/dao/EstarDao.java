package br.com.tads.estarcliente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.tads.estarcliente.model.Veiculo;


/**
 * Created by DevMaker on 3/23/16.
 */
public class EstarDao {
    private Context context;

    public EstarDao(Context context) {
        this.context = context;
    }


    public ArrayList<Veiculo> getAllVeiculo() {
        OpenHelper db = new OpenHelper(context);
        String json = "";
        Gson gson = new Gson();

        SQLiteDatabase dbl = db.getReadableDatabase();
        Cursor cursor = dbl.rawQuery("select * from veiculos ", null);
        ArrayList<Veiculo> properties = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Veiculo veiculo = new Veiculo();
                json = cursor.getString(0);
                veiculo = gson.fromJson(json,Veiculo.class);
                properties.add(veiculo);
            } while (cursor.moveToNext());

        }
        dbl.close();
        return properties;
    }

    public boolean saveVeiculo(String json) {
        try {
            OpenHelper db = new OpenHelper(context);
            SQLiteDatabase dbl = db.getWritableDatabase();


            dbl.execSQL("insert into finances(json) veiculos (?);",
                    new Object[]{
                            json
                    });

            dbl.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }



}
