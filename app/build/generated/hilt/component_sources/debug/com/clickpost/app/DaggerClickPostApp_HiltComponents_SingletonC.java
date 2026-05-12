package com.clickpost.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.clickpost.app.data.repository.ProfileRepository;
import com.clickpost.app.di.AppModule_ProvideBrandingEngineFactory;
import com.clickpost.app.di.AppModule_ProvideDeviceCapabilityCheckerFactory;
import com.clickpost.app.di.AppModule_ProvideExportEngineFactory;
import com.clickpost.app.di.AppModule_ProvideShareEngineFactory;
import com.clickpost.app.di.AppModule_ProvideStorageManagerFactory;
import com.clickpost.app.engine.BrandingEngine;
import com.clickpost.app.engine.DeviceCapabilityChecker;
import com.clickpost.app.engine.ExportEngine;
import com.clickpost.app.engine.ShareEngine;
import com.clickpost.app.social.data.Platform;
import com.clickpost.app.social.db.PublishJobDao;
import com.clickpost.app.social.db.SocialDatabase;
import com.clickpost.app.social.di.SocialModule_ProvideAccountGroupRepositoryFactory;
import com.clickpost.app.social.di.SocialModule_ProvideCredentialVaultFactory;
import com.clickpost.app.social.di.SocialModule_ProvideFacebookAdapterFactory;
import com.clickpost.app.social.di.SocialModule_ProvideInstagramAdapterFactory;
import com.clickpost.app.social.di.SocialModule_ProvidePublishEngineFactory;
import com.clickpost.app.social.di.SocialModule_ProvidePublishHistoryRepositoryFactory;
import com.clickpost.app.social.di.SocialModule_ProvidePublishJobDaoFactory;
import com.clickpost.app.social.di.SocialModule_ProvideSocialDatabaseFactory;
import com.clickpost.app.social.di.SocialModule_ProvideTikTokAdapterFactory;
import com.clickpost.app.social.di.SocialModule_ProvideYouTubeAdapterFactory;
import com.clickpost.app.social.engine.PlatformAdapter;
import com.clickpost.app.social.engine.PublishEngine;
import com.clickpost.app.social.repository.AccountGroupRepository;
import com.clickpost.app.social.repository.PublishHistoryRepository;
import com.clickpost.app.social.storage.CredentialVault;
import com.clickpost.app.social.viewmodel.AccountGroupViewModel;
import com.clickpost.app.social.viewmodel.AccountGroupViewModel_HiltModules_KeyModule_ProvideFactory;
import com.clickpost.app.social.viewmodel.PublishViewModel;
import com.clickpost.app.social.viewmodel.PublishViewModel_HiltModules_KeyModule_ProvideFactory;
import com.clickpost.app.storage.StorageManager;
import com.clickpost.app.viewmodel.MainViewModel;
import com.clickpost.app.viewmodel.MainViewModel_HiltModules_KeyModule_ProvideFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideApplicationFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerClickPostApp_HiltComponents_SingletonC {
  private DaggerClickPostApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ClickPostApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ClickPostApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ClickPostApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ClickPostApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ClickPostApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ClickPostApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ClickPostApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ClickPostApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ClickPostApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ClickPostApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ClickPostApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ClickPostApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ClickPostApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(AccountGroupViewModel_HiltModules_KeyModule_ProvideFactory.provide(), MainViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PublishViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends ClickPostApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AccountGroupViewModel> accountGroupViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<PublishViewModel> publishViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private Map<Platform, PlatformAdapter> mapOfPlatformAndPlatformAdapter() {
      return ImmutableMap.<Platform, PlatformAdapter>of(Platform.TIKTOK, singletonCImpl.provideTikTokAdapterProvider.get(), Platform.FACEBOOK, singletonCImpl.provideFacebookAdapterProvider.get(), Platform.INSTAGRAM, singletonCImpl.provideInstagramAdapterProvider.get(), Platform.YOUTUBE, singletonCImpl.provideYouTubeAdapterProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.accountGroupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.publishViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, javax.inject.Provider<ViewModel>>of("com.clickpost.app.social.viewmodel.AccountGroupViewModel", ((Provider) accountGroupViewModelProvider), "com.clickpost.app.viewmodel.MainViewModel", ((Provider) mainViewModelProvider), "com.clickpost.app.social.viewmodel.PublishViewModel", ((Provider) publishViewModelProvider));
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<String, Object>of();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.clickpost.app.social.viewmodel.AccountGroupViewModel 
          return (T) new AccountGroupViewModel(singletonCImpl.provideAccountGroupRepositoryProvider.get(), viewModelCImpl.mapOfPlatformAndPlatformAdapter());

          case 1: // com.clickpost.app.viewmodel.MainViewModel 
          return (T) new MainViewModel(ApplicationContextModule_ProvideApplicationFactory.provideApplication(singletonCImpl.applicationContextModule), singletonCImpl.profileRepositoryProvider.get(), singletonCImpl.provideStorageManagerProvider.get(), singletonCImpl.provideBrandingEngineProvider.get(), singletonCImpl.provideExportEngineProvider.get(), singletonCImpl.provideShareEngineProvider.get(), singletonCImpl.provideDeviceCapabilityCheckerProvider.get());

          case 2: // com.clickpost.app.social.viewmodel.PublishViewModel 
          return (T) new PublishViewModel(singletonCImpl.providePublishEngineProvider.get(), singletonCImpl.provideAccountGroupRepositoryProvider.get(), singletonCImpl.providePublishHistoryRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ClickPostApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ClickPostApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ClickPostApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<CredentialVault> provideCredentialVaultProvider;

    private Provider<AccountGroupRepository> provideAccountGroupRepositoryProvider;

    private Provider<PlatformAdapter> provideTikTokAdapterProvider;

    private Provider<PlatformAdapter> provideFacebookAdapterProvider;

    private Provider<PlatformAdapter> provideInstagramAdapterProvider;

    private Provider<PlatformAdapter> provideYouTubeAdapterProvider;

    private Provider<ProfileRepository> profileRepositoryProvider;

    private Provider<StorageManager> provideStorageManagerProvider;

    private Provider<BrandingEngine> provideBrandingEngineProvider;

    private Provider<ExportEngine> provideExportEngineProvider;

    private Provider<ShareEngine> provideShareEngineProvider;

    private Provider<DeviceCapabilityChecker> provideDeviceCapabilityCheckerProvider;

    private Provider<SocialDatabase> provideSocialDatabaseProvider;

    private Provider<PublishJobDao> providePublishJobDaoProvider;

    private Provider<PublishHistoryRepository> providePublishHistoryRepositoryProvider;

    private Provider<PublishEngine> providePublishEngineProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private Map<Platform, PlatformAdapter> mapOfPlatformAndPlatformAdapter() {
      return ImmutableMap.<Platform, PlatformAdapter>of(Platform.TIKTOK, provideTikTokAdapterProvider.get(), Platform.FACEBOOK, provideFacebookAdapterProvider.get(), Platform.INSTAGRAM, provideInstagramAdapterProvider.get(), Platform.YOUTUBE, provideYouTubeAdapterProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideCredentialVaultProvider = DoubleCheck.provider(new SwitchingProvider<CredentialVault>(singletonCImpl, 1));
      this.provideAccountGroupRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AccountGroupRepository>(singletonCImpl, 0));
      this.provideTikTokAdapterProvider = DoubleCheck.provider(new SwitchingProvider<PlatformAdapter>(singletonCImpl, 2));
      this.provideFacebookAdapterProvider = DoubleCheck.provider(new SwitchingProvider<PlatformAdapter>(singletonCImpl, 3));
      this.provideInstagramAdapterProvider = DoubleCheck.provider(new SwitchingProvider<PlatformAdapter>(singletonCImpl, 4));
      this.provideYouTubeAdapterProvider = DoubleCheck.provider(new SwitchingProvider<PlatformAdapter>(singletonCImpl, 5));
      this.profileRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ProfileRepository>(singletonCImpl, 6));
      this.provideStorageManagerProvider = DoubleCheck.provider(new SwitchingProvider<StorageManager>(singletonCImpl, 7));
      this.provideBrandingEngineProvider = DoubleCheck.provider(new SwitchingProvider<BrandingEngine>(singletonCImpl, 8));
      this.provideExportEngineProvider = DoubleCheck.provider(new SwitchingProvider<ExportEngine>(singletonCImpl, 9));
      this.provideShareEngineProvider = DoubleCheck.provider(new SwitchingProvider<ShareEngine>(singletonCImpl, 10));
      this.provideDeviceCapabilityCheckerProvider = DoubleCheck.provider(new SwitchingProvider<DeviceCapabilityChecker>(singletonCImpl, 11));
      this.provideSocialDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<SocialDatabase>(singletonCImpl, 15));
      this.providePublishJobDaoProvider = DoubleCheck.provider(new SwitchingProvider<PublishJobDao>(singletonCImpl, 14));
      this.providePublishHistoryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PublishHistoryRepository>(singletonCImpl, 13));
      this.providePublishEngineProvider = DoubleCheck.provider(new SwitchingProvider<PublishEngine>(singletonCImpl, 12));
    }

    @Override
    public void injectClickPostApp(ClickPostApp clickPostApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.clickpost.app.social.repository.AccountGroupRepository 
          return (T) SocialModule_ProvideAccountGroupRepositoryFactory.provideAccountGroupRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideCredentialVaultProvider.get());

          case 1: // com.clickpost.app.social.storage.CredentialVault 
          return (T) SocialModule_ProvideCredentialVaultFactory.provideCredentialVault(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // java.util.Map<com.clickpost.app.social.data.Platform,javax.inject.Provider<com.clickpost.app.social.engine.PlatformAdapter>> com.clickpost.app.social.di.SocialModule#provideTikTokAdapter 
          return (T) SocialModule_ProvideTikTokAdapterFactory.provideTikTokAdapter();

          case 3: // java.util.Map<com.clickpost.app.social.data.Platform,javax.inject.Provider<com.clickpost.app.social.engine.PlatformAdapter>> com.clickpost.app.social.di.SocialModule#provideFacebookAdapter 
          return (T) SocialModule_ProvideFacebookAdapterFactory.provideFacebookAdapter();

          case 4: // java.util.Map<com.clickpost.app.social.data.Platform,javax.inject.Provider<com.clickpost.app.social.engine.PlatformAdapter>> com.clickpost.app.social.di.SocialModule#provideInstagramAdapter 
          return (T) SocialModule_ProvideInstagramAdapterFactory.provideInstagramAdapter();

          case 5: // java.util.Map<com.clickpost.app.social.data.Platform,javax.inject.Provider<com.clickpost.app.social.engine.PlatformAdapter>> com.clickpost.app.social.di.SocialModule#provideYouTubeAdapter 
          return (T) SocialModule_ProvideYouTubeAdapterFactory.provideYouTubeAdapter();

          case 6: // com.clickpost.app.data.repository.ProfileRepository 
          return (T) new ProfileRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.clickpost.app.storage.StorageManager 
          return (T) AppModule_ProvideStorageManagerFactory.provideStorageManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.clickpost.app.engine.BrandingEngine 
          return (T) AppModule_ProvideBrandingEngineFactory.provideBrandingEngine(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.clickpost.app.engine.ExportEngine 
          return (T) AppModule_ProvideExportEngineFactory.provideExportEngine(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 10: // com.clickpost.app.engine.ShareEngine 
          return (T) AppModule_ProvideShareEngineFactory.provideShareEngine(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 11: // com.clickpost.app.engine.DeviceCapabilityChecker 
          return (T) AppModule_ProvideDeviceCapabilityCheckerFactory.provideDeviceCapabilityChecker(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.clickpost.app.social.engine.PublishEngine 
          return (T) SocialModule_ProvidePublishEngineFactory.providePublishEngine(singletonCImpl.mapOfPlatformAndPlatformAdapter(), singletonCImpl.provideCredentialVaultProvider.get(), singletonCImpl.provideAccountGroupRepositoryProvider.get(), singletonCImpl.providePublishHistoryRepositoryProvider.get());

          case 13: // com.clickpost.app.social.repository.PublishHistoryRepository 
          return (T) SocialModule_ProvidePublishHistoryRepositoryFactory.providePublishHistoryRepository(singletonCImpl.providePublishJobDaoProvider.get());

          case 14: // com.clickpost.app.social.db.PublishJobDao 
          return (T) SocialModule_ProvidePublishJobDaoFactory.providePublishJobDao(singletonCImpl.provideSocialDatabaseProvider.get());

          case 15: // com.clickpost.app.social.db.SocialDatabase 
          return (T) SocialModule_ProvideSocialDatabaseFactory.provideSocialDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
