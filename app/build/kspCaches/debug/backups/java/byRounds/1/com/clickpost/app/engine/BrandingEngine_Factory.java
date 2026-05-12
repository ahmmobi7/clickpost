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
public final class BrandingEngine_Factory implements Factory<BrandingEngine> {
  private final Provider<Context> contextProvider;

  public BrandingEngine_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BrandingEngine get() {
    return newInstance(contextProvider.get());
  }

  public static BrandingEngine_Factory create(Provider<Context> contextProvider) {
    return new BrandingEngine_Factory(contextProvider);
  }

  public static BrandingEngine newInstance(Context context) {
    return new BrandingEngine(context);
  }
}
