package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    @Override // com.google.common.collect.Table
    public abstract ImmutableMap<C, Map<R, V>> columnMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractTable
    public abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();

    abstract SerializedForm createSerializedForm();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractTable
    public abstract ImmutableCollection<V> createValues();

    @Override // com.google.common.collect.Table
    public abstract ImmutableMap<R, Map<C, V>> rowMap();

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean containsColumn(@NullableDecl Object obj) {
        return super.containsColumn(obj);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean containsRow(@NullableDecl Object obj) {
        return super.containsRow(obj);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean equals(@NullableDecl Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ Object get(@NullableDecl Object obj, @NullableDecl Object obj2) {
        return super.get(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractTable, java.lang.Object
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <R, C, V> ImmutableTable<R, C, V> of() {
        return (ImmutableTable<R, C, V>) SparseImmutableTable.EMPTY;
    }

    public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
        return new SingletonImmutableTable(rowKey, columnKey, value);
    }

    public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
        if (table instanceof ImmutableTable) {
            return (ImmutableTable) table;
        }
        return copyOf(table.cellSet());
    }

    private static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
        Builder<R, C, V> builder = builder();
        for (Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
            builder.put(cell);
        }
        return builder.build();
    }

    public static <R, C, V> Builder<R, C, V> builder() {
        return new Builder<>();
    }

    static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
        return Tables.immutableCell(Preconditions.checkNotNull(rowKey, "rowKey"), Preconditions.checkNotNull(columnKey, "columnKey"), Preconditions.checkNotNull(value, "value"));
    }

    @DoNotMock
    /* loaded from: classes.dex */
    public static final class Builder<R, C, V> {
        private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
        @NullableDecl
        private Comparator<? super C> columnComparator;
        @NullableDecl
        private Comparator<? super R> rowComparator;

        public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
            this.rowComparator = (Comparator) Preconditions.checkNotNull(rowComparator, "rowComparator");
            return this;
        }

        public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
            this.columnComparator = (Comparator) Preconditions.checkNotNull(columnComparator, "columnComparator");
            return this;
        }

        public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
            this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
            if (cell instanceof Tables.ImmutableCell) {
                Preconditions.checkNotNull(cell.getRowKey(), "row");
                Preconditions.checkNotNull(cell.getColumnKey(), "column");
                Preconditions.checkNotNull(cell.getValue(), "value");
                this.cells.add(cell);
            } else {
                put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
            return this;
        }

        public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
            for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
                put(cell);
            }
            return this;
        }

        public ImmutableTable<R, C, V> build() {
            int size = this.cells.size();
            if (size == 0) {
                return ImmutableTable.of();
            }
            if (size != 1) {
                return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
            }
            return new SingletonImmutableTable((Table.Cell) Iterables.getOnlyElement(this.cells));
        }
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
        return (ImmutableSet) super.cellSet();
    }

    @Override // com.google.common.collect.AbstractTable
    public final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    @Override // com.google.common.collect.AbstractTable
    final Iterator<V> valuesIterator() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.Table
    public ImmutableMap<R, V> column(C columnKey) {
        Preconditions.checkNotNull(columnKey, "columnKey");
        return (ImmutableMap) MoreObjects.firstNonNull((ImmutableMap) columnMap().get(columnKey), ImmutableMap.of());
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<C> columnKeySet() {
        return columnMap().keySet();
    }

    @Override // com.google.common.collect.Table
    public ImmutableMap<C, V> row(R rowKey) {
        Preconditions.checkNotNull(rowKey, "rowKey");
        return (ImmutableMap) MoreObjects.firstNonNull((ImmutableMap) rowMap().get(rowKey), ImmutableMap.of());
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public ImmutableSet<R> rowKeySet() {
        return rowMap().keySet();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean contains(@NullableDecl Object rowKey, @NullableDecl Object columnKey) {
        return get(rowKey, columnKey) != null;
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    public boolean containsValue(@NullableDecl Object value) {
        return values().contains(value);
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public final V put(R rowKey, C columnKey, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.AbstractTable, com.google.common.collect.Table
    @Deprecated
    public final V remove(Object rowKey, Object columnKey) {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes.dex */
    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID;
        private final int[] cellColumnIndices;
        private final int[] cellRowIndices;
        private final Object[] cellValues;
        private final Object[] columnKeys;
        private final Object[] rowKeys;

        private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
            this.rowKeys = rowKeys;
            this.columnKeys = columnKeys;
            this.cellValues = cellValues;
            this.cellRowIndices = cellRowIndices;
            this.cellColumnIndices = cellColumnIndices;
        }

        public static SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
            return new SerializedForm(table.rowKeySet().toArray(), table.columnKeySet().toArray(), table.values().toArray(), cellRowIndices, cellColumnIndices);
        }

        Object readResolve() {
            Object[] objArr = this.cellValues;
            if (objArr.length == 0) {
                return ImmutableTable.of();
            }
            if (objArr.length == 1) {
                return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], objArr[0]);
            }
            ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<>(objArr.length);
            int i = 0;
            while (true) {
                Object[] objArr2 = this.cellValues;
                if (i >= objArr2.length) {
                    return RegularImmutableTable.forOrderedComponents(cellListBuilder.build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
                }
                cellListBuilder.add((ImmutableList.Builder<Table.Cell<Object, Object, Object>>) ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], objArr2[i]));
                i++;
            }
        }
    }

    final Object writeReplace() {
        return createSerializedForm();
    }
}
