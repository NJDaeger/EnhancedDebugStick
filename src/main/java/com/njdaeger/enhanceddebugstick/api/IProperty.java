package com.njdaeger.enhanceddebugstick.api;

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
    }, Ageable::getAge);
    /**
     * Power property
     */
    IProperty<AnaloguePowerable, Integer> POWER = new Property<>("Power", AnaloguePowerable.class, (power) -> {
        power.setPower(power.getMaximumPower() == power.getPower() ? 0 : power.getPower() + 1);
        return power;
    }, AnaloguePowerable::getPower);
    /**
     * Attached property
     */
    IProperty<Attachable, Boolean> ATTACHED = new Property<>("Attached", Attachable.class, (attachable) -> {
        attachable.setAttached(!attachable.isAttached());
        return attachable;
    }, Attachable::isAttached);
    /**
     * Half property
     */
    IProperty<Bisected, Bisected.Half> HALF = new Property<>("Half", Bisected.class, (bisected) -> {
        bisected.setHalf(nextEnumOption(bisected.getHalf()));
        return bisected;
    }, Bisected::getHalf);
    /**
     * Facing property
     */
    IProperty<Directional, BlockFace> FACING = new Property<>("Facing", Directional.class, (directional) -> {
        directional.setFacing(nextEnumOption(directional.getFacing(), directional.getFaces()));
        return directional;
    }, Directional::getFacing);
    /**
     * Level property
     */
    IProperty<Levelled, Integer> LEVEL = new Property<>("Level", Levelled.class, (level) -> {
        level.setLevel(level.getMaximumLevel() == level.getLevel() ? 0 : level.getLevel() + 1);
        return level;
    }, Levelled::getLevel);
    /**
     * Lit property
     */
    IProperty<Lightable, Boolean> LIT = new Property<>("Lit", Lightable.class, (lit) -> {
        lit.setLit(!lit.isLit());
        return lit;
    }, Lightable::isLit);
    /**
     * Open property
     */
    IProperty<Openable, Boolean> OPEN = new Property<>("Open", Openable.class, (open) -> {
        open.setOpen(!open.isOpen());
        return open;
    }, Openable::isOpen);
    /**
     * Axis property
     */
    IProperty<Orientable, Axis> AXIS = new Property<>("Axis", Orientable.class, (axis) -> {
        axis.setAxis(nextEnumOption(axis.getAxis(), axis.getAxes()));
        return axis;
    }, Orientable::getAxis);
    /**
     * Powered property
     */
    IProperty<Powerable, Boolean> POWERED = new Property<>("Powered", Powerable.class, (powered) -> {
        powered.setPowered(!powered.isPowered());
        return powered;
    }, Powerable::isPowered);
    /**
     * Rotation property
     */
    IProperty<Rotatable, BlockFace> ROTATION = new Property<>("Rotation", Rotatable.class, (rotation) -> {
        BlockFace face = nextEnumOption(rotation.getRotation());
        int x = 0;
        while (x < 500) {
            x++;
            try {
                rotation.setRotation(face);
                break;
            } catch (IllegalArgumentException ignored) {
                face = nextEnumOption(face);
            }
        }
        return rotation;
    }, Rotatable::getRotation);
    /**
     * Snowy property
     */
    IProperty<Snowable, Boolean> SNOWY = new Property<>("Snowy", Snowable.class, (snowable) -> {
        snowable.setSnowy(!snowable.isSnowy());
        return snowable;
    }, Snowable::isSnowy);
    /**
     * Waterlogged property
     */
    IProperty<Waterlogged, Boolean> WATERLOGGED = new Property<>("Waterlogged", Waterlogged.class, (waterlogged) -> {
        waterlogged.setWaterlogged(!waterlogged.isWaterlogged());
        return waterlogged;
    }, Waterlogged::isWaterlogged);


    /**
     * East connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_EAST = new Property<>("East", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.EAST, nextEnumOption(wire.getFace(BlockFace.EAST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.EAST));
    /**
     * North connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_NORTH = new Property<>("North", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.NORTH, nextEnumOption(wire.getFace(BlockFace.NORTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.NORTH));
    /**
     * South connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_SOUTH = new Property<>("South", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.SOUTH, nextEnumOption(wire.getFace(BlockFace.SOUTH)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.SOUTH));
    /**
     * West connection property. (Redstone Wire)
     */
    IProperty<RedstoneWire, RedstoneWire.Connection> REDSTONE_WEST = new Property<>("West", RedstoneWire.class, (wire) -> {
        wire.setFace(BlockFace.WEST, nextEnumOption(wire.getFace(BlockFace.WEST)));
        return wire;
    }, (wire) -> wire.getFace(BlockFace.WEST));
    /**
     * Inverted property. (Daylight Detector)
     */
    IProperty<DaylightDetector, Boolean> DETECTOR_INVERTED = new Property<>("Inverted", DaylightDetector.class, (detector) -> {
        detector.setInverted(!detector.isInverted());
        return detector;
    }, DaylightDetector::isInverted);
    /**
     * Disarmed property. (Tripwire)
     */
    IProperty<Tripwire, Boolean> TRIPWIRE_DISARMED = new Property<>("Disarmed", Tripwire.class, (tripwire) -> {
        tripwire.setDisarmed(!tripwire.isDisarmed());
        return tripwire;
    }, Tripwire::isDisarmed);
    /**
     * Hinge property. (Door)
     */
    IProperty<Door, Door.Hinge> DOOR_HINGE = new Property<>("Hinge", Door.class, (door) -> {
        door.setHinge(nextEnumOption(door.getHinge()));
        return door;
    }, Door::getHinge);
    /**
     * Shape property. (Stairs)
     */
    IProperty<Stairs, Stairs.Shape> STAIR_SHAPE = new Property<>("Shape", Stairs.class, (stairs) -> {
        stairs.setShape(nextEnumOption(stairs.getShape()));
        return stairs;
    }, Stairs::getShape);
    /**
     * Part property. (Bed)
     */
    IProperty<Bed, Bed.Part> BED_PART = new Property<>("Part", Bed.class, (bed) -> {
        bed.setPart(nextEnumOption(bed.getPart()));
        return bed;
    }, Bed::getPart);
    /**
     * Type property. (Chest)
     */
    IProperty<Chest, Chest.Type> CHEST_TYPE = new Property<>("Type", Chest.class, (chest) -> {
        chest.setType(nextEnumOption(chest.getType()));
        return chest;
    }, Chest::getType);
    /**
     * Conditional property. (Command Block)
     */
    IProperty<CommandBlock, Boolean> COMMAND_CONDITIONAL = new Property<>("Conditional", CommandBlock.class, (commandBlock) -> {
        commandBlock.setConditional(!commandBlock.isConditional());
        return commandBlock;
    }, CommandBlock::isConditional);
    /**
     * Mode property. (Comparator)
     */
    IProperty<Comparator, Comparator.Mode> COMPARATOR_MODE = new Property<>("Mode", Comparator.class, (comparator) -> {
        comparator.setMode(nextEnumOption(comparator.getMode()));
        return comparator;
    }, Comparator::getMode);
    /**
     * Triggered property. (Dispenser)
     */
    IProperty<Dispenser, Boolean> DISPENSER_TRIGGERED = new Property<>("Triggered", Dispenser.class, (dispenser) -> {
        dispenser.setTriggered(!dispenser.isTriggered());
        return dispenser;
    }, Dispenser::isTriggered);
    /**
     * Eye property. (End portal frame)
     */
    IProperty<EndPortalFrame, Boolean> END_PORTAL_EYE = new Property<>("Eye", EndPortalFrame.class, (frame) -> {
        frame.setEye(!frame.hasEye());
        return frame;
    }, EndPortalFrame::hasEye);
    /**
     * In_wall property. (Gate)
     */
    IProperty<Gate, Boolean> GATE_IN_WALL = new Property<>("In wall", Gate.class, (gate) -> {
        gate.setInWall(!gate.isInWall());
        return gate;
    }, Gate::isInWall);
    /**
     * Enabled property. (Hopper)
     */
    IProperty<Hopper, Boolean> HOPPER_ENABLED = new Property<>("Enabled", Hopper.class, (hopper) -> {
        hopper.setEnabled(!hopper.isEnabled());
        return hopper;
    }, Hopper::isEnabled);
    /**
     * Extended property. (Piston)
     */
    IProperty<Piston, Boolean> PISTON_EXTENDED = new Property<>("Extended", Piston.class, (piston) -> {
        piston.setExtended(!piston.isExtended());
        return piston;
    }, Piston::isExtended);
    /**
     * Short property. (Piston head)
     */
    IProperty<PistonHead, Boolean> PISTON_HEAD_SHORT = new Property<>("Short", PistonHead.class, (pistonHead) -> {
        pistonHead.setShort(!pistonHead.isShort());
        return pistonHead;
    }, PistonHead::isShort);
    /**
     * Delay property. (Repeater)
     */
    IProperty<Repeater, Integer> REPEATER_DELAY = new Property<>("Delay", Repeater.class, (repeater) -> {
        repeater.setDelay(repeater.getMaximumDelay() == repeater.getDelay() ? repeater.getMinimumDelay() : repeater.getDelay() + 1);
        return repeater;
    }, Repeater::getDelay);
    /**
     * Locked property. (Repeater)
     */
    IProperty<Repeater, Boolean> REPEATER_LOCKED = new Property<>("Locked", Repeater.class, (repeater) -> {
        repeater.setLocked(!repeater.isLocked());
        return repeater;
    }, Repeater::isLocked);
    /**
     * Face property. (Switch)
     */
    IProperty<Switch, Switch.Face> SWITCH_FACE = new Property<>("Face", Switch.class, (swtch) -> {
        swtch.setFace(nextEnumOption(swtch.getFace()));
        return swtch;
    }, Switch::getFace);
    /**
     * Type property. (Technical Piston)
     */
    IProperty<TechnicalPiston, TechnicalPiston.Type> TECHNICAL_PISTON_TYPE = new Property<>("Type", TechnicalPiston.class, (technicalPiston) -> {
        technicalPiston.setType(nextEnumOption(technicalPiston.getType()));
        return technicalPiston;
    }, TechnicalPiston::getType);
    /**
     * North property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH = new Property<>("North", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH, !multipleFacing.hasFace(BlockFace.NORTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH));
    /**
     * East property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST = new Property<>("East", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST, !multipleFacing.hasFace(BlockFace.EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST));
    /**
     * South property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH = new Property<>("South", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH, !multipleFacing.hasFace(BlockFace.SOUTH));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH));
    /**
     * West property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST = new Property<>("West", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST, !multipleFacing.hasFace(BlockFace.WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST));
    /**
     * Up property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_UP = new Property<>("Up", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.UP, !multipleFacing.hasFace(BlockFace.UP));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.UP));
    /**
     * Down property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_DOWN = new Property<>("Down", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.DOWN, !multipleFacing.hasFace(BlockFace.DOWN));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.DOWN));
    /**
     * Northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_EAST = new Property<>("Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_EAST));
    /**
     * Northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_WEST = new Property<>("Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_WEST));
    /**
     * Southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_EAST = new Property<>("Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_EAST));
    /**
     * Southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_WEST = new Property<>("Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_WEST));
    /**
     * West northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST_NORTH_WEST = new Property<>("West Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_NORTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_NORTH_WEST));
    /**
     * North northwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_WEST = new Property<>("North Northwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_WEST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_WEST));
    /**
     * North northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_NORTH_NORTH_EAST = new Property<>("North Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.NORTH_NORTH_EAST, !multipleFacing.hasFace(BlockFace.NORTH_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.NORTH_NORTH_EAST));
    /**
     * East northeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST_NORTH_EAST = new Property<>("East Northeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_NORTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_NORTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_NORTH_EAST));
    /**
     * East southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_EAST_SOUTH_EAST = new Property<>("East Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.EAST_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.EAST_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.EAST_SOUTH_EAST));
    /**
     * South southeast property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_EAST = new Property<>("South Southeast", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_EAST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_EAST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_EAST));
    /**
     * South southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SOUTH_SOUTH_WEST = new Property<>("South Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SOUTH_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.SOUTH_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SOUTH_SOUTH_WEST));
    /**
     * West southwest property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_WEST_SOUTH_WEST = new Property<>("West Southwest", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.WEST_SOUTH_WEST, !multipleFacing.hasFace(BlockFace.WEST_SOUTH_WEST));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.WEST_SOUTH_WEST));
    /**
     * Self property.
     */
    IProperty<MultipleFacing, Boolean> MULTI_SELF = new Property<>("Self", MultipleFacing.class, (multipleFacing) -> {
        multipleFacing.setFace(BlockFace.SELF, !multipleFacing.hasFace(BlockFace.SELF));
        return multipleFacing;
    }, (face) -> face.hasFace(BlockFace.SELF));
    /**
     * Instrument property. (Noteblock)
     */
    IProperty<NoteBlock, Instrument> NOTEBLOCK_INSTRUMENT = new Property<>("Instrument", NoteBlock.class, (noteblock) -> {
        noteblock.setInstrument(nextEnumOption(noteblock.getInstrument()));
        return noteblock;
    }, NoteBlock::getInstrument);
    /**
     * Note property. (Noteblock)
     */
    IProperty<NoteBlock, Note> NOTEBLOCK_NOTE = new Property<>("Note", NoteBlock.class, (noteblock) -> {
        int id = noteblock.getNote().getId();
        noteblock.setNote(new Note(id == 24 ? 0 : id + 1));
        return noteblock;
    }, NoteBlock::getNote);
    /**
     * Shape property. (Rail)
     */
    IProperty<Rail, Rail.Shape> RAIL_SHAPE = new Property<>("Shape", Rail.class, (rail) -> {
        rail.setShape(nextEnumOption(rail.getShape(), rail.getShapes()));
        return rail;
    }, Rail::getShape);
    /**
     * Pickles property. (Sea pickles)
     */
    IProperty<SeaPickle, Integer> SEA_PICKLE_PICKLES = new Property<>("Pickles", SeaPickle.class, (pickle) -> {
        pickle.setPickles(pickle.getMaximumPickles() == pickle.getPickles() ? pickle.getMinimumPickles() : pickle.getPickles() + 1);
        return pickle;
    }, SeaPickle::getPickles);
    /**
     * Type property. (Slab)
     */
    IProperty<Slab, Slab.Type> SLAB_TYPE = new Property<>("Type", Slab.class, (slab) -> {
        slab.setType(nextEnumOption(slab.getType()));
        return slab;
    }, Slab::getType);
    /**
     * Bottle_0 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_1 = new Property<>("Bottle 0", BrewingStand.class, (stand) -> {
        stand.setBottle(0, !stand.hasBottle(0));
        return stand;
    }, (bottle) -> bottle.hasBottle(0));
    /**
     * Bottle_1 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_2 = new Property<>("Bottle 1", BrewingStand.class, (stand) -> {
        stand.setBottle(1, !stand.hasBottle(1));
        return stand;
    }, (bottle) -> bottle.hasBottle(1));
    /**
     * Bottle_2 property. (Brewing stand)
     */
    IProperty<BrewingStand, Boolean> BREWING_BOTTLE_3 = new Property<>("Bottle 2", BrewingStand.class, (stand) -> {
        stand.setBottle(2, !stand.hasBottle(2));
        return stand;
    }, (bottle) -> bottle.hasBottle(2));
    /**
     * Drag property. (Bubble column)
     */
    IProperty<BubbleColumn, Boolean> BUBBLE_COLUMN_DRAG = new Property<>("Drag", BubbleColumn.class, (column) -> {
        column.setDrag(!column.isDrag());
        return column;
    }, BubbleColumn::isDrag);
    /**
     * Bites property. (Cake)
     */
    IProperty<Cake, Integer> CAKE_BITES = new Property<>("Bites", Cake.class, (cake) -> {
        cake.setBites(cake.getMaximumBites() == cake.getBites() ? 0 : cake.getBites() + 1);
        return cake;
    }, Cake::getBites);
    /**
     * Moisture property. (Farmland)
     */
    IProperty<Farmland, Integer> FARMLAND_MOISTURE = new Property<>("Moisture", Farmland.class, (farmland) -> {
        farmland.setMoisture(farmland.getMaximumMoisture() == farmland.getMoisture() ? 0 : farmland.getMoisture() + 1);
        return farmland;
    }, Farmland::getMoisture);
    /**
     * Distance property. (Leaves)
     */
    IProperty<Leaves, Integer> LEAVES_DISTANCE = new Property<>("Distance", Leaves.class, (leaves) -> {
        leaves.setDistance(7 == leaves.getDistance() ? 1 : leaves.getDistance() + 1);
        return leaves;
    }, Leaves::getDistance);
    /**
     * Persistent property. (Leaves)
     */
    IProperty<Leaves, Boolean> LEAVES_PERSISTENT = new Property<>("Persistent", Leaves.class, (leaves) -> {
        leaves.setPersistent(!leaves.isPersistent());
        return leaves;
    }, Leaves::isPersistent);
    /**
     * Stage property. (Sapling)
     */
    IProperty<Sapling, Integer> SAPLING_STAGE = new Property<>("Stage", Sapling.class, (sapling) -> {
        sapling.setStage(sapling.getMaximumStage() == sapling.getStage() ? 0 : sapling.getStage() + 1);
        return sapling;
    }, Sapling::getStage);
    /**
     * Layers property. (Snow)
     */
    IProperty<Snow, Integer> SNOW_LAYERS = new Property<>("Layers", Snow.class, (snow) -> {
        snow.setLayers(snow.getMaximumLayers() == snow.getLayers() ? snow.getMinimumLayers() : snow.getLayers() + 1);
        return snow;
    }, Snow::getLayers);
    /**
     * Mode property. (Structure block)
     */
    IProperty<StructureBlock, StructureBlock.Mode> STRUCTURE_BLOCK_MODE = new Property<>("Mode", StructureBlock.class, (structureBlock) -> {
        structureBlock.setMode(nextEnumOption(structureBlock.getMode()));
        return structureBlock;
    }, StructureBlock::getMode);
    /**
     * Unstable property. (TNT)
     */
    IProperty<TNT, Boolean> TNT_UNSTABLE = new Property<>("Unstable", TNT.class, (tnt) -> {
        tnt.setUnstable(!tnt.isUnstable());
        return tnt;
    }, TNT::isUnstable);
    /**
     * Hatch property. (Turtle eggs)
     */
    IProperty<TurtleEgg, Integer> TURTLE_EGG_HATCH = new Property<>("Hatch", TurtleEgg.class, (egg) -> {
        egg.setHatch(egg.getMaximumHatch() == egg.getHatch() ? 0 : egg.getHatch() + 1);
        return egg;
    }, TurtleEgg::getHatch);
    /**
     * Eggs property. (Turtle eggs)
     */
    IProperty<TurtleEgg, Integer> TURTLE_EGG_EGGS = new Property<>("Eggs", TurtleEgg.class, (egg) -> {
        egg.setEggs(egg.getMaximumEggs() == egg.getEggs() ? egg.getMinimumEggs() : egg.getEggs() + 1);
        return egg;
    }, TurtleEgg::getEggs);

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
     * Gets the block data fromBlockData one block to the data type of this property.
     *
     * @param block The original block data.
     * @return The block data as this property's data type, or null if it is unable to be casted
     */
    D fromBlockData(Block block);

    /**
     * Gets the next BlockData for this property
     *
     * @param block The previous data
     * @return The next data
     */
    D nextBlockData(Block block);

    /**
     * Gets the data value currently associated with this property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The current value of this property as a nice string
     */
    String getNiceCurrentValue(Block block);

    /**
     * Gets the data value which is next in line for this particular property as a nice string.
     *
     * @param block The block to get the data from.
     * @return The next value of this property as a nice string
     */
    String getNiceNextValue(Block block);

    /**
     * Gets the current value of this property fromBlockData the given block.
     *
     * @param block The block to get the value of the current property of.
     * @return The current property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getCurrentValue(Block block);

    /**
     * Gets the next value of this property fromBlockData the given block.
     *
     * @param block The block to get the next value of the current property of.
     * @return The next property value of this block.
     * @throws RuntimeException If this property is not applicable to the given block.
     */
    V getNextValue(Block block);

    /**
     * Checks if a specific block has this particular property
     *
     * @param block The block to check.
     * @return False if the block does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    boolean isApplicableTo(Block block);

    /**
     * Checks if a specific material has this particular property
     *
     * @param material The material to check.
     * @return False if the material does not have any properties, or if its property list doesnt contain this specific
     *         property.
     */
    boolean isApplicableTo(Material material);

    /**
     * Gets a map of all bukkit materials which have properies, and a list of their (editable) properties.
     *
     * @return A map of bukkit materials which have editable properties and a list of their editable properties.
     */
    static Map<Material, List<IProperty<?, ?>>> getMaterialProperties() {
        return Property.getMaterialProperties();
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
     * Check whether a specific material has any properties.
     *
     * @param material The material to checkBlockData
     * @return True if the material has (editable) properties, false otherwise.
     */
    static boolean hasProperties(Material material) {
        return Property.hasProperties(material);
    }

    /**
     * Check whether a specific block has any properties.
     *
     * @param block The block to checkBlockData
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
