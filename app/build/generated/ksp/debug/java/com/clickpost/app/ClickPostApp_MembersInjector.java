package com.clickpost.app;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ClickPostApp_MembersInjector implements MembersInjector<ClickPostApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public ClickPostApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<ClickPostApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new ClickPostApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(ClickPostApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.clickpost.app.ClickPostApp.workerFactory")
  public static void injectWorkerFactory(ClickPostApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
