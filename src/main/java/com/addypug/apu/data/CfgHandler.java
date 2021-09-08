/*
  Copyright © 2021 NotAddyPug
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.addypug.apu.data;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CfgHandler {
    static Logger logger = LoggerFactory.getLogger(CfgHandler.class);
    private static final Dotenv dotenv = Dotenv.load();

    public static String valString(String key) {
        logger.debug("Retrieving String value from .env file with key " + key);
        return dotenv.get(key);
    }

    public static Boolean valBool(String key) {
        logger.debug("Retrieving Boolean value from .env file with key " + key);
        return Boolean.parseBoolean(dotenv.get(key));
    }

    public static Integer valInt(String key) {
        logger.debug("Retrieving Integer value from .env file with key " + key);
        return Integer.parseInt(dotenv.get(key));
    }
}