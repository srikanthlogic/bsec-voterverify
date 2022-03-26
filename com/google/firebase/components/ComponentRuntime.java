package com.google.firebase.components;

import android.util.Log;
import com.google.firebase.components.ComponentRuntime;
import com.google.firebase.dynamicloading.ComponentLoader;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import com.google.firebase.inject.Deferred;
import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes3.dex */
public class ComponentRuntime extends AbstractComponentContainer implements ComponentLoader {
    private static final Provider<Set<Object>> EMPTY_PROVIDER = $$Lambda$ComponentRuntime$YvUg5P3xbIDNjKaj5yOyBMxsxX0.INSTANCE;
    private final Map<Component<?>, Provider<?>> components;
    private final AtomicReference<Boolean> eagerComponentsInitializedWith;
    private final EventBus eventBus;
    private final Map<Class<?>, Provider<?>> lazyInstanceMap;
    private final Map<Class<?>, LazySet<?>> lazySetMap;
    private final List<Provider<ComponentRegistrar>> unprocessedRegistrarProviders;

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public /* bridge */ /* synthetic */ Object get(Class cls) {
        return super.get(cls);
    }

    @Override // com.google.firebase.components.AbstractComponentContainer, com.google.firebase.components.ComponentContainer
    public /* bridge */ /* synthetic */ Set setOf(Class cls) {
        return super.setOf(cls);
    }

    @Deprecated
    public ComponentRuntime(Executor defaultEventExecutor, Iterable<ComponentRegistrar> registrars, Component<?>... additionalComponents) {
        this(defaultEventExecutor, toProviders(registrars), Arrays.asList(additionalComponents));
    }

    public static Builder builder(Executor defaultEventExecutor) {
        return new Builder(defaultEventExecutor);
    }

    private ComponentRuntime(Executor defaultEventExecutor, Iterable<Provider<ComponentRegistrar>> registrars, Collection<Component<?>> additionalComponents) {
        this.components = new HashMap();
        this.lazyInstanceMap = new HashMap();
        this.lazySetMap = new HashMap();
        this.eagerComponentsInitializedWith = new AtomicReference<>();
        this.eventBus = new EventBus(defaultEventExecutor);
        List<Component<?>> componentsToAdd = new ArrayList<>();
        componentsToAdd.add(Component.of(this.eventBus, EventBus.class, Subscriber.class, Publisher.class));
        componentsToAdd.add(Component.of(this, ComponentLoader.class, new Class[0]));
        for (Component<?> additionalComponent : additionalComponents) {
            if (additionalComponent != null) {
                componentsToAdd.add(additionalComponent);
            }
        }
        this.unprocessedRegistrarProviders = iterableToList(registrars);
        discoverComponents(componentsToAdd);
    }

    private void discoverComponents(List<Component<?>> componentsToAdd) {
        List<Runnable> runAfterDiscovery = new ArrayList<>();
        synchronized (this) {
            Iterator<Provider<ComponentRegistrar>> iterator = this.unprocessedRegistrarProviders.iterator();
            while (iterator.hasNext()) {
                try {
                    ComponentRegistrar registrar = iterator.next().get();
                    if (registrar != null) {
                        componentsToAdd.addAll(registrar.getComponents());
                        iterator.remove();
                    }
                } catch (InvalidRegistrarException ex) {
                    iterator.remove();
                    Log.w("ComponentDiscovery", "Invalid component registrar.", ex);
                }
            }
            if (this.components.isEmpty()) {
                CycleDetector.detect(componentsToAdd);
            } else {
                ArrayList<Component<?>> allComponents = new ArrayList<>(this.components.keySet());
                allComponents.addAll(componentsToAdd);
                CycleDetector.detect(allComponents);
            }
            for (Component<?> component : componentsToAdd) {
                this.components.put(component, new Lazy<>((Provider<?>) new Provider(component) { // from class: com.google.firebase.components.-$$Lambda$ComponentRuntime$CLI-XObGztS2iu7RD22eoA3PpJU
                    private final /* synthetic */ Component f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // com.google.firebase.inject.Provider
                    public final Object get() {
                        return ComponentRuntime.this.lambda$discoverComponents$0$ComponentRuntime(this.f$1);
                    }
                }));
            }
            runAfterDiscovery.addAll(processInstanceComponents(componentsToAdd));
            runAfterDiscovery.addAll(processSetComponents());
            processDependencies();
        }
        for (Runnable runnable : runAfterDiscovery) {
            runnable.run();
        }
        maybeInitializeEagerComponents();
    }

    public /* synthetic */ Object lambda$discoverComponents$0$ComponentRuntime(Component component) {
        return component.getFactory().create(new RestrictedComponentContainer(component, this));
    }

    private void maybeInitializeEagerComponents() {
        Boolean isDefaultApp = this.eagerComponentsInitializedWith.get();
        if (isDefaultApp != null) {
            doInitializeEagerComponents(this.components, isDefaultApp.booleanValue());
        }
    }

    private static Iterable<Provider<ComponentRegistrar>> toProviders(Iterable<ComponentRegistrar> registrars) {
        List<Provider<ComponentRegistrar>> result = new ArrayList<>();
        for (ComponentRegistrar registrar : registrars) {
            result.add(new Provider() { // from class: com.google.firebase.components.-$$Lambda$ComponentRuntime$HmRpUE8DZPzj3YsHa6IUamGMsDE
                @Override // com.google.firebase.inject.Provider
                public final Object get() {
                    return ComponentRuntime.lambda$toProviders$1(ComponentRegistrar.this);
                }
            });
        }
        return result;
    }

    public static /* synthetic */ ComponentRegistrar lambda$toProviders$1(ComponentRegistrar registrar) {
        return registrar;
    }

    private static <T> List<T> iterableToList(Iterable<T> iterable) {
        ArrayList<T> result = new ArrayList<>();
        for (T item : iterable) {
            result.add(item);
        }
        return result;
    }

    private List<Runnable> processInstanceComponents(List<Component<?>> componentsToProcess) {
        ArrayList<Runnable> runnables = new ArrayList<>();
        for (Component<?> component : componentsToProcess) {
            if (component.isValue()) {
                Provider<?> provider = this.components.get(component);
                for (Class<?> anInterface : component.getProvidedInterfaces()) {
                    if (!this.lazyInstanceMap.containsKey(anInterface)) {
                        this.lazyInstanceMap.put(anInterface, provider);
                    } else {
                        runnables.add(new Runnable(provider) { // from class: com.google.firebase.components.-$$Lambda$ComponentRuntime$j_4NTT2xzmqjsy5GP5J0HQwebZk
                            private final /* synthetic */ Provider f$1;

                            {
                                this.f$1 = r2;
                            }

                            @Override // java.lang.Runnable
                            public final void run() {
                                OptionalProvider.this.set(this.f$1);
                            }
                        });
                    }
                }
            }
        }
        return runnables;
    }

    private List<Runnable> processSetComponents() {
        ArrayList<Runnable> runnables = new ArrayList<>();
        Map<Class<?>, Set<Provider<?>>> setIndex = new HashMap<>();
        for (Map.Entry<Component<?>, Provider<?>> entry : this.components.entrySet()) {
            Component<?> component = entry.getKey();
            if (!component.isValue()) {
                Provider<?> provider = entry.getValue();
                for (Class<?> anInterface : component.getProvidedInterfaces()) {
                    if (!setIndex.containsKey(anInterface)) {
                        setIndex.put(anInterface, new HashSet<>());
                    }
                    setIndex.get(anInterface).add(provider);
                }
            }
        }
        for (Map.Entry<Class<?>, Set<Provider<?>>> entry2 : setIndex.entrySet()) {
            if (!this.lazySetMap.containsKey(entry2.getKey())) {
                this.lazySetMap.put(entry2.getKey(), LazySet.fromCollection(entry2.getValue()));
            } else {
                LazySet<Object> existingSet = this.lazySetMap.get(entry2.getKey());
                for (Provider<?> provider2 : entry2.getValue()) {
                    runnables.add(new Runnable(provider2) { // from class: com.google.firebase.components.-$$Lambda$ComponentRuntime$tUd7uVmNp5kz4iZuK--w-dKq2BE
                        private final /* synthetic */ Provider f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            LazySet.this.add(this.f$1);
                        }
                    });
                }
            }
        }
        return runnables;
    }

    @Override // com.google.firebase.components.ComponentContainer
    public synchronized <T> Provider<T> getProvider(Class<T> anInterface) {
        Preconditions.checkNotNull(anInterface, "Null interface requested.");
        return (Provider<T>) this.lazyInstanceMap.get(anInterface);
    }

    @Override // com.google.firebase.components.ComponentContainer
    public <T> Deferred<T> getDeferred(Class<T> anInterface) {
        Provider<T> provider = getProvider(anInterface);
        if (provider == null) {
            return OptionalProvider.empty();
        }
        if (provider instanceof OptionalProvider) {
            return (OptionalProvider) provider;
        }
        return OptionalProvider.of(provider);
    }

    @Override // com.google.firebase.components.ComponentContainer
    public synchronized <T> Provider<Set<T>> setOfProvider(Class<T> anInterface) {
        LazySet<?> provider = this.lazySetMap.get(anInterface);
        if (provider != null) {
            return provider;
        }
        return (Provider<Set<T>>) EMPTY_PROVIDER;
    }

    public void initializeEagerComponents(boolean isDefaultApp) {
        HashMap<Component<?>, Provider<?>> componentsCopy;
        if (this.eagerComponentsInitializedWith.compareAndSet(null, Boolean.valueOf(isDefaultApp))) {
            synchronized (this) {
                componentsCopy = new HashMap<>(this.components);
            }
            doInitializeEagerComponents(componentsCopy, isDefaultApp);
        }
    }

    private void doInitializeEagerComponents(Map<Component<?>, Provider<?>> componentsToInitialize, boolean isDefaultApp) {
        for (Map.Entry<Component<?>, Provider<?>> entry : componentsToInitialize.entrySet()) {
            Component<?> component = entry.getKey();
            Provider<?> provider = entry.getValue();
            if (component.isAlwaysEager() || (component.isEagerInDefaultApp() && isDefaultApp)) {
                provider.get();
            }
        }
        this.eventBus.enablePublishingAndFlushPending();
    }

    @Override // com.google.firebase.dynamicloading.ComponentLoader
    public void discoverComponents() {
        synchronized (this) {
            if (!this.unprocessedRegistrarProviders.isEmpty()) {
                discoverComponents(new ArrayList());
            }
        }
    }

    public void initializeAllComponentsForTests() {
        for (Provider<?> component : this.components.values()) {
            component.get();
        }
    }

    private void processDependencies() {
        for (Component<?> component : this.components.keySet()) {
            for (Dependency dependency : component.getDependencies()) {
                if (dependency.isSet() && !this.lazySetMap.containsKey(dependency.getInterface())) {
                    this.lazySetMap.put(dependency.getInterface(), LazySet.fromCollection(Collections.emptySet()));
                } else if (this.lazyInstanceMap.containsKey(dependency.getInterface())) {
                    continue;
                } else if (dependency.isRequired()) {
                    throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", component, dependency.getInterface()));
                } else if (!dependency.isSet()) {
                    this.lazyInstanceMap.put(dependency.getInterface(), OptionalProvider.empty());
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        private final Executor defaultExecutor;
        private final List<Provider<ComponentRegistrar>> lazyRegistrars = new ArrayList();
        private final List<Component<?>> additionalComponents = new ArrayList();

        Builder(Executor defaultExecutor) {
            this.defaultExecutor = defaultExecutor;
        }

        public Builder addLazyComponentRegistrars(Collection<Provider<ComponentRegistrar>> registrars) {
            this.lazyRegistrars.addAll(registrars);
            return this;
        }

        public static /* synthetic */ ComponentRegistrar lambda$addComponentRegistrar$0(ComponentRegistrar registrar) {
            return registrar;
        }

        public Builder addComponentRegistrar(ComponentRegistrar registrar) {
            this.lazyRegistrars.add(new Provider() { // from class: com.google.firebase.components.-$$Lambda$ComponentRuntime$Builder$PuZUk0ZoyoJafAUrHnbCxfAdM_4
                @Override // com.google.firebase.inject.Provider
                public final Object get() {
                    return ComponentRuntime.Builder.lambda$addComponentRegistrar$0(ComponentRegistrar.this);
                }
            });
            return this;
        }

        public Builder addComponent(Component<?> component) {
            this.additionalComponents.add(component);
            return this;
        }

        public ComponentRuntime build() {
            return new ComponentRuntime(this.defaultExecutor, this.lazyRegistrars, this.additionalComponents);
        }
    }
}
