/*
  Copyright © 2021 NotAddyPug

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */
package com.addypug.apu;

import net.dv8tion.jda.api.JDA;

/**
 * ShardingTools contains toolsets for sharding
 */
public class ShardingTools {
    /**
     * @param jda The JDA instance
     */
    public static int getNonZeroIndexedShardId(JDA jda) {
        return jda.getShardInfo().getShardId() + 1;
    }
}
