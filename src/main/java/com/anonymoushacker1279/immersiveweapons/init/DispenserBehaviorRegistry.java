package com.anonymoushacker1279.immersiveweapons.init;

import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.CopperArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.DiamondArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.GoldArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.IronArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.NetheriteArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.StoneArrowEntity;
import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.WoodArrowEntity;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DispenserBehaviorRegistry implements IDispenseItemBehavior {

	public static void init() {
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.COPPER_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				CopperArrowEntity arrowentity = new CopperArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = CopperArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(2.15d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.IRON_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				IronArrowEntity arrowentity = new IronArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = IronArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(2.35d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.DIAMOND_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				DiamondArrowEntity arrowentity = new DiamondArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = DiamondArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(3.0d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.GOLD_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				GoldArrowEntity arrowentity = new GoldArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = GoldArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(2.10d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.STONE_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				StoneArrowEntity arrowentity = new StoneArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = StoneArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(1.85d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.WOOD_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				WoodArrowEntity arrowentity = new WoodArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = WoodArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(1.65d);
				return arrowentity;
			}
		});
		DispenserBlock.registerDispenseBehavior(DeferredRegistryHandler.NETHERITE_ARROW.get(), new ProjectileDispenseBehavior() {
			@Override
			protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				NetheriteArrowEntity arrowentity = new NetheriteArrowEntity(worldIn, position.getX(), position.getY(), position.getZ());
				arrowentity.pickupStatus = NetheriteArrowEntity.PickupStatus.ALLOWED;
				arrowentity.setDamage(5.75d);
				return arrowentity;
			}
			
		});
	}

	@Override
	public ItemStack dispense(IBlockSource p_dispense_1_, ItemStack p_dispense_2_) {
		return null;
	}
}