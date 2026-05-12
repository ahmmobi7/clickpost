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
public final class FacebookAdapter_Factory implements Factory<FacebookAdapter> {
  @Override
  public FacebookAdapter get() {
    return newInstance();
  }

  public static FacebookAdapter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FacebookAdapter newInstance() {
    return new FacebookAdapter();
  }

  private static final class InstanceHolder {
    private static final FacebookAdapter_Factory INSTANCE = new FacebookAdapter_Factory();
  }
}
