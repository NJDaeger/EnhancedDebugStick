package com.njdaeger.enhanceddebugstick;

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

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Property<D extends BlockData, V> {

    private static final Map<Material, List<Property<?, ?>>> PROPERTIES = new HashMap<>();
    private static final Map<String, Property<?, ?>> PROPERTY_FIELD_MAP = new HashMap<>();
    private static final Collection<Property<?, ?>> PROPERTY_FIELD_PRE_MAP = new ArrayList<>();

    /**
     * Age property
     */
    public static final Property<Ageable, Integer> AGE = new Property<>("Age", Ageable.class, (age) -> {
        age.setAge(age.getMaximumAge() == age.getAge() ? 0 : age.getAge() + 1);
        return age;
    }, Ageable::getAge);
    /**
     * Power property
     */
    public static final Property<AnaloguePowerable, Integer> POWER = new Property<>("Power", AnaloguePowerable.class, (power) -> {
        power.setPower(power.getMaximumPower() == power.getPower() ? 0 : power.getPower() + 1);
        return power;
    }, AnaloguePowerable::getPower);
    /**
     * Attached property
     */
    public static final Property<Attachable, Boolean> ATTACHED = new Property<>("Attached", Attachable.class, (attachable) -> {
        attachable.setAttached(!attachable.isAttached());
        return attachable;
    }, Attachable::isAttached);
    /**
     * Half property
     */
    public static final Property<Bisected, Bisected.Half> HALF = new Property<>("Half", Bisected.class, (bisected) -> {
        bisected.setHalf(nextEnumOption(bisected.getHalf()));
        return bisected;
    }, Bisected::getHalf);
    /**
     * Facing property
     */
    public static final Property<Directional, BlockFace> FACING = new Property<>("Facing", Directional.class, (directional) -> {
        directional.setFacing(nextEnumOption(directional.getFacing(), directional.getFaces()));
        return directional;
    }, Directional::getFacing);
    /**
     * Level property
     */
    public static final Property<Levelled, Integer> LEVEL = new Property<>("Level", Levelled.class, (level) -> {
        level.setLevel(level.getMaximumLevel() == level.getLevel() ? 0 : level.getLevel() + 1);
        return level;
    }, Levelled::getLevel);
    /**
     * Lit property
     */
    public static final Property<Lightable, Boolean> LIT = new Property<>("Lit", Lightable.class, (lit) -> {
        lit.setLit(!lit.isLit());
        return lit;
    }, Lightable::isLit);
    /**
     * Open property
     */
    public static final Property<Openable, Boolean> OPEN = new Property<>("Open", Openable.class, (open) -> {
        open.setOpen(!open.isOpen());
        return open;
    }, Openable::isOpen);
    /**
     * Axis property
     */
    public static final Property<Orientable, Axis> AXIS = new Property<>("Axis", Orientable.class, (axis) -> {
        axis.setAxis(nextEnumOption(axis.getAxis(), axis.getAxes()));
        return axis;
    }, Orientable::getAxis);
    /**
     * Powered property
     */
    public static final Property<Powerable, Boolean> POWERED = new Property<>("Powered", Powerable.class, (powered) -> {
        powered.setPowered(!powered.isPowered());
        return powered;
    }, Powerable::isPowered);
    /**
     * Rotation property
     */
    public static final Property<Rotatable, BlockFace> ROTATION = new Property<>("Rotation", Rotatable.class, (rotation) -> {
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
    }, Rotatable::getRotation);
    /**
     * Snowy property
     */
    public static final Property<Snowable, Boolean> SNOWY = new Property<>("Snowy", Snowable.class, (snowable) -> {
        snowable.setSnowy(!snowable.isSnowy());
        return snowable;
    }, Snowable::isSnowy);
    /**
     * Waterlogged property
     */
    public static final Property<Waterlogged, Boolean> WATERLOGGED = new Property<>("Waterlogged", Waterlogged.class, (waterlogged) -> {
        waterlogged.setWaterlogged(!waterlogged.isWaterlogged());
        return waterlogged;
    }, Waterlogged::isWaterlogged);


    /**
     * East connection property. (Redstone Wire)
     */
    public static final Property<RedstoneWire, RedstoneWire.Connection> REDSTONE_EAST = new Property<>("East", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.EAST, nextEnumOption(wire.getFace(BlockFace.EAST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.EAST));
    /**
     * North connection property. (Redstone Wire)
     */
    public static final Property<RedstoneWire, RedstoneWire.Connection> REDSTONE_NORTH = new Property<>("North", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.NORTH, nextEnumOption(wire.getFace(BlockFace.NORTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.NORTH));
    /**
     * South connection property. (Redstone Wire)
     */
    public static final Property<RedstoneWire, RedstoneWire.Connection> REDSTONE_SOUTH = new Property<>("South", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.SOUTH, nextEnumOption(wire.getFace(BlockFace.SOUTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.SOUTH));
    /**
     * West connection property. (Redstone Wire)
     */
    public static final Property<RedstoneWire, RedstoneWire.Connection> REDSTONE_WEST = new Property<>("West", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.WEST, nextEnumOption(wire.getFace(BlockFace.WEST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.WEST));
    /**
     * Inverted property. (Daylight Detector)
     */
    public static final Property<DaylightDetector, Boolean> DETECTOR_INVERTED = new Property<>("Inverted", DaylightDetector.class, (detector) -> {
        detector.setInverted(!detector.isInverted());
        return detector;
    }, DaylightDetector::isInverted);
    /**
     * Disarmed property. (Tripwire)
     */
    public static final Property<Tripwire, Boolean> TRIPWIRE_DISARMED = new Property<>("Disarmed", Tripwire.class, (tripwire) -> {
        tripwire.setDisarmed(!tripwire.isDisarmed());
        return tripwire;
    }, Tripwire::isDisarmed);
    /**
     * Hinge property. (Door)
     */
    public static final Property<Door, Door.Hinge> DOOR_HINGE = new Property<>("Hinge", Door.class, (door) -> {
        door.setHinge(nextEnumOption(door.getHinge()));
        return door;
    }, Door::getHinge);
    /**
     * Shape property. (Stairs)
     */
    public static final Property<Stairs, Stairs.Shape> STAIR_SHAPE = new Property<>("Shape", Stairs.class, (stairs) -> {
        stairs.setShape(nextEnumOption(stairs.getShape()));
        return stairs;
    }, Stairs::getShape);
    /**
     * Part property. (Bed)
     */
    public static final Property<Bed, Bed.Part> BED_PART = new Property<>("Part", Bed.class, (bed) -> {
        bed.setPart(nextEnumOption(bed.getPart()));
        return bed;
    }, Bed::getPart);
    /**
     * Type property. (Chest)
     */
    public static final Property<Chest, Chest.Type> CHEST_TYPE = new Property<>("Type", Chest.class, (chest) -> {
        chest.setType(nextEnumOption(chest.getType()));
        return chest;
    }, Chest::getType);
    /**
     * Conditional property. (Command Block)
     */
    public static final Property<CommandBlock, Boolean> COMMAND_CONDITIONAL = new Property<>("Conditional", CommandBlock.class, (commandBlock) -> {
        commandBlock.setConditional(!commandBlock.isConditional());
        return commandBlock;
    }, CommandBlock::isConditional);
    /**
     * Mode property. (Comparator)
     */
    public static final Property<Comparator, Comparator.Mode> COMPARATOR_MODE = new Property<>("Mode", Comparator.class, (comparator) -> {
        comparator.setMode(nextEnumOption(comparator.getMode()));
        return comparator;
    }, Comparator::getMode);
    /**
     * Triggered property. (Dispenser)
     */
    public static final Property<Dispenser, Boolean> DISPENSER_TRIGGERED = new Property<>("Triggered", Dispenser.class, (dispenser) -> {
        dispenser.setTriggered(!dispenser.isTriggered());
        return dispenser;
    }, Dispenser::isTriggered);
    /**
     * Eye property. (End portal frame)
     */
    public static final Property<EndPortalFrame, Boolean> END_PORTAL_EYE = new Property<>("Eye", EndPortalFrame.class, (frame) -> {
        frame.setEye(!frame.hasEye());
        return frame;
    }, EndPortalFrame::hasEye);
    /**
     * In_wall property. (Gate)
     */
    public static final Property<Gate, Boolean> GATE_IN_WALL = new Property<>("In wall", Gate.class, (gate) -> {
        gate.setInWall(!gate.isInWall());
        return gate;
    }, Gate::isInWall);
    /**
     * Enabled property. (Hopper)
     */
    public static final Property<Hopper, Boolean> HOPPER_ENABLED = new Property<>("Enabled", Hopper.class, (hopper) -> {
        hopper.setEnabled(!hopper.isEnabled());
        return hopper;
    }, Hopper::isEnabled);
    /**
     * Extended property. (Piston)
     */
    public static final Property<Piston, Boolean> PISTON_EXTENDED = new Property<>("Extended", Piston.class, (piston) -> {
        piston.setExtended(!piston.isExtended());
        return piston;
    }, Piston::isExtended);
    /**
     * Short property. (Piston head)
     */
    public static final Property<PistonHead, Boolean> PISTON_HEAD_SHORT = new Property<>("Short", PistonHead.class, (pistonHead) -> {
        pistonHead.setShort(!pistonHead.isShort());
        return pistonHead;
    }, PistonHead::isShort);
    /**
     * Delay property. (Repeater)
     */
    public static final Property<Repeater, Integer> REPEATER_DELAY = new Property<>("Delay", Repeater.class, (repeater) -> {
        repeater.setDelay(repeater.getMaximumDelay() == repeater.getDelay() ? repeater.getMinimumDelay() : repeater.getDelay() + 1);
        return repeater;
    }, Repeater::getDelay);
    /**
     * Locked property. (Repeater)
     */
    public static final Property<Repeater, Boolean> REPEATER_LOCKED = new Property<>("Locked", Repeater.class, (repeater) -> {
        repeater.setLocked(!repeater.isLocked());
        return repeater;
    }, Repeater::isLocked);
    /**
     * Face property. (Switch)
     */
    public static final Property<Switch, Switch.Face> SWITCH_FACE = new Property<>("Face", Switch.class, (swtch) -> {
        swtch.setFace(nextEnumOption(swtch.getFace()));
        return swtch;
    }, Switch::getFace);
    /**
     * Type property. (Technical Piston)
     */
    public static final Property<TechnicalPiston, TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = new Property<>("Type", TechnicalPiston.class, (technicalPiston) -> {
        technicalPiston.setType(nextEnumOption(technicalPiston.getType()));
        return technicalPiston;
    }, TechnicalPiston::getType);
    /**
     * North property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_NORTH = new Property<>("North", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH, !multipleFacing.hasFace(BlockFace.NORTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH));
    /**
     * East property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_EAST = new Property<>("East", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST, !multipleFacing.hasFace(BlockFace.EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST));
    /**
     * South property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SOUTH = new Property<>("South", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH, !multipleFacing.hasFace(BlockFace.SOUTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH));
    /**
     * West property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_WEST = new Property<>("West", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST, !multipleFacing.hasFace(BlockFace.WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST));
    /**
     * Up property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_UP = new Property<>("Up", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.UP, !multipleFacing.hasFace(BlockFace.UP));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.UP));
    /**
     * Down property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_DOWN = new Property<>("Down", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.DOWN, !multipleFacing.hasFace(BlockFace.DOWN));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.DOWN));
    /**
     * Northeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_NORTH_EAST = new Property<>("Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_EAST));
    /**
     * Northwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_NORTH_WEST = new Property<>("Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_WEST));
    /**
     * Southeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SOUTH_EAST = new Property<>("Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_EAST));
    /**
     * Southwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SOUTH_WEST = new Property<>("Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_WEST));
    /**
     * West northwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_WEST_NORTH_WEST = new Property<>("West Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_NORTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_NORTH_WEST));
    /**
     * North northwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_NORTH_NORTH_WEST = new Property<>("North Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_WEST));
    /**
     * North northeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_NORTH_NORTH_EAST = new Property<>("North Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_EAST));
    /**
     * East northeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_EAST_NORTH_EAST = new Property<>("East Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_NORTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_NORTH_EAST));
    /**
     * East southeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_EAST_SOUTH_EAST = new Property<>("East Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_SOUTH_EAST));
    /**
     * South southeast property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_EAST = new Property<>("South Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_EAST));
    /**
     * South southwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_WEST = new Property<>("South Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_WEST));
    /**
     * West southwest property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_WEST_SOUTH_WEST = new Property<>("West Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_SOUTH_WEST));
    /**
     * Self property.
     */
    public static final Property<MultipleFacing, Boolean> MULTI_SELF = new Property<>("Self", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SELF, !multipleFacing.hasFace(BlockFace.SELF));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SELF));
    /**
     * Instrument property. (Noteblock)
     */
    public static final Property<NoteBlock, Instrument> NOTEBLOCK_INSTRUMENT = new Property<>("Instrument", NoteBlock.class, (noteblock) -> {
        noteblock.setInstrument(nextEnumOption(noteblock.getInstrument()));
        return noteblock;
    }, NoteBlock::getInstrument);
    /**
     * Note property. (Noteblock)
     */
    public static final Property<NoteBlock, Note> NOTEBLOCK_NOTE = new Property<>("Note", NoteBlock.class, (noteblock) -> {
        int id = noteblock.getNote().getId();
        noteblock.setNote(new Note(id == 24 ? 0 : id + 1));
        return noteblock;
    }, NoteBlock::getNote);
    /**
     * Shape property. (Rail)
     */
    public static final Property<Rail, Rail.Shape> RAIL_SHAPE = new Property<>("Shape", Rail.class, (rail) -> {
        rail.setShape(nextEnumOption(rail.getShape(), rail.getShapes()));
        return rail;
    }, Rail::getShape);
    /**
     * Pickles property. (Sea pickles)
     */
    public static final Property<SeaPickle, Integer> SEA_PICKLE_PICKLES = new Property<>("Pickles", SeaPickle.class, (pickle) -> {
        pickle.setPickles(pickle.getMaximumPickles() == pickle.getPickles() ? pickle.getMinimumPickles() : pickle.getPickles() + 1);
        return pickle;
    }, SeaPickle::getPickles);
    /**
     * Type property. (Slab)
     */
    public static final Property<Slab, Slab.Type> SLAB_TYPE = new Property<>("Type", Slab.class, (slab) -> {
        slab.setType(nextEnumOption(slab.getType()));
        return slab;
    }, Slab::getType);
    /**
     * Bottle_0 property. (Brewing stand)
     */
    public static final Property<BrewingStand, Boolean> BREWING_BOTTLE_1 = new Property<>("Bottle 0", BrewingStand.class, (stand) -> {
        stand.setBottle(0, !stand.hasBottle(0));
        return stand;
    }, (bottle) -> bottle.hasBottle(0));
    /**
     * Bottle_1 property. (Brewing stand)
     */
    public static final Property<BrewingStand, Boolean> BREWING_BOTTLE_2 = new Property<>("Bottle 1", BrewingStand.class, (stand) -> {
        stand.setBottle(1, !stand.hasBottle(1));
        return stand;
    }, (bottle) -> bottle.hasBottle(1));
    /**
     * Bottle_2 property. (Brewing stand)
     */
    public static final Property<BrewingStand, Boolean> BREWING_BOTTLE_3 = new Property<>("Bottle 2", BrewingStand.class, (stand) -> {
        stand.setBottle(2, !stand.hasBottle(2));
        return stand;
    }, (bottle) -> bottle.hasBottle(2));
    /**
     * Drag property. (Bubble column)
     */
    public static final Property<BubbleColumn, Boolean> BUBBLE_COLUMN_DRAG = new Property<>("Drag", BubbleColumn.class, (column) -> {
        column.setDrag(!column.isDrag());
        return column;
    }, BubbleColumn::isDrag);
    /**
     * Bites property. (Cake)
     */
    public static final Property<Cake, Integer> CAKE_BITES = new Property<>("Bites", Cake.class, (cake) -> {
        cake.setBites(cake.getMaximumBites() == cake.getBites() ? 0 : cake.getBites() + 1);
        return cake;
    }, Cake::getBites);
    /**
     * Moisture property. (Farmland)
     */
    public static final Property<Farmland, Integer> FARMLAND_MOISTURE = new Property<>("Moisture", Farmland.class, (farmland) -> {
        farmland.setMoisture(farmland.getMaximumMoisture() == farmland.getMoisture() ? 0 : farmland.getMoisture() + 1);
        return farmland;
    }, Farmland::getMoisture);
    /**
     * Distance property. (Leaves)
     */
    public static final Property<Leaves, Integer> LEAVES_DISTANCE = new Property<>("Distance", Leaves.class, (leaves) -> {
        leaves.setDistance(7 == leaves.getDistance() ? 1 : leaves.getDistance() + 1);
        return leaves;
    }, Leaves::getDistance);
    /**
     * Persistent property. (Leaves)
     */
    public static final Property<Leaves, Boolean> LEAVES_PERSISTENT = new Property<>("Persistent", Leaves.class, (leaves) -> {
        leaves.setPersistent(!leaves.isPersistent());
        return leaves;
    }, Leaves::isPersistent);
    /**
     * Stage property. (Sapling)
     */
    public static final Property<Sapling, Integer> SAPLING_STAGE = new Property<>("Stage", Sapling.class, (sapling) -> {
        sapling.setStage(sapling.getMaximumStage() == sapling.getStage() ? 0 : sapling.getStage() + 1);
        return sapling;
    }, Sapling::getStage);
    /**
     * Layers property. (Snow)
     */
    public static final Property<Snow, Integer> SNOW_LAYERS = new Property<>("Layers", Snow.class, (snow) -> {
        snow.setLayers(snow.getMaximumLayers() == snow.getLayers() ? snow.getMinimumLayers() : snow.getLayers() + 1);
        return snow;
    }, Snow::getLayers);
    /**
     * Mode property. (Structure block)
     */
    public static final Property<StructureBlock, StructureBlock.Mode> STRUCTURE_BLOCK_MODE = new Property<>("Mode", StructureBlock.class, (structureBlock) -> {
        structureBlock.setMode(nextEnumOption(structureBlock.getMode()));
        return structureBlock;
    }, StructureBlock::getMode);
    /**
     * Unstable property. (TNT)
     */
    public static final Property<TNT, Boolean> TNT_UNSTABLE = new Property<>("Unstable", TNT.class, (tnt) -> {
        tnt.setUnstable(!tnt.isUnstable());
        return tnt;
    }, TNT::isUnstable);
    /**
     * Hatch property. (Turtle eggs)
     */
    public static final Property<TurtleEgg, Integer> TURTLE_EGG_HATCH = new Property<>("Hatch", TurtleEgg.class, (egg) -> {
        egg.setHatch(egg.getMaximumHatch() == egg.getHatch() ? 0 : egg.getHatch() + 1);
        return egg;
    }, TurtleEgg::getHatch);
    /**
     * Eggs property. (Turtle eggs)
     */
    public static final Property<TurtleEgg, Integer> TURTLE_EGG_EGGS = new Property<>("Eggs", TurtleEgg.class, (egg) -> {
        egg.setEggs(egg.getMaximumEggs() == egg.getEggs() ? egg.getMinimumEggs() : egg.getEggs() + 1);
        return egg;
    }, TurtleEgg::getEggs);

    static {
        //Loading the Field Map
        for (Field field : Property.class.getFields()) {
            if (!field.getType().equals(Property.class)) continue;
            for (Property<?, ?> prop : PROPERTY_FIELD_PRE_MAP) {
                try {
                    if (field.get(null).equals(prop)) PROPERTY_FIELD_MAP.put(field.getName(), prop);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //Loading the Property Map
        for (Material mat : Material.values()) {
            if (!mat.isBlock()) continue;
            Set<Property<?, ?>> properties = new LinkedHashSet<>();
            BlockData data = mat.createBlockData();
            for (Property<?, ?> prop : values()) {
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

    private Property(String niceName, Class<D> dataType, Function<D, D> next, Function<D, V> current) {
        this.ordinal = PROPERTY_FIELD_PRE_MAP.size();
        PROPERTY_FIELD_PRE_MAP.add(this);
        this.niceName = niceName;
        this.dataType = dataType;
        this.current = current;
        this.next = next;
    }

    /**
     * The constant ordinal
     *
     * @return The constant ordinal
     */
    public int ordinal() {
        return ordinal;
    }

    /**
     * Gets the nice name of this property.
     *
     * @return The property nice name
     */
    public String getNiceName() {
        return niceName;
    }

    /**
     * Gets the corresponding class type of the property.
     *
     * @return The class type
     */
    public Class<D> getDataType() {
        return dataType;
    }

    /**
     * Check if a specific BlockData object is an instance of this property's data type.
     *
     * @param block Whether the block data has this specific property
     * @return True if the block data has this property.
     */
    public boolean checkBlockData(BlockData block) {
        return dataType.isInstance(block);
    }

    /**
     * Gets the block data fromBlockData one block to the data type of this property.
     *
     * @param block The original block data.
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    public D fromBlockData(Block block) {
        return checkBlockData(block.getBlockData()) ? (D) block.getBlockData() : null;
    }

    /**
     * Gets the next BlockData for this property
     *
     * @param block The previous data
     * @return The next data
     */
    public D nextBlockData(Block block) {
        return checkBlockData(block.getBlockData()) ? next.apply((D) block.getBlockData()) : null;
    }

    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The current value of this property as a nice string
     */
    public String getNiceCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        BlockData data = fromBlockData(block);
        if (data == null) return "";
        String property = niceName.replace(" ", "_").toLowerCase();
        String split = data.getAsString().split(property + "=")[1];
        return split.substring(0, !split.contains(",") ? split.length() - 1 : split.indexOf(","));
    }

    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The next value of this property as a nice string
     */
    public String getNiceNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        BlockData data = nextBlockData(block);
        if (data == null) return "";
        String property = niceName.replace(" ", "_").toLowerCase();
        String split = data.getAsString().split(property + "=")[1];
        return split.substring(0, !split.contains(",") ? split.length() - 1 : split.indexOf(","));
    }

    /**
     * Gets the current value of this property fromBlockData the given block.
     *
     * @param block The block to get the value of the current property of.
     * @return The current property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    public V getCurrentValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(fromBlockData(block));
    }

    /**
     * Gets the next value of this property fromBlockData the given block.
     *
     * @param block The block to get the next value of the current property of.
     * @return The next property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    public V getNextValue(Block block) {
        if (!isApplicableTo(block))
            throw new RuntimeException("Property \"" + getNiceName() + "\" is not applicable for block " + block.getType().name());
        return current.apply(nextBlockData(block));
    }

    /**
     * Checks if a specific block has this particular property
     *
     * @param block The block to check.
     * @return False if the block does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    public boolean isApplicableTo(Block block) {
        return isApplicableTo(block.getType());
    }

    /**
     * Checks if a specific material has this particular property
     *
     * @param material The material to check.
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    public boolean isApplicableTo(Material material) {
        if (hasProperties(material)) {
            return getProperties(material).contains(this);
        } else return false;
    }

    /**
     * Gets all the properties
     *
     * @return An array of all the properties
     */
    public static Property<?, ?>[] values() {
        return Collections.unmodifiableCollection(PROPERTY_FIELD_MAP.values()).toArray(new Property[0]);
    }

    /**
     * Check whether a specific block has any properties.
     *
     * @param block The block to checkBlockData
     * @return True if the block has (editable) properties, false otherwise.
     */
    public static boolean hasProperties(Block block) {
        return hasProperties(block.getType());
    }

    /**
     * Check whether a specific material has any properties.
     *
     * @param material The material to checkBlockData
     * @return True if the material has (editable) properties, false otherwise.
     */
    public static boolean hasProperties(Material material) {
        return PROPERTIES.containsKey(material) && !PROPERTIES.get(material).isEmpty();
    }

    /**
     * Gets a list of (editable) properties this block has.
     *
     * @param block The block to get the list of properties of.
     * @return The list of properties, or an empty list if this block has no (editable) properties.
     */
    public static List<Property<?, ?>> getProperties(Block block) {
        return getProperties(block.getType());
    }

    /**
     * Get a list of (editable) properties this material has, if any.
     *
     * @param material The material to get the list of properties of.
     * @return The list of properties, or an empty list if the block has no (editable) properties.
     */
    public static List<Property<?, ?>> getProperties(Material material) {
        return !hasProperties(material) ? new ArrayList<>() : PROPERTIES.get(material);
    }

    /**
     * Gets a map of all bukkit materials which have properies, and a list of their (editable) properties.
     *
     * @return A map of bukkit materials which have editable properties and a list of their editable properties.
     */
    public static Map<Material, List<Property<?, ?>>> getMaterialProperties() {
        return Collections.unmodifiableMap(PROPERTIES);
    }

    private static <E extends Enum<E>> E nextEnumOption(E current) {
        Class<E> enumeration = current.getDeclaringClass();
        int size = enumeration.getEnumConstants().length;
        if (current.ordinal() == size - 1) return enumeration.getEnumConstants()[0];
        else return enumeration.getEnumConstants()[current.ordinal() + 1];
    }

    private static <E extends Enum<E>> E nextEnumOption(E current, Set<E> allowed) {
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
