/*
 * This file is part of AlmightyNotch.
 *
 * Copyright (c) 2014 <http://dev.bukkit.org/server-mods/almightynotch//>
 *
 * AlmightyNotch is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AlmightyNotch is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AlmightyNotch.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.almightynotch.event.mob;

import ninja.amp.almightynotch.AlmightyNotchPlugin;
import ninja.amp.almightynotch.Message;
import ninja.amp.almightynotch.Mood;
import ninja.amp.almightynotch.event.WorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;

public class CreeperExplodeEvent extends WorldEvent {
    public CreeperExplodeEvent() {
        super("CreeperExplode");
        setMoods(Mood.DISPLEASED);
        setProbability(1);
        setDescription("Makes all the creepers in the world explode.");
        setOccurMessage(Message.CREEPER_EXPLODE_EVENT);
        setMoodModifier(15);
    }

    @Override
    public void trigger(AlmightyNotchPlugin plugin, World world) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.getType() == EntityType.CREEPER) {
                Creeper creeper = (Creeper) entity;

                float power = creeper.isPowered() ? 6.0F : 3.0F;
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(creeper, power, false);
                pluginManager.callEvent(event);
                if (!event.isCancelled()) {
                    creeper.damage(creeper.getHealth());
                    world.createExplosion(creeper.getLocation(), power);
                }
            }
        }
        plugin.getMessenger().sendMessage(Bukkit.getServer(), getOccurMessage());
    }
}
