package gg.desolve.commons.redis;

import gg.desolve.commons.Commons;
import gg.desolve.commons.redis.subscribe.BroadcastSubscriber;
import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;

@Getter
public class SubscriberDirector {

    private final List<JedisPubSub> subscribers;

    public SubscriberDirector() {
        subscribers = subscribers();
    }

    private List<JedisPubSub> subscribers() {
        List<JedisPubSub> subscriberList = Arrays.asList(
                new BroadcastSubscriber()
        );

        subscriberList.forEach(subscriber ->
                Commons.getInstance().getRedisManager().subscribe(
                        subscriber,
                        subscriber.getClass().getSimpleName().replace("Subscriber", "")));
        return subscriberList;
    }

}
