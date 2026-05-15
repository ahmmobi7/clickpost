package com.clickpost.app.promo.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = PromoWorker.class
)
public interface PromoWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.clickpost.app.promo.worker.PromoWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(PromoWorker_AssistedFactory factory);
}
