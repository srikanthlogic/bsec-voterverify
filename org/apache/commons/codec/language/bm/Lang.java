package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.language.bm.Languages;
/* loaded from: classes3.dex */
public class Lang {
    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
    private static final Map<NameType, Lang> Langs = new EnumMap(NameType.class);
    private final Languages languages;
    private final List<LangRule> rules;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class LangRule {
        private final boolean acceptOnMatch;
        private final Set<String> languages;
        private final Pattern pattern;

        private LangRule(Pattern pattern, Set<String> set, boolean z) {
            this.pattern = pattern;
            this.languages = set;
            this.acceptOnMatch = z;
        }

        public boolean matches(String str) {
            return this.pattern.matcher(str).find();
        }
    }

    static {
        NameType[] values = NameType.values();
        for (NameType nameType : values) {
            Langs.put(nameType, loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(nameType)));
        }
    }

    private Lang(List<LangRule> list, Languages languages) {
        this.rules = Collections.unmodifiableList(list);
        this.languages = languages;
    }

    public static Lang instance(NameType nameType) {
        return Langs.get(nameType);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00aa, code lost:
        throw new java.lang.IllegalArgumentException("Malformed line '" + r4 + "' in language resource '" + r9 + "'");
     */
    /* JADX WARN: Finally extract failed */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static Lang loadFromResource(String str, Languages languages) {
        ArrayList arrayList = new ArrayList();
        InputStream resourceAsStream = Lang.class.getClassLoader().getResourceAsStream(str);
        if (resourceAsStream != null) {
            Scanner scanner = new Scanner(resourceAsStream, "UTF-8");
            loop0: while (true) {
                boolean z = false;
                while (scanner.hasNextLine()) {
                    try {
                        String nextLine = scanner.nextLine();
                        if (!z) {
                            if (!nextLine.startsWith("/*")) {
                                int indexOf = nextLine.indexOf("//");
                                String trim = (indexOf >= 0 ? nextLine.substring(0, indexOf) : nextLine).trim();
                                if (trim.length() != 0) {
                                    String[] split = trim.split("\\s+");
                                    if (split.length != 3) {
                                        break loop0;
                                    }
                                    arrayList.add(new LangRule(Pattern.compile(split[0]), new HashSet(Arrays.asList(split[1].split("\\+"))), split[2].equals("true")));
                                }
                            } else {
                                z = true;
                            }
                        } else if (nextLine.endsWith("*/")) {
                            break;
                        }
                    } catch (Throwable th) {
                        scanner.close();
                        throw th;
                    }
                }
                scanner.close();
                return new Lang(arrayList, languages);
            }
        }
        throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
    }

    public String guessLanguage(String str) {
        Languages.LanguageSet guessLanguages = guessLanguages(str);
        return guessLanguages.isSingleton() ? guessLanguages.getAny() : Languages.ANY;
    }

    public Languages.LanguageSet guessLanguages(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        HashSet hashSet = new HashSet(this.languages.getLanguages());
        for (LangRule langRule : this.rules) {
            if (langRule.matches(lowerCase)) {
                boolean z = langRule.acceptOnMatch;
                Set set = langRule.languages;
                if (z) {
                    hashSet.retainAll(set);
                } else {
                    hashSet.removeAll(set);
                }
            }
        }
        Languages.LanguageSet from = Languages.LanguageSet.from(hashSet);
        return from.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : from;
    }
}
