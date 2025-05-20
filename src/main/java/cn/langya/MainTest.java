package cn.langya;

import com.cubk.event.EventManager;
import com.cubk.event.impl.Event;
import com.darkmagician6.eventapi.EventTarget;
import dev.tigr.simpleevents.listener.EventHandler;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.bus.Bus;
import io.github.nevalackin.homoBus.bus.impl.EventBus;
import me.bush.eventbus.annotation.EventListener;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author LangYa466
 * @date 2025/5/21
 */
public class MainTest {
    private static final EventManager cubk1EM = new EventManager();
    private static final Bus<Object> homoEM = new EventBus<>();
    private static final me.bush.eventbus.bus.EventBus therealbushEM = new me.bush.eventbus.bus.EventBus();
    private static final dev.tigr.simpleevents.EventManager simpleEM = new dev.tigr.simpleevents.EventManager();
    private static final org.greenrobot.eventbus.EventBus greenrobotEM = new org.greenrobot.eventbus.EventBus();

    public static void main(String[] args) {
        // https://github.com/cubk1/EventManager
        cn.langya.EventManager cubk1EventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                cubk1EM.call((Event) object);
            }

            @Override
            public void register(Object object) {
                cubk1EM.register(object);
            }

            @Override
            public void unregister(Object object) {
                cubk1EM.unregister(object);
            }
        };

        doTest("cubk1/EventManager", cubk1EventManager);

        // https://bitbucket.org/DarkMagician6/eventapi/src/master/
        cn.langya.EventManager darkMagician6EventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                com.darkmagician6.eventapi.EventManager.call((com.darkmagician6.eventapi.events.Event) object);
            }

            @Override
            public void register(Object object) {
                com.darkmagician6.eventapi.EventManager.register(object);
            }

            @Override
            public void unregister(Object object) {
                com.darkmagician6.eventapi.EventManager.unregister(object);
            }
        };

        doTest("DarkMagician6/eventapi", darkMagician6EventManager);

        // https://github.com/nevalackin/homo-bus
        cn.langya.EventManager homoEventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                homoEM.post(object);
            }

            @Override
            public void register(Object object) {
                homoEM.subscribe(object);
            }

            @Override
            public void unregister(Object object) {
                homoEM.unsubscribe(object);
            }
        };

        doTest("nevalackin/homo-bus", homoEventManager);

        // https://github.com/therealbush/eventbus
        cn.langya.EventManager therealbushEventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                therealbushEM.post((me.bush.eventbus.event.Event) object);
            }

            @Override
            public void register(Object object) {
                therealbushEM.subscribe(object);
            }

            @Override
            public void unregister(Object object) {
                therealbushEM.unsubscribe(object);
            }
        };

        doTest("therealbush/eventbus", therealbushEventManager);

        // https://github.com/Tigermouthbear/SimpleEvents
        cn.langya.EventManager simpleEventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                simpleEM.post(object);
            }

            @Override
            public void register(Object object) {
                simpleEM.register(object);
            }

            @Override
            public void unregister(Object object) {
                simpleEM.unregister(object);
            }
        };

        doTest("Tigermouthbear/SimpleEvents", simpleEventManager);

        // https://github.com/greenrobot/EventBus
        cn.langya.EventManager greenrobotEventManager = new cn.langya.EventManager() {
            @Override
            public void call(Object object) {
                greenrobotEM.post(object);
            }

            @Override
            public void register(Object object) {
                greenrobotEM.register(object);
            }

            @Override
            public void unregister(Object object) {
                greenrobotEM.unregister(object);
            }
        };

        doTest("greenrobot/EventBus", greenrobotEventManager);
    }

    private static void doTest(String name, cn.langya.EventManager eventManager) {
        long totalTime = 0;

        for (int i = 1; i <= 3; i++) {
            long startTime = System.currentTimeMillis();

            TestListener testListener = new TestListener();
            eventManager.register(testListener);
            for (int j = 0; j < 10000; j++) {
                eventManager.call(new TestEvent());
            }
            eventManager.unregister(testListener);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            totalTime += duration;

            System.out.println("[" + name + "] 第 " + i + " 次耗时: " + duration + "ms");
        }

        double averageTime = totalTime / 3.0;
        System.out.println("[" + name + "] 平均耗时: " + averageTime + "ms");
    }

    public static class TestListener {
        @EventTarget
        public void onTest(TestEvent event) { }
        @com.cubk.event.annotations.EventTarget
        public void onTest2(TestEvent event) { }

        @EventLink
        public final Listener<Object> onTest3 = event -> { };

        @EventListener
        public void onTest4(TestEvent event) { }

        @EventHandler
        private dev.tigr.simpleevents.listener.EventListener<TestEvent> onTest5 = new dev.tigr.simpleevents.listener.EventListener<>(event -> { });

        @Subscribe
        public void onTest6(TestEvent event) { }
    }

    public static class TestEvent extends me.bush.eventbus.event.Event implements Event, com.darkmagician6.eventapi.events.Event {
        @Override
        protected boolean isCancellable() {
            return false;
        }
    }
}
