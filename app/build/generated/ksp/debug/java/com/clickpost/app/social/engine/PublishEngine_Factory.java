package com.clickpost.app.social.engine;

import com.clickpost.app.social.data.Platform;
import com.clickpost.app.social.repository.AccountGroupRepository;
import com.clickpost.app.social.repository.PublishHistoryRepository;
import com.clickpost.app.social.storage.CredentialVault;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PublishEngine_Factory implements Factory<PublishEngine> {
  private final Provider<Map<Platform, ? extends PlatformAdapter>> adaptersProvider;

  private final Provider<CredentialVault> vaultProvider;

  private final Provider<AccountGroupRepository> groupRepoProvider;

  private final Provider<PublishHistoryRepository> historyRepoProvider;

  public PublishEngine_Factory(Provider<Map<Platform, ? extends PlatformAdapter>> adaptersProvider,
      Provider<CredentialVault> vaultProvider, Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    this.adaptersProvider = adaptersProvider;
    this.vaultProvider = vaultProvider;
    this.groupRepoProvider = groupRepoProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  @Override
  public PublishEngine get() {
    return newInstance(adaptersProvider.get(), vaultProvider.get(), groupRepoProvider.get(), historyRepoProvider.get());
  }

  public static PublishEngine_Factory create(
      Provider<Map<Platform, ? extends PlatformAdapter>> adaptersProvider,
      Provider<CredentialVault> vaultProvider, Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    return new PublishEngine_Factory(adaptersProvider, vaultProvider, groupRepoProvider, historyRepoProvider);
  }

  public static PublishEngine newInstance(Map<Platform, ? extends PlatformAdapter> adapters,
      CredentialVault vault, AccountGroupRepository groupRepo,
      PublishHistoryRepository historyRepo) {
    return new PublishEngine(adapters, vault, groupRepo, historyRepo);
  }
}
