package com.clickpost.app.di;

import android.content.Context;
import com.clickpost.app.storage.StorageManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppModule_ProvideStorageManagerFactory implements Factory<StorageManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideStorageManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StorageManager get() {
    return provideStorageManager(contextProvider.get());
  }

  public static AppModule_ProvideStorageManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideStorageManagerFactory(contextProvider);
  }

  public static StorageManager provideStorageManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideStorageManager(context));
  }
}
