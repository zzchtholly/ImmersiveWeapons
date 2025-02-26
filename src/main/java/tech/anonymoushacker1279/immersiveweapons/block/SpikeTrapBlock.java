package tech.anonymoushacker1279.immersiveweapons.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import org.jetbrains.annotations.NotNull;
import tech.anonymoushacker1279.immersiveweapons.init.DeferredRegistryHandler;
import tech.anonymoushacker1279.immersiveweapons.init.PacketHandler;

import java.util.function.Supplier;

public class SpikeTrapBlock extends Block implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private final DamageSource damageSource = new DamageSource("immersiveweapons.spike_trap");

	/**
	 * Constructor for SpikeTrapBlock.
	 *
	 * @param properties the <code>Properties</code> of the block
	 */
	public SpikeTrapBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE).setValue(POWERED, Boolean.FALSE));
	}

	/**
	 * Set the shape of the block.
	 *
	 * @param state            the <code>BlockState</code> of the block
	 * @param blockGetter      the <code>BlockGetter</code> for the block
	 * @param pos              the <code>BlockPos</code> the block is at
	 * @param collisionContext the <code>CollisionContext</code> of the block
	 * @return VoxelShape
	 */
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos,
	                                    @NotNull CollisionContext collisionContext) {
		return SHAPE;
	}

	/**
	 * Updates the block when required.
	 *
	 * @param state         the <code>BlockState</code> of the block
	 * @param facing        the <code>Direction</code> the block is facing
	 * @param neighborState the <code>BlockState</code> of the neighbor block
	 * @param levelAccessor the <code>LevelAccessor</code> the block is in
	 * @param currentPos    the <code>BlockPos</code> the block is at
	 * @param facingPos     the <code>BlocKPos</code> the facing block is at
	 * @return BlockState
	 */
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState neighborState,
	                                       @NotNull LevelAccessor levelAccessor, @NotNull BlockPos currentPos,
	                                       @NotNull BlockPos facingPos) {

		if (state.getValue(WATERLOGGED)) {
			levelAccessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		}

		return facing == Direction.DOWN && !state.canSurvive(levelAccessor, currentPos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, facing, neighborState, levelAccessor, currentPos, facingPos);
	}

	/**
	 * Set placement properties.
	 * Sets the facing direction of the block for placement.
	 *
	 * @param context the <code>BlockPlaceContext</code> during placement
	 * @return BlockState
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(WATERLOGGED, context.getLevel()
				.getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
	}

	/**
	 * Set FluidState properties.
	 * Allows the block to exhibit waterlogged behavior.
	 *
	 * @param state the <code>BlockState</code> of the block
	 * @return FluidState
	 */
	@SuppressWarnings("deprecation")
	@Override
	public @NotNull FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	/**
	 * Create the BlockState definition.
	 *
	 * @param builder the <code>StateContainer.Builder</code> of the block
	 */
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, POWERED);
	}

	/**
	 * Determines if the block can exist in a given state.
	 *
	 * @param state  the <code>BlockState</code> of the block
	 * @param reader the <code>LevelReader</code> for the block
	 * @param pos    the <code>BlocKPos</code> the block is at
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader reader, BlockPos pos) {
		return Block.canSupportCenter(reader, pos.below(), Direction.UP);
	}

	/**
	 * Runs when an entity is inside the block's collision area.
	 * Allows the block to deal damage on contact.
	 *
	 * @param state  the <code>BlockState</code> of the block
	 * @param level  the <code>Level</code> the block is in
	 * @param pos    the <code>BlockPos</code> the block is at
	 * @param entity the <code>Entity</code> passing through the block
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if (entity instanceof Player || entity instanceof Mob) {
			if (state.getValue(POWERED)) {
				entity.hurt(damageSource, 2f);
			}
		}
	}

	/**
	 * Runs when neighboring blocks change state.
	 *
	 * @param state    the <code>BlockState</code> of the block
	 * @param level    the <code>Level</code> the block is in
	 * @param pos      the <code>BlockPos</code> the block is at
	 * @param oldState the <code>BlockState</code> the block previously had
	 * @param isMoving determines if the block is moving
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!oldState.is(state.getBlock())) {
			if (level.hasNeighborSignal(pos)) {
				level.setBlock(pos, state.setValue(POWERED, true), 3);
			}
		}
	}

	/**
	 * Runs when neighboring blocks change state.
	 *
	 * @param state    the <code>BlockState</code> of the block
	 * @param level    the <code>Level</code> the block is in
	 * @param pos      the <code>BlockPos</code> the block is at
	 * @param blockIn  the <code>Block</code> that is changing
	 * @param fromPos  the <code>BlockPos</code> of the changing block
	 * @param isMoving determines if the block is moving
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block blockIn,
	                            @NotNull BlockPos fromPos, boolean isMoving) {
		boolean flag = level.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			state = state.setValue(POWERED, flag);
			if (state.getValue(POWERED)) {
				PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() ->
								new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 12, level.dimension())),
						new SpikeTrapBlockPacketHandler(pos, true));
			} else {
				PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() ->
								new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 12, level.dimension())),
						new SpikeTrapBlockPacketHandler(pos, false));
			}
			level.setBlock(pos, state.setValue(POWERED, flag), 2);
		}
	}

	public record SpikeTrapBlockPacketHandler(BlockPos blockPos, boolean extend) {

		/**
		 * Constructor for SpikeTrapBlockPacketHandler.
		 */
		public SpikeTrapBlockPacketHandler {
		}

		/**
		 * Encodes a packet
		 *
		 * @param msg          the <code>SpikeTrapBlockPacketHandler</code> message being sent
		 * @param packetBuffer the <code>PacketBuffer</code> containing packet data
		 */
		public static void encode(SpikeTrapBlockPacketHandler msg, FriendlyByteBuf packetBuffer) {
			packetBuffer.writeBlockPos(msg.blockPos).writeBoolean(msg.extend);
		}

		/**
		 * Decodes a packet
		 *
		 * @param packetBuffer the <code>PacketBuffer</code> containing packet data
		 * @return SpikeTrapBlockPacketHandler
		 */
		public static SpikeTrapBlockPacketHandler decode(FriendlyByteBuf packetBuffer) {
			return new SpikeTrapBlockPacketHandler(packetBuffer.readBlockPos(), packetBuffer.readBoolean());
		}

		/**
		 * Handles an incoming packet, by sending it to the client/server
		 *
		 * @param msg             the <code>SpikeTrapBlockPacketHandler</code> message being sent
		 * @param contextSupplier the <code>Supplier</code> providing context
		 */
		public static void handle(SpikeTrapBlockPacketHandler msg, Supplier<Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient(msg)));
			context.setPacketHandled(true);
		}

		/**
		 * Runs specifically on the client, when a packet is received
		 *
		 * @param msg the <code>SpikeTrapBlockPacketHandler</code> message being sent
		 */
		private static void handleOnClient(SpikeTrapBlockPacketHandler msg) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level != null) {
				if (msg.extend) {
					minecraft.level.playLocalSound(msg.blockPos, DeferredRegistryHandler.SPIKE_TRAP_EXTEND.get(),
							SoundSource.BLOCKS, 1.0f, 1.0f, true);
				} else {
					minecraft.level.playLocalSound(msg.blockPos, DeferredRegistryHandler.SPIKE_TRAP_RETRACT.get(),
							SoundSource.BLOCKS, 1.0f, 1.0f, true);
				}
			}
		}
	}
}