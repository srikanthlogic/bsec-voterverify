package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Rule;
/* loaded from: classes3.dex */
public class PhoneticEngine {
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap(NameType.class);
    private final boolean concat;
    private final Lang lang;
    private final int maxPhonemes;
    private final NameType nameType;
    private final RuleType ruleType;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.apache.commons.codec.language.bm.PhoneticEngine$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$commons$codec$language$bm$NameType = new int[NameType.values().length];

        static {
            try {
                $SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.SEPHARDIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.ASHKENAZI.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$commons$codec$language$bm$NameType[NameType.GENERIC.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class PhonemeBuilder {
        private final Set<Rule.Phoneme> phonemes;

        private PhonemeBuilder(Set<Rule.Phoneme> set) {
            this.phonemes = set;
        }

        /* synthetic */ PhonemeBuilder(Set set, AnonymousClass1 r2) {
            this(set);
        }

        private PhonemeBuilder(Rule.Phoneme phoneme) {
            this.phonemes = new LinkedHashSet();
            this.phonemes.add(phoneme);
        }

        public static PhonemeBuilder empty(Languages.LanguageSet languageSet) {
            return new PhonemeBuilder(new Rule.Phoneme("", languageSet));
        }

        public void append(CharSequence charSequence) {
            for (Rule.Phoneme phoneme : this.phonemes) {
                phoneme.append(charSequence);
            }
        }

        public void apply(Rule.PhonemeExpr phonemeExpr, int i) {
            LinkedHashSet linkedHashSet = new LinkedHashSet(i);
            loop0: for (Rule.Phoneme phoneme : this.phonemes) {
                for (Rule.Phoneme phoneme2 : phonemeExpr.getPhonemes()) {
                    Languages.LanguageSet restrictTo = phoneme.getLanguages().restrictTo(phoneme2.getLanguages());
                    if (!restrictTo.isEmpty()) {
                        Rule.Phoneme phoneme3 = new Rule.Phoneme(phoneme, phoneme2, restrictTo);
                        if (linkedHashSet.size() < i) {
                            linkedHashSet.add(phoneme3);
                            if (linkedHashSet.size() >= i) {
                                break loop0;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
            this.phonemes.clear();
            this.phonemes.addAll(linkedHashSet);
        }

        public Set<Rule.Phoneme> getPhonemes() {
            return this.phonemes;
        }

        public String makeString() {
            StringBuilder sb = new StringBuilder();
            for (Rule.Phoneme phoneme : this.phonemes) {
                if (sb.length() > 0) {
                    sb.append("|");
                }
                sb.append(phoneme.getPhonemeText());
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class RulesApplication {
        private final Map<String, List<Rule>> finalRules;
        private boolean found;
        private int i;
        private final CharSequence input;
        private final int maxPhonemes;
        private PhonemeBuilder phonemeBuilder;

        public RulesApplication(Map<String, List<Rule>> map, CharSequence charSequence, PhonemeBuilder phonemeBuilder, int i, int i2) {
            if (map != null) {
                this.finalRules = map;
                this.phonemeBuilder = phonemeBuilder;
                this.input = charSequence;
                this.i = i;
                this.maxPhonemes = i2;
                return;
            }
            throw new NullPointerException("The finalRules argument must not be null");
        }

        public int getI() {
            return this.i;
        }

        public PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }

        public RulesApplication invoke() {
            this.found = false;
            Map<String, List<Rule>> map = this.finalRules;
            CharSequence charSequence = this.input;
            int i = this.i;
            List<Rule> list = map.get(charSequence.subSequence(i, i + 1));
            int i2 = 1;
            if (list != null) {
                Iterator<Rule> it = list.iterator();
                i2 = 1;
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Rule next = it.next();
                    int length = next.getPattern().length();
                    if (next.patternAndContextMatches(this.input, this.i)) {
                        this.phonemeBuilder.apply(next.getPhoneme(), this.maxPhonemes);
                        this.found = true;
                        i2 = length;
                        break;
                    }
                    i2 = length;
                }
            } else {
                i2 = 1;
            }
            if (this.found) {
            }
            this.i += i2;
            return this;
        }

        public boolean isFound() {
            return this.found;
        }
    }

    static {
        NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
        NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
        NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
    }

    public PhoneticEngine(NameType nameType, RuleType ruleType, boolean z) {
        this(nameType, ruleType, z, 20);
    }

    public PhoneticEngine(NameType nameType, RuleType ruleType, boolean z, int i) {
        if (ruleType != RuleType.RULES) {
            this.nameType = nameType;
            this.ruleType = ruleType;
            this.concat = z;
            this.lang = Lang.instance(nameType);
            this.maxPhonemes = i;
            return;
        }
        throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
    }

    private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, Map<String, List<Rule>> map) {
        if (map == null) {
            throw new NullPointerException("finalRules can not be null");
        } else if (map.isEmpty()) {
            return phonemeBuilder;
        } else {
            TreeSet treeSet = new TreeSet(Rule.Phoneme.COMPARATOR);
            for (Rule.Phoneme phoneme : phonemeBuilder.getPhonemes()) {
                PhonemeBuilder empty = PhonemeBuilder.empty(phoneme.getLanguages());
                String charSequence = phoneme.getPhonemeText().toString();
                int i = 0;
                while (i < charSequence.length()) {
                    RulesApplication invoke = new RulesApplication(map, charSequence, empty, i, this.maxPhonemes).invoke();
                    boolean isFound = invoke.isFound();
                    empty = invoke.getPhonemeBuilder();
                    if (!isFound) {
                        empty.append(charSequence.subSequence(i, i + 1));
                    }
                    i = invoke.getI();
                }
                treeSet.addAll(empty.getPhonemes());
            }
            return new PhonemeBuilder(treeSet, null);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:3:0x000d, code lost:
        if (r2.hasNext() != false) goto L_0x000f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x000f, code lost:
        r0.append(r2.next());
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001c, code lost:
        if (r2.hasNext() == false) goto L_0x0022;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x001e, code lost:
        r0.append(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0026, code lost:
        return r0.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static String join(Iterable<String> iterable, String str) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = iterable.iterator();
    }

    public String encode(String str) {
        return encode(str, this.lang.guessLanguages(str));
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x017e A[LOOP:2: B:38:0x0178->B:40:0x017e, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public String encode(String str, Languages.LanguageSet languageSet) {
        String str2;
        int i;
        Map<String, List<Rule>> instanceMap = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
        Map<String, List<Rule>> instanceMap2 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
        Map<String, List<Rule>> instanceMap3 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
        String trim = str.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
        if (this.nameType == NameType.GENERIC) {
            if (trim.length() < 2 || !trim.substring(0, 2).equals("d'")) {
                for (String str3 : NAME_PREFIXES.get(this.nameType)) {
                    if (trim.startsWith(str3 + " ")) {
                        String substring = trim.substring(str3.length() + 1);
                        String str4 = str3 + substring;
                        return "(" + encode(substring) + ")-(" + encode(str4) + ")";
                    }
                }
            } else {
                String substring2 = trim.substring(2);
                String str5 = "d" + substring2;
                return "(" + encode(substring2) + ")-(" + encode(str5) + ")";
            }
        }
        List<String> asList = Arrays.asList(trim.split("\\s+"));
        ArrayList<String> arrayList = new ArrayList();
        int i2 = AnonymousClass1.$SwitchMap$org$apache$commons$codec$language$bm$NameType[this.nameType.ordinal()];
        if (i2 == 1) {
            for (String str6 : asList) {
                String[] split = str6.split("'");
                arrayList.add(split[split.length - 1]);
            }
        } else if (i2 == 2) {
            arrayList.addAll(asList);
        } else if (i2 == 3) {
            arrayList.addAll(asList);
            if (!this.concat) {
                str2 = join(arrayList, " ");
            } else if (arrayList.size() == 1) {
                str2 = (String) asList.iterator().next();
            } else {
                StringBuilder sb = new StringBuilder();
                for (String str7 : arrayList) {
                    sb.append("-");
                    sb.append(encode(str7));
                }
                return sb.substring(1);
            }
            PhonemeBuilder empty = PhonemeBuilder.empty(languageSet);
            i = 0;
            while (i < str2.length()) {
                RulesApplication invoke = new RulesApplication(instanceMap, str2, empty, i, this.maxPhonemes).invoke();
                i = invoke.getI();
                empty = invoke.getPhonemeBuilder();
            }
            return applyFinalRules(applyFinalRules(empty, instanceMap2), instanceMap3).makeString();
        } else {
            throw new IllegalStateException("Unreachable case: " + this.nameType);
        }
        arrayList.removeAll(NAME_PREFIXES.get(this.nameType));
        if (!this.concat) {
        }
        PhonemeBuilder empty2 = PhonemeBuilder.empty(languageSet);
        i = 0;
        while (i < str2.length()) {
        }
        return applyFinalRules(applyFinalRules(empty2, instanceMap2), instanceMap3).makeString();
    }

    public Lang getLang() {
        return this.lang;
    }

    public int getMaxPhonemes() {
        return this.maxPhonemes;
    }

    public NameType getNameType() {
        return this.nameType;
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public boolean isConcat() {
        return this.concat;
    }
}
