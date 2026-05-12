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
public final class SocialModule_ProvideInstagramAdapterFactory implements Factory<PlatformAdapter> {
  @Override
  public PlatformAdapter get() {
    return provideInstagramAdapter();
  }

  public static SocialModule_ProvideInstagramAdapterFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PlatformAdapter provideInstagramAdapter() {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideInstagramAdapter());
  }

  private static final class InstanceHolder {
    private static final SocialModule_ProvideInstagramAdapterFactory INSTANCE = new SocialModule_ProvideInstagramAdapterFactory();
  }
}
