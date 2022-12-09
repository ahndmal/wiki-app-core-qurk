package com.anma.cloud;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import io.quarkus.credentials.CredentialsProvider;
import org.mockito.Mockito;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import static org.junit.jupiter.api.Assertions.*;
class GoogleCredentialsMockProducerTest {
    @Produces
    @Singleton
    @Default
    public GoogleCredentials googleCredential() {
        return Mockito.mock(GoogleCredentials.class);
    }

    // only needed if you're injecting it inside one of your CDI beans
    @Produces
    @Singleton
    @Default
    public NoCredentialsProvider credentialsProvider() {
        return NoCredentialsProvider.create();
    }
}