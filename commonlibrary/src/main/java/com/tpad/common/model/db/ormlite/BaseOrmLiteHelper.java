package com.tpad.common.model.db.ormlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.tpad.common.model.download.autodownload.AutoDownloadData;
import com.tpad.common.model.download.httpconnection.DownloadEntity;
import com.tpad.common.model.processData.entities.LongTextString;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * Created by jone.sun on 2015/9/24.
 */
public abstract class BaseOrmLiteHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = BaseOrmLiteHelper.class.getSimpleName();

    public BaseOrmLiteHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, LongTextString.class);
            TableUtils.createTableIfNotExists(connectionSource, DownloadEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, AutoDownloadData.class);
        } catch (Exception e) {
            Log.e(TAG, "数据表创建失败 ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public void upgradeClazzField(SQLiteDatabase sqLiteDatabase, Class clazz){
        try {
            Log.e(TAG, "onUpgrade>>clazz: " + clazz);
            DatabaseTable databaseTable = (DatabaseTable) clazz.getAnnotation(DatabaseTable.class);
            String tabName = databaseTable.tableName();
            if(tabName == null || tabName.length() == 0){
                tabName = clazz.getSimpleName();
            }
            String tmpTabName = "_temp_" + tabName;

            StringBuilder stringBuilder = new StringBuilder();
            sqLiteDatabase.execSQL("ALTER TABLE " + tabName + " RENAME TO " + tmpTabName);
            TableUtils.createTableIfNotExists(connectionSource, clazz);
            GenericRawResults<String[]> rawResultsTmp =
                    getDao(clazz).queryRaw("select * from " + tmpTabName + " limit 1");
            GenericRawResults<String[]> rawResults =
                    getDao(clazz).queryRaw("select * from " + tabName + " limit 1");
            String[] columnNamesTmp = rawResultsTmp.getColumnNames();
            String[] columnNames = rawResults.getColumnNames();
            int incrementalColumnNum = columnNames.length - columnNamesTmp.length;
            if(incrementalColumnNum > 0){
                for(int i = 0; i < incrementalColumnNum; i++){
                    stringBuilder.append(",");
                    stringBuilder.append("''");
                }
            }
            String sql = "insert into " + tabName + " select *" + stringBuilder.toString() + " from " + tmpTabName;
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.execSQL("DROP TABLE " + tmpTabName);
            Log.e(TAG, "sql: " + sql + "\r\n"
                    + "columnNamesTmp: " + columnNamesTmp.length + "\r\n"
                    + "columnNames: " + columnNames.length);

        }catch (Exception e){
            Log.e(TAG, "error>>upgradeClassField：" + clazz.getName(), e);
            try {
                TableUtils.dropTable(connectionSource, clazz, true);
            } catch (SQLException e1) {
                Log.e(TAG, "error>>upgradeClassField dropTable：" + clazz.getName(), e1);
            }
        }
    }

//    public void upgradeClassField(SQLiteDatabase sqLiteDatabase, Class clazz, int oldVersion){
//        try {
//            Field[] fields = clazz.getFields();
//            Log.e(TAG, "onUpgrade>>clazz: " + clazz + " " + fields.length);
//            for(Field field : fields){
//                Log.e(TAG, "Field>>name: " + field.getName() + " ,type: " + field.getType().getName());
//                DBVersion dbVersion = field.getAnnotation(DBVersion.class);
//                if(dbVersion != null){
//                    DatabaseTable databaseTable = (DatabaseTable) clazz.getAnnotation(DatabaseTable.class);
//                    if(dbVersion.value() - oldVersion >= 1){
//                        String tabName = databaseTable.tableName();
//                        if(tabName == null || tabName.length() == 0){
//                            tabName = clazz.getSimpleName();
//                        }
//                        Dao studentDao = getDao(clazz);
//                        String fieldType = field.getType().getName();
//                        if(fieldType.equals(Integer.class.getName())){
//                            fieldType = "INTEGER";
//                        }else if(fieldType.equals(String.class.getName())){
//                            fieldType = "TEXT";
//                        }
//                        String sql = "ALTER TABLE '" + tabName + "' ADD COLUMN "
//                                + field.getName() + " " + fieldType + "";
//                        int result = studentDao.executeRaw(sql);
//                        Log.e(TAG, "result: " + result + " sql>>" + sql);
//                    }
//                }
//            }
//        }catch (Exception e){
//            Log.e(TAG, "error>>upgradeClassField：" + clazz.getName(), e);
//        }
//    }

    public static <D extends Dao<T, ?>, T> D getDao(Class<T> clazz, ConnectionSource connectionSource) throws SQLException {
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                dao = DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = DaoManager.createDao(connectionSource, tableConfig);
            }
        }
        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                dao = DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = DaoManager.createDao(connectionSource, tableConfig);
            }
        }
        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
