package com.clickpost.app.social.di;

import com.clickpost.app.social.engine.PlatformAdapter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class SocialModule_ProvideTikTokAdapterFactory implements Factory<PlatformAdapter> {
  @Override
  public PlatformAdapter get() {
    return provideTikTokAdapter();
  }

  public static SocialModule_ProvideTikTokAdapterFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlatformAdapter provideTikTokAdapter() {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideTikTokAdapter());
  }

  private static final class InstanceHolder {
    private static final SocialModule_ProvideTikTokAdapterFactory INSTANCE = new SocialModule_ProvideTikTokAdapterFactory();
  }
}
