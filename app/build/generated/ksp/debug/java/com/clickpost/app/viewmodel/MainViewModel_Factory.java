package com.clickpost.app.viewmodel;

import android.app.Application;
import com.clickpost.app.data.repository.ProfileRepository;
import com.clickpost.app.engine.BrandingEngine;
import com.clickpost.app.engine.DeviceCapabilityChecker;
import com.clickpost.app.engine.ExportEngine;
import com.clickpost.app.engine.ShareEngine;
import com.clickpost.app.storage.StorageManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<ProfileRepository> profileRepoProvider;

  private final Provider<StorageManager> storageManagerProvider;

  private final Provider<BrandingEngine> brandingEngineProvider;

  private final Provider<ExportEngine> exportEngineProvider;

  private final Provider<ShareEngine> shareEngineProvider;

  private final Provider<DeviceCapabilityChecker> deviceCapCheckerProvider;

  public MainViewModel_Factory(Provider<Application> applicationProvider,
      Provider<ProfileRepository> profileRepoProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<BrandingEngine> brandingEngineProvider, Provider<ExportEngine> exportEngineProvider,
      Provider<ShareEngine> shareEngineProvider,
      Provider<DeviceCapabilityChecker> deviceCapCheckerProvider) {
    this.applicationProvider = applicationProvider;
    this.profileRepoProvider = profileRepoProvider;
    this.storageManagerProvider = storageManagerProvider;
    this.brandingEngineProvider = brandingEngineProvider;
    this.exportEngineProvider = exportEngineProvider;
    this.shareEngineProvider = shareEngineProvider;
    this.deviceCapCheckerProvider = deviceCapCheckerProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(applicationProvider.get(), profileRepoProvider.get(), storageManagerProvider.get(), brandingEngineProvider.get(), exportEngineProvider.get(), shareEngineProvider.get(), deviceCapCheckerProvider.get());
  }

  public static MainViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<ProfileRepository> profileRepoProvider,
      Provider<StorageManager> storageManagerProvider,
      Provider<BrandingEngine> brandingEngineProvider, Provider<ExportEngine> exportEngineProvider,
      Provider<ShareEngine> shareEngineProvider,
      Provider<DeviceCapabilityChecker> deviceCapCheckerProvider) {
    return new MainViewModel_Factory(applicationProvider, profileRepoProvider, storageManagerProvider, brandingEngineProvider, exportEngineProvider, shareEngineProvider, deviceCapCheckerProvider);
  }

  public static MainViewModel newInstance(Application application, ProfileRepository profileRepo,
      StorageManager storageManager, BrandingEngine brandingEngine, ExportEngine exportEngine,
      ShareEngine shareEngine, DeviceCapabilityChecker deviceCapChecker) {
    return new MainViewModel(application, profileRepo, storageManager, brandingEngine, exportEngine, shareEngine, deviceCapChecker);
  }
}
