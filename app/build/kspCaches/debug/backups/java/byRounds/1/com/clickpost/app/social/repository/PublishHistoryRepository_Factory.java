package com.clickpost.app.social.repository;

import com.clickpost.app.social.db.PublishJobDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PublishHistoryRepository_Factory implements Factory<PublishHistoryRepository> {
  private final Provider<PublishJobDao> daoProvider;

  public PublishHistoryRepository_Factory(Provider<PublishJobDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public PublishHistoryRepository get() {
    return newInstance(daoProvider.get());
  }

  public static PublishHistoryRepository_Factory create(Provider<PublishJobDao> daoProvider) {
    return new PublishHistoryRepository_Factory(daoProvider);
  }

  public static PublishHistoryRepository newInstance(PublishJobDao dao) {
    return new PublishHistoryRepository(dao);
  }
}
