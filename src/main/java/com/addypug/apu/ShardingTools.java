package com.addypug.apu;

import net.dv8tion.jda.api.JDA;

/**
 * ShardingTools contains toolsets for sgarding
 */
public class ShardingTools {
    /**
     *
     * @param jda The JDA instance
     */
    public static int getNonZeroIndexedShardId(JDA jda) {
        return jda.getShardInfo().getShardId() + 1;
    }
}
