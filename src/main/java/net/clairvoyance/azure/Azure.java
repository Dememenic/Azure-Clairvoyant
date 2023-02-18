/*
 * Copyright (C) 2023 Dememenic
 *
 * This file is part of Azure.
 *
 * Azure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Azure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Azure.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.clairvoyance.azure;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.clairvoyance.azure.commands.CommandManager;
import net.clairvoyance.azure.listeners.EventListeners;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@SuppressWarnings("unused")
public class Azure {

    public static final Color COLOR = new Color(1, 1, 255);
    public static final long TIME = System.currentTimeMillis();

    private final Dotenv config;
    private final ShardManager shardManager;
    private final String[] activities = {
            "the screams of the damned."
    };
    private static final Properties properties = new Properties();

    public Azure() {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        int randomIndex = new Random().nextInt(activities.length);
        String activity = activities[randomIndex];

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening(activity));
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListeners(), new CommandManager());
    }

    public static void main(String[] args) {
        new Azure();
    }

    public Dotenv getConfig() {
        return config;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public String[] getActivities() {
        return activities;
    }

    public static Properties getProperties() {
        try {
            properties.load(Azure.class.getClassLoader().getResourceAsStream("project.properties"));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
