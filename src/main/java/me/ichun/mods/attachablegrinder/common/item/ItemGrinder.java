package me.ichun.mods.attachablegrinder.common.item;

import me.ichun.mods.attachablegrinder.common.Grinder;
import me.ichun.mods.attachablegrinder.common.entity.EntityGrinder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ItemGrinder extends Item
{
    public ItemGrinder()
    {
        maxStackSize = 1;
        setCreativeTab(CreativeTabs.TOOLS);
        setUnlocalizedName("Grinder");
        setRegistryName(new ResourceLocation(Grinder.MOD_ID, "grinder"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entityliving, EnumHand hand)
    {
        if(canAttach(entityliving) && !entityliving.isChild())
        {
            List ents = entityliving.world.getEntitiesWithinAABBExcludingEntity(entityliving, entityliving.getEntityBoundingBox().grow(0.2D));
            for(int i = 0; i < ents.size(); i++)
            {
                Entity ent = (Entity)ents.get(i);
                if(!(ent instanceof EntityGrinder))
                {
                    continue;
                }
                EntityGrinder grinder = (EntityGrinder)ent;
                if(grinder.animal == entityliving)
                {
                    return false;
                }
            }
            if(!entityliving.world.isRemote)
            {
                entityliving.world.spawnEntity(new EntityGrinder(entityliving.world, entityliving));
            }
            itemstack.shrink(1);
            return true;
        }
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        return !itemInteractionForEntity(itemstack, (EntityPlayer)null, entityliving, EnumHand.MAIN_HAND);
    }

    public boolean canAttach(EntityLivingBase ent)
    {
        return ent instanceof EntityPig && Grinder.config.allowPig == 1 || ent instanceof EntityCow && Grinder.config.allowCow == 1 || ent instanceof EntityChicken && Grinder.config.allowChicken == 1 || ent instanceof EntityCreeper && Grinder.config.allowCreeper == 1 || ent instanceof EntityZombie && Grinder.config.allowZombie == 1 || ent instanceof AbstractSkeleton && Grinder.config.allowSkeleton == 1 || ent instanceof EntityPlayer && Grinder.config.allowPlayer == 1;
    }
}
