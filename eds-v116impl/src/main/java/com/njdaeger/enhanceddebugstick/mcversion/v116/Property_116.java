package com.njdaeger.enhanceddebugstick.mcversion.v116;

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
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
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
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Chest;
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
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.Scaffolding;
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
public class Property_116<D extends BlockData, V> extends AbstractProperty<D, V> {
    
    /**
     * Age property
     */
    static IProperty<Ageable, Integer> AGE = new Property_116<>("Age", Ageable.class, Integer.class, (age) -> {
        age.setAge(age.getMaximumAge() == age.getAge() ? 0 : age.getAge() + 1);
        return age;
    }, Ageable::getAge, (age, value) -> {
        age.setAge(value);
        return age;
    });
    /**
     * Power property
     */
    static IProperty<AnaloguePowerable, Integer> POWER = new Property_116<>("Power", AnaloguePowerable.class, Integer.class, (power) -> {
        power.setPower(power.getMaximumPower() == power.getPower() ? 0 : power.getPower() + 1);
        return power;
    }, AnaloguePowerable::getPower, (power, value) -> {
        power.setPower(value);
        return power;
    });
    /**
     * Attached property
     */
    static IProperty<Attachable, Boolean> ATTACHED = new Property_116<>("Attached", Attachable.class, Boolean.class, (attachable) -> {
        attachable.setAttached(!attachable.isAttached());
        return attachable;
    }, Attachable::isAttached, ((attachable, value) -> {
        attachable.setAttached(value);
        return attachable;
    }));
    /**
     * Half property
     */
    static IProperty<Bisected, Bisected.Half> HALF = new Property_116<>("Half", Bisected.class, Bisected.Half.class, (bisected) -> {
        bisected.setHalf(nextEnumOption(bisected.getHalf()));
        return bisected;
    }, Bisected::getHalf, (bisected, value) -> {
        bisected.setHalf(value);
        return bisected;
    });
    /**
     * Facing property
     */
    static IProperty<Directional, BlockFace> FACING = new Property_116<>("Facing", Directional.class, BlockFace.class, (directional) -> {
        directional.setFacing(nextEnumOption(directional.getFacing(), directional.getFaces()));
        return directional;
    }, Directional::getFacing, (directional, value) -> {
        directional.setFacing(value);
        return directional;
    });
    /**
     * Level property
     */
    static IProperty<Levelled, Integer> LEVEL = new Property_116<>("Level", Levelled.class, Integer.class, (level) -> {
        level.setLevel(level.getMaximumLevel() == level.getLevel() ? 0 : level.getLevel() + 1);
        return level;
    }, Levelled::getLevel, (level, value) -> {
        level.setLevel(value);
        return level;
    });
    /**
     * Lit property
     */
    static IProperty<Lightable, Boolean> LIT = new Property_116<>("Lit", Lightable.class, Boolean.class, (lit) -> {
        lit.setLit(!lit.isLit());
        return lit;
    }, Lightable::isLit, (lit, value) -> {
        lit.setLit(value);
        return lit;
    });
    /**
     * Open property
     */
    static IProperty<Openable, Boolean> OPEN = new Property_116<>("Open", Openable.class, Boolean.class, (open) -> {
        open.setOpen(!open.isOpen());
        return open;
    }, Openable::isOpen, (open, value) -> {
        open.setOpen(value);
        return open;
    });
    /**
     * Axis property
     */
    static IProperty<Orientable, Axis> AXIS = new Property_116<>("Axis", Orientable.class, Axis.class, (axis) -> {
        axis.setAxis(nextEnumOption(axis.getAxis(), axis.getAxes()));
        return axis;
    }, Orientable::getAxis, (axis, value) -> {
        axis.setAxis(value);
        return axis;
    });
    /**
     * Powered property
     */
    static IProperty<Powerable, Boolean> POWERED = new Property_116<>("Powered", Powerable.class, Boolean.class, (powered) -> {
        powered.setPowered(!powered.isPowered());
        return powered;
    }, Powerable::isPowered, (powered, value) -> {
        powered.setPowered(value);
        return powered;
    });
    /**
     * Rotation property
     */
    static IProperty<Rotatable, BlockFace> ROTATION = new Property_116<>("Rotation", Rotatable.class, BlockFace.class, (rotation) -> {
        BlockFace face = rotation.getRotation();
        int x = 0;
        while (x < BlockFace.values().length) {
            try {
                switch (face) {
                    case NORTH:
                        face = NORTH_NORTH_EAST;
                        break;
                    case NORTH_NORTH_EAST:
                        face = NORTH_EAST;
                        break;
                    case NORTH_EAST:
                        face = EAST_NORTH_EAST;
                        break;
                    case EAST_NORTH_EAST:
                        face = EAST;
                        break;
                    case EAST:
                        face = EAST_SOUTH_EAST;
                        break;
                    case EAST_SOUTH_EAST:
                        face = SOUTH_EAST;
                        break;
                    case SOUTH_EAST:
                        face = SOUTH_SOUTH_EAST;
                        break;
                    case SOUTH_SOUTH_EAST:
                        face = SOUTH;
                        break;
                    case SOUTH:
                        face = SOUTH_SOUTH_WEST;
                        break;
                    case SOUTH_SOUTH_WEST:
                        face = SOUTH_WEST;
                        break;
                    case SOUTH_WEST:
                        face = WEST_SOUTH_WEST;
                        break;
                    case WEST_SOUTH_WEST:
                        face = WEST;
                        break;
                    case WEST:
                        face = WEST_NORTH_WEST;
                        break;
                    case WEST_NORTH_WEST:
                        face = NORTH_WEST;
                        break;
                    case NORTH_WEST:
                        face = NORTH_NORTH_WEST;
                        break;
                    case NORTH_NORTH_WEST:
                        face = UP;
                        break;
                    case UP:
                        face = DOWN;
                        break;
                    case DOWN:
                        face = SELF;
                        break;
                    case SELF:
                        face = NORTH;
                        break;
                }
                x++;
                rotation.setRotation(face);
                break;
            } catch (IllegalArgumentException ignored) {
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
    static IProperty<Snowable, Boolean> SNOWY = new Property_116<>("Snowy", Snowable.class, Boolean.class, (snowable) -> {
        snowable.setSnowy(!snowable.isSnowy());
        return snowable;
    }, Snowable::isSnowy, (snowable, value) -> {
        snowable.setSnowy(value);
        return snowable;
    });
    /**
     * Waterlogged property
     */
    static IProperty<Waterlogged, Boolean> WATERLOGGED = new Property_116<>("Waterlogged", Waterlogged.class, Boolean.class, (waterlogged) -> {
        waterlogged.setWaterlogged(!waterlogged.isWaterlogged());
        return waterlogged;
    }, Waterlogged::isWaterlogged, (waterlogged, value) -> {
        waterlogged.setWaterlogged(value);
        return waterlogged;
    });
    
    
    /**
     * East connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_EAST = new Property_116<>("East", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(EAST, nextEnumOption(wire.getFace(EAST)));
        return wire;
    }, (wire) -> wire.getFace(EAST), (wire, value) -> {
        wire.setFace(EAST, value);
        return wire;
    });
    /**
     * North connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_NORTH = new Property_116<>("North", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(NORTH, nextEnumOption(wire.getFace(NORTH)));
        return wire;
    }, (wire) -> wire.getFace(NORTH), (wire, value) -> {
        wire.setFace(NORTH, value);
        return wire;
    });
    /**
     * South connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_SOUTH = new Property_116<>("South", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(SOUTH, nextEnumOption(wire.getFace(SOUTH)));
        return wire;
    }, (wire) -> wire.getFace(SOUTH), (wire, value) -> {
        wire.setFace(SOUTH, value);
        return wire;
    });
    /**
     * West connection property. (Redstone Wire)
     */
    static IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_WEST = new Property_116<>("West", RedstoneWire.class, RedstoneWire.Connection.class, (wire) -> {
        wire.setFace(WEST, nextEnumOption(wire.getFace(WEST)));
        return wire;
    }, (wire) -> wire.getFace(WEST), (wire, value) -> {
        wire.setFace(WEST, value);
        return wire;
    });
    /**
     * Inverted property. (Daylight Detector)
     */
    static IProperty<DaylightDetector, Boolean> DETECTOR_INVERTED = new Property_116<>("Inverted", DaylightDetector.class, Boolean.class, (detector) -> {
        detector.setInverted(!detector.isInverted());
        return detector;
    }, DaylightDetector::isInverted, (detector, value) -> {
        detector.setInverted(value);
        return detector;
    });
    /**
     * Disarmed property. (Tripwire)
     */
    static IProperty<Tripwire, Boolean> TRIPWIRE_DISARMED = new Property_116<>("Disarmed", Tripwire.class, Boolean.class, (tripwire) -> {
        tripwire.setDisarmed(!tripwire.isDisarmed());
        return tripwire;
    }, Tripwire::isDisarmed, (tripwire, value) -> {
        tripwire.setDisarmed(value);
        return tripwire;
    });
    /**
     * Hinge property. (Door)
     */
    static IProperty<Door, Door.Hinge> DOOR_HINGE = new Property_116<>("Hinge", Door.class, Door.Hinge.class, (door) -> {
        door.setHinge(nextEnumOption(door.getHinge()));
        return door;
    }, Door::getHinge, (door, value) -> {
        door.setHinge(value);
        return door;
    });
    /**
     * Shape property. (Stairs)
     */
    static IProperty<Stairs, Stairs.Shape> STAIR_SHAPE = new Property_116<>("Shape", Stairs.class, Stairs.Shape.class, (stairs) -> {
        stairs.setShape(nextEnumOption(stairs.getShape()));
        return stairs;
    }, Stairs::getShape, (shape, value) -> {
        shape.setShape(value);
        return shape;
    });
    /**
     * Part property. (Bed)
     */
    static IProperty<Bed, Bed.Part> BED_PART = new Property_116<>("Part", Bed.class, Bed.Part.class, (bed) -> {
        bed.setPart(nextEnumOption(bed.getPart()));
        return bed;
    }, Bed::getPart, (bed, value) -> {
        bed.setPart(value);
        return bed;
    });
    /**
     * Type property. (Chest)
     */
    static IProperty<Chest, Chest.Type> CHEST_TYPE = new Property_116<>("Type", Chest.class, Chest.Type.class, (chest) -> {
        chest.setType(nextEnumOption(chest.getType()));
        return chest;
    }, Chest::getType, (chest, value) -> {
        chest.setType(value);
        return chest;
    });
    /**
     * Conditional property. (Command Block)
     */
    static IProperty<CommandBlock, Boolean> COMMAND_CONDITIONAL = new Property_116<>("Conditional", CommandBlock.class, Boolean.class, (commandBlock) -> {
        commandBlock.setConditional(!commandBlock.isConditional());
        return commandBlock;
    }, CommandBlock::isConditional, (commandBlock, value) -> {
        commandBlock.setConditional(value);
        return commandBlock;
    });
    /**
     * Mode property. (Comparator)
     */
    static IProperty<Comparator, Comparator.Mode> COMPARATOR_MODE = new Property_116<>("Mode", Comparator.class, Comparator.Mode.class, (comparator) -> {
        comparator.setMode(nextEnumOption(comparator.getMode()));
        return comparator;
    }, Comparator::getMode, (comparator, value) -> {
        comparator.setMode(value);
        return comparator;
    });
    /**
     * Triggered property. (Dispenser)
     */
    static IProperty<Dispenser, Boolean> DISPENSER_TRIGGERED = new Property_116<>("Triggered", Dispenser.class, Boolean.class, (dispenser) -> {
        dispenser.setTriggered(!dispenser.isTriggered());
        return dispenser;
    }, Dispenser::isTriggered, (dispenser, value) -> {
        dispenser.setTriggered(value);
        return dispenser;
    });
    /**
     * Eye property. (End portal frame)
     */
    static IProperty<EndPortalFrame, Boolean> END_PORTAL_EYE = new Property_116<>("Eye", EndPortalFrame.class, Boolean.class, (frame) -> {
        frame.setEye(!frame.hasEye());
        return frame;
    }, EndPortalFrame::hasEye, (frame, value) -> {
        frame.setEye(value);
        return frame;
    });
    /**
     * In_wall property. (Gate)
     */
    static IProperty<Gate, Boolean> GATE_IN_WALL = new Property_116<>("In wall", Gate.class, Boolean.class, (gate) -> {
        gate.setInWall(!gate.isInWall());
        return gate;
    }, Gate::isInWall, (gate, value) -> {
        gate.setInWall(value);
        return gate;
    });
    /**
     * Enabled property. (Hopper)
     */
    static IProperty<Hopper, Boolean> HOPPER_ENABLED = new Property_116<>("Enabled", Hopper.class, Boolean.class, (hopper) -> {
        hopper.setEnabled(!hopper.isEnabled());
        return hopper;
    }, Hopper::isEnabled, (hopper, value) -> {
        hopper.setEnabled(value);
        return hopper;
    });
    /**
     * Extended property. (Piston)
     */
    static IProperty<Piston, Boolean> PISTON_EXTENDED = new Property_116<>("Extended", Piston.class, Boolean.class, (piston) -> {
        piston.setExtended(!piston.isExtended());
        return piston;
    }, Piston::isExtended, (piston, value) -> {
        piston.setExtended(value);
        return piston;
    });
    /**
     * Short property. (Piston head)
     */
    static IProperty<PistonHead, Boolean> PISTON_HEAD_SHORT = new Property_116<>("Short", PistonHead.class, Boolean.class, (pistonHead) -> {
        pistonHead.setShort(!pistonHead.isShort());
        return pistonHead;
    }, PistonHead::isShort, (pistonHead, value) -> {
        pistonHead.setShort(value);
        return pistonHead;
    });
    /**
     * Delay property. (Repeater)
     */
    static IProperty<Repeater, Integer> REPEATER_DELAY = new Property_116<>("Delay", Repeater.class, Integer.class, (repeater) -> {
        repeater.setDelay(repeater.getMaximumDelay() == repeater.getDelay() ? repeater.getMinimumDelay() : repeater.getDelay() + 1);
        return repeater;
    }, Repeater::getDelay, (repeater, value) -> {
        repeater.setDelay(value);
        return repeater;
    });
    /**
     * Locked property. (Repeater)
     */
    static IProperty<Repeater, Boolean> REPEATER_LOCKED = new Property_116<>("Locked", Repeater.class, Boolean.class, (repeater) -> {
        repeater.setLocked(!repeater.isLocked());
        return repeater;
    }, Repeater::isLocked, (repeater, value) -> {
        repeater.setLocked(value);
        return repeater;
    });
    /**
     * Type property. (Technical Piston)
     */
    static IProperty<TechnicalPiston, TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = new Property_116<>("Type", TechnicalPiston.class, TechnicalPiston.Type.class, (technicalPiston) -> {
        technicalPiston.setType(nextEnumOption(technicalPiston.getType()));
        return technicalPiston;
    }, TechnicalPiston::getType, (technicalPiston, value) -> {
        technicalPiston.setType(value);
        return technicalPiston;
    });
    /**
     * North property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH = new Property_116<>("North", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH, !multipleFacing.hasFace(NORTH));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH, value);
        return multipleFacing;
    });
    /**
     * East property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST = new Property_116<>("East", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST, !multipleFacing.hasFace(EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST, value);
        return multipleFacing;
    });
    /**
     * South property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH = new Property_116<>("South", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH, !multipleFacing.hasFace(SOUTH));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH, value);
        return multipleFacing;
    });
    /**
     * West property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST = new Property_116<>("West", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST, !multipleFacing.hasFace(WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST, value);
        return multipleFacing;
    });
    /**
     * Up property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_UP = new Property_116<>("Up", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(UP, !multipleFacing.hasFace(UP));
        return multipleFacing;
    }, (face) -> face.hasFace(UP), (multipleFacing, value) -> {
        multipleFacing.setFace(UP, value);
        return multipleFacing;
    });
    /**
     * Down property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_DOWN = new Property_116<>("Down", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(DOWN, !multipleFacing.hasFace(DOWN));
        return multipleFacing;
    }, (face) -> face.hasFace(DOWN), (multipleFacing, value) -> {
        multipleFacing.setFace(DOWN, value);
        return multipleFacing;
    });
    /**
     * Northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_EAST = new Property_116<>("Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_EAST, !multipleFacing.hasFace(NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_WEST = new Property_116<>("Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_WEST, !multipleFacing.hasFace(NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_EAST = new Property_116<>("Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_EAST, !multipleFacing.hasFace(SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_WEST = new Property_116<>("Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_WEST, !multipleFacing.hasFace(SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST_NORTH_WEST = new Property_116<>("West Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST_NORTH_WEST, !multipleFacing.hasFace(WEST_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_WEST = new Property_116<>("North Northwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_NORTH_WEST, !multipleFacing.hasFace(NORTH_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_EAST = new Property_116<>("North Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(NORTH_NORTH_EAST, !multipleFacing.hasFace(NORTH_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(NORTH_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(NORTH_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East northeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST_NORTH_EAST = new Property_116<>("East Northeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST_NORTH_EAST, !multipleFacing.hasFace(EAST_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_EAST_SOUTH_EAST = new Property_116<>("East Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(EAST_SOUTH_EAST, !multipleFacing.hasFace(EAST_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(EAST_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(EAST_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southeast property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_EAST = new Property_116<>("South Southeast", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_SOUTH_EAST, !multipleFacing.hasFace(SOUTH_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_WEST = new Property_116<>("South Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SOUTH_SOUTH_WEST, !multipleFacing.hasFace(SOUTH_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(SOUTH_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(SOUTH_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West southwest property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_WEST_SOUTH_WEST = new Property_116<>("West Southwest", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(WEST_SOUTH_WEST, !multipleFacing.hasFace(WEST_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(WEST_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(WEST_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Self property.
     */
    static IProperty<MultipleFacing, Boolean> MULTI_SELF = new Property_116<>("Self", MultipleFacing.class, Boolean.class, (multipleFacing) -> {
        multipleFacing.setFace(SELF, !multipleFacing.hasFace(SELF));
        return multipleFacing;
    }, (face) -> face.hasFace(SELF), (multipleFacing, value) -> {
        multipleFacing.setFace(SELF, value);
        return multipleFacing;
    });
    /**
     * Instrument property. (Noteblock)
     */
    static IProperty<NoteBlock, Instrument> NOTEBLOCK_INSTRUMENT = new Property_116<>("Instrument", NoteBlock.class, Instrument.class, (noteblock) -> {
        noteblock.setInstrument(nextEnumOption(noteblock.getInstrument()));
        return noteblock;
    }, NoteBlock::getInstrument, (noteblock, value) -> {
        noteblock.setInstrument(value);
        return noteblock;
    });
    /**
     * Note property. (Noteblock)
     */
    static IProperty<NoteBlock, Note> NOTEBLOCK_NOTE = new Property_116<>("Note", NoteBlock.class, Note.class, (noteblock) -> {
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
    static IProperty<Rail, Rail.Shape> RAIL_SHAPE = new Property_116<>("Shape", Rail.class, Rail.Shape.class, (rail) -> {
        rail.setShape(nextEnumOption(rail.getShape(), rail.getShapes()));
        return rail;
    }, Rail::getShape, (rail, value) -> {
        rail.setShape(value);
        return rail;
    });
    /**
     * Pickles property. (Sea pickles)
     */
    static IProperty<SeaPickle, Integer> SEA_PICKLE_PICKLES = new Property_116<>("Pickles", SeaPickle.class, Integer.class, (pickle) -> {
        pickle.setPickles(pickle.getMaximumPickles() == pickle.getPickles() ? pickle.getMinimumPickles() : pickle.getPickles() + 1);
        return pickle;
    }, SeaPickle::getPickles, (pickle, value) -> {
        pickle.setPickles(value);
        return pickle;
    });
    /**
     * Type property. (Slab)
     */
    static IProperty<Slab, Slab.Type> SLAB_TYPE = new Property_116<>("Type", Slab.class, Slab.Type.class, (slab) -> {
        slab.setType(nextEnumOption(slab.getType()));
        return slab;
    }, Slab::getType, (slab, value) -> {
        slab.setType(value);
        return slab;
    });
    /**
     * Bottle_0 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_1 = new Property_116<>("Bottle 0", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(0, !stand.hasBottle(0));
        return stand;
    }, (bottle) -> bottle.hasBottle(0), (stand, value) -> {
        stand.setBottle(0, value);
        return stand;
    });
    /**
     * Bottle_1 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_2 = new Property_116<>("Bottle 1", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(1, !stand.hasBottle(1));
        return stand;
    }, (bottle) -> bottle.hasBottle(1), (stand, value) -> {
        stand.setBottle(1, value);
        return stand;
    });
    /**
     * Bottle_2 property. (Brewing stand)
     */
    static IProperty<BrewingStand, Boolean> BREWING_BOTTLE_3 = new Property_116<>("Bottle 2", BrewingStand.class, Boolean.class, (stand) -> {
        stand.setBottle(2, !stand.hasBottle(2));
        return stand;
    }, (bottle) -> bottle.hasBottle(2), (stand, value) -> {
        stand.setBottle(2, value);
        return stand;
    });
    /**
     * Drag property. (Bubble column)
     */
    static IProperty<BubbleColumn, Boolean> BUBBLE_COLUMN_DRAG = new Property_116<>("Drag", BubbleColumn.class, Boolean.class, (column) -> {
        column.setDrag(!column.isDrag());
        return column;
    }, BubbleColumn::isDrag, (column, value) -> {
        column.setDrag(value);
        return column;
    });
    /**
     * Bites property. (Cake)
     */
    static IProperty<Cake, Integer> CAKE_BITES = new Property_116<>("Bites", Cake.class, Integer.class, (cake) -> {
        cake.setBites(cake.getMaximumBites() == cake.getBites() ? 0 : cake.getBites() + 1);
        return cake;
    }, Cake::getBites, (cake, value) -> {
        cake.setBites(value);
        return cake;
    });
    /**
     * Moisture property. (Farmland)
     */
    static IProperty<Farmland, Integer> FARMLAND_MOISTURE = new Property_116<>("Moisture", Farmland.class, Integer.class, (farmland) -> {
        farmland.setMoisture(farmland.getMaximumMoisture() == farmland.getMoisture() ? 0 : farmland.getMoisture() + 1);
        return farmland;
    }, Farmland::getMoisture, (farmland, value) -> {
        farmland.setMoisture(value);
        return farmland;
    });
    /**
     * Distance property. (Leaves)
     */
    static IProperty<Leaves, Integer> LEAVES_DISTANCE = new Property_116<>("Distance", Leaves.class, Integer.class, (leaves) -> {
        leaves.setDistance(7 == leaves.getDistance() ? 1 : leaves.getDistance() + 1);
        return leaves;
    }, Leaves::getDistance, (leaves, value) -> {
        leaves.setDistance(value);
        return leaves;
    });
    /**
     * Persistent property. (Leaves)
     */
    static IProperty<Leaves, Boolean> LEAVES_PERSISTENT = new Property_116<>("Persistent", Leaves.class, Boolean.class, (leaves) -> {
        leaves.setPersistent(!leaves.isPersistent());
        return leaves;
    }, Leaves::isPersistent, (leaves, value) -> {
        leaves.setPersistent(value);
        return leaves;
    });
    /**
     * Stage property. (Sapling)
     */
    static IProperty<Sapling, Integer> SAPLING_STAGE = new Property_116<>("Stage", Sapling.class, Integer.class, (sapling) -> {
        sapling.setStage(sapling.getMaximumStage() == sapling.getStage() ? 0 : sapling.getStage() + 1);
        return sapling;
    }, Sapling::getStage, (sapling, value) -> {
        sapling.setStage(value);
        return sapling;
    });
    /**
     * Layers property. (Snow)
     */
    static IProperty<Snow, Integer> SNOW_LAYERS = new Property_116<>("Layers", Snow.class, Integer.class, (snow) -> {
        snow.setLayers(snow.getMaximumLayers() == snow.getLayers() ? snow.getMinimumLayers() : snow.getLayers() + 1);
        return snow;
    }, Snow::getLayers, (snow, value) -> {
        snow.setLayers(value);
        return snow;
    });
    /**
     * Mode property. (Structure block)
     */
    static IProperty<StructureBlock, StructureBlock.Mode> STRUCTURE_BLOCK_MODE = new Property_116<>("Mode", StructureBlock.class, StructureBlock.Mode.class, (structureBlock) -> {
        structureBlock.setMode(nextEnumOption(structureBlock.getMode()));
        return structureBlock;
    }, StructureBlock::getMode, ((structureBlock, value) -> {
        structureBlock.setMode(value);
        return structureBlock;
    }));
    /**
     * Unstable property. (TNT)
     */
    static IProperty<TNT, Boolean> TNT_UNSTABLE = new Property_116<>("Unstable", TNT.class, Boolean.class, (tnt) -> {
        tnt.setUnstable(!tnt.isUnstable());
        return tnt;
    }, TNT::isUnstable, (tnt, value) -> {
        tnt.setUnstable(value);
        return tnt;
    });
    /**
     * Hatch property. (Turtle eggs)
     */
    static IProperty<TurtleEgg, Integer> TURTLE_EGG_HATCH = new Property_116<>("Hatch", TurtleEgg.class, Integer.class, (egg) -> {
        egg.setHatch(egg.getMaximumHatch() == egg.getHatch() ? 0 : egg.getHatch() + 1);
        return egg;
    }, TurtleEgg::getHatch, (egg, value) -> {
        egg.setHatch(value);
        return egg;
    });
    /**
     * Eggs property. (Turtle eggs)
     */
    static IProperty<TurtleEgg, Integer> TURTLE_EGG_EGGS = new Property_116<>("Eggs", TurtleEgg.class, Integer.class, (egg) -> {
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
    
    static IProperty<Lantern, Boolean> LANTERN_HANGING = new Property_116<>("Hanging", Lantern.class, Boolean.class, (lantern) -> {
        lantern.setHanging(!lantern.isHanging());
        return lantern;
    }, Lantern::isHanging, (lantern, hanging) -> {
        lantern.setHanging(hanging);
        return lantern;
    });
    
    static IProperty<Bamboo, Bamboo.Leaves> BAMBOO_LEAVES = new Property_116<>("Leaves", Bamboo.class, Bamboo.Leaves.class, (bamboo) -> {
        bamboo.setLeaves(nextEnumOption(bamboo.getLeaves()));
        return bamboo;
    }, Bamboo::getLeaves, ((bamboo, leaves) -> {
        bamboo.setLeaves(leaves);
        return bamboo;
    }));
    
    static IProperty<Bell, Bell.Attachment> BELL_ATTACHMENT = new Property_116<>("Attachment", Bell.class, Bell.Attachment.class, (bell) -> {
        bell.setAttachment(nextEnumOption(bell.getAttachment()));
        return bell;
    }, Bell::getAttachment, (bell, attachment) -> {
        bell.setAttachment(attachment);
        return bell;
    });
    
    static IProperty<Campfire, Boolean> CAMPFIRE_SIGNAL_FIRE = new Property_116<>("Signal Fire", Campfire.class, Boolean.class, (campfire) -> {
        campfire.setSignalFire(!campfire.isSignalFire());
        return campfire;
    }, Campfire::isSignalFire, (campfire, signalFire) -> {
        campfire.setSignalFire(signalFire);
        return campfire;
    });
    
    static IProperty<Scaffolding, Boolean> SCAFFOLDING_IS_BOTTOM = new Property_116<>("Bottom", Scaffolding.class, Boolean.class, (scaffolding) -> {
        scaffolding.setBottom(!scaffolding.isBottom());
        return scaffolding;
    }, Scaffolding::isBottom, (scaffolding, bottom) -> {
        scaffolding.setBottom(bottom);
        return scaffolding;
    });
    
    static IProperty<Scaffolding, Integer> SCAFFOLDING_DISTANCE = new Property_116<>("Distance", Scaffolding.class, Integer.class, (scaffolding) -> {
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
    
    static IProperty<Beehive, Integer> HONEY_LEVEL = new Property_116<>("Honey Level", Beehive.class, Integer.class, (beehive) -> {
        beehive.setHoneyLevel(beehive.getMaximumHoneyLevel() == beehive.getHoneyLevel() ? 0 : beehive.getHoneyLevel() + 1);
        return beehive;
    }, Beehive::getHoneyLevel, (beehive, level) -> {
        beehive.setHoneyLevel(level);
        return beehive;
    });
    
    static IProperty<FaceAttachable, FaceAttachable.AttachedFace> FACE_ATTACHABLE = new Property_116<>("Face", FaceAttachable.class, FaceAttachable.AttachedFace.class, (attachable) -> {
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
    
    static IProperty<Jigsaw, Jigsaw.Orientation> JIGSAW = new Property_116<>("Orientation", Jigsaw.class, Jigsaw.Orientation.class, (jigsaw) -> {
        jigsaw.setOrientation(nextEnumOption(jigsaw.getOrientation()));
        return jigsaw;
    }, Jigsaw::getOrientation, (jigsaw, orientation) -> {
        jigsaw.setOrientation(orientation);
        return jigsaw;
    });
    
    static IProperty<RespawnAnchor, Integer> RESPAWN_ANCHOR = new Property_116<>("Charges", RespawnAnchor.class, Integer.class, (anchor) -> {
        anchor.setCharges(anchor.getMaximumCharges() == anchor.getCharges() ? 0 : anchor.getCharges() + 1);
        return anchor;
    }, RespawnAnchor::getCharges, (anchor, charges) -> {
        anchor.setCharges(charges);
        return anchor;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_NORTH = new Property_116<>("North Height", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(NORTH, nextEnumOption(wall.getHeight(NORTH)));
        return wall;
    }, (wall) -> wall.getHeight(NORTH), (wall, height) -> {
        wall.setHeight(NORTH, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_SOUTH = new Property_116<>("South Height", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(SOUTH, nextEnumOption(wall.getHeight(SOUTH)));
        return wall;
    }, (wall) -> wall.getHeight(SOUTH), (wall, height) -> {
        wall.setHeight(SOUTH, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_EAST = new Property_116<>("East Height", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(EAST, nextEnumOption(wall.getHeight(EAST)));
        return wall;
    }, (wall) -> wall.getHeight(EAST), (wall, height) -> {
        wall.setHeight(EAST, height);
        return wall;
    });
    
    static IProperty<Wall, Wall.Height> WALL_HEIGHT_WEST = new Property_116<>("West Height", Wall.class, Wall.Height.class, (wall) -> {
        wall.setHeight(WEST, nextEnumOption(wall.getHeight(WEST)));
        return wall;
    }, (wall) -> wall.getHeight(WEST), (wall, height) -> {
        wall.setHeight(WEST, height);
        return wall;
    });
    
    public static void registerProperties() {
        if (registered) throw new RuntimeException("Cannot re-register properties after they've been registered.");
        registered = true;
        for (Field field : Property_116.class.getDeclaredFields()) {
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
    
    private Property_116(String niceName, Class<D> dataType, Class<V> valueType, Function<D, D> next, Function<D, V> current, BiFunction<D, V, D> set) {
        super(niceName, dataType, valueType, next, current, set);
    }
    
}
