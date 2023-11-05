package net.pedroricardo.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrushableBlock.class)
public abstract class ScatterMixin extends BlockWithEntity {
	@Final
	@Shadow
	private static IntProperty DUSTED;

	protected ScatterMixin(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		BrushableBlockEntity blockEntity = (BrushableBlockEntity) world.getBlockEntity(pos);
		if (world.isClient() || blockEntity == null || !blockEntity.item.isEmpty() || state.get(DUSTED) < 3) return super.onUse(state, world, pos, player, hand, hit);
		blockEntity.item = stack.copyWithCount(1);
		stack.decrement(1);
		blockEntity.markDirty();
		world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
		return ActionResult.SUCCESS;
	}
}