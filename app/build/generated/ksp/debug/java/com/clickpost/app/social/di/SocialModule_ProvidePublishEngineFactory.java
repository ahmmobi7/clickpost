package com.clickpost.app.social.di;

import com.clickpost.app.social.data.Platform;
import com.clickpost.app.social.engine.PlatformAdapter;
import com.clickpost.app.social.engine.PublishEngine;
import com.clickpost.app.social.repository.AccountGroupRepository;
import com.clickpost.app.social.repository.PublishHistoryRepository;
import com.clickpost.app.social.storage.CredentialVault;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class SocialModule_ProvidePublishEngineFactory implements Factory<PublishEngine> {
  private final Provider<Map<Platform, PlatformAdapter>> adaptersProvider;

  private final Provider<CredentialVault> vaultProvider;

  private final Provider<AccountGroupRepository> groupRepoProvider;

  private final Provider<PublishHistoryRepository> historyRepoProvider;

  public SocialModule_ProvidePublishEngineFactory(
      Provider<Map<Platform, PlatformAdapter>> adaptersProvider,
      Provider<CredentialVault> vaultProvider, Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    this.adaptersProvider = adaptersProvider;
    this.vaultProvider = vaultProvider;
    this.groupRepoProvider = groupRepoProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  @Override
  public PublishEngine get() {
    return providePublishEngine(adaptersProvider.get(), vaultProvider.get(), groupRepoProvider.get(), historyRepoProvider.get());
  }

  public static SocialModule_ProvidePublishEngineFactory create(
      Provider<Map<Platform, PlatformAdapter>> adaptersProvider,
      Provider<CredentialVault> vaultProvider, Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    return new SocialModule_ProvidePublishEngineFactory(adaptersProvider, vaultProvider, groupRepoProvider, historyRepoProvider);
  }

  public static PublishEngine providePublishEngine(Map<Platform, PlatformAdapter> adapters,
      CredentialVault vault, AccountGroupRepository groupRepo,
      PublishHistoryRepository historyRepo) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.providePublishEngine(adapters, vault, groupRepo, historyRepo));
  }
}
