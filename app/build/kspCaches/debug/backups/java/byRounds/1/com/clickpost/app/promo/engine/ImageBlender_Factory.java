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
public final class ImageBlender_Factory implements Factory<ImageBlender> {
  @Override
  public ImageBlender get() {
    return newInstance();
  }

  public static ImageBlender_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ImageBlender newInstance() {
    return new ImageBlender();
  }

  private static final class InstanceHolder {
    private static final ImageBlender_Factory INSTANCE = new ImageBlender_Factory();
  }
}
