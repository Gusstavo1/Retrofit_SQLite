package com.gcr.retrofit_sqlite.utils;

public class SQLScripts {

    public static String CATEGORIA = "categoria";
    public static String PRECIO = "precio";
    public static String INSTRUCCIONES = "instrucciones";
    public static String FOTO_URL = "foto";
    public static String FOTO = "bitmap";
    public static String NOMBRE = "nombre";
    public static String ID = "id";
    public static String TABLE_NAME = "flores";

    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            CATEGORIA + " TEXT ," +
            PRECIO + " DOUBLE ," +
            INSTRUCCIONES + " TEXT ," +
            FOTO_URL + " VARCHAR(50) ," +
            NOMBRE + " TEXT ," +
            ID + " INTEGER PRIMARY KEY)";
}
