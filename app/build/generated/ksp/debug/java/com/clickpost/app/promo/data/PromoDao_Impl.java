package com.clickpost.app.promo.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PromoDao_Impl implements PromoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HookVideo> __insertionAdapterOfHookVideo;

  private final EntityInsertionAdapter<ProductAsset> __insertionAdapterOfProductAsset;

  private final EntityInsertionAdapter<ModelImage> __insertionAdapterOfModelImage;

  private final EntityInsertionAdapter<GeneratedPromo> __insertionAdapterOfGeneratedPromo;

  private final EntityInsertionAdapter<PromoMusic> __insertionAdapterOfPromoMusic;

  private final EntityDeletionOrUpdateAdapter<GeneratedPromo> __deletionAdapterOfGeneratedPromo;

  public PromoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHookVideo = new EntityInsertionAdapter<HookVideo>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `hook_videos` (`id`,`uri`,`metadata`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HookVideo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUri());
        if (entity.getMetadata() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMetadata());
        }
      }
    };
    this.__insertionAdapterOfProductAsset = new EntityInsertionAdapter<ProductAsset>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `product_assets` (`id`,`uri`,`type`,`description`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductAsset entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUri());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getDescription());
      }
    };
    this.__insertionAdapterOfModelImage = new EntityInsertionAdapter<ModelImage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `model_images` (`id`,`uri`,`metadata`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ModelImage entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUri());
        if (entity.getMetadata() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMetadata());
        }
      }
    };
    this.__insertionAdapterOfGeneratedPromo = new EntityInsertionAdapter<GeneratedPromo>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `generated_promos` (`id`,`filePath`,`timestamp`,`metadata`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GeneratedPromo entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getFilePath());
        statement.bindLong(3, entity.getTimestamp());
        if (entity.getMetadata() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMetadata());
        }
      }
    };
    this.__insertionAdapterOfPromoMusic = new EntityInsertionAdapter<PromoMusic>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `promo_music` (`id`,`uri`,`name`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PromoMusic entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUri());
        statement.bindString(3, entity.getName());
      }
    };
    this.__deletionAdapterOfGeneratedPromo = new EntityDeletionOrUpdateAdapter<GeneratedPromo>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `generated_promos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GeneratedPromo entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertHookVideo(final HookVideo hookVideo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHookVideo.insert(hookVideo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProductAsset(final ProductAsset productAsset,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductAsset.insert(productAsset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertModelImage(final ModelImage modelImage,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfModelImage.insert(modelImage);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertGeneratedPromo(final GeneratedPromo generatedPromo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGeneratedPromo.insert(generatedPromo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMusic(final PromoMusic music, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPromoMusic.insert(music);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGeneratedPromo(final GeneratedPromo generatedPromo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGeneratedPromo.handle(generatedPromo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HookVideo>> getAllHookVideos() {
    final String _sql = "SELECT * FROM hook_videos";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"hook_videos"}, new Callable<List<HookVideo>>() {
      @Override
      @NonNull
      public List<HookVideo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final List<HookVideo> _result = new ArrayList<HookVideo>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HookVideo _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            _item = new HookVideo(_tmpId,_tmpUri,_tmpMetadata);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ProductAsset>> getAllProductAssets() {
    final String _sql = "SELECT * FROM product_assets";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"product_assets"}, new Callable<List<ProductAsset>>() {
      @Override
      @NonNull
      public List<ProductAsset> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<ProductAsset> _result = new ArrayList<ProductAsset>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductAsset _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item = new ProductAsset(_tmpId,_tmpUri,_tmpType,_tmpDescription);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ModelImage>> getAllModelImages() {
    final String _sql = "SELECT * FROM model_images";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"model_images"}, new Callable<List<ModelImage>>() {
      @Override
      @NonNull
      public List<ModelImage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final List<ModelImage> _result = new ArrayList<ModelImage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ModelImage _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            _item = new ModelImage(_tmpId,_tmpUri,_tmpMetadata);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GeneratedPromo>> getAllGeneratedPromos() {
    final String _sql = "SELECT * FROM generated_promos ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"generated_promos"}, new Callable<List<GeneratedPromo>>() {
      @Override
      @NonNull
      public List<GeneratedPromo> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMetadata = CursorUtil.getColumnIndexOrThrow(_cursor, "metadata");
          final List<GeneratedPromo> _result = new ArrayList<GeneratedPromo>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GeneratedPromo _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFilePath;
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMetadata;
            if (_cursor.isNull(_cursorIndexOfMetadata)) {
              _tmpMetadata = null;
            } else {
              _tmpMetadata = _cursor.getString(_cursorIndexOfMetadata);
            }
            _item = new GeneratedPromo(_tmpId,_tmpFilePath,_tmpTimestamp,_tmpMetadata);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PromoMusic>> getAllMusic() {
    final String _sql = "SELECT * FROM promo_music";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"promo_music"}, new Callable<List<PromoMusic>>() {
      @Override
      @NonNull
      public List<PromoMusic> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final List<PromoMusic> _result = new ArrayList<PromoMusic>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PromoMusic _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new PromoMusic(_tmpId,_tmpUri,_tmpName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
