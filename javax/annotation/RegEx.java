package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
@Syntax("RegEx")
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes3.dex */
public @interface RegEx {
    When when() default When.ALWAYS;

    /* loaded from: classes3.dex */
    public static class Checker implements TypeQualifierValidator<RegEx> {
        public When forConstantValue(RegEx annotation, Object value) {
            if (!(value instanceof String)) {
                return When.NEVER;
            }
            try {
                Pattern.compile((String) value);
                return When.ALWAYS;
            } catch (PatternSyntaxException e) {
                return When.NEVER;
            }
        }
    }
}
