package com.njdaeger.enhanceddebugstick.api;

import net.minecraft.server.v1_13_R2.BlockDataAbstract;
import org.bukkit.Axis;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Attachable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.block.data.type.Cake;
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
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.block.data.type.TurtleEgg;

import java.util.List;
import java.util.Map;

import static com.njdaeger.enhanceddebugstick.api.Property.nextEnumOption;
import static com.njdaeger.enhanceddebugstick.api.Property.registerProperties;

/**
 * Represents a property
 *
 * @param <D> The blockdata type associated with this property
 * @param <V> The value type associated with this property
 */
@SuppressWarnings("unused")
public interface IProperty<D extends BlockData, V> {

    /**
     * Age property
     */
    IProperty<Ageable, Integer> AGE = new Property<>("Age", Ageable.class, (age) -> {
        age.setAge(age.getMaximumAge() == age.getAge() ? 0 : age.getAge() + 1);
        return age;
    }, Ageable::getAge, (age, value) -> {
        age.setAge(value);
        return age;
    });
    /**
     * Power property
     */
    IProperty<AnaloguePowerable, Integer> POWER = new Property<>("Power", AnaloguePowerable.class, (power) -> {
        power.setPower(power.getMaximumPower() == power.getPower() ? 0 : power.getPower() + 1);
        return power;
    }, AnaloguePowerable::getPower, (power, value) -> {
        power.setPower(value);
        return power;
    });
    /**
     * Attached property
     */
    IProperty<Attachable, Boolean> ATTACHED = new Property<>("Attached", Attachable.class, (attachable) -> {
        attachable.setAttached(!attachable.isAttached());
        return attachable;
    }, Attachable::isAttached, ((attachable, value) -> {
        attachable.setAttached(value);
        return attachable;
    }));
    /**
     * Half property
     */
    IProperty<Bisected, Bisected.Half> HALF = new Property<>("Half", Bisected.class, (bisected) -> {
        bisected.setHalf(nextEnumOption(bisected.getHalf()));
        return bisected;
    }, Bisected::getHalf, (bisected, value) -> {
        bisected.setHalf(value);
        return bisected;
    });
    /**
     * Facing property
     */
    IProperty<Directional, BlockFace> FACING = new Property<>("Facing", Directional.class, (directional) -> {
        directional.setFacing(nextEnumOption(directional.getFacing(), directional.getFaces()));
        return directional;
    }, Directional::getFacing, (directional, value) -> {
        directional.setFacing(value);
        return directional;
    });
    /**
     * Level property
     */
    IProperty<Levelled, Integer> LEVEL = new Property<>("Level", Levelled.class, (level) -> {
        level.setLevel(level.getMaximumLevel() == level.getLevel() ? 0 : level.getLevel() + 1);
        return level;
    }, Levelled::getLevel, (level, value) -> {
        level.setLevel(value);
        return level;
    });
    /**
     * Lit property
     */
    IProperty<Lightable, Boolean> LIT = new Property<>("Lit", Lightable.class, (lit) -> {
        lit.setLit(!lit.isLit());
        return lit;
    }, Lightable::isLit, (lit, value) -> {
        lit.setLit(value);
        return lit;
    });
    /**
     * Open property
     */
    IProperty<Openable, Boolean> OPEN = new Property<>("Open", Openable.class, (open) -> {
        open.setOpen(!open.isOpen());
        return open;
    }, Openable::isOpen, (open, value) -> {
        open.setOpen(value);
        return open;
    });
    /**
     * Axis property
     */
    IProperty<Orientable, Axis> AXIS = new Property<>("Axis", Orientable.class, (axis) -> {
        axis.setAxis(nextEnumOption(axis.getAxis(), axis.getAxes()));
        return axis;
    }, Orientable::getAxis, (axis, value) -> {
        axis.setAxis(value);
        return axis;
    });
    /**
     * Powered property
     */
    IProperty<Powerable, Boolean> POWERED = new Property<>("Powered", Powerable.class, (powered) -> {
        powered.setPowered(!powered.isPowered());
        return powered;
    }, Powerable::isPowered, (powered, value) -> {
        powered.setPowered(value);
        return powered;
    });
    /**
     * Rotation property
     */
    IProperty<Rotatable, BlockFace> ROTATION = new Property<>("Rotation", Rotatable.class, (rotation) -> {
        BlockFace face = rotation.getRotation();
        int x = 0;
        while (x < BlockFace.values().length) {
            try {
                switch (face) {
                    case NORTH:
                        face = BlockFace.NORTH_NORTH_EAST;
                        break;
                    case NORTH_NORTH_EAST:
                        face = BlockFace.NORTH_EAST;
                        break;
                    case NORTH_EAST:
                        face = BlockFace.EAST_NORTH_EAST;
                        break;
                    case EAST_NORTH_EAST:
                        face = BlockFace.EAST;
                        break;
                    case EAST:
                        face = BlockFace.EAST_SOUTH_EAST;
                        break;
                    case EAST_SOUTH_EAST:
                        face = BlockFace.SOUTH_EAST;
                        break;
                    case SOUTH_EAST:
                        face = BlockFace.SOUTH_SOUTH_EAST;
                        break;
                    case SOUTH_SOUTH_EAST:
                        face = BlockFace.SOUTH;
                        break;
                    case SOUTH:
                        face = BlockFace.SOUTH_SOUTH_WEST;
                        break;
                    case SOUTH_SOUTH_WEST:
                        face = BlockFace.SOUTH_WEST;
                        break;
                    case SOUTH_WEST:
                        face = BlockFace.WEST_SOUTH_WEST;
                        break;
                    case WEST_SOUTH_WEST:
                        face = BlockFace.WEST;
                        break;
                    case WEST:
                        face = BlockFace.WEST_NORTH_WEST;
                        break;
                    case WEST_NORTH_WEST:
                        face = BlockFace.NORTH_WEST;
                        break;
                    case NORTH_WEST:
                        face = BlockFace.NORTH_NORTH_WEST;
                        break;
                    case NORTH_NORTH_WEST:
                        face = BlockFace.UP;
                        break;
                    case UP:
                        face = BlockFace.DOWN;
                        break;
                    case DOWN:
                        face = BlockFace.SELF;
                        break;
                    case SELF:
                        face = BlockFace.NORTH;
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
    IProperty<Snowable, Boolean> SNOWY = new Property<>("Snowy", Snowable.class, (snowable) -> {
        snowable.setSnowy(!snowable.isSnowy());
        return snowable;
    }, Snowable::isSnowy, (snowable, value) -> {
        snowable.setSnowy(value);
        return snowable;
    });
    /**
     * Waterlogged property
     */
    IProperty<Waterlogged, Boolean> WATERLOGGED = new Property<>("Waterlogged", Waterlogged.class, (waterlogged) -> {
        waterlogged.setWaterlogged(!waterlogged.isWaterlogged());
        return waterlogged;
    }, Waterlogged::isWaterlogged, (waterlogged, value) -> {
        waterlogged.setWaterlogged(value);
        return waterlogged;
    });


    /**
     * East connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_EAST = new Property<>("East", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.EAST, nextEnumOption(wire.getFace(BlockFace.EAST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.EAST), (wire, value) -> {
        wire.setFace(BlockFace.EAST, value);
        return wire;
    });
    /**
     * North connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_NORTH = new Property<>("North", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.NORTH, nextEnumOption(wire.getFace(BlockFace.NORTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.NORTH), (wire, value) -> {
        wire.setFace(BlockFace.NORTH, value);
        return wire;
    });
    /**
     * South connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_SOUTH = new Property<>("South", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.SOUTH, nextEnumOption(wire.getFace(BlockFace.SOUTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.SOUTH), (wire, value) -> {
        wire.setFace(BlockFace.SOUTH, value);
        return wire;
    });
    /**
     * West connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_WEST = new Property<>("West", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.WEST, nextEnumOption(wire.getFace(BlockFace.WEST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.WEST), (wire, value) -> {
        wire.setFace(BlockFace.WEST, value);
        return wire;
    });
    /**
     * Inverted property. (Daylight Detector)
     */
    IProperty<DaylightDetector, Boolean> DETECTOR_INVERTED = new Property<>("Inverted", DaylightDetector.class, (detector) -> {
        detector.setInverted(!detector.isInverted());
        return detector;
    }, DaylightDetector::isInverted, (detector, value) -> {
        detector.setInverted(value);
        return detector;
    });
    /**
     * Disarmed property. (Tripwire)
     */
    IProperty<Tripwire, Boolean> TRIPWIRE_DISARMED = new Property<>("Disarmed", Tripwire.class, (tripwire) -> {
        tripwire.setDisarmed(!tripwire.isDisarmed());
        return tripwire;
    }, Tripwire::isDisarmed, (tripwire, value) -> {
        tripwire.setDisarmed(value);
        return tripwire;
    });
    /**
     * Hinge property. (Door)
     */
    IProperty<Door, Door.Hinge> DOOR_HINGE = new Property<>("Hinge", Door.class, (door) -> {
        door.setHinge(nextEnumOption(door.getHinge()));
        return door;
    }, Door::getHinge, (door, value) -> {
        door.setHinge(value);
        return door;
    });
    /**
     * Shape property. (Stairs)
     */
    IProperty<Stairs, Stairs.Shape> STAIR_SHAPE = new Property<>("Shape", Stairs.class, (stairs) -> {
        stairs.setShape(nextEnumOption(stairs.getShape()));
        return stairs;
    }, Stairs::getShape, (shape, value) -> {
        shape.setShape(value);
        return shape;
    });
    /**
     * Part property. (Bed)
     */
    IProperty<Bed, Bed.Part> BED_PART = new Property<>("Part", Bed.class, (bed) -> {
        bed.setPart(nextEnumOption(bed.getPart()));
        return bed;
    }, Bed::getPart, (bed, value) -> {
        bed.setPart(value);
        return bed;
    });
    /**
     * Type property. (Chest)
     */
    IProperty<Chest, Chest.Type> CHEST_TYPE = new Property<>("Type", Chest.class, (chest) -> {
        chest.setType(nextEnumOption(chest.getType()));
        return chest;
    }, Chest::getType, (chest, value) -> {
        chest.setType(value);
        return chest;
    });
    /**
     * Conditional property. (Command Block)
     */
    IProperty<CommandBlock, Boolean> COMMAND_CONDITIONAL = new Property<>("Conditional", CommandBlock.class, (commandBlock) -> {
        commandBlock.setConditional(!commandBlock.isConditional());
        return commandBlock;
    }, CommandBlock::isConditional, (commandBlock, value) -> {
        commandBlock.setConditional(value);
        return commandBlock;
    });
    /**
     * Mode property. (Comparator)
     */
    IProperty<Comparator, Comparator.Mode> COMPARATOR_MODE = new Property<>("Mode", Comparator.class, (comparator) -> {
        comparator.setMode(nextEnumOption(comparator.getMode()));
        return comparator;
    }, Comparator::getMode, (comparator, value) -> {
        comparator.setMode(value);
        return comparator;
    });
    /**
     * Triggered property. (Dispenser)
     */
    IProperty<Dispenser, Boolean> DISPENSER_TRIGGERED = new Property<>("Triggered", Dispenser.class, (dispenser) -> {
        dispenser.setTriggered(!dispenser.isTriggered());
        return dispenser;
    }, Dispenser::isTriggered, (dispenser, value) -> {
        dispenser.setTriggered(value);
        return dispenser;
    });
    /**
     * Eye property. (End portal frame)
     */
    IProperty<EndPortalFrame, Boolean> END_PORTAL_EYE = new Property<>("Eye", EndPortalFrame.class, (frame) -> {
        frame.setEye(!frame.hasEye());
        return frame;
    }, EndPortalFrame::hasEye, (frame, value) -> {
        frame.setEye(value);
        return frame;
    });
    /**
     * In_wall property. (Gate)
     */
    IProperty<Gate, Boolean> GATE_IN_WALL = new Property<>("In wall", Gate.class, (gate) -> {
        gate.setInWall(!gate.isInWall());
        return gate;
    }, Gate::isInWall, (gate, value) -> {
        gate.setInWall(value);
        return gate;
    });
    /**
     * Enabled property. (Hopper)
     */
    IProperty<Hopper, Boolean> HOPPER_ENABLED = new Property<>("Enabled", Hopper.class, (hopper) -> {
        hopper.setEnabled(!hopper.isEnabled());
        return hopper;
    }, Hopper::isEnabled, (hopper, value) -> {
        hopper.setEnabled(value);
        return hopper;
    });
    /**
     * Extended property. (Piston)
     */
    IProperty<Piston, Boolean> PISTON_EXTENDED = new Property<>("Extended", Piston.class, (piston) -> {
        piston.setExtended(!piston.isExtended());
        return piston;
    }, Piston::isExtended, (piston, value) -> {
        piston.setExtended(value);
        return piston;
    });
    /**
     * Short property. (Piston head)
     */
    IProperty<PistonHead, Boolean> PISTON_HEAD_SHORT = new Property<>("Short", PistonHead.class, (pistonHead) -> {
        pistonHead.setShort(!pistonHead.isShort());
        return pistonHead;
    }, PistonHead::isShort, (pistonHead, value) -> {
        pistonHead.setShort(value);
        return pistonHead;
    });
    /**
     * Delay property. (Repeater)
     */
    IProperty<Repeater, Integer> REPEATER_DELAY = new Property<>("Delay", Repeater.class, (repeater) -> {
        repeater.setDelay(repeater.getMaximumDelay() == repeater.getDelay() ? repeater.getMinimumDelay() : repeater.getDelay() + 1);
        return repeater;
    }, Repeater::getDelay, (repeater, value) -> {
        repeater.setDelay(value);
        return repeater;
    });
    /**
     * Locked property. (Repeater)
     */
    IProperty<Repeater, Boolean> REPEATER_LOCKED = new Property<>("Locked", Repeater.class, (repeater) -> {
        repeater.setLocked(!repeater.isLocked());
        return repeater;
    }, Repeater::isLocked, (repeater, value) -> {
        repeater.setLocked(value);
        return repeater;
    });
    /**
     * Face property. (Switch)
     */
    IProperty<Switch, Switch.Face> SWITCH_FACE = new Property<>("Face", Switch.class, (swtch) -> {
        swtch.setFace(nextEnumOption(swtch.getFace()));
        return swtch;
    }, Switch::getFace, (swtch, value) -> {
        swtch.setFace(value);
        return swtch;
    });
    /**
     * Type property. (Technical Piston)
     */
    IProperty<TechnicalPiston, TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = new Property<>("Type", TechnicalPiston.class, (technicalPiston) -> {
        technicalPiston.setType(nextEnumOption(technicalPiston.getType()));
        return technicalPiston;
    }, TechnicalPiston::getType, (technicalPiston, value) -> {
        technicalPiston.setType(value);
        return technicalPiston;
    });
    /**
     * North property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH = new Property<>("North", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH, !multipleFacing.hasFace(BlockFace.NORTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.NORTH, value);
        return multipleFacing;
    });
    /**
     * East property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST = new Property<>("East", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST, !multipleFacing.hasFace(BlockFace.EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.EAST, value);
        return multipleFacing;
    });
    /**
     * South property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH = new Property<>("South", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH, !multipleFacing.hasFace(BlockFace.SOUTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SOUTH, value);
        return multipleFacing;
    });
    /**
     * West property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST = new Property<>("West", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST, !multipleFacing.hasFace(BlockFace.WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.WEST, value);
        return multipleFacing;
    });
    /**
     * Up property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_UP = new Property<>("Up", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.UP, !multipleFacing.hasFace(BlockFace.UP));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.UP), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.UP, value);
        return multipleFacing;
    });
    /**
     * Down property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_DOWN = new Property<>("Down", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.DOWN, !multipleFacing.hasFace(BlockFace.DOWN));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.DOWN), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.DOWN, value);
        return multipleFacing;
    });
    /**
     * Northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_EAST = new Property<>("Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_WEST = new Property<>("Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_EAST = new Property<>("Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * Southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_WEST = new Property<>("Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST_NORTH_WEST = new Property<>("West Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_NORTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.WEST_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_WEST = new Property<>("North Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_WEST, value);
        return multipleFacing;
    });
    /**
     * North northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_EAST = new Property<>("North Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST_NORTH_EAST = new Property<>("East Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_NORTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_NORTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.EAST_NORTH_EAST, value);
        return multipleFacing;
    });
    /**
     * East southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST_SOUTH_EAST = new Property<>("East Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.EAST_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_EAST = new Property<>("South Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_EAST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_EAST, value);
        return multipleFacing;
    });
    /**
     * South southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_WEST = new Property<>("South Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * West southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST_SOUTH_WEST = new Property<>("West Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_SOUTH_WEST), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.WEST_SOUTH_WEST, value);
        return multipleFacing;
    });
    /**
     * Self property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SELF = new Property<>("Self", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SELF, !multipleFacing.hasFace(BlockFace.SELF));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SELF), (multipleFacing, value) -> {
        multipleFacing.setFace(BlockFace.SELF, value);
        return multipleFacing;
    });
    /**
     * Instrument property. (Noteblock)
     */
    IProperty<NoteBlock, Instrument> NOTEBLOCK_INSTRUMENT = new Property<>("Instrument", NoteBlock.class, (noteblock) -> {
        noteblock.setInstrument(nextEnumOption(noteblock.getInstrument()));
        return noteblock;
    }, NoteBlock::getInstrument, (noteblock, value) -> {
        noteblock.setInstrument(value);
        return noteblock;
    });
    /**
     * Note property. (Noteblock)
     */
    IProperty<NoteBlock, Note> NOTEBLOCK_NOTE = new Property<>("Note", NoteBlock.class, (noteblock) -> {
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
    IProperty<Rail, Rail.Shape> RAIL_SHAPE = new Property<>("Shape", Rail.class, (rail) -> {
        rail.setShape(nextEnumOption(rail.getShape(), rail.getShapes()));
        return rail;
    }, Rail::getShape, (rail, value) -> {
        rail.setShape(value);
        return rail;
    });
    /**
     * Pickles property. (Sea pickles)
     */
    IProperty<SeaPickle, Integer> SEA_PICKLE_PICKLES = new Property<>("Pickles", SeaPickle.class, (pickle) -> {
        pickle.setPickles(pickle.getMaximumPickles() == pickle.getPickles() ? pickle.getMinimumPickles() : pickle.getPickles() + 1);
        return pickle;
    }, SeaPickle::getPickles, (pickle, value) -> {
        pickle.setPickles(value);
        return pickle;
    });
    /**
     * Type property. (Slab)
     */
    IProperty<Slab, Slab.Type> SLAB_TYPE = new Property<>("Type", Slab.class, (slab) -> {
        slab.setType(nextEnumOption(slab.getType()));
        return slab;
    }, Slab::getType, (slab, value) -> {
        slab.setType(value);
        return slab;
    });
    /**
     * Bottle_0 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_1 = new Property<>("Bottle 0", BrewingStand.class, (stand) -> {
        stand.setBottle(0, !stand.hasBottle(0));
        return stand;
    }, (bottle) -> bottle.hasBottle(0), (stand, value) -> {
        stand.setBottle(0, value);
        return stand;
    });
    /**
     * Bottle_1 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_2 = new Property<>("Bottle 1", BrewingStand.class, (stand) -> {
        stand.setBottle(1, !stand.hasBottle(1));
        return stand;
    }, (bottle) -> bottle.hasBottle(1), (stand, value) -> {
        stand.setBottle(1, value);
        return stand;
    });
    /**
     * Bottle_2 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_3 = new Property<>("Bottle 2", BrewingStand.class, (stand) -> {
        stand.setBottle(2, !stand.hasBottle(2));
        return stand;
    }, (bottle) -> bottle.hasBottle(2), (stand, value) -> {
        stand.setBottle(2, value);
        return stand;
    });
    /**
     * Drag property. (Bubble column)
     */
    IProperty<BubbleColumn, Boolean> BUBBLE_COLUMN_DRAG = new Property<>("Drag", BubbleColumn.class, (column) -> {
        column.setDrag(!column.isDrag());
        return column;
    }, BubbleColumn::isDrag, (column, value) -> {
        column.setDrag(value);
        return column;
    });
    /**
     * Bites property. (Cake)
     */
    IProperty<Cake, Integer> CAKE_BITES = new Property<>("Bites", Cake.class, (cake) -> {
        cake.setBites(cake.getMaximumBites() == cake.getBites() ? 0 : cake.getBites() + 1);
        return cake;
    }, Cake::getBites, (cake, value) -> {
        cake.setBites(value);
        return cake;
    });
    /**
     * Moisture property. (Farmland)
     */
    IProperty<Farmland, Integer> FARMLAND_MOISTURE = new Property<>("Moisture", Farmland.class, (farmland) -> {
        farmland.setMoisture(farmland.getMaximumMoisture() == farmland.getMoisture() ? 0 : farmland.getMoisture() + 1);
        return farmland;
    }, Farmland::getMoisture, (farmland, value) -> {
        farmland.setMoisture(value);
        return farmland;
    });
    /**
     * Distance property. (Leaves)
     */
    IProperty<Leaves, Integer> LEAVES_DISTANCE = new Property<>("Distance", Leaves.class, (leaves) -> {
        leaves.setDistance(7 == leaves.getDistance() ? 1 : leaves.getDistance() + 1);
        return leaves;
    }, Leaves::getDistance, (leaves, value) -> {
        leaves.setDistance(value);
        return leaves;
    });
    /**
     * Persistent property. (Leaves)
     */
    IProperty<Leaves, Boolean> LEAVES_PERSISTENT = new Property<>("Persistent", Leaves.class, (leaves) -> {
        leaves.setPersistent(!leaves.isPersistent());
        return leaves;
    }, Leaves::isPersistent, (leaves, value) -> {
        leaves.setPersistent(value);
        return leaves;
    });
    /**
     * Stage property. (Sapling)
     */
    IProperty<Sapling, Integer> SAPLING_STAGE = new Property<>("Stage", Sapling.class, (sapling) -> {
        sapling.setStage(sapling.getMaximumStage() == sapling.getStage() ? 0 : sapling.getStage() + 1);
        return sapling;
    }, Sapling::getStage, (sapling, value) -> {
        sapling.setStage(value);
        return sapling;
    });
    /**
     * Layers property. (Snow)
     */
    IProperty<Snow, Integer> SNOW_LAYERS = new Property<>("Layers", Snow.class, (snow) -> {
        snow.setLayers(snow.getMaximumLayers() == snow.getLayers() ? snow.getMinimumLayers() : snow.getLayers() + 1);
        return snow;
    }, Snow::getLayers, (snow, value) -> {
        snow.setLayers(value);
        return snow;
    });
    /**
     * Mode property. (Structure block)
     */
    IProperty<StructureBlock, StructureBlock.Mode> STRUCTURE_BLOCK_MODE = new Property<>("Mode", StructureBlock.class, (structureBlock) -> {
        structureBlock.setMode(nextEnumOption(structureBlock.getMode()));
        return structureBlock;
    }, StructureBlock::getMode, ((structureBlock, value) -> {
        structureBlock.setMode(value);
        return structureBlock;
    }));
    /**
     * Unstable property. (TNT)
     */
    IProperty<TNT, Boolean> TNT_UNSTABLE = new Property<>("Unstable", TNT.class, (tnt) -> {
        tnt.setUnstable(!tnt.isUnstable());
        return tnt;
    }, TNT::isUnstable, (tnt, value) -> {
        tnt.setUnstable(value);
        return tnt;
    });
    /**
     * Hatch property. (Turtle eggs)
     */
    IProperty<TurtleEgg, Integer> TURTLE_EGG_HATCH = new Property<>("Hatch", TurtleEgg.class, (egg) -> {
        egg.setHatch(egg.getMaximumHatch() == egg.getHatch() ? 0 : egg.getHatch() + 1);
        return egg;
    }, TurtleEgg::getHatch, (egg, value) -> {
        egg.setHatch(value);
        return egg;
    });
    /**
     * Eggs property. (Turtle eggs)
     */
    IProperty<TurtleEgg, Integer> TURTLE_EGG_EGGS = new Property<>("Eggs", TurtleEgg.class, (egg) -> {
        egg.setEggs(egg.getMaximumEggs() == egg.getEggs() ? egg.getMinimumEggs() : egg.getEggs() + 1);
        return egg;
    }, TurtleEgg::getEggs, (egg, value) -> {
        egg.setEggs(value);
        return egg;
    });

    /**
     * The constant ordinal
     *
     * @return The constant ordinal
     */
    int ordinal();

    /**
     * Gets the nice name of this property.
     *
     * @return The property nice name
     */
    String getNiceName();

    /**
     * Gets the corresponding class type of the property.
     *
     * @return The class type
     */
    Class<D> getDataType();

    /**
     * Check if a specific BlockData object is an instance of this property's data type.
     *
     * @param block Whether the block data has this specific property
     * @return True if the block data has this property.
     */
    boolean checkBlockData(BlockData block);

    /**
     * Merges the value of this property from the original block data, to the property of the new block data.
     *
     * @param original The original block data to take this property from
     * @param data The block data to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, BlockData data);

    /**
     * Merges the value of this property from the original block data, to the property of the new material.
     *
     * @param original The original block data to take this property from
     * @param material The material to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, Material material);

    /**
     * Merges the value of this property from the original block data, to the property of the new block.
     * @param original The original block data to take this property from
     * @param block The block to apply the original value to
     * @return The modified block data. Or null if either of the provided arguments are not applicable to this property.
     */
    D mergeBlockData(BlockData original, Block block);

    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param data The block data to get the property from
     * @return The current value of this property as a nice string.
     * @throws RuntimeException if the property is not applicable to the given BlockData
     */
    String getNiceCurrentValue(BlockData data);

    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The current value of this property as a nice string
     * @throws RuntimeException if the property is not applicable to the given block
     */
    String getNiceCurrentValue(Block block);

    /**
     * Gets the current value of this property from the given block data.
     *
     * @param data The block data to get the value of the current property of
     * @return The current property value of this block data
     * @throws RuntimeException If this property is not applicable to the given BlockData
     */
    V getCurrentValue(BlockData data);

    /**
     * Gets the current value of this property from the given block.
     *
     * @param block The block to get the value of the current property of.
     * @return The current property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getCurrentValue(Block block);

    /**
     * Gets the block data from a block data object to the correct block data type of this property.
     *
     * @param data THe original block data
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    D currentBlockData(BlockData data);

    /**
     * Gets the block data from one block to the data type of this property.
     *
     * @param block The original block.
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    D currentBlockData(Block block);

    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param data The block data to get the property from
     * @return The next value of this property as a nice string.
     * @throws RuntimeException if the property is not applicable to the given BlockData
     */
    String getNiceNextValue(BlockData data);

    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The next value of this property as a nice string
     * @throws RuntimeException if the property is not applicable to the given block
     */
    String getNiceNextValue(Block block);

    /**
     * Gets the next value of this property from the given BlockData
     * @param data  The block data to get the next value of the current property of
     * @return The next property value of this block
     * @throws RuntimeException If this property is not applicable to the given BlockData
     */
    V getNextValue(BlockData data);

    /**
     * Gets the next value of this property from the given block.
     *
     * @param block The block to get the next value of the current property of.
     * @return The next property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getNextValue(Block block);

    /**
     * Gets the next BlockData for this property
     *
     * @param data The previous data
     * @return The next data
     */
    D nextBlockData(BlockData data);

    /**
     * Gets the next BlockData for this property
     *
     * @param block The previous data
     * @return The next data
     */
    D nextBlockData(Block block);

    /**
     * Checks if a specific BlockData is applicable to this particular property
     *
     * @param data The block data to check
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    boolean isApplicableTo(BlockData data);

    /**
     * Checks if a specific material has this particular property
     *
     * @param material The material to check.
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    boolean isApplicableTo(Material material);

    /**
     * Checks if a specific block has this particular property
     *
     * @param block The block to check.
     * @return False if the block does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    boolean isApplicableTo(Block block);

    /**
     * Gets a map of all bukkit materials which have properies, and a list of their (editable) properties.
     *
     * @return A map of bukkit materials which have editable properties and a list of their editable properties.
     */
    static Map<Material, List<IProperty<?, ?>>> getMaterialProperties() {
        return Property.getMaterialProperties();
    }

    /**
     * Get a list of (editable) properties this BlockData has, if any.
     *
     * @param blockData The BlockData to transform into a list of properties
     * @return The list of properties, or an empty list if the block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(BlockData blockData) {
        return Property.getProperties(blockData.getMaterial());
    }

    /**
     * Get a list of (editable) properties this material has, if any.
     *
     * @param material The material to get the list of properties of.
     * @return The list of properties, or an empty list if the block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(Material material) {
        return Property.getProperties(material);
    }

    /**
     * Gets a list of (editable) properties this block has.
     *
     * @param block The block to get the list of properties of.
     * @return The list of properties, or an empty list if this block has no (editable) properties.
     */
    static List<IProperty<?, ?>> getProperties(Block block) {
        return Property.getProperties(block);
    }

    /**
     * Check whether a specific block data has any properties.
     *
     * @param data The block to check
     * @return True if the block data has (editable) properties, false otherwise.
     */
    static boolean hasProperties(BlockData data) {
        return Property.hasProperties(data);
    }

    /**
     * Check whether a specific material has any properties.
     *
     * @param material The material to check
     * @return True if the material has (editable) properties, false otherwise.
     */
    static boolean hasProperties(Material material) {
        return Property.hasProperties(material);
    }

    /**
     * Check whether a specific block has any properties.
     *
     * @param block The block to check
     * @return True if the block has (editable) properties, false otherwise.
     */
    static boolean hasProperties(Block block) {
        return Property.hasProperties(block);
    }

    /**
     * Gets all the properties
     *
     * @return An array of all the properties
     */
    static IProperty<?, ?>[] values() {
        return Property.values();
    }

}
