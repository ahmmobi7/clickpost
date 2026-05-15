package com.clickpost.app.promo.di;

import android.content.Context;
import com.clickpost.app.promo.db.PromoDatabase;
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
public final class PromoModule_ProvidePromoDatabaseFactory implements Factory<PromoDatabase> {
  private final Provider<Context> contextProvider;

  public PromoModule_ProvidePromoDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PromoDatabase get() {
    return providePromoDatabase(contextProvider.get());
  }

  public static PromoModule_ProvidePromoDatabaseFactory create(Provider<Context> contextProvider) {
    return new PromoModule_ProvidePromoDatabaseFactory(contextProvider);
  }

  public static PromoDatabase providePromoDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(PromoModule.INSTANCE.providePromoDatabase(context));
  }
}
