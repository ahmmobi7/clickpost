package com.clickpost.app.social.viewmodel;

import com.clickpost.app.social.data.Platform;
import com.clickpost.app.social.engine.PlatformAdapter;
import com.clickpost.app.social.repository.AccountGroupRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Map;
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
public final class AccountGroupViewModel_Factory implements Factory<AccountGroupViewModel> {
  private final Provider<AccountGroupRepository> groupRepoProvider;

  private final Provider<Map<Platform, PlatformAdapter>> adaptersProvider;

  public AccountGroupViewModel_Factory(Provider<AccountGroupRepository> groupRepoProvider,
      Provider<Map<Platform, PlatformAdapter>> adaptersProvider) {
    this.groupRepoProvider = groupRepoProvider;
    this.adaptersProvider = adaptersProvider;
  }

  @Override
  public AccountGroupViewModel get() {
    return newInstance(groupRepoProvider.get(), adaptersProvider.get());
  }

  public static AccountGroupViewModel_Factory create(
      Provider<AccountGroupRepository> groupRepoProvider,
      Provider<Map<Platform, PlatformAdapter>> adaptersProvider) {
    return new AccountGroupViewModel_Factory(groupRepoProvider, adaptersProvider);
  }

  public static AccountGroupViewModel newInstance(AccountGroupRepository groupRepo,
      Map<Platform, PlatformAdapter> adapters) {
    return new AccountGroupViewModel(groupRepo, adapters);
  }
}
