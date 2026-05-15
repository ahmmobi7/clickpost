package com.clickpost.app.promo.di;

import com.clickpost.app.promo.data.PromoDao;
import com.clickpost.app.promo.db.PromoDatabase;
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
public final class PromoModule_ProvidePromoDaoFactory implements Factory<PromoDao> {
  private final Provider<PromoDatabase> databaseProvider;

  public PromoModule_ProvidePromoDaoFactory(Provider<PromoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PromoDao get() {
    return providePromoDao(databaseProvider.get());
  }

  public static PromoModule_ProvidePromoDaoFactory create(
      Provider<PromoDatabase> databaseProvider) {
    return new PromoModule_ProvidePromoDaoFactory(databaseProvider);
  }

  public static PromoDao providePromoDao(PromoDatabase database) {
    return Preconditions.checkNotNullFromProvides(PromoModule.INSTANCE.providePromoDao(database));
  }
}
