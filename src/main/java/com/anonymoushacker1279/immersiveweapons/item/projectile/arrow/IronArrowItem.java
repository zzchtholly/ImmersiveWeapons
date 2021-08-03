package com.anonymoushacker1279.immersiveweapons.item.projectile.arrow;

import com.anonymoushacker1279.immersiveweapons.entity.projectile.CustomArrowEntity.IronArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IronArrowItem extends AbstractArrowItem {

	/**
	 * Constructor for IronArrowItem.
	 * @param properties the <code>Properties</code> for the item
	 * @param damageIn the damage to deal on impact
	 */
	public IronArrowItem(Properties properties, double damageIn) {
		super(properties, damageIn);
		damage = damageIn;
	}

	/**
	 * Create an arrow item.
	 * @param worldIn the <code>World</code> the shooter is in
	 * @param stack the <code>ItemStack</code> being shot
	 * @param shooter the <code>LivingEntity</code> shooting the arrow
	 * @return IronArrowEntity
	 */
	@Override
	public IronArrowEntity createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
		IronArrowEntity arrowEntity = new IronArrowEntity(shooter, worldIn, ref.get());
		arrowEntity.setBaseDamage(damage);
		return arrowEntity;
	}
}