package com.clickpost.app.promo.db;

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
import com.clickpost.app.promo.data.PromoDao;
import com.clickpost.app.promo.data.PromoDao_Impl;
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
public final class PromoDatabase_Impl extends PromoDatabase {
  private volatile PromoDao _promoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `hook_videos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `metadata` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `product_assets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `type` TEXT NOT NULL, `description` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `model_images` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `metadata` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `generated_promos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `filePath` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `metadata` TEXT, `productAssetIds` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `promo_music` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c67a383632c1b98fa2fa30055507ef36')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `hook_videos`");
        db.execSQL("DROP TABLE IF EXISTS `product_assets`");
        db.execSQL("DROP TABLE IF EXISTS `model_images`");
        db.execSQL("DROP TABLE IF EXISTS `generated_promos`");
        db.execSQL("DROP TABLE IF EXISTS `promo_music`");
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
        final HashMap<String, TableInfo.Column> _columnsHookVideos = new HashMap<String, TableInfo.Column>(3);
        _columnsHookVideos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookVideos.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookVideos.put("metadata", new TableInfo.Column("metadata", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHookVideos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHookVideos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHookVideos = new TableInfo("hook_videos", _columnsHookVideos, _foreignKeysHookVideos, _indicesHookVideos);
        final TableInfo _existingHookVideos = TableInfo.read(db, "hook_videos");
        if (!_infoHookVideos.equals(_existingHookVideos)) {
          return new RoomOpenHelper.ValidationResult(false, "hook_videos(com.clickpost.app.promo.data.HookVideo).\n"
                  + " Expected:\n" + _infoHookVideos + "\n"
                  + " Found:\n" + _existingHookVideos);
        }
        final HashMap<String, TableInfo.Column> _columnsProductAssets = new HashMap<String, TableInfo.Column>(4);
        _columnsProductAssets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAssets.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAssets.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductAssets.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductAssets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductAssets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductAssets = new TableInfo("product_assets", _columnsProductAssets, _foreignKeysProductAssets, _indicesProductAssets);
        final TableInfo _existingProductAssets = TableInfo.read(db, "product_assets");
        if (!_infoProductAssets.equals(_existingProductAssets)) {
          return new RoomOpenHelper.ValidationResult(false, "product_assets(com.clickpost.app.promo.data.ProductAsset).\n"
                  + " Expected:\n" + _infoProductAssets + "\n"
                  + " Found:\n" + _existingProductAssets);
        }
        final HashMap<String, TableInfo.Column> _columnsModelImages = new HashMap<String, TableInfo.Column>(3);
        _columnsModelImages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModelImages.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsModelImages.put("metadata", new TableInfo.Column("metadata", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysModelImages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesModelImages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoModelImages = new TableInfo("model_images", _columnsModelImages, _foreignKeysModelImages, _indicesModelImages);
        final TableInfo _existingModelImages = TableInfo.read(db, "model_images");
        if (!_infoModelImages.equals(_existingModelImages)) {
          return new RoomOpenHelper.ValidationResult(false, "model_images(com.clickpost.app.promo.data.ModelImage).\n"
                  + " Expected:\n" + _infoModelImages + "\n"
                  + " Found:\n" + _existingModelImages);
        }
        final HashMap<String, TableInfo.Column> _columnsGeneratedPromos = new HashMap<String, TableInfo.Column>(5);
        _columnsGeneratedPromos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGeneratedPromos.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGeneratedPromos.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGeneratedPromos.put("metadata", new TableInfo.Column("metadata", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGeneratedPromos.put("productAssetIds", new TableInfo.Column("productAssetIds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGeneratedPromos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGeneratedPromos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGeneratedPromos = new TableInfo("generated_promos", _columnsGeneratedPromos, _foreignKeysGeneratedPromos, _indicesGeneratedPromos);
        final TableInfo _existingGeneratedPromos = TableInfo.read(db, "generated_promos");
        if (!_infoGeneratedPromos.equals(_existingGeneratedPromos)) {
          return new RoomOpenHelper.ValidationResult(false, "generated_promos(com.clickpost.app.promo.data.GeneratedPromo).\n"
                  + " Expected:\n" + _infoGeneratedPromos + "\n"
                  + " Found:\n" + _existingGeneratedPromos);
        }
        final HashMap<String, TableInfo.Column> _columnsPromoMusic = new HashMap<String, TableInfo.Column>(3);
        _columnsPromoMusic.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromoMusic.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPromoMusic.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPromoMusic = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPromoMusic = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPromoMusic = new TableInfo("promo_music", _columnsPromoMusic, _foreignKeysPromoMusic, _indicesPromoMusic);
        final TableInfo _existingPromoMusic = TableInfo.read(db, "promo_music");
        if (!_infoPromoMusic.equals(_existingPromoMusic)) {
          return new RoomOpenHelper.ValidationResult(false, "promo_music(com.clickpost.app.promo.data.PromoMusic).\n"
                  + " Expected:\n" + _infoPromoMusic + "\n"
                  + " Found:\n" + _existingPromoMusic);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c67a383632c1b98fa2fa30055507ef36", "5d33d0825cd4ff14c4cb5aab58f1ed2c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "hook_videos","product_assets","model_images","generated_promos","promo_music");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `hook_videos`");
      _db.execSQL("DELETE FROM `product_assets`");
      _db.execSQL("DELETE FROM `model_images`");
      _db.execSQL("DELETE FROM `generated_promos`");
      _db.execSQL("DELETE FROM `promo_music`");
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
    _typeConvertersMap.put(PromoDao.class, PromoDao_Impl.getRequiredConverters());
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
  public PromoDao promoDao() {
    if (_promoDao != null) {
      return _promoDao;
    } else {
      synchronized(this) {
        if(_promoDao == null) {
          _promoDao = new PromoDao_Impl(this);
        }
        return _promoDao;
      }
    }
  }
}
