package com.anonymoushacker1279.immersiveweapons.tileentity;

import com.anonymoushacker1279.immersiveweapons.entity.passive.MinutemanEntity;
import com.anonymoushacker1279.immersiveweapons.init.DeferredRegistryHandler;
import com.anonymoushacker1279.immersiveweapons.util.GeneralUtilities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Objects;

public class MinutemanStatueTileEntity extends TileEntity implements ITickableTileEntity {

	private int cooldown;
	private int scannedMinutemen;

	/**
	 * Constructor for MinutemanStatueTileEntity.
	 */
	public MinutemanStatueTileEntity() {
		super(DeferredRegistryHandler.MINUTEMAN_STATUE_TILE_ENTITY.get());
	}

	/**
	 * Runs once each tick. Handle scanning and spawning entities.
	 */
	@Override
	public void tick() {
		if (level != null && !level.isClientSide && Objects.equals(level.getBiome(getBlockPos()).getRegistryName(), DeferredRegistryHandler.BATTLEFIELD.get().getRegistryName()) && cooldown == 0) {
			List<MinutemanEntity> listOfMinutemenInArea = level.getEntitiesOfClass(MinutemanEntity.class, new AxisAlignedBB(getBlockPos().getX() - 48, getBlockPos().getY() - 16, getBlockPos().getZ() - 48, getBlockPos().getX() + 48, getBlockPos().getY() + 16, getBlockPos().getZ() + 48));
			scannedMinutemen = listOfMinutemenInArea.size();

			if (scannedMinutemen <= 16) {
				MinutemanEntity minutemanEntity = DeferredRegistryHandler.MINUTEMAN_ENTITY.get().create(level);
				if (minutemanEntity != null) {
					while (true) {
						BlockPos blockPos = getRandomPositionInArea();
						if (level.getBlockState(blockPos) == Blocks.AIR.defaultBlockState()) {
							minutemanEntity.moveTo(blockPos, 0.0F, 0.0F);
							level.addFreshEntity(minutemanEntity);
							spawnParticles();
							cooldown = 400;
							break;
						}
					}
				}
			}
		} else if (cooldown > 0) {
			cooldown--;
		}
	}

	/**
	 * Spawn particles.
	 */
	private void spawnParticles() {
		ServerWorld serverWorld = (ServerWorld) getLevel();
		if (serverWorld != null) {
			serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, getBlockPos().getX() + 0.5d, getBlockPos().getY(), getBlockPos().getZ() + 0.75d, 5, GeneralUtilities.getRandomNumber(-0.05d, 0.05d), GeneralUtilities.getRandomNumber(-0.25d, 0.25d), GeneralUtilities.getRandomNumber(-0.05d, 0.05d), GeneralUtilities.getRandomNumber(-0.15d, 0.15d));
		}
	}

	/**
	 * Get a random position in the nearby area.
	 * @return BlockPos
	 */
	private BlockPos getRandomPositionInArea() {
		return new BlockPos(getBlockPos().getX() + GeneralUtilities.getRandomNumber(-15, 15), getBlockPos().getY(), getBlockPos().getZ() + GeneralUtilities.getRandomNumber(-15, 15));
	}

	/**
	 * Save NBT data.
	 * @param nbt the <code>CompoundNBT</code> to save
	 */
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("scanCooldown", cooldown);
		nbt.putInt("scannedMinutemen", scannedMinutemen);
		return nbt;
	}

	/**
	 * Load NBT data.
	 * @param state the <code>BlockState</code> of the block
	 * @param nbt the <code>CompoundNBT</code> to load
	 */
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		cooldown = nbt.getInt("scanCooldown");
		scannedMinutemen = nbt.getInt("scannedMinutemen");
	}
}