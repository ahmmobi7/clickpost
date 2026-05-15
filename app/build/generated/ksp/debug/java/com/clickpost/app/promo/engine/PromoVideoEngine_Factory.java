package com.clickpost.app.promo.engine;

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
public final class PromoVideoEngine_Factory implements Factory<PromoVideoEngine> {
  private final Provider<Context> contextProvider;

  public PromoVideoEngine_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PromoVideoEngine get() {
    return newInstance(contextProvider.get());
  }

  public static PromoVideoEngine_Factory create(Provider<Context> contextProvider) {
    return new PromoVideoEngine_Factory(contextProvider);
  }

  public static PromoVideoEngine newInstance(Context context) {
    return new PromoVideoEngine(context);
  }
}
