package com.clickpost.app.social.di;

import android.content.Context;
import com.clickpost.app.social.repository.AccountGroupRepository;
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
public final class SocialModule_ProvideAccountGroupRepositoryFactory implements Factory<AccountGroupRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<CredentialVault> vaultProvider;

  public SocialModule_ProvideAccountGroupRepositoryFactory(Provider<Context> contextProvider,
      Provider<CredentialVault> vaultProvider) {
    this.contextProvider = contextProvider;
    this.vaultProvider = vaultProvider;
  }

  @Override
  public AccountGroupRepository get() {
    return provideAccountGroupRepository(contextProvider.get(), vaultProvider.get());
  }

  public static SocialModule_ProvideAccountGroupRepositoryFactory create(
      Provider<Context> contextProvider, Provider<CredentialVault> vaultProvider) {
    return new SocialModule_ProvideAccountGroupRepositoryFactory(contextProvider, vaultProvider);
  }

  public static AccountGroupRepository provideAccountGroupRepository(Context context,
      CredentialVault vault) {
    return Preconditions.checkNotNullFromProvides(SocialModule.INSTANCE.provideAccountGroupRepository(context, vault));
  }
}
