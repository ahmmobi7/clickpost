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
public final class SocialModule_ProvideYouTubeAdapterFactory implements Factory<PlatformAdapter> {
  @Override
  public PlatformAdapter get() {
    return provideYouTubeAdapter();
  }

  public static SocialModule_ProvideYouTubeAdapterFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlatformAdapter provideYouTubeAdapter() {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideYouTubeAdapter());
  }

  private static final class InstanceHolder {
    private static final SocialModule_ProvideYouTubeAdapterFactory INSTANCE = new SocialModule_ProvideYouTubeAdapterFactory();
  }
}
