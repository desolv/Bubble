package gg.desolve.mithril.redis;

import gg.desolve.mithril.Mithril;
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
                Mithril.getInstance().getRedisManager().subscribe(
                        subscriber,
                        subscriber.getClass().getSimpleName().replace("Subscriber", "")));
        return subscriberList;
    }

}
