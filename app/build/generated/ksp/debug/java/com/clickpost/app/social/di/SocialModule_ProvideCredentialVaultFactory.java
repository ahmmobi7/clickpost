package com.clickpost.app.social.di;

import android.content.Context;
import com.clickpost.app.social.storage.CredentialVault;
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
public final class SocialModule_ProvideCredentialVaultFactory implements Factory<CredentialVault> {
  private final Provider<Context> contextProvider;

  public SocialModule_ProvideCredentialVaultFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CredentialVault get() {
    return provideCredentialVault(contextProvider.get());
  }

  public static SocialModule_ProvideCredentialVaultFactory create(
      Provider<Context> contextProvider) {
    return new SocialModule_ProvideCredentialVaultFactory(contextProvider);
  }

  public static CredentialVault provideCredentialVault(Context context) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideCredentialVault(context));
  }
}
