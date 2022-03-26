package com.google.firebase.encoders.json;

import com.google.firebase.encoders.ValueEncoder;
import com.google.firebase.encoders.ValueEncoderContext;
/* compiled from: lambda */
/* renamed from: com.google.firebase.encoders.json.-$$Lambda$JsonDataEncoderBuilder$qsuhyVxU2dugEaZ-Uhp79euedYA  reason: invalid class name */
/* loaded from: classes3.dex */
public final /* synthetic */ class $$Lambda$JsonDataEncoderBuilder$qsuhyVxU2dugEaZUhp79euedYA implements ValueEncoder {
    public static final /* synthetic */ $$Lambda$JsonDataEncoderBuilder$qsuhyVxU2dugEaZUhp79euedYA INSTANCE = new $$Lambda$JsonDataEncoderBuilder$qsuhyVxU2dugEaZUhp79euedYA();

    private /* synthetic */ $$Lambda$JsonDataEncoderBuilder$qsuhyVxU2dugEaZUhp79euedYA() {
    }

    @Override // com.google.firebase.encoders.Encoder
    public final void encode(Object obj, ValueEncoderContext valueEncoderContext) {
        valueEncoderContext.add(((Boolean) obj).booleanValue());
    }
}
