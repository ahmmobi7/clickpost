package com.clickpost.app.di;

import android.content.Context;
import com.clickpost.app.engine.DeviceCapabilityChecker;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideDeviceCapabilityCheckerFactory implements Factory<DeviceCapabilityChecker> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideDeviceCapabilityCheckerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DeviceCapabilityChecker get() {
    return provideDeviceCapabilityChecker(contextProvider.get());
  }

  public static AppModule_ProvideDeviceCapabilityCheckerFactory create(
      Provider<Context> contextProvider) {
    return new AppModule_ProvideDeviceCapabilityCheckerFactory(contextProvider);
  }

  public static DeviceCapabilityChecker provideDeviceCapabilityChecker(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDeviceCapabilityChecker(context));
  }
}
