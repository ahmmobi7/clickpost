package com.clickpost.app.promo.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.clickpost.app.promo.data.PromoRepository;
import com.clickpost.app.promo.engine.BackgroundRemover;
import com.clickpost.app.promo.engine.ImageBlender;
import com.clickpost.app.promo.engine.PromoVideoEngine;
import dagger.internal.DaggerGenerated;
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
public final class PromoWorker_Factory {
  private final Provider<BackgroundRemover> backgroundRemoverProvider;

  private final Provider<ImageBlender> imageBlenderProvider;

  private final Provider<PromoVideoEngine> videoEngineProvider;

  private final Provider<PromoRepository> repositoryProvider;

  public PromoWorker_Factory(Provider<BackgroundRemover> backgroundRemoverProvider,
      Provider<ImageBlender> imageBlenderProvider, Provider<PromoVideoEngine> videoEngineProvider,
      Provider<PromoRepository> repositoryProvider) {
    this.backgroundRemoverProvider = backgroundRemoverProvider;
    this.imageBlenderProvider = imageBlenderProvider;
    this.videoEngineProvider = videoEngineProvider;
    this.repositoryProvider = repositoryProvider;
  }

  public PromoWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, backgroundRemoverProvider.get(), imageBlenderProvider.get(), videoEngineProvider.get(), repositoryProvider.get());
  }

  public static PromoWorker_Factory create(Provider<BackgroundRemover> backgroundRemoverProvider,
      Provider<ImageBlender> imageBlenderProvider, Provider<PromoVideoEngine> videoEngineProvider,
      Provider<PromoRepository> repositoryProvider) {
    return new PromoWorker_Factory(backgroundRemoverProvider, imageBlenderProvider, videoEngineProvider, repositoryProvider);
  }

  public static PromoWorker newInstance(Context context, WorkerParameters params,
      BackgroundRemover backgroundRemover, ImageBlender imageBlender, PromoVideoEngine videoEngine,
      PromoRepository repository) {
    return new PromoWorker(context, params, backgroundRemover, imageBlender, videoEngine, repository);
  }
}
