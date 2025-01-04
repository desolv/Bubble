package gg.desolve.commons.redis.subscribe;

import gg.desolve.commons.Commons;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;

public class SubscriberDirector {

    private final List<JedisPubSub> subscribers;

    public SubscriberDirector() {
        subscribers = subscribers();
    }

    private List<JedisPubSub> subscribers() {
        List<JedisPubSub> subscriberList = Arrays.asList(
                new InstanceSubscriber()
        );

        subscriberList.forEach(subscriber ->
                Commons.getInstance().getRedisManager().subscribe(
                        subscriber,
                        subscriber.getClass().getSimpleName().replace("Subscriber", "")));
        return subscriberList;
    }

}
