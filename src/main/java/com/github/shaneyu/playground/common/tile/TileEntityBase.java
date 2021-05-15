package com.github.shaneyu.playground.common.tile;

import com.github.shaneyu.playground.Playground;
import com.github.shaneyu.playground.client.sound.SoundHandler;
import com.github.shaneyu.playground.common.block.attribute.Attribute;
import com.github.shaneyu.playground.common.block.attribute.AttributeSound;
import com.github.shaneyu.playground.common.block.attribute.AttributeStateActive;
import com.github.shaneyu.playground.common.block.attribute.AttributeStateFacing;
import com.github.shaneyu.playground.common.block.interfaces.IHasTileEntity;
import com.github.shaneyu.playground.common.config.PlaygroundConfig;
import com.github.shaneyu.playground.common.tile.interfaces.ITileActive;
import com.github.shaneyu.playground.common.tile.interfaces.ITileDirectional;
import com.github.shaneyu.playground.common.tile.interfaces.ITileSound;
import com.github.shaneyu.playground.common.util.*;
import com.github.shaneyu.playground.lib.item.IWrench;
import com.github.shaneyu.playground.lib.provider.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.IntSupplier;

public abstract class TileEntityBase extends TileEntityUpdatable implements ITickableTileEntity, ITileActive, ITileDirectional, ITileSound {
    protected final IBlockProvider blockProvider;

    private boolean isActivatable;
    private boolean isDirectional;
    private boolean hasSound;

    /** A timer used to send packets to clients */
    public int ticker;

    // ITileActivatable
    private boolean currentActive;
    private int updateDelay;
    protected IntSupplier delaySupplier = PlaygroundConfig.general.blockDeactivationDelay::get;

    // ITileSound
    @Nullable
    private final SoundEvent soundEvent;

    /** Only used on the client */
    private ISound activeSound;
    private int playSoundCooldown = 0;

    public TileEntityBase(IBlockProvider blockProvider) {
        super(((IHasTileEntity<? extends TileEntity>) blockProvider.getBlock()).getTileType());
        this.blockProvider = blockProvider;

        setSupportedFeatures(this.blockProvider.getBlock());
        presetVariables();

        if (hasSound()) {
            //noinspection ConstantConditions
            soundEvent = Attribute.get(blockProvider.getBlock(), AttributeSound.class).getSoundEvent();
        } else {
            soundEvent = null;
        }
    }

    private void setSupportedFeatures(Block block) {
        isDirectional = Attribute.has(block, AttributeStateFacing.class);
        hasSound = Attribute.has(block, AttributeSound.class);
        isActivatable = hasSound || Attribute.has(block, AttributeStateActive.class);
    }

    /**
     * Sets variables up, called immediately after {@link #setSupportedFeatures(Block)} but before any things start being created.
     *
     * This method should be used for setting any variables that would normally be set directly, except that gets run to late to set things up properly in our
     * constructor.
     */
    protected void presetVariables() {}

    public Block getBlockType() {
        return blockProvider.getBlock();
    }

    /** Update call for machines. Use instead of updateEntity -- it's called every tick on the client side */
    protected void onUpdateClient() {}

    /** Update call for machines. Use instead of updateEntity -- it's called every tick on the server side */
    protected void onUpdateServer() {}

    /** Called when block is placed by player */
    public void onPlaced() {}

    /** Called when block is placed in the world */
    public void onAdded() {}

    /** Called when a neighbor block changes */
    public void onNeighborChange(Block block, BlockPos neighborPos) {}

    public ITextComponent getName() {
        return TextComponentUtil.translate(Util.makeTranslationKey("container", getBlockType().getRegistryName()));
    }

    @Override
    public final boolean isActivatable() {
        return isActivatable;
    }

    @Override
    public final boolean isDirectional() {
        return isDirectional;
    }

    @Override
    public final boolean hasSound() {
        return hasSound;
    }

    @Override
    public void tick() {
        if (isRemote()) {
            if (hasSound()) {
                updateSound();
            }

            if (isActivatable()) {
                if (ticker == 0) {
                    WorldUtil.updateBlock(getWorld(), getPos(), this);
                }
            }

            onUpdateClient();
        } else {
            if (isActivatable()) {
                if (updateDelay > 0) {
                    updateDelay--;

                    if (updateDelay == 0 && getClientActive() != currentActive) {
                        // If it doesn't match and we are done with the delay period, then update it
                        // noinspection ConstantConditions
                        world.setBlockState(pos, Attribute.setActive(getBlockState(), currentActive));
                    }
                }
            }

            onUpdateServer();
        }

        ticker++;
    }

    @Override
    public void remove() {
        super.remove();

        if (isRemote() && hasSound()) {
            updateSound();
        }
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);

        if (isActivatable()) {
            NBTUtil.setBooleanIfPresent(nbt, NBTConstants.ACTIVE_STATE, value -> currentActive = value);
            NBTUtil.setIntIfPresent(nbt, NBTConstants.UPDATE_DELAY, value -> updateDelay = value);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);

        if (isActivatable()) {
            compound.putBoolean(NBTConstants.ACTIVE_STATE, currentActive);
            compound.putInt(NBTConstants.UPDATE_DELAY, updateDelay);
        }

        return compound;
    }

    public WrenchResult tryWrench(BlockState state, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty()) {
            IWrench wrenchHandler = PlaygroundUtil.getWrench(stack);

            if (wrenchHandler != null) {
                if (wrenchHandler.canUseWrench(stack, player, rayTrace.getPos())) {
                    if (player.isSneaking()) {
                        WorldUtil.dismantleBlock(state, getWorld(), pos, this);

                        return WrenchResult.DISMANTLED;
                    }

                    // Special ITileDirectional handling
                    if (isDirectional() && Attribute.get(getBlockType(), AttributeStateFacing.class).canRotate()) {
                        setFacing(getDirection().rotateY());
                    }

                    return WrenchResult.SUCCESS;
                }
            }
        }

        return WrenchResult.PASS;
    }


    //
    // ITileActivatable

    @Override
    public boolean getActive() {
        return isRemote() ? getClientActive() : currentActive;
    }

    private boolean getClientActive() {
        return isActivatable() && Attribute.isActive(getBlockState());
    }

    @Override
    public void setActive(boolean active) {
        if (isActivatable() && active != currentActive) {
            BlockState state = getBlockState();
            Block block = state.getBlock();

            if (Attribute.has(block, AttributeStateActive.class)) {
                currentActive = active;

                if (getClientActive() != active) {
                    if (active) {
                        // Always turn on instantly
                        state = Attribute.setActive(state, true);
                        // noinspection ConstantConditions
                        world.setBlockState(pos, state);
                    } else {
                        // If the update delay is already zero, we can go ahead and set the state
                        if (updateDelay == 0) {
                            // noinspection ConstantConditions
                            world.setBlockState(pos, Attribute.setActive(getBlockState(), currentActive));
                        }

                        // We always reset the update delay when turning off
                        updateDelay = delaySupplier.getAsInt();
                    }
                }
            }
        }
    }


    //
    // ITileSound

    /** Used to check if this tile should attempt to play its sound */
    protected boolean canPlaySound() {
        return getActive();
    }

    /** Only call this from the client */
    private void updateSound() {
        // If machine sounds are disabled, noop
        if (!hasSound() || !PlaygroundConfig.client.enableMachineSounds.get() || soundEvent == null) {
            return;
        }

        if (canPlaySound() && !isRemoved()) {
            // If sounds are being muted, we can attempt to start them on every tick, only to have them
            // denied by the event bus, so use a cooldown period that ensures we're only trying once every
            // second or so to start a sound.
            if (--playSoundCooldown > 0) {
                return;
            }

            // If this machine isn't fully muffled and we don't seem to be playing a sound for it, go ahead and play it
            if (activeSound == null || !Minecraft.getInstance().getSoundHandler().isPlaying(activeSound)) {
                activeSound = SoundHandler.startTileSound(soundEvent, getSoundCategory(), getInitialVolume(), getSoundPos());
            }

            // Always reset the cooldown; either we just attempted to play a sound or we're fully muffled; either way
            // we don't want to try again
            playSoundCooldown = 20;
        } else if (activeSound != null) {
            SoundHandler.stopTileSound(getSoundPos());
            activeSound = null;
            playSoundCooldown = 0;
        }
    }


    //
    // ITileDirectional

    @Nullable
    @Override
    public Direction getDirection() {
        if (isDirectional()) {
            BlockState state = getBlockState();
            Direction facing = Attribute.getFacing(state);

            if (facing != null) {
                return facing;
            }

            if (!getType().isValidBlock(state.getBlock())) {
                Playground.logger.warn("Error invalid block for tile {} at {} in {}. Unable to get direction, falling back to north, "
                        + "things will probably not work correctly. This is almost certainly due to another mod incorrectly "
                        + "trying to move this tile and not properly updating the position.", getType().getRegistryName(), pos, world);
            }
        }

        return Direction.NORTH;
    }

    @Override
    public void setFacing(Direction direction) {
        if (isDirectional()) {
            BlockState state = Attribute.setFacing(getBlockState(), direction);

            if (world != null && state != null) {
                world.setBlockState(pos, state);
            }
        }
    }
}
