package com.clickpost.app.di;

import android.content.Context;
import com.clickpost.app.engine.BrandingEngine;
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
public final class AppModule_ProvideBrandingEngineFactory implements Factory<BrandingEngine> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideBrandingEngineFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BrandingEngine get() {
    return provideBrandingEngine(contextProvider.get());
  }

  public static AppModule_ProvideBrandingEngineFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideBrandingEngineFactory(contextProvider);
  }

  public static BrandingEngine provideBrandingEngine(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideBrandingEngine(context));
  }
}
