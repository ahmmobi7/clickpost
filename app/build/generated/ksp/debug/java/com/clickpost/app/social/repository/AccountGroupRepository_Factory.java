package com.clickpost.app.social.repository;

import android.content.Context;
import com.clickpost.app.social.storage.CredentialVault;
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
public final class AccountGroupRepository_Factory implements Factory<AccountGroupRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<CredentialVault> vaultProvider;

  public AccountGroupRepository_Factory(Provider<Context> contextProvider,
      Provider<CredentialVault> vaultProvider) {
    this.contextProvider = contextProvider;
    this.vaultProvider = vaultProvider;
  }

  @Override
  public AccountGroupRepository get() {
    return newInstance(contextProvider.get(), vaultProvider.get());
  }

  public static AccountGroupRepository_Factory create(Provider<Context> contextProvider,
      Provider<CredentialVault> vaultProvider) {
    return new AccountGroupRepository_Factory(contextProvider, vaultProvider);
  }

  public static AccountGroupRepository newInstance(Context context, CredentialVault vault) {
    return new AccountGroupRepository(context, vault);
  }
}
