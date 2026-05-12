package com.clickpost.app.social.di;

import com.clickpost.app.social.db.PublishJobDao;
import com.clickpost.app.social.db.SocialDatabase;
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
public final class SocialModule_ProvidePublishJobDaoFactory implements Factory<PublishJobDao> {
  private final Provider<SocialDatabase> dbProvider;

  public SocialModule_ProvidePublishJobDaoFactory(Provider<SocialDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PublishJobDao get() {
    return providePublishJobDao(dbProvider.get());
  }

  public static SocialModule_ProvidePublishJobDaoFactory create(
      Provider<SocialDatabase> dbProvider) {
    return new SocialModule_ProvidePublishJobDaoFactory(dbProvider);
  }

  public static PublishJobDao providePublishJobDao(SocialDatabase db) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.providePublishJobDao(db));
  }
}
