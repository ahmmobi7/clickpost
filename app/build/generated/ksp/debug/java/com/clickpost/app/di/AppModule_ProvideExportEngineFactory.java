package com.clickpost.app.di;

import android.content.Context;
import com.clickpost.app.engine.ExportEngine;
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
public final class AppModule_ProvideExportEngineFactory implements Factory<ExportEngine> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideExportEngineFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ExportEngine get() {
    return provideExportEngine(contextProvider.get());
  }

  public static AppModule_ProvideExportEngineFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideExportEngineFactory(contextProvider);
  }

  public static ExportEngine provideExportEngine(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExportEngine(context));
  }
}
