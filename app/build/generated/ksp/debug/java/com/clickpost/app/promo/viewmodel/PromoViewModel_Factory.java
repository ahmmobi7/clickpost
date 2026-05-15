package com.clickpost.app.promo.viewmodel;

import android.content.Context;
import androidx.work.WorkManager;
import com.clickpost.app.promo.data.PromoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class PromoViewModel_Factory implements Factory<PromoViewModel> {
  private final Provider<PromoRepository> repositoryProvider;

  private final Provider<WorkManager> workManagerProvider;

  private final Provider<Context> contextProvider;

  public PromoViewModel_Factory(Provider<PromoRepository> repositoryProvider,
      Provider<WorkManager> workManagerProvider, Provider<Context> contextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.workManagerProvider = workManagerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public PromoViewModel get() {
    return newInstance(repositoryProvider.get(), workManagerProvider.get(), contextProvider.get());
  }

  public static PromoViewModel_Factory create(Provider<PromoRepository> repositoryProvider,
      Provider<WorkManager> workManagerProvider, Provider<Context> contextProvider) {
    return new PromoViewModel_Factory(repositoryProvider, workManagerProvider, contextProvider);
  }

  public static PromoViewModel newInstance(PromoRepository repository, WorkManager workManager,
      Context context) {
    return new PromoViewModel(repository, workManager, context);
  }
}
