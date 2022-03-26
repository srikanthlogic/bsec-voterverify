package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;
@TypeQualifier(applicableTo = String.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes3.dex */
public @interface MatchesPattern {
    int flags() default 0;

    @RegEx
    String value();

    /* loaded from: classes3.dex */
    public static class Checker implements TypeQualifierValidator<MatchesPattern> {
        public When forConstantValue(MatchesPattern annotation, Object value) {
            if (Pattern.compile(annotation.value(), annotation.flags()).matcher((String) value).matches()) {
                return When.ALWAYS;
            }
            return When.NEVER;
        }
    }
}
