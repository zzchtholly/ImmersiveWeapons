package com.anonymoushacker1279.immersiveweapons.item;

import com.anonymoushacker1279.immersiveweapons.init.DeferredRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;

public class MoltenItem {

	public static class MoltenSword extends SwordItem {
		public MoltenSword(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
			super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(DeferredRegistryHandler.ITEM_GROUP).isImmuneToFire());
		}

		@Override
		public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLiving, LivingEntity par3EntityLiving) {
			par2EntityLiving.setFire(10);
			return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		}
	}

	public static class MoltenAxe extends AxeItem {

		public MoltenAxe(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
			super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(DeferredRegistryHandler.ITEM_GROUP).isImmuneToFire());
		}

		@Override
		public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLiving, LivingEntity par3EntityLiving) {
			par2EntityLiving.setFire(10);
			return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		}
	}

	public static class MoltenPickaxe extends PickaxeItem {

		public MoltenPickaxe(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
			super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(DeferredRegistryHandler.ITEM_GROUP).isImmuneToFire());
		}

		@Override
		public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLiving, LivingEntity par3EntityLiving) {
			par2EntityLiving.setFire(10);
			return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		}
	}

	public static class MoltenShovel extends ShovelItem {

		public MoltenShovel(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
			super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(DeferredRegistryHandler.ITEM_GROUP).isImmuneToFire());
		}

		@Override
		public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLiving, LivingEntity par3EntityLiving) {
			par2EntityLiving.setFire(10);
			return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		}
	}

	public static class MoltenHoe extends HoeItem {

		public MoltenHoe(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
			super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(DeferredRegistryHandler.ITEM_GROUP).isImmuneToFire());
		}

		@Override
		public boolean hitEntity(ItemStack par1ItemStack, LivingEntity par2EntityLiving, LivingEntity par3EntityLiving) {
			par2EntityLiving.setFire(10);
			return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		}
	}

}