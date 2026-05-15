package com.clickpost.app.promo.engine;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class BackgroundRemover_Factory implements Factory<BackgroundRemover> {
  @Override
  public BackgroundRemover get() {
    return newInstance();
  }

  public static BackgroundRemover_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BackgroundRemover newInstance() {
    return new BackgroundRemover();
  }

  private static final class InstanceHolder {
    private static final BackgroundRemover_Factory INSTANCE = new BackgroundRemover_Factory();
  }
}
