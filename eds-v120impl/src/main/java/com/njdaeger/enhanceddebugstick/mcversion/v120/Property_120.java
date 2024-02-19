package com.njdaeger.enhanceddebugstick.mcversion.v120;

import com.njdaeger.enhanceddebugstick.api.AbstractProperty;
import com.njdaeger.enhanceddebugstick.api.IProperty;
import org.bukkit.Axis;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Attachable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Brushable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Hangable;
import org.bukkit.block.data.Hatchable;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.CalibratedSculkSensor;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.ChiseledBookshelf;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.PinkPetals;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.block.data.type.SculkCatalyst;
import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.block.data.type.Wall;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.bukkit.block.BlockFace.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Property_120<D extends BlockData, V> extends AbstractProperty<D, V> {
    
    /**
     * Age property
     */
    static IProperty<Ageable, Integer> AGE = new Property_120<>("Age", Ageable.class, Integer.class, (age) -> {
        age.setAge(age.getMaximumAge() == age.getAge() ? 0 : age.getAge() + 1);
        return age;
    }, Ageable::getAge, (age, value) -> {
        age.setAge(value);
        return age;
    });
    /**
     * Power property
     */
    static IProperty<AnaloguePowerable, Integer> POWER = new Property_120<>("Power", AnaloguePowerable.class, Integer.class, (power) -> {
        power.setPower(power.getMaximumPower() == power.getPower() ? 0 : power.getPower() + 1);
        return power;
    }, AnaloguePowerable::getPower, (power, value) -> {
        power.setPower(value);
        return power;
    });
    /**
     * Attached property
     */
    static IProperty<Attachable, Boolean> ATTACHED = new Property_120<>("Attached", Attachable.class, Boolean.class, (attachable) -> {
        attachable.setAttached(!attachable.isAttached());
        return attachable;
    }, Attachable::isAttached, ((attachable, value) -> {
        attachable.setAttached(value);
        return attachable;
    }));
    /**
     * Half property
     */
    static IProperty<Bisected, Bisected.Half> HALF = new Property_120<>("Half", Bisected.class, Bisected.Half.class, (bisected) -> {
        bisected.setHalf(nextEnumOption(bisected.getHalf()));
        return bisected;
    }, Bisected::getHalf, (bisected, value) -> {
        bisected.setHalf(value);
        return bisected;
    });
    /**
     * Facing property
     */
    static IProperty<Directional, BlockFace> FACING = new Property_120<>("Facing", Directional.class, BlockFace.class, (directional) -> {
        directional.setFacing(nextEnumOption(directional.getFacing(), directional.getFaces()));
        return directional;
    }, Directional::getFacing, (directional, value) -> {
        directional.setFacing(value);
        return directional;
    });
    /**
     * Level property
     */
    static IProperty<Levelled, Integer> LEVEL = new Property_120<>("Level", Levelled.class, Integer.class, (level) -> {
        level.setLevel(level.getMaximumLevel() == level.getLevel() ? 1 : level.getLevel() + 1);
        return level;
    }, Levelled::getLevel, (level, value) -> {
        level.setLevel(value);
        return level;
    });
    /**
     * Lit property
     */
    static IProperty<Lightable, Boolean> LIT = new Property_120<>("Lit", Lightable.class, Boolean.class, (lit) -> {
        lit.setLit(!lit.isLit());
        return lit;
    }, Lightable::isLit, (lit, value) -> {
        lit.setLit(value);
        return lit;
    });
    /**
     * Open property
     */
    static IProperty<Openable, Boolean> OPEN = new Property_120<>("Open", Openable.class, Boolean.class, (open) -> {
        open.setOpen(!open.isOpen());
        return open;
    }, Openable::isOpen, (open, value) -> {
        open.setOpen(value);
        return open;
    });
    /**
     * Axis property
     */
    static IProperty<Orientable, Axis> AXIS = new Property_120<>("Axis", Orientable.class, Axis.class, (axis) -> {
        axis.setAxis(nextEnumOption(axis.getAxis(), axis.getAxes()));
        return axis;
    }, Orientable::getAxis, (axis, value) -> {
        axis.setAxis(value);
        return axis;
    });
    /**
     * Powered property
     */
    static IProperty<Powerable, Boolean> POWERED = new Property_120<>("Powered", Powerable.class, Boolean.class, (powered) -> {
        powered.setPowered(!powered.isPowered());
        return powered;
    }, Powerable::isPowered, (powered, value) -> {
        powered.setPowered(value);
        return powered;
    });
    /**
     * Rotation property
     */
    static IProperty<Rotatable, BlockFace> ROTATION = new Property_120<>("Rotation", Rotatable.class, BlockFace.class, (rotation) -> {
        BlockFace face = rotation.getRotation();
        int x = 0;
        while (x < BlockFace.values().length) {
            try {
                face = switch (face) {
                    case NORTH -> NORTH_NORTH_EAST;
                    case NORTH_NORTH_EAST -> NORTH_EAST;
                    case NORTH_EAST -> EAST_NORTH_EAST;
                    case EAST_NORTH_EAST -> EAST;
                    case EAST -> EAST_SOUTH_EAST;
                    case EAST_SOUTH_EAST -> SOUTH_EAST;
                    case SOUTH_EAST -> SOUTH_SOUTH_EAST;
                    case SOUTH_SOUTH_EAST -> SOUTH;
                    case SOUTH -> SOUTH_SOUTH_WEST;
                    case SOUTH_SOUTH_WEST -> SOUTH_WEST;
                    case SOUTH_WEST -> WEST_SOUTH_WEST;
                    case WEST_SOUTH_WEST -> WEST;
                    case WEST -> WEST_NORTH_WEST;
                    case WEST_NORTH_WEST -> NORTH_WEST;
                    case NORTH_WEST -> NORTH_NORTH_WEST;
                    case NORTH_NORTH_WEST -> UP;
                    case UP -> DOWN;
                    case DOWN -> SELF;
                    case SELF -> NORTH;
                };
                x++;
                rotation.setRotation(face);
                break;
            }
            catch (IllegalArgumentException ignored) {
            }
        }
        return rotation;
    }, Rotatable::getRotation, (rotation, value) -> {
        rotation.setRotation(value);
        return rotation;
    });
    /**
     * Snowy property
     */
    static IProperty<Snowable, Boolean> SNOWY = new Property_120<>("Snowy", Snowable.class, Boolean.class, (snowable) -> {
        snowable.setSnowy(!snowable.isSnowy());
        return snowable;
    }, Snowable::isSnowy, (snowable, value) -> {
        snowable.setSnowy(value);
        return snowable;
    });
    /**
     * Waterlogged property
     */
    static IProperty<Waterlogged, Boolean> WATERLOGGED = new Property_120<>("Waterlogged", Waterlogged.class, Boolean.class, (waterlogged) -> {
        waterlogged.setWaterlogged(!waterlogged.isWaterlogged());
        return waterlogged;
    }, Waterlogged::isWaterlogged, (waterlogged, value) -> {
        waterlogged.setWaterlogged(value);
        return waterlogged;
    });
    
    
    /**
     * East connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_EAST = new Property_120<>("East", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(EAST, nextEnumOption(wire.getFace(EAST)));
        return wire;
    }, (wire) -> wire.getFace(EAST), (wire, value) -> {
        wire.setFace(EAST, value);
        return wire;
    });
    /**
     * North connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_NORTH = new Property_120<>("North", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(NORTH, nextEnumOption(wire.getFace(NORTH)));
        return wire;
    }, (wire) -> wire.getFace(NORTH), (wire, value) -> {
        wire.setFace(NORTH, value);
        return wire;
    });
    /**
     * South connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_SOUTH = new Property_120<>("South", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(SOUTH, nextEnumOption(wire.getFace(SOUTH)));
        return wire;
    }, (wire) -> wire.getFace(SOUTH), (wire, value) -> {
        wire.setFace(SOUTH, value);
        return wire;
    });
    /**
     * West connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_WEST = new Property_120<>("West", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(WEST, nextEnumOption(wire.getFace(WEST)));
        return wire;
    }, (wire) -> wire.getFace(WEST), (wire, value) -> {
        wire.setFace(WEST, value);
        return wire;
    });
    /**
     * Inverted property. (Daylight Detector)
     */
    static IProperty<DaylightDetector, Boolean> DETECTOR_INVERTED = new Property_120<>("Inverted", DaylightDetector.class, Boolean.class, (detector) -> {
        detector.setInverted(!detector.isInverted());
        return detector;
    }, DaylightDetector::isInverted, (detector, value) -> {
        detector.setInverted(value);
        return detector;
    });
    /**
     * Disarmed property. (Tripwire)
     */
    static IProperty<Tripwire, Boolean> TRIPWIRE_DISARMED = new Property_120<>("Disarmed", Tripwire.class, Boolean.class, (tripwire) -> {
        tripwire.setDisarmed(!tripwire.isDisarmed());
        return tripwire;
    }, Tripwire::isDisarmed, (tripwire, value) -> {
        tripwire.setDisarmed(value);
        return tripwire;
    });
    /**
     * Hinge property. (Door)
     */
    static IProperty<Door, Door.Hinge> DOOR_HINGE = new Property_120<>("Hinge", Door.class, Door.Hinge.class, (door) -> {
        door.setHinge(nextEnumOption(door.getHinge()));
        return door;
    }, Door::getHinge, (door, value) -> {
        door.setHinge(value);
        return door;
    });
    /**
     * Shape property. (Stairs)
     */
    static IProperty<Stairs, Stairs.Shape> STAIR_SHAPE = new Property_120<>("Shape", Stairs.class, Stairs.Shape.class, (stairs) -> {
        stairs.setShape(nextEnumOption(stairs.getShape()));
        return stairs;
    }, Stairs::getShape, (shape, value) -> {
        shape.setShape(value);
        return shape;
    });
    /**
     * Part property. (Bed)
     */
    static IProperty<Bed, Bed.Part> BED_PART = new Property_120<>("Part", Bed.class, Bed.Part.class, (bed) -> {
        bed.setPart(nextEnumOption(bed.getPart()));
        return bed;
    }, Bed::getPart, (bed, value) -> {
        bed.setPart(value);
        return bed;
    });
    /**
     * Type property. (Chest)
     */
    static IProperty<Chest, Chest.Type> CHEST_TYPE = new Property_120<>("Type", Chest.class, Chest.Type.class, (chest) -> {
        chest.setType(nextEnumOption(chest.getType()));
        return chest;
    }, Chest::getType, (chest, value) -> {
        chest.setType(value);
        return chest;
    });
    /**
     * Conditional property. (Command Block)
     */
    static IProperty<CommandBlock, Boolean> COMMAND_CONDITIONAL = new Property_120<>("Conditional", CommandBlock.class, Boolean.class, (commandBlock) -> {
        commandBlock.setConditional(!commandBlock.isConditional());
        return commandBlock;
    }, CommandBlock::isConditional, (commandBlock, value) -> {
        commandBlock.setConditional(value);
        return commandBlock;
    });
    /**
     * Mode property. (Comparator)
     */
    static IProperty<Comparator, Comparator.Mode> COMPARATOR_MODE = new Property_120<>("Mode", Comparator.class, Comparator.Mode.class, (comparator) -> {
        comparator.setMode(nextEnumOption(comparator.getMode()));
        return comparator;
    }, Comparator::getMode, (comparator, value) -> {
        comparator.setMode(value);
        return comparator;
    });
    /**
     * Triggered property. (Dispenser)
     */
    static IProperty<Dispenser, Boolean> DISPENSER_TRIGGERED = new Property_120<>("Triggered", Dispenser.class, Boolean.class, (dispenser) -> {
        dispenser.setTriggered(!dispenser.isTriggered());
        return dispenser;
    }, Dispenser::isTriggered, (dispenser, value) -> {
        dispenser.setTriggered(value);
        return dispenser;
    });
    /**
     * Eye property. (End portal frame)
     */
    static IProperty<EndPortalFrame, Boolean> END_PORTAL_EYE = new Property_120<>("Eye", EndPortalFrame.class, Boolean.class, (frame) -> {
        frame.setEye(!frame.hasEye());
        return frame;
    }, EndPortalFrame::hasEye, (frame, value) -> {
        frame.setEye(value);
        return frame;
    });
    /**
     * In_wall property. (Gate)
     */
    static IProperty<Gate, Boolean> GATE_IN_WALL = new Property_120<>("In wall", Gate.class, Boolean.class, (gate) -> {
        gate.setInWall(!gate.isInWall());
        return gate;
    }, Gate::isInWall, (gate, value) -> {
        gate.setInWall(value);
        return gate;
    });
    /**
     * Enabled property. (Hopper)
     */
    static IProperty<Hopper, Boolean> HOPPER_ENABLED = new Property_120<>("Enabled", Hopper.class, Boolean.class, (hopper) -> {
        hopper.setEnabled(!hopper.isEnabled());
        return hopper;
    }, Hopper::isEnabled, (hopper, value) -> {
        hopper.setEnabled(value);
        return hopper;
    });
    /**
     * Extended property. (Piston)
     */
    static IProperty<Piston, Boolean> PISTON_EXTENDED = new Property_120<>("Extended", Piston.class, Boolean.class, (piston) -> {
        piston.setExtended(!piston.isExtended());
        return piston;
    }, Piston::isExtended, (piston, value) -> {
        piston.setExtended(value);
        return piston;
    });
    /**
     * Short property. (Piston head)
     */
    static IProperty<PistonHead, Boolean> PISTON_HEAD_SHORT = new Property_120<>("Short", PistonHead.class, Boolean.class, (pistonHead) -> {
        pistonHead.setShort(!pistonHead.isShort());
        return pistonHead;
    }, PistonHead::isShort, (pistonHead, value) -> {
        pistonHead.setShort(value);
        return pistonHead;
    });
    /**
     * Delay property. (Repeater)
     */
    static IProperty<Repeater, Integer> REPEATER_DELAY = new Property_120<>("Delay", Repeater.class, Integer.class, (repeater) -> {
        repeater.setDelay(repeater.getMaximumDelay() == repeater.getDelay() ? repeater.getMinimumDelay() : repeater.getDelay() + 1);
        return repeater;
    }, Repeater::getDelay, (repeater, value) -> {
        repeater.setDelay(value);
        return repeater;
    });
    /**
     * Locked property. (Repeater)
     */
    static IProperty<Repeater, Boolean> REPEATER_LOCKED = new Property_120<>("Locked", Repeater.class, Boolean.class, (repeater) -> {
        repeater.setLocked(!repeater.isLocked());
        return repeater;
    }, Repeater::isLocked, (repeater, value) -> {
        repeater.setLocked(value);
        return repeater;
    });
    /**
     * Type property. (Technical Piston)
     */
    static IProperty<TechnicalPiston, TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = new Property_120<>("Type", TechnicalPiston.class, TechnicalPiston.Type.class, (technicalPiston) -> {
        technicalPiston.setType(nextEnumOption(technicalPiston.getType()));
        return technicalPiston;
    }, TechnicalPiston::getType, (technicalPiston, value) -> {
        technicalPiston.setType(value);
        return technicalPiston;
    });
    /**
     * North property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH = new Property_120<>("North", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH, !multipleFacing.hasFace(NORTH));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH, value);
        return multipleFacing;
    });
    /**
     * East property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST = new Property_120<>("East", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST, !multipleFacing.hasFace(EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST, value);
        return multipleFacing;
    });
    /**
     * South property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH = new Property_120<>("South", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH, !multipleFacing.hasFace(SOUTH));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH, value);
        return multipleFacing;
    });
    /**
     * West property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST = new Property_120<>("West", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST, !multipleFacing.hasFace(WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST, value);
        return multipleFacing;
    });
    /**
     * Up property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_UP = new Property_120<>("Up", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(UP, !multipleFacing.hasFace(UP));
        return multipleFacing;
    }, (face) -> face.hasFace(UP), (multipleFacing, value) -> {
        multipleFacing.setFace(UP, value);
        return multipleFacing;
    });
    /**
     * Down property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_DOWN = new Property_120<>("Down", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(DOWN, !multipleFacing.hasFace(DOWN));
        return multipleFacing;
    }, (face) -> face.hasFace(DOWN), (multipleFacing, value) -> {
        multipleFacing.setFace(DOWN, value);
        return multipleFacing;
    });
    /**
     * Northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_EAST = new Property_120<>("Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_EAST, !multipleFacing.hasFace(NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_WEST = new Property_120<>("Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_WEST, !multipleFacing.hasFace(NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_EAST = new Property_120<>("Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_EAST, !multipleFacing.hasFace(SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_WEST = new Property_120<>("Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_WEST, !multipleFacing.hasFace(SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST_NORTH_WEST = new Property_120<>("West Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST_NORTH_WEST, !multipleFacing.hasFace(WEST_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_WEST = new Property_120<>("North Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_NORTH_WEST, !multipleFacing.hasFace(NORTH_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_EAST = new Property_120<>("North Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_NORTH_EAST, !multipleFacing.hasFace(NORTH_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST_NORTH_EAST = new Property_120<>("East Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST_NORTH_EAST, !multipleFacing.hasFace(EAST_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST_SOUTH_EAST = new Property_120<>("East Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST_SOUTH_EAST, !multipleFacing.hasFace(EAST_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_EAST = new Property_120<>("South Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_SOUTH_EAST, !multipleFacing.hasFace(SOUTH_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_WEST = new Property_120<>("South Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_SOUTH_WEST, !multipleFacing.hasFace(SOUTH_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST_SOUTH_WEST = new Property_120<>("West Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST_SOUTH_WEST, !multipleFacing.hasFace(WEST_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Self property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SELF = new Property_120<>("Self", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SELF, !multipleFacing.hasFace(SELF));
        return multipleFacing;
    }, (face) -> face.hasFace(SELF), (multipleFacing, value) -> {
        multipleFacing.setFace(SELF, value);
        return multipleFacing;
    });
    /**
     * Instrument property. (Noteblock)
     */
    static IProperty<NoteBlock, Instrument> NOTEBLOCK_INSTRUMENT = new Property_120<>("Instrument", NoteBlock.class, Instrument.class, (noteblock) -> {
        noteblock.setInstrument(nextEnumOption(noteblock.getInstrument()));
        return noteblock;
    }, NoteBlock::getInstrument, (noteblock, value) -> {
        noteblock.setInstrument(value);
        return noteblock;
    });
    /**
     * Note property. (Noteblock)
     */
    static IProperty<NoteBlock, Note> NOTEBLOCK_NOTE = new Property_120<>("Note", NoteBlock.class, Note.class, (noteblock) -> {
        int id = noteblock.getNote().getId();
        noteblock.setNote(new Note(id == 24 ? 0 : id + 1));
        return noteblock;
    }, NoteBlock::getNote, (noteblock, value) -> {
        noteblock.setNote(value);
        return noteblock;
    });
    /**
     * Shape property. (Rail)
     */
    static IProperty<Rail, Rail.Shape> RAIL_SHAPE = new Property_120<>("Shape", Rail.class, Rail.Shape.class, (rail) -> {
        rail.setShape(nextEnumOption(rail.getShape(), rail.getShapes()));
        return rail;
    }, Rail::getShape, (rail, value) -> {
        rail.setShape(value);
        return rail;
    });
    /**
     * Pickles property. (Sea pickles)
     */
    static IProperty<SeaPickle, Integer> SEA_PICKLE_PICKLES = new Property_120<>("Pickles", SeaPickle.class, Integer.class, (pickle) -> {
        pickle.setPickles(pickle.getMaximumPickles() == pickle.getPickles() ? pickle.getMinimumPickles() : pickle.getPickles() + 1);
        return pickle;
    }, SeaPickle::getPickles, (pickle, value) -> {
        pickle.setPickles(value);
        return pickle;
    });
    /**
     * Type property. (Slab)
     */
    static IProperty<Slab, Slab.Type> SLAB_TYPE = new Property_120<>("Type", Slab.class, Slab.Type.class, (slab) -> {
        slab.setType(nextEnumOption(slab.getType()));
        return slab;
    }, Slab::getType, (slab, value) -> {
        slab.setType(value);
        return slab;
    });
    /**
     * Bottle_0 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_1 = new Property_120<>("Bottle 0", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(0, !stand.hasBottle(0));
        return stand;
    }, (bottle) -> bottle.hasBottle(0), (stand, value) -> {
        stand.setBottle(0, value);
        return stand;
    });
    /**
     * Bottle_1 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_2 = new Property_120<>("Bottle 1", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(1, !stand.hasBottle(1));
        return stand;
    }, (bottle) -> bottle.hasBottle(1), (stand, value) -> {
        stand.setBottle(1, value);
        return stand;
    });
    /**
     * Bottle_2 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_3 = new Property_120<>("Bottle 2", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(2, !stand.hasBottle(2));
        return stand;
    }, (bottle) -> bottle.hasBottle(2), (stand, value) -> {
        stand.setBottle(2, value);
        return stand;
    });
    /**
     * Drag property. (Bubble column)
     */
    static IProperty<BubbleColumn, Boolean> BUBBLE_COLUMN_DRAG = new Property_120<>("Drag", BubbleColumn.class, Boolean.class, (column) -> {
        column.setDrag(!column.isDrag());
        return column;
    }, BubbleColumn::isDrag, (column, value) -> {
        column.setDrag(value);
        return column;
    });
    /**
     * Bites property. (Cake)
     */
    static IProperty<Cake, Integer> CAKE_BITES = new Property_120<>("Bites", Cake.class, Integer.class, (cake) -> {
        cake.setBites(cake.getMaximumBites() == cake.getBites() ? 0 : cake.getBites() + 1);
        return cake;
    }, Cake::getBites, (cake, value) -> {
        cake.setBites(value);
        return cake;
    });
    /**
     * Moisture property. (Farmland)
     */
    static IProperty<Farmland, Integer> FARMLAND_MOISTURE = new Property_120<>("Moisture", Farmland.class, Integer.class, (farmland) -> {
        farmland.setMoisture(farmland.getMaximumMoisture() == farmland.getMoisture() ? 0 : farmland.getMoisture() + 1);
        return farmland;
    }, Farmland::getMoisture, (farmland, value) -> {
        farmland.setMoisture(value);
        return farmland;
    });
    /**
     * Distance property. (Leaves)
     */
    static IProperty<Leaves, Integer> LEAVES_DISTANCE = new Property_120<>("Distance", Leaves.class, Integer.class, (leaves) -> {
        leaves.setDistance(7 == leaves.getDistance() ? 1 : leaves.getDistance() + 1);
        return leaves;
    }, Leaves::getDistance, (leaves, value) -> {
        leaves.setDistance(value);
        return leaves;
    });
    /**
     * Persistent property. (Leaves)
     */
    static IProperty<Leaves, Boolean> LEAVES_PERSISTENT = new Property_120<>("Persistent", Leaves.class, Boolean.class, (leaves) -> {
        leaves.setPersistent(!leaves.isPersistent());
        return leaves;
    }, Leaves::isPersistent, (leaves, value) -> {
        leaves.setPersistent(value);
        return leaves;
    });
    /**
     * Stage property. (Sapling)
     */
    static IProperty<Sapling, Integer> SAPLING_STAGE = new Property_120<>("Stage", Sapling.class, Integer.class, (sapling) -> {
        sapling.setStage(sapling.getMaximumStage() == sapling.getStage() ? 0 : sapling.getStage() + 1);
        return sapling;
    }, Sapling::getStage, (sapling, value) -> {
        sapling.setStage(value);
        return sapling;
    });
    /**
     * Layers property. (Snow)
     */
    static IProperty<Snow, Integer> SNOW_LAYERS = new Property_120<>("Layers", Snow.class, Integer.class, (snow) -> {
        snow.setLayers(snow.getMaximumLayers() == snow.getLayers() ? snow.getMinimumLayers() : snow.getLayers() + 1);
        return snow;
    }, Snow::getLayers, (snow, value) -> {
        snow.setLayers(value);
        return snow;
    });
    /**
     * Mode property. (Structure block)
     */
    static IProperty<StructureBlock, StructureBlock.Mode> STRUCTURE_BLOCK_MODE = new Property_120<>("Mode", StructureBlock.class, StructureBlock.Mode.class, (structureBlock) -> {
        structureBlock.setMode(nextEnumOption(structureBlock.getMode()));
        return structureBlock;
    }, StructureBlock::getMode, ((structureBlock, value) -> {
        structureBlock.setMode(value);
        return structureBlock;
    }));
    /**
     * Unstable property. (TNT)
     */
    static IProperty<TNT, Boolean> TNT_UNSTABLE = new Property_120<>("Unstable", TNT.class, Boolean.class, (tnt) -> {
        tnt.setUnstable(!tnt.isUnstable());
        return tnt;
    }, TNT::isUnstable, (tnt, value) -> {
        tnt.setUnstable(value);
        return tnt;
    });
    // removed in 1.20
//    /**
//     * Hatch property. (Turtle eggs)
//     */
//    static IProperty<TurtleEgg, Integer> TURTLE_EGG_HATCH = new Property_120<>("Hatch", TurtleEgg.class, Integer.class, (egg) -> {
//        egg.setHatch(egg.getMaximumHatch() == egg.getHatch() ? 0 : egg.getHatch() + 1);
//        return egg;
//    }, TurtleEgg::getHatch, (egg, value) -> {
//        egg.setHatch(value);
//        return egg;
//    });
    /**
     * Eggs property. (Turtle eggs)
     */
    static IProperty<TurtleEgg, Integer> TURTLE_EGG_EGGS = new Property_120<>("Eggs", TurtleEgg.class, Integer.class, (egg) -> {
        egg.setEggs(egg.getMaximumEggs() == egg.getEggs() ? egg.getMinimumEggs() : egg.getEggs() + 1);
        return egg;
    }, TurtleEgg::getEggs, (egg, value) -> {
        egg.setEggs(value);
        return egg;
    });
    
    // #   # #  #
    // #   # #  #
    // #   # ####
    // #   #    #
    // # # #    #
    
    //removed this in 1.19 due to the 'Hanging' datatype
//    static IProperty<Lantern, Boolean> LANTERN_HANGING = new Property_119<>("Hanging", Lantern.class, Boolean.class, (lantern) -> {
//        lantern.setHanging(!lantern.isHanging());
//        return lantern;
//    }, Lantern::isHanging, (lantern, hanging) -> {
//        lantern.setHanging(hanging);
//        return lantern;
//    });
    
    static IProperty<Bamboo, Bamboo.Leaves> BAMBOO_LEAVES = new Property_120<>("Leaves", Bamboo.class, Bamboo.Leaves.class, (bamboo) -> {
        bamboo.setLeaves(nextEnumOption(bamboo.getLeaves()));
        return bamboo;
    }, Bamboo::getLeaves, ((bamboo, leaves) -> {
        bamboo.setLeaves(leaves);
        return bamboo;
    }));
    
    static IProperty<Bell, Bell.Attachment> BELL_ATTACHMENT = new Property_120<>("Attachment", Bell.class, Bell.Attachment.class, (bell) -> {
        bell.setAttachment(nextEnumOption(bell.getAttachment()));
        return bell;
    }, Bell::getAttachment, (bell, attachment) -> {
        bell.setAttachment(attachment);
        return bell;
    });
    
    static IProperty<Campfire, Boolean> CAMPFIRE_SIGNAL_FIRE = new Property_120<>("Signal Fire", Campfire.class, Boolean.class, (campfire) -> {
        campfire.setSignalFire(!campfire.isSignalFire());
        return campfire;
    }, Campfire::isSignalFire, (campfire, signalFire) -> {
        campfire.setSignalFire(signalFire);
        return campfire;
    });
    
    static IProperty<Scaffolding, Boolean> SCAFFOLDING_IS_BOTTOM = new Property_120<>("Bottom", Scaffolding.class, Boolean.class, (scaffolding) -> {
        scaffolding.setBottom(!scaffolding.isBottom());
        return scaffolding;
    }, Scaffolding::isBottom, (scaffolding, bottom) -> {
        scaffolding.setBottom(bottom);
        return scaffolding;
    });
    
    static IProperty<Scaffolding, Integer> SCAFFOLDING_DISTANCE = new Property_120<>("Distance", Scaffolding.class, Integer.class, (scaffolding) -> {
        scaffolding.setDistance(scaffolding.getMaximumDistance() == scaffolding.getDistance() ? 0 : scaffolding.getDistance() + 1);
        return scaffolding;
    }, Scaffolding::getDistance, (scaffolding, distance) -> {
        scaffolding.setDistance(distance);
        return scaffolding;
    });
    
    // #   # ####
    // #   # #
    // #   # ####
    // #   #    #
    // # # # ####
    
    static IProperty<Beehive, Integer> HONEY_LEVEL = new Property_120<>("Honey Level", Beehive.class, Integer.class, (beehive) -> {
        beehive.setHoneyLevel(beehive.getMaximumHoneyLevel() == beehive.getHoneyLevel() ? 0 : beehive.getHoneyLevel() + 1);
        return beehive;
    }, Beehive::getHoneyLevel, (beehive, level) -> {
        beehive.setHoneyLevel(level);
        return beehive;
    });
    
    static IProperty<FaceAttachable, FaceAttachable.AttachedFace> FACE_ATTACHABLE = new Property_120<>("Face", FaceAttachable.class, FaceAttachable.AttachedFace.class, (attachable) -> {
        attachable.setAttachedFace(nextEnumOption(attachable.getAttachedFace()));
        return attachable;
    }, FaceAttachable::getAttachedFace, (attachable, face) -> {
        attachable.setAttachedFace(face);
        return attachable;
    });
    
    // #   # ####
    // #   # #
    // #   # ####
    // #   # #  #
    // # # # ####
    
    static IProperty<Jigsaw, Jigsaw.Orientation> JIGSAW = new Property_120<>("Orientation", Jigsaw.class, Jigsaw.Orientation.class, (jigsaw) -> {
        jigsaw.setOrientation(nextEnumOption(jigsaw.getOrientation()));
        return jigsaw;
    }, Jigsaw::getOrientation, (jigsaw, orientation) -> {
        jigsaw.setOrientation(orientation);
        return jigsaw;
    });
    
    static IProperty<RespawnAnchor, Integer> RESPAWN_ANCHOR = new Property_120<>("Charges", RespawnAnchor.class, Integer.class, (anchor) -> {
        anchor.setCharges(anchor.getMaximumCharges() == anchor.getCharges() ? 0 : anchor.getCharges() + 1);
        return anchor;
    }, RespawnAnchor::getCharges, (anchor, charges) -> {
        anchor.setCharges(charges);
        return anchor;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_NORTH = new Property_120<>("North", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(NORTH, nextEnumOption(wall.getHeight(NORTH)));
        return wall;
    }, (wall) -> wall.getHeight(NORTH), (wall, height) -> {
        wall.setHeight(NORTH, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_SOUTH = new Property_120<>("South", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(SOUTH, nextEnumOption(wall.getHeight(SOUTH)));
        return wall;
    }, (wall) -> wall.getHeight(SOUTH), (wall, height) -> {
        wall.setHeight(SOUTH, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_EAST = new Property_120<>("East", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(EAST, nextEnumOption(wall.getHeight(EAST)));
        return wall;
    }, (wall) -> wall.getHeight(EAST), (wall, height) -> {
        wall.setHeight(EAST, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_WEST = new Property_120<>("West", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(WEST, nextEnumOption(wall.getHeight(WEST)));
        return wall;
    }, (wall) -> wall.getHeight(WEST), (wall, height) -> {
        wall.setHeight(WEST, height);
        return wall;
    });
    
    static IProperty<Wall, Boolean> WALL_UP = new Property_120<>("Up", Wall.class, Boolean.class, (wall) -> {
        wall.setUp(!wall.isUp());
        return wall;
    }, Wall::isUp, (wall, up) -> {
        wall.setUp(up);
        return wall;
    });
    
    // #   # ####
    // #   #    #
    // #   #   #
    // #   #   #
    // # # #   #
    
    static IProperty<BigDripleaf, BigDripleaf.Tilt> DRIPLEAF_TILT = new Property_120<>("Tilt", BigDripleaf.class, BigDripleaf.Tilt.class, (leaf) -> {
        leaf.setTilt(nextEnumOption(leaf.getTilt()));
        return leaf;
    }, BigDripleaf::getTilt, (leaf, tilt) -> {
        leaf.setTilt(tilt);
        return leaf;
    });
    
    static IProperty<Candle, Integer> CANDLE_CANDLES = new Property_120<>("Candles", Candle.class, Integer.class, (candle) -> {
        candle.setCandles(candle.getMaximumCandles() == candle.getCandles() ? 1 : candle.getCandles() + 1);
        return candle;
    }, Candle::getCandles, (candle, candles) -> {
        candle.setCandles(candles);
        return candle;
    });
    
    static IProperty<CaveVinesPlant, Boolean> CAVE_VINE_BERRIES = new Property_120<>("Has Berries", CaveVinesPlant.class, Boolean.class, (vines) -> {
        vines.setBerries(!vines.isBerries());
        return vines;
    }, CaveVinesPlant::isBerries, (vines, berries) -> {
        vines.setBerries(berries);
        return vines;
    });
    
    static IProperty<PointedDripstone, BlockFace> DRIPSTONE_VERTICAL_DIRECTION = new Property_120<>("Vertical Direction", PointedDripstone.class, BlockFace.class, (dripstone) -> {
        dripstone.setVerticalDirection(nextEnumOption(dripstone.getVerticalDirection(), dripstone.getVerticalDirections()));
        return dripstone;
    }, PointedDripstone::getVerticalDirection, (dripstone, direction) -> {
        dripstone.setVerticalDirection(direction);
        return dripstone;
    });
    
    static IProperty<PointedDripstone, PointedDripstone.Thickness> DRIPSTONE_THICKNESS = new Property_120<>("Thickness", PointedDripstone.class, PointedDripstone.Thickness.class, (dripstone) -> {
        dripstone.setThickness(nextEnumOption(dripstone.getThickness()));
        return dripstone;
    }, PointedDripstone::getThickness, (dripstone, thickness) -> {
        dripstone.setThickness(thickness);
        return dripstone;
    });
    
    static IProperty<SculkSensor, SculkSensor.Phase> SCULK_PHASE = new Property_120<>("Phase", SculkSensor.class, SculkSensor.Phase.class, (sensor) -> {
        sensor.setPhase(nextEnumOption(sensor.getPhase()));
        return sensor;
    }, SculkSensor::getPhase, (sensor, phase) -> {
        sensor.setPhase(phase);
        return sensor;
    });
    
    // #   # ####
    // #   # #  #
    // #   # ####
    // #   #    #
    // # # # ####
    
    static IProperty<Hangable, Boolean> HUNG = new Property_120<>("Hanging", Hangable.class, Boolean.class, (hung) -> {
        hung.setHanging(!hung.isHanging());
        return hung;
    }, Hangable::isHanging, (block, newState) -> {
        block.setHanging(newState);
        return block;
    });
    
    static IProperty<SculkCatalyst, Boolean> SCULK_CATALYST = new Property_120<>("Bloom", SculkCatalyst.class, Boolean.class, (sculk) -> {
        sculk.setBloom(!sculk.isBloom());
        return sculk;
    }, SculkCatalyst::isBloom, (sculk, bloom) -> {
        sculk.setBloom(bloom);
        return sculk;
    });
    
    static IProperty<SculkShrieker, Boolean> SCULK_CAN_SUMMON = new Property_120<>("Can Summon", SculkShrieker.class, Boolean.class, (sculk) -> {
        sculk.setCanSummon(!sculk.isCanSummon());
        return sculk;
    }, SculkShrieker::isCanSummon, (sculk, canSummon) -> {
        sculk.setCanSummon(canSummon);
        return sculk;
    });
    
    static IProperty<SculkShrieker, Boolean> SCULK_SKEIEKING = new Property_120<>("Shrieking", SculkShrieker.class, Boolean.class, (sculk) -> {
        sculk.setShrieking(!sculk.isShrieking());
        return sculk;
    }, SculkShrieker::isShrieking, (sculk, shrieking) -> {
        sculk.setShrieking(shrieking);
        return sculk;
    });
    
    // #   #### ####
    // #      # #  #
    // #   #### #  #
    // #   #    #  #
    // # # #### ####
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_0_OCCUPIED = new Property_120<>("Slot 0 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(0, !shelf.isSlotOccupied(0));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(0), (shelf, occupied) -> {
        shelf.setSlotOccupied(0, occupied);
        return shelf;
    });
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_1_OCCUPIED = new Property_120<>("Slot 1 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(1, !shelf.isSlotOccupied(1));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(1), (shelf, occupied) -> {
        shelf.setSlotOccupied(1, occupied);
        return shelf;
    });
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_2_OCCUPIED = new Property_120<>("Slot 2 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(2, !shelf.isSlotOccupied(2));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(2), (shelf, occupied) -> {
        shelf.setSlotOccupied(2, occupied);
        return shelf;
    });
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_3_OCCUPIED = new Property_120<>("Slot 3 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(3, !shelf.isSlotOccupied(3));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(3), (shelf, occupied) -> {
        shelf.setSlotOccupied(3, occupied);
        return shelf;
    });
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_4_OCCUPIED = new Property_120<>("Slot 4 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(4, !shelf.isSlotOccupied(4));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(4), (shelf, occupied) -> {
        shelf.setSlotOccupied(4, occupied);
        return shelf;
    });
    
    static IProperty<ChiseledBookshelf, Boolean> CHISELED_BOOKSHELF_SLOT_5_OCCUPIED = new Property_120<>("Slot 5 Occupied", ChiseledBookshelf.class, Boolean.class, (shelf) -> {
        shelf.setSlotOccupied(5, !shelf.isSlotOccupied(5));
        return shelf;
    }, (shelf) -> shelf.isSlotOccupied(5), (shelf, occupied) -> {
        shelf.setSlotOccupied(5, occupied);
        return shelf;
    });
    
    static IProperty<PinkPetals, Integer> PINK_PETALS_FLOWER_AMOUNT = new Property_120<>("Flowers", PinkPetals.class, Integer.class, (petals) -> {
        petals.setFlowerAmount(petals.getMaximumFlowerAmount() == petals.getFlowerAmount() ? 0 : petals.getFlowerAmount() + 1);
        return petals;
    }, PinkPetals::getFlowerAmount, (petals, amount) -> {
        petals.setFlowerAmount(amount);
        return petals;
    });
    
    static IProperty<Brushable, Integer> BRUSHABLE_DUSTED = new Property_120<>("Dusted", Brushable.class, Integer.class, (brushable) -> {
        brushable.setDusted(brushable.getDusted() == brushable.getMaximumDusted() ? 0 : brushable.getDusted() + 1);
        return brushable;
    }, Brushable::getDusted, (brushable, dusted) -> {
        brushable.setDusted(dusted);
        return brushable;
    });
    
    static IProperty<Hatchable, Integer> HATCHABLE_HATCH = new Property_120<>("Hatch", Hatchable.class, Integer.class, (hatchable) -> {
        hatchable.setHatch(hatchable.getHatch() == hatchable.getMaximumHatch() ? 0 : hatchable.getHatch() + 1);
        return hatchable;
    }, Hatchable::getHatch, (hatchable, hatch) -> {
        hatchable.setHatch(hatch);
        return hatchable;
    });
    
    public static void registerProperties() {
        if (registered)
            throw new RuntimeException("Cannot re-register properties after they've been registered.");
        registered = true;
        for (Field field : Property_120.class.getDeclaredFields()) {
            if (field.getType() == IProperty.class) {
                try {
                    PROPERTY_FIELD_MAP.put(field.getName(), (IProperty<?, ?>)field.get(null));
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Material mat : Material.values()) {
            if (!mat.isBlock())
                continue;
            Set<IProperty<?, ?>> properties = new LinkedHashSet<>();
            BlockData data = mat.createBlockData();
            for (IProperty<?, ?> prop : values()) {
                if (prop.checkBlockData(data)) {
                    //We need custom checks for Multiple facing because blocks dont always use all the faces
                    if (prop.getDataType() == MultipleFacing.class) {
                        MultipleFacing facing = (MultipleFacing)data;
                        for (BlockFace face : facing.getAllowedFaces()) {
                            switch (face) {
                            case NORTH -> properties.add(MULTI_NORTH);
                            case EAST -> properties.add(MULTI_EAST);
                            case SOUTH -> properties.add(MULTI_SOUTH);
                            case WEST -> properties.add(MULTI_WEST);
                            case UP -> properties.add(MULTI_UP);
                            case DOWN -> properties.add(MULTI_DOWN);
                            case NORTH_EAST -> properties.add(MULTI_NORTH_EAST);
                            case NORTH_WEST -> properties.add(MULTI_NORTH_WEST);
                            case SOUTH_EAST -> properties.add(MULTI_SOUTH_EAST);
                            case SOUTH_WEST -> properties.add(MULTI_SOUTH_WEST);
                            case WEST_NORTH_WEST -> properties.add(MULTI_WEST_NORTH_WEST);
                            case NORTH_NORTH_WEST -> properties.add(MULTI_NORTH_NORTH_WEST);
                            case NORTH_NORTH_EAST -> properties.add(MULTI_NORTH_NORTH_EAST);
                            case EAST_NORTH_EAST -> properties.add(MULTI_EAST_NORTH_EAST);
                            case EAST_SOUTH_EAST -> properties.add(MULTI_EAST_SOUTH_EAST);
                            case SOUTH_SOUTH_EAST -> properties.add(MULTI_SOUTH_SOUTH_EAST);
                            case SOUTH_SOUTH_WEST -> properties.add(MULTI_SOUTH_SOUTH_WEST);
                            case WEST_SOUTH_WEST -> properties.add(MULTI_WEST_SOUTH_WEST);
                            case SELF -> properties.add(MULTI_SELF);
                            }
                        }
                    } else
                        properties.add(prop);
                }
            }
            PROPERTIES.put(mat, new ArrayList<>(properties));
        }
    }
    
    private Property_120(String niceName, Class<D> dataType, Class<V> valueType, Function<D, D> next, Function<D, V> current, BiFunction<D, V, D> set) {
        super(niceName, dataType, valueType, next, current, set);
    }
}