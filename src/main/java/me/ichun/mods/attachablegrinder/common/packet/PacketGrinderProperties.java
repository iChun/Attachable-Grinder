package me.ichun.mods.attachablegrinder.common.packet;

import com.google.gson.Gson;
import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.ichunutil.common.network.AbstractPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;

public class PacketGrinderProperties extends AbstractPacket
{
    public Gson gson = new Gson();
    public HashMap<ResourceLocation, GrinderProperties.PropertiesClient> props = new HashMap<>();

    public PacketGrinderProperties(){}

    public PacketGrinderProperties(boolean ignored)
    {
        AttachableGrinder.grinderProperties.objects.forEach((k, v) -> {
            props.put(k, v.toClient());
        });
    }

    @Override
    public void writeTo(PacketBuffer buf)
    {
        props.forEach((k, v) -> {
            buf.writeString(k.toString());
            buf.writeString(gson.toJson(v));
        });
        buf.writeString("##endPacket");
    }

    @Override
    public void readFrom(PacketBuffer buf)
    {
        String s = readString(buf); //this method is client only but this is a s->c packet
        while(!s.equals("##endPacket"))
        {
            String json = readString(buf);
            props.put(new ResourceLocation(s), gson.fromJson(json, GrinderProperties.PropertiesClient.class));
            s = readString(buf);
        }
    }

    @Override
    public void process(NetworkEvent.Context context)
    {
        context.enqueueWork(() -> {
            props.forEach((k, v) -> {
                if(!(AttachableGrinder.grinderProperties.objects.containsKey(k) && AttachableGrinder.grinderProperties.objects.get(k).lootTable != null)) // if we already have one and it has a loot table set, it's the server's!
                {
                    AttachableGrinder.grinderProperties.objects.put(k, v.toServer());
                }
            });
        });
    }
}
