package com.anma.rest.msg;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.*;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Path("/pubsub")
public class PubSubResource {
    private static final Logger LOG = Logger.getLogger(PubSubResource.class);
    private final String TOPIC_NAME = "wiki-pages-main";
    private final String SUB_NAME = "test-subscription";

    @ConfigProperty(name = "quarkus.google.cloud.project-id")
    String projectId;

    private TopicName topicName;
    private Subscriber subscriber;

    @Inject
    CredentialsProvider credentialsProvider;

    @PostConstruct
    void init() throws IOException {
        topicName = TopicName.of(projectId, TOPIC_NAME);
        ProjectSubscriptionName subscriptionName = initSubscription();

        // Subscribe to PubSub
        MessageReceiver receiver = (message, consumer) -> {
            LOG.infov("Got message {0}", message.getData().toStringUtf8());
            consumer.ack();
        };
        subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
        subscriber.startAsync().awaitRunning();
    }

    @PreDestroy
    void destroy() {
        // Stop the subscription at destroy time
        if (subscriber != null) {
            subscriber.stopAsync();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void pubSub() throws IOException,InterruptedException {
        Publisher publisher = Publisher.newBuilder(topicName)
                .setCredentialsProvider(credentialsProvider)
                .build();
        try {
            ByteString data = ByteString.copyFromUtf8("my-message");
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);// Publish the message
            ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {// Wait for message submission and log the result
                public void onSuccess(String messageId) {
                    LOG.infov("published with message id {0}", messageId);
                }

                public void onFailure(Throwable t) {
                    LOG.warnv("failed to publish: {0}", t);
                }
            }, MoreExecutors.directExecutor());
        } finally {
            publisher.shutdown();
            publisher.awaitTermination(1, TimeUnit.MINUTES);
        }
    }


    private ProjectSubscriptionName initSubscription() throws IOException {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, SUB_NAME);
        SubscriptionAdminSettings subscriptionAdminSettings = SubscriptionAdminSettings.newBuilder()
                .setCredentialsProvider(credentialsProvider)
                .build();
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriptionAdminSettings)) {
            Iterable<Subscription> subscriptions = subscriptionAdminClient.listSubscriptions(ProjectName.of(projectId))
                    .iterateAll();
            Optional<Subscription> existing = StreamSupport.stream(subscriptions.spliterator(), false)
                    .filter(sub -> sub.getName().equals(subscriptionName.toString()))
                    .findFirst();
            if (!existing.isPresent()) {
                subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 0);
            }
        }
        return subscriptionName;
    }


}
