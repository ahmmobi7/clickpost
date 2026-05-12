package com.clickpost.app.engine;

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
public final class DeviceCapabilityChecker_Factory implements Factory<DeviceCapabilityChecker> {
  private final Provider<Context> contextProvider;

  public DeviceCapabilityChecker_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DeviceCapabilityChecker get() {
    return newInstance(contextProvider.get());
  }

  public static DeviceCapabilityChecker_Factory create(Provider<Context> contextProvider) {
    return new DeviceCapabilityChecker_Factory(contextProvider);
  }

  public static DeviceCapabilityChecker newInstance(Context context) {
    return new DeviceCapabilityChecker(context);
  }
}
