package com.clickpost.app.social.viewmodel;

import com.clickpost.app.social.engine.PublishEngine;
import com.clickpost.app.social.repository.AccountGroupRepository;
import com.clickpost.app.social.repository.PublishHistoryRepository;
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
public final class PublishViewModel_Factory implements Factory<PublishViewModel> {
  private final Provider<PublishEngine> engineProvider;

  private final Provider<AccountGroupRepository> groupRepoProvider;

  private final Provider<PublishHistoryRepository> historyRepoProvider;

  public PublishViewModel_Factory(Provider<PublishEngine> engineProvider,
      Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    this.engineProvider = engineProvider;
    this.groupRepoProvider = groupRepoProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  @Override
  public PublishViewModel get() {
    return newInstance(engineProvider.get(), groupRepoProvider.get(), historyRepoProvider.get());
  }

  public static PublishViewModel_Factory create(Provider<PublishEngine> engineProvider,
      Provider<AccountGroupRepository> groupRepoProvider,
      Provider<PublishHistoryRepository> historyRepoProvider) {
    return new PublishViewModel_Factory(engineProvider, groupRepoProvider, historyRepoProvider);
  }

  public static PublishViewModel newInstance(PublishEngine engine, AccountGroupRepository groupRepo,
      PublishHistoryRepository historyRepo) {
    return new PublishViewModel(engine, groupRepo, historyRepo);
  }
}
