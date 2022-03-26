package androidx.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class TransitionInflater {
    private final Context mContext;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    private static final ArrayMap<String, Constructor> CONSTRUCTORS = new ArrayMap<>();

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    public Transition inflateTransition(int resource) {
        XmlResourceParser parser;
        try {
            parser = this.mContext.getResources().getXml(resource);
            try {
                return createTransitionFromXml(parser, Xml.asAttributeSet(parser), null);
            } catch (IOException e) {
                throw new InflateException(parser.getPositionDescription() + ": " + e.getMessage(), e);
            } catch (XmlPullParserException e2) {
                throw new InflateException(e2.getMessage(), e2);
            }
        } finally {
            parser.close();
        }
    }

    public TransitionManager inflateTransitionManager(int resource, ViewGroup sceneRoot) {
        XmlResourceParser parser;
        try {
            parser = this.mContext.getResources().getXml(resource);
            try {
                return createTransitionManagerFromXml(parser, Xml.asAttributeSet(parser), sceneRoot);
            } catch (IOException e) {
                InflateException ex = new InflateException(parser.getPositionDescription() + ": " + e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (XmlPullParserException e2) {
                InflateException ex2 = new InflateException(e2.getMessage());
                ex2.initCause(e2);
                throw ex2;
            }
        } finally {
            parser.close();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:79:0x018a, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private Transition createTransitionFromXml(XmlPullParser parser, AttributeSet attrs, Transition parent) throws XmlPullParserException, IOException {
        Transition transition = null;
        int depth = parser.getDepth();
        TransitionSet transitionSet = parent instanceof TransitionSet ? (TransitionSet) parent : null;
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if ("fade".equals(name)) {
                        transition = new Fade(this.mContext, attrs);
                    } else if ("changeBounds".equals(name)) {
                        transition = new ChangeBounds(this.mContext, attrs);
                    } else if ("slide".equals(name)) {
                        transition = new Slide(this.mContext, attrs);
                    } else if ("explode".equals(name)) {
                        transition = new Explode(this.mContext, attrs);
                    } else if ("changeImageTransform".equals(name)) {
                        transition = new ChangeImageTransform(this.mContext, attrs);
                    } else if ("changeTransform".equals(name)) {
                        transition = new ChangeTransform(this.mContext, attrs);
                    } else if ("changeClipBounds".equals(name)) {
                        transition = new ChangeClipBounds(this.mContext, attrs);
                    } else if ("autoTransition".equals(name)) {
                        transition = new AutoTransition(this.mContext, attrs);
                    } else if ("changeScroll".equals(name)) {
                        transition = new ChangeScroll(this.mContext, attrs);
                    } else if ("transitionSet".equals(name)) {
                        transition = new TransitionSet(this.mContext, attrs);
                    } else if ("transition".equals(name)) {
                        transition = (Transition) createCustom(attrs, Transition.class, "transition");
                    } else if ("targets".equals(name)) {
                        getTargetIds(parser, attrs, parent);
                    } else if ("arcMotion".equals(name)) {
                        if (parent != null) {
                            parent.setPathMotion(new ArcMotion(this.mContext, attrs));
                        } else {
                            throw new RuntimeException("Invalid use of arcMotion element");
                        }
                    } else if ("pathMotion".equals(name)) {
                        if (parent != null) {
                            parent.setPathMotion((PathMotion) createCustom(attrs, PathMotion.class, "pathMotion"));
                        } else {
                            throw new RuntimeException("Invalid use of pathMotion element");
                        }
                    } else if (!"patternPathMotion".equals(name)) {
                        throw new RuntimeException("Unknown scene name: " + parser.getName());
                    } else if (parent != null) {
                        parent.setPathMotion(new PatternPathMotion(this.mContext, attrs));
                    } else {
                        throw new RuntimeException("Invalid use of patternPathMotion element");
                    }
                    if (transition == null) {
                        continue;
                    } else {
                        if (!parser.isEmptyElementTag()) {
                            createTransitionFromXml(parser, attrs, transition);
                        }
                        if (transitionSet != null) {
                            transitionSet.addTransition(transition);
                            transition = null;
                        } else if (parent != null) {
                            throw new InflateException("Could not add transition to another transition.");
                        }
                    }
                }
            }
        }
    }

    private Object createCustom(AttributeSet attrs, Class expectedType, String tag) {
        Object newInstance;
        Class<?> c;
        String className = attrs.getAttributeValue(null, "class");
        if (className != null) {
            try {
                synchronized (CONSTRUCTORS) {
                    Constructor constructor = CONSTRUCTORS.get(className);
                    if (constructor == null && (c = this.mContext.getClassLoader().loadClass(className).asSubclass(expectedType)) != null) {
                        constructor = c.getConstructor(CONSTRUCTOR_SIGNATURE);
                        constructor.setAccessible(true);
                        CONSTRUCTORS.put(className, constructor);
                    }
                    newInstance = constructor.newInstance(this.mContext, attrs);
                }
                return newInstance;
            } catch (Exception e) {
                throw new InflateException("Could not instantiate " + expectedType + " class " + className, e);
            }
        } else {
            throw new InflateException(tag + " tag must have a 'class' attribute");
        }
    }

    private void getTargetIds(XmlPullParser parser, AttributeSet attrs, Transition transition) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                return;
            }
            if (type == 2) {
                if (parser.getName().equals("target")) {
                    TypedArray a2 = this.mContext.obtainStyledAttributes(attrs, Styleable.TRANSITION_TARGET);
                    int id = TypedArrayUtils.getNamedResourceId(a2, parser, "targetId", 1, 0);
                    if (id != 0) {
                        transition.addTarget(id);
                    } else {
                        int id2 = TypedArrayUtils.getNamedResourceId(a2, parser, "excludeId", 2, 0);
                        if (id2 != 0) {
                            transition.excludeTarget(id2, true);
                        } else {
                            String transitionName = TypedArrayUtils.getNamedString(a2, parser, "targetName", 4);
                            if (transitionName != null) {
                                transition.addTarget(transitionName);
                            } else {
                                String transitionName2 = TypedArrayUtils.getNamedString(a2, parser, "excludeName", 5);
                                if (transitionName2 != null) {
                                    transition.excludeTarget(transitionName2, true);
                                } else {
                                    String className = TypedArrayUtils.getNamedString(a2, parser, "excludeClass", 3);
                                    if (className != null) {
                                        try {
                                            transition.excludeTarget(Class.forName(className), true);
                                        } catch (ClassNotFoundException e) {
                                            a2.recycle();
                                            throw new RuntimeException("Could not create " + className, e);
                                        }
                                    } else {
                                        String className2 = TypedArrayUtils.getNamedString(a2, parser, "targetClass", 0);
                                        if (className2 != null) {
                                            transition.addTarget(Class.forName(className2));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    a2.recycle();
                } else {
                    throw new RuntimeException("Unknown scene name: " + parser.getName());
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0056, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private TransitionManager createTransitionManagerFromXml(XmlPullParser parser, AttributeSet attrs, ViewGroup sceneRoot) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        TransitionManager transitionManager = null;
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if (name.equals("transitionManager")) {
                        transitionManager = new TransitionManager();
                    } else if (!name.equals("transition") || transitionManager == null) {
                        break;
                    } else {
                        loadTransition(attrs, parser, sceneRoot, transitionManager);
                    }
                }
            }
        }
        throw new RuntimeException("Unknown scene name: " + parser.getName());
    }

    private void loadTransition(AttributeSet attrs, XmlPullParser parser, ViewGroup sceneRoot, TransitionManager transitionManager) throws Resources.NotFoundException {
        Transition transition;
        TypedArray a2 = this.mContext.obtainStyledAttributes(attrs, Styleable.TRANSITION_MANAGER);
        int transitionId = TypedArrayUtils.getNamedResourceId(a2, parser, "transition", 2, -1);
        int fromId = TypedArrayUtils.getNamedResourceId(a2, parser, "fromScene", 0, -1);
        Scene toScene = null;
        Scene fromScene = fromId < 0 ? null : Scene.getSceneForLayout(sceneRoot, fromId, this.mContext);
        int toId = TypedArrayUtils.getNamedResourceId(a2, parser, "toScene", 1, -1);
        if (toId >= 0) {
            toScene = Scene.getSceneForLayout(sceneRoot, toId, this.mContext);
        }
        if (transitionId >= 0 && (transition = inflateTransition(transitionId)) != null) {
            if (toScene == null) {
                throw new RuntimeException("No toScene for transition ID " + transitionId);
            } else if (fromScene == null) {
                transitionManager.setTransition(toScene, transition);
            } else {
                transitionManager.setTransition(fromScene, toScene, transition);
            }
        }
        a2.recycle();
    }
}
