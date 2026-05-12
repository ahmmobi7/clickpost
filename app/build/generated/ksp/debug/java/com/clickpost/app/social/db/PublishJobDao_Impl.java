package com.clickpost.app.social.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
public final class PublishJobDao_Impl implements PublishJobDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PublishJobEntity> __insertionAdapterOfPublishJobEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public PublishJobDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPublishJobEntity = new EntityInsertionAdapter<PublishJobEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `publish_jobs` (`id`,`videoPath`,`caption`,`hashtagsJson`,`visibility`,`selectedGroupIdsJson`,`resultsJson`,`status`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PublishJobEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getVideoPath());
        statement.bindString(3, entity.getCaption());
        statement.bindString(4, entity.getHashtagsJson());
        statement.bindString(5, entity.getVisibility());
        statement.bindString(6, entity.getSelectedGroupIdsJson());
        statement.bindString(7, entity.getResultsJson());
        statement.bindString(8, entity.getStatus());
        statement.bindLong(9, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE publish_jobs SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM publish_jobs WHERE createdAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PublishJobEntity job, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPublishJobEntity.insert(job);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final String id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PublishJobEntity>> getAllJobs() {
    final String _sql = "SELECT * FROM publish_jobs ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"publish_jobs"}, new Callable<List<PublishJobEntity>>() {
      @Override
      @NonNull
      public List<PublishJobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVideoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "videoPath");
          final int _cursorIndexOfCaption = CursorUtil.getColumnIndexOrThrow(_cursor, "caption");
          final int _cursorIndexOfHashtagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "hashtagsJson");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfSelectedGroupIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedGroupIdsJson");
          final int _cursorIndexOfResultsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "resultsJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<PublishJobEntity> _result = new ArrayList<PublishJobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PublishJobEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpVideoPath;
            _tmpVideoPath = _cursor.getString(_cursorIndexOfVideoPath);
            final String _tmpCaption;
            _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
            final String _tmpHashtagsJson;
            _tmpHashtagsJson = _cursor.getString(_cursorIndexOfHashtagsJson);
            final String _tmpVisibility;
            _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            final String _tmpSelectedGroupIdsJson;
            _tmpSelectedGroupIdsJson = _cursor.getString(_cursorIndexOfSelectedGroupIdsJson);
            final String _tmpResultsJson;
            _tmpResultsJson = _cursor.getString(_cursorIndexOfResultsJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new PublishJobEntity(_tmpId,_tmpVideoPath,_tmpCaption,_tmpHashtagsJson,_tmpVisibility,_tmpSelectedGroupIdsJson,_tmpResultsJson,_tmpStatus,_tmpCreatedAt);
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
  public Object getById(final String id, final Continuation<? super PublishJobEntity> $completion) {
    final String _sql = "SELECT * FROM publish_jobs WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PublishJobEntity>() {
      @Override
      @Nullable
      public PublishJobEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVideoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "videoPath");
          final int _cursorIndexOfCaption = CursorUtil.getColumnIndexOrThrow(_cursor, "caption");
          final int _cursorIndexOfHashtagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "hashtagsJson");
          final int _cursorIndexOfVisibility = CursorUtil.getColumnIndexOrThrow(_cursor, "visibility");
          final int _cursorIndexOfSelectedGroupIdsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedGroupIdsJson");
          final int _cursorIndexOfResultsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "resultsJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final PublishJobEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpVideoPath;
            _tmpVideoPath = _cursor.getString(_cursorIndexOfVideoPath);
            final String _tmpCaption;
            _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
            final String _tmpHashtagsJson;
            _tmpHashtagsJson = _cursor.getString(_cursorIndexOfHashtagsJson);
            final String _tmpVisibility;
            _tmpVisibility = _cursor.getString(_cursorIndexOfVisibility);
            final String _tmpSelectedGroupIdsJson;
            _tmpSelectedGroupIdsJson = _cursor.getString(_cursorIndexOfSelectedGroupIdsJson);
            final String _tmpResultsJson;
            _tmpResultsJson = _cursor.getString(_cursorIndexOfResultsJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new PublishJobEntity(_tmpId,_tmpVideoPath,_tmpCaption,_tmpHashtagsJson,_tmpVisibility,_tmpSelectedGroupIdsJson,_tmpResultsJson,_tmpStatus,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
