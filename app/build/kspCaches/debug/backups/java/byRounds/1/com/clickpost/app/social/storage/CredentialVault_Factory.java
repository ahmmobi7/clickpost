package com.clickpost.app.social.storage;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class CredentialVault_Factory implements Factory<CredentialVault> {
  private final Provider<Context> contextProvider;

  public CredentialVault_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CredentialVault get() {
    return newInstance(contextProvider.get());
  }

  public static CredentialVault_Factory create(Provider<Context> contextProvider) {
    return new CredentialVault_Factory(contextProvider);
  }

  public static CredentialVault newInstance(Context context) {
    return new CredentialVault(context);
  }
}
