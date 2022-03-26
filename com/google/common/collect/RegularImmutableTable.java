package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    abstract Table.Cell<R, C, V> getCell(int i);

    abstract V getValue(int i);

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    public final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    /* loaded from: classes.dex */
    public final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
        private CellSet() {
            RegularImmutableTable.this = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return RegularImmutableTable.this.size();
        }

        @Override // com.google.common.collect.IndexedImmutableSet
        public Table.Cell<R, C, V> get(int index) {
            return RegularImmutableTable.this.getCell(index);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@NullableDecl Object object) {
            if (!(object instanceof Table.Cell)) {
                return false;
            }
            Table.Cell<?, ?, ?> cell = (Table.Cell) object;
            Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            if (value == null || !value.equals(cell.getValue())) {
                return false;
            }
            return true;
        }

        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return false;
        }
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    public final ImmutableCollection<V> createValues() {
        return isEmpty() ? ImmutableList.of() : new Values();
    }

    /* loaded from: classes.dex */
    public final class Values extends ImmutableList<V> {
        private Values() {
            RegularImmutableTable.this = r1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return RegularImmutableTable.this.size();
        }

        @Override // java.util.List
        public V get(int index) {
            return (V) RegularImmutableTable.this.getValue(index);
        }

        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }
    }

    public static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @NullableDecl final Comparator<? super R> rowComparator, @NullableDecl final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (!(rowComparator == null && columnComparator == null)) {
            Collections.sort(cells, new Comparator<Table.Cell<R, C, V>>() { // from class: com.google.common.collect.RegularImmutableTable.1
                @Override // java.util.Comparator
                public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                    return compare((Table.Cell) ((Table.Cell) obj), (Table.Cell) ((Table.Cell) obj2));
                }

                public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
                    int rowCompare;
                    Comparator comparator = rowComparator;
                    if (comparator == null) {
                        rowCompare = 0;
                    } else {
                        rowCompare = comparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    }
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    Comparator comparator2 = columnComparator;
                    if (comparator2 == null) {
                        return 0;
                    }
                    return comparator2.compare(cell1.getColumnKey(), cell2.getColumnKey());
                }
            });
        }
        return forCellsInternal(cells, rowComparator, columnComparator);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
        return forCellsInternal(cells, null, null);
    }

    private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @NullableDecl Comparator<? super R> rowComparator, @NullableDecl Comparator<? super C> columnComparator) {
        ImmutableSet<R> rowSpace;
        ImmutableSet<C> columnSpace;
        Set<R> rowSpaceBuilder = new LinkedHashSet<>();
        Set<C> columnSpaceBuilder = new LinkedHashSet<>();
        ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
        for (Table.Cell<R, C, V> cell : cells) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        if (rowComparator == null) {
            rowSpace = ImmutableSet.copyOf((Collection) rowSpaceBuilder);
        } else {
            rowSpace = ImmutableSet.copyOf((Collection) ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
        }
        if (columnComparator == null) {
            columnSpace = ImmutableSet.copyOf((Collection) columnSpaceBuilder);
        } else {
            columnSpace = ImmutableSet.copyOf((Collection) ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
        }
        return forOrderedComponents(cellList, rowSpace, columnSpace);
    }

    public static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        return ((long) cellList.size()) > (((long) rowSpace.size()) * ((long) columnSpace.size())) / 2 ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
    }

    final void checkNoDuplicate(R rowKey, C columnKey, V existingValue, V newValue) {
        Preconditions.checkArgument(existingValue == null, "Duplicate key: (row=%s, column=%s), values: [%s, %s].", rowKey, columnKey, newValue, existingValue);
    }
}
