package com.mantouland.atool;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by asparaw on 2019/3/19.
 */

public class Ason {
    private static final String TAG="ASON";

    /***
     * pass in the bean object
     * support one generic type
     * create generic object by an anonymous inner class
     * @param jsonString
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonString,T object){
        try {
                JSONObject pointer=new JSONObject(jsonString);
                return jsonDFS(pointer,object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * pass in the bean class
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz){
        Log.d(TAG, "fromJson: "+jsonString.length());
        try {
            JSONObject pointer=new JSONObject(jsonString);
            return jsonDFS(pointer,clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static <T> T jsonDFS(JSONObject pointer, Class<T> clazz) throws IllegalAccessException, NoSuchFieldException, JSONException, InstantiationException {
        T bean=clazz.newInstance();
        Iterator<String> keys= pointer.keys();
        while (keys.hasNext()){
            String name= keys.next();
            Field field= bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object value=pointer.get(name);
            if (value instanceof JSONObject){
                Class setType;
                Type checkType=clazz.getClass().getGenericSuperclass();
                if (checkType instanceof  ParameterizedType){
                    setType= (Class) ((ParameterizedType)clazz.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                }else {
                    setType=field.getType();
                }
                field.set(bean,jsonDFS((JSONObject) value,setType));
            }else if (value instanceof JSONArray){
                JSONArray jsonArray= (JSONArray) value;
                List list=new ArrayList();
                if(jsonArray.length()==0){
                    //DO NOTHING
                }else if (jsonArray.get(0) instanceof JSONArray || jsonArray.get(0) instanceof  JSONObject){
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class type = (Class) pt.getActualTypeArguments()[0];
                    //get the generic type from the original list
                    for (int i=0;i<jsonArray.length();i++){
                        list.add(jsonDFS((JSONObject) jsonArray.get(i),type));
                    }
                }else {
                    for (int i=0;i<jsonArray.length();i++){
                        list.add(jsonArray.get(i));
                    }
                }
                field.set(bean,list);
            }else {
                //Log.d(TAG, "jsonDFS: "+bean+" "+value);
                field.set(bean,value);
            }
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    private static <T> T jsonDFS(JSONObject pointer, T object) throws IllegalAccessException, NoSuchFieldException, JSONException, InstantiationException {
        Iterator<String> keys= pointer.keys();
        while (keys.hasNext()){
            String name= keys.next();
            Field field;
            Type checkType=object.getClass().getGenericSuperclass();
            if (checkType instanceof ParameterizedType){
                field= object.getClass().getSuperclass().getDeclaredField(name);
            }else {
                field= object.getClass().getDeclaredField(name);
            }
            field.setAccessible(true);
            Object value=pointer.get(name);
            if (value instanceof JSONObject){
                Object newObject;
                Class setType;
                if (checkType instanceof  ParameterizedType){
                    setType= (Class) ((ParameterizedType)object.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    newObject=setType.newInstance();
                    field.set(object,jsonDFS((JSONObject) value,newObject));
                }else {
                    setType=field.getType();
                    newObject=setType.newInstance();

                }
                field.set(object,jsonDFS((JSONObject) value,newObject));
            }else if (value instanceof JSONArray){
                JSONArray jsonArray= (JSONArray) value;
                List list=new ArrayList();
                if(jsonArray.length()==0){
                    //DO NOTHING
                }else if (jsonArray.get(0) instanceof JSONArray || jsonArray.get(0) instanceof  JSONObject){
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class type = (Class) pt.getActualTypeArguments()[0];
                    Object newObject =type.newInstance();
                    //get the generic type from the original list
                    for (int i=0;i<jsonArray.length();i++){
                        list.add(jsonDFS((JSONObject) jsonArray.get(i),newObject));
                    }
                }else {
                    for (int i=0;i<jsonArray.length();i++){
                        list.add(jsonArray.get(i));
                    }
                }
                field.set(object,list);
            }else {
                field.set(object,value);
            }
        }
        return object;
    }

}