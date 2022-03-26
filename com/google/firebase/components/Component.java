package com.google.firebase.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes3.dex */
public final class Component<T> {
    private final Set<Dependency> dependencies;
    private final ComponentFactory<T> factory;
    private final int instantiation;
    private final Set<Class<? super T>> providedInterfaces;
    private final Set<Class<?>> publishedEvents;
    private final int type;

    private Component(Set<Class<? super T>> providedInterfaces, Set<Dependency> dependencies, int instantiation, int type, ComponentFactory<T> factory, Set<Class<?>> publishedEvents) {
        this.providedInterfaces = Collections.unmodifiableSet(providedInterfaces);
        this.dependencies = Collections.unmodifiableSet(dependencies);
        this.instantiation = instantiation;
        this.type = type;
        this.factory = factory;
        this.publishedEvents = Collections.unmodifiableSet(publishedEvents);
    }

    public Set<Class<? super T>> getProvidedInterfaces() {
        return this.providedInterfaces;
    }

    public Set<Dependency> getDependencies() {
        return this.dependencies;
    }

    public ComponentFactory<T> getFactory() {
        return this.factory;
    }

    public Set<Class<?>> getPublishedEvents() {
        return this.publishedEvents;
    }

    public boolean isLazy() {
        return this.instantiation == 0;
    }

    public boolean isAlwaysEager() {
        return this.instantiation == 1;
    }

    public boolean isEagerInDefaultApp() {
        return this.instantiation == 2;
    }

    public boolean isValue() {
        return this.type == 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Component<");
        sb.append(Arrays.toString(this.providedInterfaces.toArray()));
        sb.append(">{");
        sb.append(this.instantiation);
        sb.append(", type=");
        sb.append(this.type);
        sb.append(", deps=");
        sb.append(Arrays.toString(this.dependencies.toArray()));
        return sb.append("}").toString();
    }

    public static <T> Builder<T> builder(Class<T> anInterface) {
        return new Builder<>(anInterface, new Class[0]);
    }

    @SafeVarargs
    public static <T> Builder<T> builder(Class<T> anInterface, Class<? super T>... additionalInterfaces) {
        return new Builder<>(anInterface, additionalInterfaces);
    }

    public static /* synthetic */ Object lambda$of$0(Object value, ComponentContainer args) {
        return value;
    }

    @Deprecated
    public static <T> Component<T> of(Class<T> anInterface, T value) {
        return builder(anInterface).factory(new ComponentFactory(value) { // from class: com.google.firebase.components.-$$Lambda$Component$LDWunTP14i_DtHnzlsCO6vhCq9U
            private final /* synthetic */ Object f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return Component.lambda$of$0(this.f$0, componentContainer);
            }
        }).build();
    }

    public static /* synthetic */ Object lambda$of$1(Object value, ComponentContainer args) {
        return value;
    }

    @SafeVarargs
    public static <T> Component<T> of(T value, Class<T> anInterface, Class<? super T>... additionalInterfaces) {
        return builder(anInterface, additionalInterfaces).factory(new ComponentFactory(value) { // from class: com.google.firebase.components.-$$Lambda$Component$7yY_HNIPR6WlFLeM3WGK-K88BE8
            private final /* synthetic */ Object f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return Component.lambda$of$1(this.f$0, componentContainer);
            }
        }).build();
    }

    public static <T> Builder<T> intoSetBuilder(Class<T> anInterface) {
        return builder(anInterface).intoSet();
    }

    public static <T> Component<T> intoSet(T value, Class<T> anInterface) {
        return intoSetBuilder(anInterface).factory(new ComponentFactory(value) { // from class: com.google.firebase.components.-$$Lambda$Component$qH3Nws2f03n0HLNrv1h2pQr-B08
            private final /* synthetic */ Object f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.firebase.components.ComponentFactory
            public final Object create(ComponentContainer componentContainer) {
                return Component.lambda$intoSet$2(this.f$0, componentContainer);
            }
        }).build();
    }

    public static /* synthetic */ Object lambda$intoSet$2(Object value, ComponentContainer c) {
        return value;
    }

    /* loaded from: classes3.dex */
    public static class Builder<T> {
        private final Set<Dependency> dependencies;
        private ComponentFactory<T> factory;
        private int instantiation;
        private final Set<Class<? super T>> providedInterfaces;
        private Set<Class<?>> publishedEvents;
        private int type;

        @SafeVarargs
        private Builder(Class<T> anInterface, Class<? super T>... additionalInterfaces) {
            this.providedInterfaces = new HashSet();
            this.dependencies = new HashSet();
            this.instantiation = 0;
            this.type = 0;
            this.publishedEvents = new HashSet();
            Preconditions.checkNotNull(anInterface, "Null interface");
            this.providedInterfaces.add(anInterface);
            for (Class<? super T> iface : additionalInterfaces) {
                Preconditions.checkNotNull(iface, "Null interface");
            }
            Collections.addAll(this.providedInterfaces, additionalInterfaces);
        }

        public Builder<T> add(Dependency dependency) {
            Preconditions.checkNotNull(dependency, "Null dependency");
            validateInterface(dependency.getInterface());
            this.dependencies.add(dependency);
            return this;
        }

        public Builder<T> alwaysEager() {
            return setInstantiation(1);
        }

        public Builder<T> eagerInDefaultApp() {
            return setInstantiation(2);
        }

        public Builder<T> publishes(Class<?> eventType) {
            this.publishedEvents.add(eventType);
            return this;
        }

        private Builder<T> setInstantiation(int instantiation) {
            Preconditions.checkState(this.instantiation == 0, "Instantiation type has already been set.");
            this.instantiation = instantiation;
            return this;
        }

        private void validateInterface(Class<?> anInterface) {
            Preconditions.checkArgument(!this.providedInterfaces.contains(anInterface), "Components are not allowed to depend on interfaces they themselves provide.");
        }

        public Builder<T> factory(ComponentFactory<T> value) {
            this.factory = (ComponentFactory) Preconditions.checkNotNull(value, "Null factory");
            return this;
        }

        public Builder<T> intoSet() {
            this.type = 1;
            return this;
        }

        public Component<T> build() {
            Preconditions.checkState(this.factory != null, "Missing required property: factory.");
            return new Component<>(new HashSet(this.providedInterfaces), new HashSet(this.dependencies), this.instantiation, this.type, this.factory, this.publishedEvents);
        }
    }
}
