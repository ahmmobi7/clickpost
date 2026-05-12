package com.clickpost.app.engine;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ExportEngine_Factory implements Factory<ExportEngine> {
  private final Provider<Context> contextProvider;

  public ExportEngine_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ExportEngine get() {
    return newInstance(contextProvider.get());
  }

  public static ExportEngine_Factory create(Provider<Context> contextProvider) {
    return new ExportEngine_Factory(contextProvider);
  }

  public static ExportEngine newInstance(Context context) {
    return new ExportEngine(context);
  }
}
