/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.option

import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * @author squid233
 * @since 2020/09/15
 */
object Options {
    @JvmStatic
    val OPTIONS = Properties(7)
    const val BLOCK_HIGHLIGHT = "block.highlight"
    const val KEY_CREATIVE_TAB = "key.creativeTab"
    @JvmStatic
    private val logger = LogManager.getLogger()

    @JvmStatic
    fun init() {
        val file = File("options.txt")
        if (!file.exists()) {
            try {
                FileOutputStream(file).use { os ->
                    OPTIONS[KEY_CREATIVE_TAB] = "E"
                    OPTIONS[BLOCK_HIGHLIGHT] = "false"
                    OPTIONS.store(os, null)
                }
            } catch (e: IOException) {
                logger.catching(e)
            }
        }
        try {
            FileReader(file).use { r -> OPTIONS.load(r) }
        } catch (e: IOException) {
            logger.catching(e)
        }
    }

    @JvmStatic
    fun getB(key: String?, def: String?): Boolean {
        return if (OPTIONS.containsKey(key)) OPTIONS.getProperty(key).toBoolean() else def.toBoolean()
    }

    @JvmStatic
    operator fun get(key: String?, def: String): String {
        return OPTIONS.getProperty(key, def)
    }
}