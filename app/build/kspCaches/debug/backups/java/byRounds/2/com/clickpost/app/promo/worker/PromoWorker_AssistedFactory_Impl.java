package com.clickpost.app.promo.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PromoWorker_AssistedFactory_Impl implements PromoWorker_AssistedFactory {
  private final PromoWorker_Factory delegateFactory;

  PromoWorker_AssistedFactory_Impl(PromoWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public PromoWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<PromoWorker_AssistedFactory> create(PromoWorker_Factory delegateFactory) {
    return InstanceFactory.create(new PromoWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<PromoWorker_AssistedFactory> createFactoryProvider(
      PromoWorker_Factory delegateFactory) {
    return InstanceFactory.create(new PromoWorker_AssistedFactory_Impl(delegateFactory));
  }
}
