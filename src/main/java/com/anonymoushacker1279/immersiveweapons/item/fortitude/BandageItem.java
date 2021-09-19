package com.anonymoushacker1279.immersiveweapons.item.fortitude;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BandageItem extends Item {

	/**
	 * Constructor for BandageItem.
	 * @param properties the <code>Properties</code> for the item
	 */
	public BandageItem(Properties properties) {
		super(properties);
	}

	/**
	 * Runs when the player right-clicks.
	 * @param worldIn the <code>World</code> the player is in
	 * @param playerIn the <code>PlayerEntity</code> performing the action
	 * @param handIn the <code>Hand</code> the player is using
	 * @return ActionResult extending ItemStack
	 */
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (playerIn.getPersistentData().get(Player.PERSISTED_NBT_TAG) != null && Objects.requireNonNull(playerIn.getPersistentData().get(Player.PERSISTED_NBT_TAG)).toString().contains("handbookHealingExpert")) {
			playerIn.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 312, 1, false, true));
		} else {
			playerIn.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 240, 0, false, true));
		}
		if (!playerIn.isCreative()) {
			itemstack.shrink(1);
			playerIn.getCooldowns().addCooldown(this, 300);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
	}

	/**
	 * Runs when the player right-clicks an entity.
	 * @param stack the <code>ItemStack</code> right-clicked with
	 * @param playerIn the <code>PlayerEntity</code> performing the action
	 * @param entity the <code>LivingEntity</code> being interacted with
	 * @param hand the <code>Hand</code> the player is using
	 * @return ActionResultType
	 */
	@Override
	public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, LivingEntity entity, @NotNull InteractionHand hand) {
		if (entity.level.isClientSide) {
			return InteractionResult.PASS;
		}
		if (playerIn.getPersistentData().get(Player.PERSISTED_NBT_TAG) != null && Objects.requireNonNull(playerIn.getPersistentData().get(Player.PERSISTED_NBT_TAG)).toString().contains("handbookHealingExpert")) {
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 208, 1, false, true));
		} else {
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 0, false, true));
		}
		if (!playerIn.isCreative()) {
			stack.shrink(1);
		}

		return InteractionResult.PASS;
	}
}