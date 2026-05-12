package com.clickpost.app.social.di;

import com.clickpost.app.social.db.PublishJobDao;
import com.clickpost.app.social.repository.PublishHistoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class SocialModule_ProvidePublishHistoryRepositoryFactory implements Factory<PublishHistoryRepository> {
  private final Provider<PublishJobDao> daoProvider;

  public SocialModule_ProvidePublishHistoryRepositoryFactory(Provider<PublishJobDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public PublishHistoryRepository get() {
    return providePublishHistoryRepository(daoProvider.get());
  }

  public static SocialModule_ProvidePublishHistoryRepositoryFactory create(
      Provider<PublishJobDao> daoProvider) {
    return new SocialModule_ProvidePublishHistoryRepositoryFactory(daoProvider);
  }

  public static PublishHistoryRepository providePublishHistoryRepository(PublishJobDao dao) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.providePublishHistoryRepository(dao));
  }
}
