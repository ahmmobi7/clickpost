package com.clickpost.app.social.adapters;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class YouTubeAdapter_Factory implements Factory<YouTubeAdapter> {
  @Override
  public YouTubeAdapter get() {
    return newInstance();
  }

  public static YouTubeAdapter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static YouTubeAdapter newInstance() {
    return new YouTubeAdapter();
  }

  private static final class InstanceHolder {
    private static final YouTubeAdapter_Factory INSTANCE = new YouTubeAdapter_Factory();
  }
}
