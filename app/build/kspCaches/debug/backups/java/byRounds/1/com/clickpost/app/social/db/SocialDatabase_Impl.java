package com.clickpost.app.social.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SocialDatabase_Impl extends SocialDatabase {
  private volatile PublishJobDao _publishJobDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `publish_jobs` (`id` TEXT NOT NULL, `videoPath` TEXT NOT NULL, `caption` TEXT NOT NULL, `hashtagsJson` TEXT NOT NULL, `visibility` TEXT NOT NULL, `selectedGroupIdsJson` TEXT NOT NULL, `resultsJson` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3738667eae1674265f606a0ec708b1e5')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `publish_jobs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPublishJobs = new HashMap<String, TableInfo.Column>(9);
        _columnsPublishJobs.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("videoPath", new TableInfo.Column("videoPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("caption", new TableInfo.Column("caption", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("hashtagsJson", new TableInfo.Column("hashtagsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("visibility", new TableInfo.Column("visibility", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("selectedGroupIdsJson", new TableInfo.Column("selectedGroupIdsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("resultsJson", new TableInfo.Column("resultsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPublishJobs.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPublishJobs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPublishJobs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPublishJobs = new TableInfo("publish_jobs", _columnsPublishJobs, _foreignKeysPublishJobs, _indicesPublishJobs);
        final TableInfo _existingPublishJobs = TableInfo.read(db, "publish_jobs");
        if (!_infoPublishJobs.equals(_existingPublishJobs)) {
          return new RoomOpenHelper.ValidationResult(false, "publish_jobs(com.clickpost.app.social.db.PublishJobEntity).\n"
                  + " Expected:\n" + _infoPublishJobs + "\n"
                  + " Found:\n" + _existingPublishJobs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3738667eae1674265f606a0ec708b1e5", "4e407338fe19a6c60249d5e2b43c9828");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "publish_jobs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `publish_jobs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PublishJobDao.class, PublishJobDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PublishJobDao publishJobDao() {
    if (_publishJobDao != null) {
      return _publishJobDao;
    } else {
      synchronized(this) {
        if(_publishJobDao == null) {
          _publishJobDao = new PublishJobDao_Impl(this);
        }
        return _publishJobDao;
      }
    }
  }
}
