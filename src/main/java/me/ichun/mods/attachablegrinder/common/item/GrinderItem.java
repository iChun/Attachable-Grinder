package me.ichun.mods.attachablegrinder.common.item;

import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.entity.GrinderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

import java.util.List;

public class GrinderItem extends Item
{
    public GrinderItem(Properties properties)
    {
        super(properties);
    }

    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity living, Hand hand)
    {
        if(canAttach(living) && !living.isChild() && living.isAlive())
        {
            List<Entity> ents = living.world.getEntitiesWithinAABB(GrinderEntity.class, living.getBoundingBox().expand(0.1D, 0.1D, 0.1D), (o) -> o instanceof GrinderEntity && ((GrinderEntity)o).parent == living);
            if(ents.isEmpty()) //no attached grinder
            {
                if(!living.world.isRemote)
                {
                    living.world.addEntity(new GrinderEntity(AttachableGrinder.EntityTypes.GRINDER.get(), living.world).setParent(living));
                }
                living.world.playSound(player, living.getPosX(), living.getPosY(), living.getPosZ(), SoundEvents.ENTITY_PIG_SADDLE, living.getSoundCategory(), 0.5F, 1.0F);

                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public static boolean canAttach(LivingEntity living)
    {
        return living.getType().getRegistryName() != null && !AttachableGrinder.config.disabledEntities.contains(living.getType().getRegistryName().toString()) && AttachableGrinder.grinderProperties.getFor(living.getType().getRegistryName()) != null;
    }
}
