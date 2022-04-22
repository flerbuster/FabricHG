package de.royzer.fabrichg.mixins.server.network;

import com.mojang.authlib.GameProfile;
import de.royzer.fabrichg.kit.events.KitEventsKt;
import de.royzer.fabrichg.mixinskt.ServerPlayerEntityMixinKt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(
            method = "hurt",
            at = @At("HEAD")
    )
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntityMixinKt.INSTANCE.onDamage(source, amount, cir, (ServerPlayer) (Object) (this));
    }
    @Redirect(
            method = "hurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    public boolean reduceDamage(Player instance, DamageSource source, float amount) {
        if (source.getEntity() instanceof ServerPlayer) {
            return super.hurt(source, (float) (amount * 0.6));
        } else {
            return super.hurt(source, amount);
        }
    }
    @Inject(
            method = "drop",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onDropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntityMixinKt.INSTANCE.onDropSelectedItem(entireStack, cir, (ServerPlayer) (Object) this);
    }
    @Inject(
            method = "attack",
            at = @At("HEAD")
    )
    public void onAttackPlayer(Entity target, CallbackInfo ci) {
        KitEventsKt.onAttackEntity(target, (ServerPlayer) (Object) this);
    }
}