package androidx.constraintlayout.solver;

import androidx.exifinterface.media.ExifInterface;
import java.util.Arrays;
import java.util.HashSet;
/* loaded from: classes.dex */
public class SolverVariable {
    private static final boolean INTERNAL_DEBUG;
    static final int MAX_STRENGTH;
    public static final int STRENGTH_BARRIER;
    public static final int STRENGTH_CENTERING;
    public static final int STRENGTH_EQUALITY;
    public static final int STRENGTH_FIXED;
    public static final int STRENGTH_HIGH;
    public static final int STRENGTH_HIGHEST;
    public static final int STRENGTH_LOW;
    public static final int STRENGTH_MEDIUM;
    public static final int STRENGTH_NONE;
    private static final boolean VAR_USE_HASH;
    public float computedValue;
    int definitionId;
    float[] goalStrengthVector;
    public int id;
    public boolean inGoal;
    HashSet<ArrayRow> inRows;
    public boolean isFinalValue;
    boolean isSynonym;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    int synonym;
    float synonymDelta;
    public int usageInRowCount;
    private static int uniqueSlackId = 1;
    private static int uniqueErrorId = 1;
    private static int uniqueUnrestrictedId = 1;
    private static int uniqueConstantId = 1;
    private static int uniqueId = 1;

    /* loaded from: classes.dex */
    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public static void increaseErrorId() {
        uniqueErrorId++;
    }

    private static String getUniqueName(Type type, String prefix) {
        if (prefix != null) {
            return prefix + uniqueErrorId;
        }
        int i = AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[type.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("U");
            int i2 = uniqueUnrestrictedId + 1;
            uniqueUnrestrictedId = i2;
            sb.append(i2);
            return sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("C");
            int i3 = uniqueConstantId + 1;
            uniqueConstantId = i3;
            sb2.append(i3);
            return sb2.toString();
        } else if (i == 3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(ExifInterface.LATITUDE_SOUTH);
            int i4 = uniqueSlackId + 1;
            uniqueSlackId = i4;
            sb3.append(i4);
            return sb3.toString();
        } else if (i == 4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("e");
            int i5 = uniqueErrorId + 1;
            uniqueErrorId = i5;
            sb4.append(i5);
            return sb4.toString();
        } else if (i == 5) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
            int i6 = uniqueId + 1;
            uniqueId = i6;
            sb5.append(i6);
            return sb5.toString();
        } else {
            throw new AssertionError(type.name());
        }
    }

    /* renamed from: androidx.constraintlayout.solver.SolverVariable$1 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[Type.UNRESTRICTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[Type.CONSTANT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[Type.SLACK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[Type.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$androidx$constraintlayout$solver$SolverVariable$Type[Type.UNKNOWN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public SolverVariable(String name, Type type) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.isFinalValue = false;
        this.strengthVector = new float[9];
        this.goalStrengthVector = new float[9];
        this.mClientEquations = new ArrayRow[16];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        this.inRows = null;
        this.mName = name;
        this.mType = type;
    }

    public SolverVariable(Type type, String prefix) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.isFinalValue = false;
        this.strengthVector = new float[9];
        this.goalStrengthVector = new float[9];
        this.mClientEquations = new ArrayRow[16];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        this.inRows = null;
        this.mType = type;
    }

    void clearStrengths() {
        for (int i = 0; i < 9; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    String strengthsToString() {
        String representation = this + "[";
        boolean negative = false;
        boolean empty = true;
        for (int j = 0; j < this.strengthVector.length; j++) {
            String representation2 = representation + this.strengthVector[j];
            float[] fArr = this.strengthVector;
            if (fArr[j] > 0.0f) {
                negative = false;
            } else if (fArr[j] < 0.0f) {
                negative = true;
            }
            if (this.strengthVector[j] != 0.0f) {
                empty = false;
            }
            if (j < this.strengthVector.length - 1) {
                representation = representation2 + ", ";
            } else {
                representation = representation2 + "] ";
            }
        }
        if (negative) {
            representation = representation + " (-)";
        }
        if (!empty) {
            return representation;
        }
        return representation + " (*)";
    }

    public final void addToRow(ArrayRow row) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i >= i2) {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = row;
                this.mClientEquationsCount = i3 + 1;
                return;
            } else if (this.mClientEquations[i] != row) {
                i++;
            } else {
                return;
            }
        }
    }

    public final void removeFromRow(ArrayRow row) {
        int count = this.mClientEquationsCount;
        for (int i = 0; i < count; i++) {
            if (this.mClientEquations[i] == row) {
                for (int j = i; j < count - 1; j++) {
                    ArrayRow[] arrayRowArr = this.mClientEquations;
                    arrayRowArr[j] = arrayRowArr[j + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public final void updateReferencesWithNewDefinition(LinearSystem system, ArrayRow definition) {
        int count = this.mClientEquationsCount;
        for (int i = 0; i < count; i++) {
            this.mClientEquations[i].updateFromRow(system, definition, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void setFinalValue(LinearSystem system, float value) {
        this.computedValue = value;
        this.isFinalValue = true;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int count = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i = 0; i < count; i++) {
            this.mClientEquations[i].updateFromFinalVariable(system, this, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void setSynonym(LinearSystem system, SolverVariable synonymVariable, float value) {
        this.isSynonym = true;
        this.synonym = synonymVariable.id;
        this.synonymDelta = value;
        int count = this.mClientEquationsCount;
        this.definitionId = -1;
        for (int i = 0; i < count; i++) {
            this.mClientEquations[i].updateFromSynonymVariable(system, this, false);
        }
        this.mClientEquationsCount = 0;
        system.displayReadableRows();
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.isFinalValue = false;
        this.isSynonym = false;
        this.synonym = -1;
        this.synonymDelta = 0.0f;
        int count = this.mClientEquationsCount;
        for (int i = 0; i < count; i++) {
            this.mClientEquations[i] = null;
        }
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.inGoal = false;
        Arrays.fill(this.goalStrengthVector, 0.0f);
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setType(Type type, String prefix) {
        this.mType = type;
    }

    public String toString() {
        if (this.mName != null) {
            return "" + this.mName;
        }
        return "" + this.id;
    }
}
