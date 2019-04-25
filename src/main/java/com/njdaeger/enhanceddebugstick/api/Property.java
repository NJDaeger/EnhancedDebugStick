package com.njdaeger.enhanceddebugstick.api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Implementing class of the IProperty class
 * @param <D> BlockData type
 * @param <V> Value type
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Property<D extends BlockData, V> implements IProperty<D, V> {

    private static boolean registered;
    private static int currentOrdinal;
    private static final Map<Material, List<IProperty<?, ?>>> PROPERTIES = new HashMap<>();
    private static final Map<String, IProperty<?, ?>> PROPERTY_FIELD_MAP = new HashMap<>();
    private static final Collection<IProperty<?, ?>> PROPERTY_FIELD_PRE_MAP = new ArrayList<>();

    public static void registerProperties() {
        if (registered) throw new RuntimeException("Cannot re-register properties after they've been registered.");
        registered = true;
        for (Field field : IProperty.class.getFields()) {
            if (field.getType() == IProperty.class) {
                try {
                    PROPERTY_FIELD_MAP.put(field.getName(), (IProperty<?, ?>) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Material mat : Material.values()) {
            if (!mat.isBlock()) continue;
            Set<IProperty<?, ?>> properties = new LinkedHashSet<>();
            BlockData data = mat.createBlockData();
            for (IProperty<?, ?> prop : values()) {
                if (prop.checkBlockData(data)) {
                    //We need custom checks for Multiple facing because blocks dont always use all the faces
                    if (prop.getDataType() == MultipleFacing.class) {
                        MultipleFacing facing = (MultipleFacing) data;
                        for (BlockFace face : facing.getAllowedFaces()) {
                            switch (face) {
                                case NORTH:
                                    properties.add(MULTI_NORTH);
                                    break;
                                case EAST:
                                    properties.add(MULTI_EAST);
                                    break;
                                case SOUTH:
                                    properties.add(MULTI_SOUTH);
                                    break;
                                case WEST:
                                    properties.add(MULTI_WEST);
                                    break;
                                case UP:
                                    properties.add(MULTI_UP);
                                    break;
                                case DOWN:
                                    properties.add(MULTI_DOWN);
                                    break;
                                case NORTH_EAST:
                                    properties.add(MULTI_NORTH_EAST);
                                    break;
                                case NORTH_WEST:
                                    properties.add(MULTI_NORTH_WEST);
                                    break;
                                case SOUTH_EAST:
                                    properties.add(MULTI_SOUTH_EAST);
                                    break;
                                case SOUTH_WEST:
                                    properties.add(MULTI_SOUTH_WEST);
                                    break;
                                case WEST_NORTH_WEST:
                                    properties.add(MULTI_WEST_NORTH_WEST);
                                    break;
                                case NORTH_NORTH_WEST:
                                    properties.add(MULTI_NORTH_NORTH_WEST);
                                    break;
                                case NORTH_NORTH_EAST:
                                    properties.add(MULTI_NORTH_NORTH_EAST);
                                    break;
                                case EAST_NORTH_EAST:
                                    properties.add(MULTI_EAST_NORTH_EAST);
                                    break;
                                case EAST_SOUTH_EAST:
                                    properties.add(MULTI_EAST_SOUTH_EAST);
                                    break;
                                case SOUTH_SOUTH_EAST:
                                    properties.add(MULTI_SOUTH_SOUTH_EAST);
                                    break;
                                case SOUTH_SOUTH_WEST:
                                    properties.add(MULTI_SOUTH_SOUTH_WEST);
                                    break;
                                case WEST_SOUTH_WEST:
                                    properties.add(MULTI_WEST_SOUTH_WEST);
                                    break;
                                case SELF:
                                    properties.add(MULTI_SELF);
                                    break;
                            }
                        }
                    } else properties.add(prop);
                }
            }
            PROPERTIES.put(mat, new ArrayList<>(properties));
        }
    }

    private final Function<D, V> current;
    private final Function<D, D> next;
    private final Class<D> dataType;
    private final String niceName;
    private final int ordinal;

    Property(String niceName, Class<D> dataType, Function<D, D> next, Function<D, V> current) {
        this.ordinal = currentOrdinal++;
        this.niceName = niceName;
        this.dataType = dataType;
        this.current = current;
        this.next = next;
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

    @Override
    public boolean checkBlockData(BlockData block) {
        return dataType.isInstance(block);
    }

    @Override
    public D fromBlockData(Block block) {
        return checkBlockData(block.getBlockData()) ? (D) block.getBlockData() : null;
    }

    @Override
    public D nextBlockData(Block block) {
        return checkBlockData(block.getBlockData()) ? next.apply((D) block.getBlockData()) : null;
    }

    @Override
    public String getNiceCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        BlockData data = fromBlockData(block);
        if (data == null) return "";
        String property = niceName.replace(" ", "_").toLowerCase();
        String split = data.getAsString().split(property + "=")[1];
        return split.substring(0, !split.contains(",") ? split.length() - 1 : split.indexOf(","));
    }

    @Override
    public String getNiceNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        BlockData data = nextBlockData(block);
        if (data == null) return "";
        String property = niceName.replace(" ", "_").toLowerCase();
        String split = data.getAsString().split(property + "=")[1];
        return split.substring(0, !split.contains(",") ? split.length() - 1 : split.indexOf(","));
    }

    @Override
    public V getCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(fromBlockData(block));
    }

    @Override
    public V getNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(nextBlockData(block));
    }

    @Override
    public boolean isApplicableTo(Block block) {
        return isApplicableTo(block.getType());
    }

    @Override
    public boolean isApplicableTo(Material material) {
        if (hasProperties(material)) {
            return getProperties(material).contains(this);
        } else return false;
    }

    static IProperty<?, ?>[] values() {
        return Collections.unmodifiableCollection(PROPERTY_FIELD_MAP.values()).toArray(new IProperty[0]);
    }

    static boolean hasProperties(Block block) {
        return hasProperties(block.getType());
    }

    static boolean hasProperties(Material material) {
        return PROPERTIES.containsKey(material) && !PROPERTIES.get(material).isEmpty();
    }

    static List<IProperty<?, ?>> getProperties(Block block) {
        return getProperties(block.getType());
    }

    static List<IProperty<?, ?>> getProperties(Material material) {
        return !hasProperties(material) ? new ArrayList<>() : PROPERTIES.get(material);
    }

    static Map<Material, List<IProperty<?, ?>>> getMaterialProperties() {
        return Collections.unmodifiableMap(PROPERTIES);
    }

    static <E extends Enum<E>> E nextEnumOption(E current) {
        Class<E> enumeration = current.getDeclaringClass();
        int size = enumeration.getEnumConstants().length;
        if (current.ordinal() == size - 1) return enumeration.getEnumConstants()[0];
        else return enumeration.getEnumConstants()[current.ordinal() + 1];
    }

    static <E extends Enum<E>> E nextEnumOption(E current, Set<E> allowed) {
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
