package com.example.ouamar.ouamsapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ouamar.ouamsapp.R;
import com.example.ouamar.ouamsapp.entite.Note;
import com.example.ouamar.ouamsapp.entite.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * Created by Ouamar on 18/10/2015.
 */
public class OuamsappDatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "ouamsapp";
    private static final int DATABASE_VERSION = 1;
    private static OuamsappDatabaseHelper instance;
    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<Note, Long> noteDao;
    private Dao<User, Long> userssDao;

    public OuamsappDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                /**
                 * R.raw.ormlite_config is a reference to the ormlite_config.txt file in the
                 * /res/raw/ directory of this project
                 * */
                R.raw.ormlite_config);
    }


    public static synchronized OuamsappDatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new OuamsappDatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the database tables
             */
            TableUtils.createTable(connectionSource, Note.class);
            TableUtils.createTable(connectionSource, User.class);


        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Echec de la création de la database" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */

            TableUtils.dropTable(connectionSource, Note.class, false);
            TableUtils.dropTable(connectionSource, User.class, false);

            onCreate(database, connectionSource);


        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Echec de la mise à jour de la database");
        }
    }

    /**
     * Returns an instance of the data access object
     *
     * @return
     * @throws SQLException
     */

    //Getting Adresse
    public Dao<Note, Long> getNoteDao() throws SQLException {
        if (noteDao == null) {
            noteDao = getDao(Note.class);
        }
        return noteDao;
    }

    // Getting user
    public Dao<User, Long> getUsersDao() throws SQLException {
        if (userssDao == null) {
            userssDao = getDao(User.class);
        }
        return userssDao;
    }


}



