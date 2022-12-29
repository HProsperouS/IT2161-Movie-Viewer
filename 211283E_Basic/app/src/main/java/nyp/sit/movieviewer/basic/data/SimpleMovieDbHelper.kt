package nyp.sit.movieviewer.basic.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import nyp.sit.movieviewer.basic.entity.SimpleMovieItem
import java.lang.Exception

class SimpleMovieDbHelper(c: Context) : SQLiteOpenHelper(c, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        val DATABASE_NAME = "simplemovie.db"
        val DATABASE_VER = 1
        private const val TBL_FAV = "tbl_favourites"
        private const val ID = "id"
        private const val NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblMovie = ("CREATE TABLE " + TBL_FAV + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT"
                + ")"
                )
        db?.execSQL(createTblMovie)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_FAV")
        onCreate(db)
    }

    fun insertFav(movie: SimpleMovieItem):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(NAME,movie.title)

        val success = db.insert(TBL_FAV,null,contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAll():ArrayList<SimpleMovieItem>{
        val favouriteList:ArrayList<SimpleMovieItem> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_FAV"

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var name: String

        if (cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex("name"))
                val movie = SimpleMovieItem(title = name)
                favouriteList.add(movie)

            }while (cursor.moveToNext())
        }
        return favouriteList
    }

}