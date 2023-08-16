package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "INFO";
    public static final String NAME = "NAME";
    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Fall_UserInfo.db";
    ;
    private static final String ID = "ID";
    public static final String AGE = "AGE";
    public static final String ADDRESS = "ADDRESS";
    public static final String FAMILYPHONE = "FAMILYPHONE";
    public static final String USERPHONE = "USERPHONE";
    public static final String BLOODTYPE = "BLOODTYPE";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // 데이터베이스가 생성될 때 호출되는 메서드
    @Override
    public void onCreate(SQLiteDatabase db) {
        // INFO 테이블을 생성하는 쿼리문
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + AGE + " INTEGER, "
                + BLOODTYPE + " TEXT, "
                + USERPHONE + " TEXT, "
                + FAMILYPHONE + " TEXT, "
                + ADDRESS + " TEXT)";
        db.execSQL(query);
    }

    // 데이터베이스를 업그레이드할 때 호출되는 메서드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블을 삭제하고 새로운 버전의 테이블을 생성하는 쿼리문
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 정보를 추가하는 메서드
    void addInfo(String name, int age, String bloodType, String userPhone, String familyPhone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, name);
        cv.put(AGE, age);
        cv.put(BLOODTYPE, bloodType);
        cv.put(USERPHONE, userPhone);
        cv.put(FAMILYPHONE, familyPhone);
        cv.put(ADDRESS, address);

        // INFO 테이블에 데이터 삽입
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "데이터 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "데이터가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    void addupdate(int id, String name, int age, String bloodtype, String userphone, String familyphone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, name);
        cv.put(AGE, age);
        cv.put(BLOODTYPE, bloodtype);
        cv.put(USERPHONE, userphone);
        cv.put(FAMILYPHONE, familyphone);
        cv.put(ADDRESS, address);

        // 기본키인 'id' 값을 조건으로 설정하여 해당 행을 업데이트
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{String.valueOf(id)});

        if (result == 1) {
            // 업데이트 성공
            Toast.makeText(context, "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            // 업데이트 실패
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    // DBHelper 클래스 내에 추가할 메서드: 주소를 업데이트합니다.
    void updateAddress(String id, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ADDRESS, address);
        // 기본키(id) 값을 기준으로 해당 행의 주소를 새로운 값으로 업데이트합니다.
        int rowsUpdated = db.update(TABLE_NAME, cv, ID + "=?", new String[]{id});
        if (rowsUpdated > 0) {
            Toast.makeText(context, "주소가 수정되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "주소 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    // 모든 정보를 조회하여 리스트로 반환하는 메서드
    List<String> getAllInfo() {
        List<String> infoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(NAME));
                @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex(AGE));
                @SuppressLint("Range") String bloodType = cursor.getString(cursor.getColumnIndex(BLOODTYPE));
                @SuppressLint("Range") String userPhone = cursor.getString(cursor.getColumnIndex(USERPHONE));
                @SuppressLint("Range") String familyPhone = cursor.getString(cursor.getColumnIndex(FAMILYPHONE));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                String info = "이름: " + name + ", 나이: " + age + ", 혈액형: " + bloodType
                        + ", 사용자 전화번호: " + userPhone + ", 가족 전화번호: " + familyPhone
                        + ", 주소: " + address;
                infoList.add(info);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return infoList;
    }

    // 특정 ID를 기준으로 행을 삭제하는 메서드
    public void deleteInfo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to get database
    @SuppressLint("Range")
    public String getFamilyPhone() {
        String familyPhone = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + FAMILYPHONE + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            familyPhone = cursor.getString(cursor.getColumnIndex(FAMILYPHONE));
            cursor.close();
        }
        return familyPhone;
    }

    @SuppressLint("Range")
    public String getName() {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + NAME + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(NAME));
            cursor.close();
        }
        return name;
    }

    @SuppressLint("Range")
    public String getAddress() {
        String address = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ADDRESS + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            address = cursor.getString(cursor.getColumnIndex(ADDRESS));
            cursor.close();
        }
        return address;
    }

    @SuppressLint("Range")
    public String getBloodtype() {
        String bloodtype = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + BLOODTYPE + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            bloodtype = cursor.getString(cursor.getColumnIndex(BLOODTYPE));
            cursor.close();
        }
        return bloodtype;
    }

    @SuppressLint("Range")
    public String getAge() {
        String age = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + AGE + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            age = String.valueOf(cursor.getInt(cursor.getColumnIndex(AGE)));
            cursor.close();
        }
        return age;
    }
}


