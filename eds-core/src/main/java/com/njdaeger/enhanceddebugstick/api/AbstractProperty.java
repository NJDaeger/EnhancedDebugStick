package com.njdaeger.enhanceddebugstick.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractProperty<D extends BlockData, V> implements IProperty<D, V> {
    
    protected static boolean registered;
    private static int currentOrdinal;
    protected static final Map<Material, List<IProperty<?, ?>>> PROPERTIES = new HashMap<>();
    protected static final Map<String, IProperty<?, ?>> PROPERTY_FIELD_MAP = new HashMap<>();
    
    private final BiFunction<D, V, D> set;
    private final Function<D, V> current;
    private final Function<D, D> next;
    private final Class<V> valueType;
    private final Class<D> dataType;
    private final String niceName;
    private final int ordinal;
    
    protected AbstractProperty(String niceName, Class<D> dataType, Class<V> valueType, Function<D, D> next, Function<D, V> current, BiFunction<D, V, D> set) {
        this.ordinal = currentOrdinal++;
        this.valueType = valueType;
        this.niceName = niceName;
        this.dataType = dataType;
        this.current = current;
        this.next = next;
        this.set = set;
    }
    
    @Override
    public int ordinal() {
        return ordinal;
    }
    
    @Override
    public String getNiceName() {
        return niceName;
    }
    
    @Override
    public Class<D> getDataType() {
        return dataType;
    }
    
    public Class<V> getValueType() {
        return valueType;
    }
    
    @Override
    public boolean checkBlockData(BlockData block) {
        return dataType.isInstance(block);
    }
    
    @Override
    public D mergeBlockData(BlockData original, BlockData data) {
        if (!isApplicableTo(original) || !isApplicableTo(data)) return null;
        return set.apply((D) data, getCurrentValue(original));
    }
    
    @Override
    public D mergeBlockData(BlockData original, Material material) {
        return mergeBlockData(original, material.createBlockData());
    }
    
    @Override
    public D mergeBlockData(BlockData original, Block block) {
        return mergeBlockData(original, block.getBlockData());
    }
    
    @Override
    public D getBlockData(Block block, Object value) {
        return getBlockData(block.getBlockData(), value);
    }
    
    @Override
    public D getBlockData(BlockData data, Object value) {
        if (!isApplicableTo(data)) throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + data.getMaterial().name());
        if (!getValueType().isInstance(value)) throw new RuntimeException("Value \"" + value + "\" is not the required type of " + getValueType().getTypeName());
        return set.apply((D)data, (V)value);
    }
    
    @Override
    public D getBlockData(Material material, Object value) {
        return getBlockData(material.createBlockData(), value);
    }
    
    @Override
    public String getNiceCurrentValue(BlockData data) {
        if (!isApplicableTo(data)) throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + data.getMaterial().name());
        String property = niceName.replace(" ", "_").toLowerCase();
        String split = data.getAsString().split(property + "=")[1];
        return split.substring(0, !split.contains(",") ? split.length() - 1 : split.indexOf(","));
    }
    
    @Override
    public String getNiceCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return getNiceCurrentValue(currentBlockData(block));
    }
    
    @Override
    public V getCurrentValue(BlockData data) {
        if (!isApplicableTo(data))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + data.getMaterial().name());
        return current.apply(currentBlockData(data));
    }
    
    @Override
    public V getCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(currentBlockData(block));
    }
    
    @Override
    public D currentBlockData(BlockData data) {
        return checkBlockData(data) ? (D) data : null;
    }
    
    @Override
    public D currentBlockData(Block block) {
        return currentBlockData(block.getBlockData());
    }
    
    @Override
    public String getNiceNextValue(BlockData data) {
        if (!isApplicableTo(data))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + data.getMaterial().name());
        return getNiceCurrentValue(nextBlockData(data));
    }
    
    @Override
    public String getNiceNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return getNiceCurrentValue(nextBlockData(block));
    }
    
    @Override
    public V getNextValue(BlockData data) {
        if (!isApplicableTo(data))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + data.getMaterial().name());
        return current.apply(nextBlockData(data));
    }
    
    @Override
    public V getNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(nextBlockData(block));
    }
    
    @Override
    public D nextBlockData(BlockData data) {
        return checkBlockData(data) ? next.apply((D) data) : null;
    }
    
    @Override
    public D nextBlockData(Block block) {
        return nextBlockData(block.getBlockData());
    }
    
    @Override
    public boolean isApplicableTo(BlockData data) {
        return isApplicableTo(data.getMaterial());
    }
    
    @Override
    public boolean isApplicableTo(Material material) {
        if (hasProperties(material)) {
            return getProperties(material).contains(this);
        } else return false;
    }
    
    @Override
    public boolean isApplicableTo(Block block) {
        return isApplicableTo(block.getType());
    }
    
    protected static IProperty<?, ?>[] values() {
        return Collections.unmodifiableCollection(PROPERTY_FIELD_MAP.values()).toArray(new IProperty[0]);
    }
    
    protected static boolean hasProperties(Block block) {
        return hasProperties(block.getType());
    }
    
    protected static boolean hasProperties(Material material) {
        return PROPERTIES.containsKey(material) && !PROPERTIES.get(material).isEmpty();
    }
    
    protected static boolean hasProperties(BlockData data) {
        return hasProperties(data.getMaterial());
    }
    
    protected static List<IProperty<?, ?>> getProperties(Block block) {
        return getProperties(block.getType());
    }
    
    protected static List<IProperty<?, ?>> getProperties(Material material) {
        return !hasProperties(material) ? new ArrayList<>() : PROPERTIES.get(material);
    }
    
    protected static List<IProperty<?, ?>> getProperties(BlockData data) {
        return getProperties(data.getMaterial());
    }
    
    protected static Map<Material, List<IProperty<?, ?>>> getMaterialProperties() {
        return Collections.unmodifiableMap(PROPERTIES);
    }
    
    protected static <E extends Enum<E>> E nextEnumOption(E current) {
        Class<E> enumeration = current.getDeclaringClass();
        int size = enumeration.getEnumConstants().length;
        if (current.ordinal() == size - 1) return enumeration.getEnumConstants()[0];
        else return enumeration.getEnumConstants()[current.ordinal() + 1];
    }
    
    protected static <E extends Enum<E>> E nextEnumOption(E current, Set<E> allowed) {
        Class<E> enumeration = current.getDeclaringClass();
        int size = enumeration.getEnumConstants().length;
        E next;
        int ordinal = current.ordinal();
        if (ordinal == size - 1) next = enumeration.getEnumConstants()[0];
        else next = enumeration.getEnumConstants()[ordinal + 1];
        while (!allowed.contains(next)) {
            ordinal++;
            if (ordinal == size) ordinal = 0;
            next = enumeration.getEnumConstants()[ordinal];
        }
        return next;
    }
    
}
