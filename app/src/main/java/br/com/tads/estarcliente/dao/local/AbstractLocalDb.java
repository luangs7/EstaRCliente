package br.com.tads.estarcliente.dao.local;

/**
 * Created by renato on 02/02/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.text.Normalizer;
import java.util.List;

import br.com.tads.estarcliente.dao.voley.JsonListHelper;


public abstract class AbstractLocalDb<E> {
    protected Context context;
    private Gson gson;
    private final String spName = "MySharedPreferences";

    protected AbstractLocalDb(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void save(E object, Class<E> type, String name) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = settings.edit();

        String objectJson = gson.toJson(object, type);

        editor.putString(name, objectJson);
        editor.commit();
    }

    public void save(E object) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = settings.edit();

        String objectJson = gson.toJson(object, object.getClass());

        editor.putString(object.getClass().getSimpleName(), objectJson);
        editor.commit();
    }

    public E get(Class<E> type, String name) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);

        E model;
        String objectJson = settings.getString(name, null);
        if (objectJson != null) {
            model = gson.fromJson(objectJson, type);
        }
        else {
            return null;
        }

        return model;

    }

    public E getDefault(Class<E> type) {
        return get(type, type.getSimpleName());
    }

    public void clearObject(String name) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name);
        editor.commit();
    }

    public void clearObject(Class<E> type) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(type.getSimpleName());
        editor.commit();
    }

    private List<E> getList(Class<E> type, JSONArray json) throws Exception {
        Gson gsonB = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        return gsonB.fromJson(removeAcentos(json.toString()), new JsonListHelper<>(type));
    }

    public void saveList(List<E> objectList, Class<E> type, String name) {
        Gson gsonB = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        String json = new Gson().toJson(objectList);
        System.out.println(json);

        SharedPreferences settings = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(name, json);
        editor.commit();
    }

    public List<E> getList(Class<E> type, String name) {
        SharedPreferences settings = context.getSharedPreferences(spName, 0);

        String listJson = settings.getString(name, null);
        if (listJson != null) {
            try {
                List<E> list = getList(type, new JSONArray(listJson));
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<E> getDefaultList(Class<E> type) {
        return getList(type, type.getSimpleName());
    }

    public String removeAcentos(String str) {

        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;

    }

}