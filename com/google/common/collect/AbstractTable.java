package com.google.common.collect;

import com.google.common.collect.Table;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractTable<R, C, V> implements Table<R, C, V> {
    @NullableDecl
    @LazyInit
    private transient Set<Table.Cell<R, C, V>> cellSet;
    @NullableDecl
    @LazyInit
    private transient Collection<V> values;

    abstract Iterator<Table.Cell<R, C, V>> cellIterator();

    @Override // com.google.common.collect.Table
    public boolean containsRow(@NullableDecl Object rowKey) {
        return Maps.safeContainsKey(rowMap(), rowKey);
    }

    @Override // com.google.common.collect.Table
    public boolean containsColumn(@NullableDecl Object columnKey) {
        return Maps.safeContainsKey(columnMap(), columnKey);
    }

    @Override // com.google.common.collect.Table
    public Set<R> rowKeySet() {
        return rowMap().keySet();
    }

    @Override // com.google.common.collect.Table
    public Set<C> columnKeySet() {
        return columnMap().keySet();
    }

    @Override // com.google.common.collect.Table
    public boolean containsValue(@NullableDecl Object value) {
        for (Map<C, V> row : rowMap().values()) {
            if (row.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Table
    public boolean contains(@NullableDecl Object rowKey, @NullableDecl Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        return row != null && Maps.safeContainsKey(row, columnKey);
    }

    @Override // com.google.common.collect.Table
    public V get(@NullableDecl Object rowKey, @NullableDecl Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        if (row == null) {
            return null;
        }
        return (V) Maps.safeGet(row, columnKey);
    }

    @Override // com.google.common.collect.Table
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // com.google.common.collect.Table
    public void clear() {
        Iterators.clear(cellSet().iterator());
    }

    @Override // com.google.common.collect.Table
    public V remove(@NullableDecl Object rowKey, @NullableDecl Object columnKey) {
        Map<C, V> row = (Map) Maps.safeGet(rowMap(), rowKey);
        if (row == null) {
            return null;
        }
        return (V) Maps.safeRemove(row, columnKey);
    }

    @Override // com.google.common.collect.Table
    public V put(R rowKey, C columnKey, V value) {
        return row(rowKey).put(columnKey, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Table
    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    @Override // com.google.common.collect.Table
    public Set<Table.Cell<R, C, V>> cellSet() {
        Set<Table.Cell<R, C, V>> result = this.cellSet;
        if (result != null) {
            return result;
        }
        Set<Table.Cell<R, C, V>> createCellSet = createCellSet();
        this.cellSet = createCellSet;
        return createCellSet;
    }

    Set<Table.Cell<R, C, V>> createCellSet() {
        return new CellSet();
    }

    /* loaded from: classes.dex */
    public class CellSet extends AbstractSet<Table.Cell<R, C, V>> {
        CellSet() {
            AbstractTable.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Table.Cell)) {
                return false;
            }
            Table.Cell<?, ?, ?> cell = (Table.Cell) o;
            Map<C, V> row = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (row == null || !Collections2.safeContains(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()))) {
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@NullableDecl Object o) {
            if (!(o instanceof Table.Cell)) {
                return false;
            }
            Table.Cell<?, ?, ?> cell = (Table.Cell) o;
            Map<C, V> row = (Map) Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
            if (row == null || !Collections2.safeRemove(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()))) {
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractTable.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
        public Iterator<Table.Cell<R, C, V>> iterator() {
            return AbstractTable.this.cellIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractTable.this.size();
        }
    }

    @Override // com.google.common.collect.Table
    public Collection<V> values() {
        Collection<V> result = this.values;
        if (result != null) {
            return result;
        }
        Collection<V> createValues = createValues();
        this.values = createValues;
        return createValues;
    }

    Collection<V> createValues() {
        return new Values();
    }

    Iterator<V> valuesIterator() {
        return new TransformedIterator<Table.Cell<R, C, V>, V>(cellSet().iterator()) { // from class: com.google.common.collect.AbstractTable.1
            @Override // com.google.common.collect.TransformedIterator
            /* bridge */ /* synthetic */ Object transform(Object obj) {
                return transform((Table.Cell<R, C, Object>) ((Table.Cell) obj));
            }

            V transform(Table.Cell<R, C, V> cell) {
                return cell.getValue();
            }
        };
    }

    /* loaded from: classes.dex */
    public class Values extends AbstractCollection<V> {
        Values() {
            AbstractTable.this = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return AbstractTable.this.valuesIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object o) {
            return AbstractTable.this.containsValue(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractTable.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractTable.this.size();
        }
    }

    @Override // com.google.common.collect.Table
    public boolean equals(@NullableDecl Object obj) {
        return Tables.equalsImpl(this, obj);
    }

    @Override // com.google.common.collect.Table
    public int hashCode() {
        return cellSet().hashCode();
    }

    @Override // java.lang.Object
    public String toString() {
        return rowMap().toString();
    }
}
