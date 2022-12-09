package com.anma.cloud;

import io.quarkus.credentials.CredentialsProvider;

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
    public CredentialsProvider credentialsProvider() {
        return NoCredentialsProvider.create();
    }
}