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
public final class InstagramAdapter_Factory implements Factory<InstagramAdapter> {
  @Override
  public InstagramAdapter get() {
    return newInstance();
  }

  public static InstagramAdapter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static InstagramAdapter newInstance() {
    return new InstagramAdapter();
  }

  private static final class InstanceHolder {
    private static final InstagramAdapter_Factory INSTANCE = new InstagramAdapter_Factory();
  }
}
