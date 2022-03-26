package com.google.common.collect;

import androidx.exifinterface.media.ExifInterface;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.errorprone.annotations.Immutable;
import java.lang.reflect.Array;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable(containerOf = {"R", "C", ExifInterface.GPS_MEASUREMENT_INTERRUPTED})
/* loaded from: classes.dex */
public final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    private final int[] cellColumnIndices;
    private final int[] cellRowIndices;
    private final int[] columnCounts;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
    private final int[] rowCounts;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
    private final V[][] values;

    public DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        this.values = (V[][]) ((Object[][]) Array.newInstance(Object.class, rowSpace.size(), columnSpace.size()));
        this.rowKeyToIndex = Maps.indexMap(rowSpace);
        this.columnKeyToIndex = Maps.indexMap(columnSpace);
        this.rowCounts = new int[this.rowKeyToIndex.size()];
        this.columnCounts = new int[this.columnKeyToIndex.size()];
        int[] cellRowIndices = new int[cellList.size()];
        int[] cellColumnIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); i++) {
            Table.Cell<R, C, V> cell = cellList.get(i);
            R rowKey = cell.getRowKey();
            C columnKey = cell.getColumnKey();
            int rowIndex = this.rowKeyToIndex.get(rowKey).intValue();
            int columnIndex = this.columnKeyToIndex.get(columnKey).intValue();
            checkNoDuplicate(rowKey, columnKey, this.values[rowIndex][columnIndex], cell.getValue());
            this.values[rowIndex][columnIndex] = cell.getValue();
            int[] iArr = this.rowCounts;
            iArr[rowIndex] = iArr[rowIndex] + 1;
            int[] iArr2 = this.columnCounts;
            iArr2[columnIndex] = iArr2[columnIndex] + 1;
            cellRowIndices[i] = rowIndex;
            cellColumnIndices[i] = columnIndex;
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnIndices = cellColumnIndices;
        this.rowMap = new RowMap();
        this.columnMap = new ColumnMap();
    }

    /* loaded from: classes.dex */
    public static abstract class ImmutableArrayMap<K, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
        private final int size;

        @NullableDecl
        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        ImmutableArrayMap(int size) {
            this.size = size;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        K getKey(int index) {
            return keyToIndex().keySet().asList().get(index);
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap, com.google.common.collect.ImmutableMap
        public ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        @Override // java.util.Map
        public int size() {
            return this.size;
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public V get(@NullableDecl Object key) {
            Integer keyIndex = keyToIndex().get(key);
            if (keyIndex == null) {
                return null;
            }
            return getValue(keyIndex.intValue());
        }

        @Override // com.google.common.collect.ImmutableMap.IteratorBasedImmutableMap
        UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
            return new AbstractIterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.DenseImmutableTable.ImmutableArrayMap.1
                private int index = -1;
                private final int maxIndex;

                {
                    this.maxIndex = ImmutableArrayMap.this.keyToIndex().size();
                }

                @Override // com.google.common.collect.AbstractIterator
                public Map.Entry<K, V> computeNext() {
                    int i = this.index;
                    while (true) {
                        this.index = i + 1;
                        int i2 = this.index;
                        if (i2 >= this.maxIndex) {
                            return endOfData();
                        }
                        Object value = ImmutableArrayMap.this.getValue(i2);
                        if (value != null) {
                            return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                        }
                        i = this.index;
                    }
                }
            };
        }
    }

    /* loaded from: classes.dex */
    public final class Row extends ImmutableArrayMap<C, V> {
        private final int rowIndex;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        Row(int rowIndex) {
            super(r1.rowCounts[rowIndex]);
            DenseImmutableTable.this = r1;
            this.rowIndex = rowIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        V getValue(int keyIndex) {
            return (V) DenseImmutableTable.this.values[this.rowIndex][keyIndex];
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isPartialView() {
            return true;
        }
    }

    /* loaded from: classes.dex */
    public final class Column extends ImmutableArrayMap<R, V> {
        private final int columnIndex;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        Column(int columnIndex) {
            super(r1.columnCounts[columnIndex]);
            DenseImmutableTable.this = r1;
            this.columnIndex = columnIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        V getValue(int keyIndex) {
            return (V) DenseImmutableTable.this.values[keyIndex][this.columnIndex];
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isPartialView() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class RowMap extends ImmutableArrayMap<R, ImmutableMap<C, V>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private RowMap() {
            super(r1.rowCounts.length);
            DenseImmutableTable.this = r1;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<R, Integer> keyToIndex() {
            return DenseImmutableTable.this.rowKeyToIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        public ImmutableMap<C, V> getValue(int keyIndex) {
            return new Row(keyIndex);
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isPartialView() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class ColumnMap extends ImmutableArrayMap<C, ImmutableMap<R, V>> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private ColumnMap() {
            super(r1.columnCounts.length);
            DenseImmutableTable.this = r1;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        ImmutableMap<C, Integer> keyToIndex() {
            return DenseImmutableTable.this.columnKeyToIndex;
        }

        @Override // com.google.common.collect.DenseImmutableTable.ImmutableArrayMap
        public ImmutableMap<R, V> getValue(int keyIndex) {
            return new Column(keyIndex);
        }

        @Override // com.google.common.collect.ImmutableMap
        public boolean isPartialView() {
            return false;
        }
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.copyOf(this.columnMap);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.copyOf(this.rowMap);
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public V get(@NullableDecl Object rowKey, @NullableDecl Object columnKey) {
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        if (rowIndex == null || columnIndex == null) {
            return null;
        }
        return this.values[rowIndex.intValue()][columnIndex.intValue()];
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return this.cellRowIndices.length;
    }

    @Override // com.google.common.collect.RegularImmutableTable
    Table.Cell<R, C, V> getCell(int index) {
        int rowIndex = this.cellRowIndices[index];
        int columnIndex = this.cellColumnIndices[index];
        return cellOf(rowKeySet().asList().get(rowIndex), columnKeySet().asList().get(columnIndex), this.values[rowIndex][columnIndex]);
    }

    @Override // com.google.common.collect.RegularImmutableTable
    V getValue(int index) {
        return this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]];
    }

    @Override // com.google.common.collect.ImmutableTable
    ImmutableTable.SerializedForm createSerializedForm() {
        return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
    }
}
