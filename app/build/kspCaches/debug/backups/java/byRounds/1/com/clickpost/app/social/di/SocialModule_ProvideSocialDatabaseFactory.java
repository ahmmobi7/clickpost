package com.clickpost.app.social.di;

import android.content.Context;
import com.clickpost.app.social.db.SocialDatabase;
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
public final class SocialModule_ProvideSocialDatabaseFactory implements Factory<SocialDatabase> {
  private final Provider<Context> contextProvider;

  public SocialModule_ProvideSocialDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SocialDatabase get() {
    return provideSocialDatabase(contextProvider.get());
  }

  public static SocialModule_ProvideSocialDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new SocialModule_ProvideSocialDatabaseFactory(contextProvider);
  }

  public static SocialDatabase provideSocialDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideSocialDatabase(context));
  }
}
