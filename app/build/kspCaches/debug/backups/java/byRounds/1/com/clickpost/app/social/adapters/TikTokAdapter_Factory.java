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
public final class TikTokAdapter_Factory implements Factory<TikTokAdapter> {
  @Override
  public TikTokAdapter get() {
    return newInstance();
  }

  public static TikTokAdapter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TikTokAdapter newInstance() {
    return new TikTokAdapter();
  }

  private static final class InstanceHolder {
    private static final TikTokAdapter_Factory INSTANCE = new TikTokAdapter_Factory();
  }
}
