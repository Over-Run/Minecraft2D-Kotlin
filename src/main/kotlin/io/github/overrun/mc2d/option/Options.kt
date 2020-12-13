/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

import io.github.overrun.mc2d.Minecraft2D
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*

/**
 * @author squid233
 * @since 2020/09/15
 */
object Options {
    private val OPTIONS = Properties(5)
    const val DEBUG = "debug"
    const val WIDTH = "width"
    const val HEIGHT = "height"
    const val FPS = "fps"
    const val LANG = "lang"

    fun save() {
        try {
            FileWriter("options.properties").use { w -> OPTIONS.store(w, null) }
        } catch (e: IOException) {
            Minecraft2D.LOGGER.exception("Cannot save options!", e)
        }
    }

    operator fun set(k: String, v: String?) {
        OPTIONS.setProperty(k, v)
    }

    fun setAndSave(k: String, v: String?) {
        Options[k] = v
        save()
    }

    operator fun get(k: String) = OPTIONS.getProperty(k)

    operator fun get(k: String, def: String?) = OPTIONS.getProperty(k, def)

    fun getB(k: String) = Options[k].toBoolean()

    fun getI(k: String, def: Int) = Options[k, def.toString()].toInt()

    init {
        val f = File("options.properties")
        if (!f.exists()) {
            try {
                FileWriter(f).use { fw ->
                    OPTIONS[DEBUG] = "false"
                    OPTIONS[WIDTH] = "854"
                    OPTIONS[HEIGHT] = "480"
                    OPTIONS[FPS] = "60"
                    OPTIONS[LANG] = "en_us"
                    OPTIONS.store(fw, null)
                }
            } catch (e: IOException) {
                Minecraft2D.LOGGER.exception("Cannot write options!", e)
            }
        }
        try {
            FileReader(f).use { fr -> OPTIONS.load(fr) }
        } catch (e: IOException) {
            Minecraft2D.LOGGER.exception("Cannot read options!", e)
        }
    }
}