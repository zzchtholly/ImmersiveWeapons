package com.anonymoushacker1279.immersiveweapons.entity;

import com.anonymoushacker1279.immersiveweapons.advancement.IWCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface GrantAdvancementOnDiscovery {

	default void checkForDiscovery(Entity entity, Player player) {
		if (isLookingAtMe(entity, player)) {
			IWCriteriaTriggers.ENTITY_DISCOVERED_TRIGGER.trigger((ServerPlayer) player, entity.getType().getRegistryName());
		}
	}

	default boolean isLookingAtMe(Entity entity, Player player) {
		Vec3 viewVector = player.getViewVector(1.0F).normalize();
		Vec3 viewDelta = new Vec3(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
		double deltaLength = viewDelta.length();
		viewDelta = viewDelta.normalize();
		double deltaDot = viewVector.dot(viewDelta);
		return deltaDot > 1.0D - 0.025D / deltaLength && player.hasLineOfSight(entity);
	}
}