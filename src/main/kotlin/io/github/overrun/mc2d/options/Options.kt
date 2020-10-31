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
package io.github.overrun.mc2d.options

import io.github.overrun.mc2d.util.Constants.FALSE
import java.io.*
import java.util.*
import java.util.Map.of

/**
 * @author squid233
 * @since 2020/09/15
 */
object Options {
    val OPTIONS = Properties(3)
    const val DEBUGGING = "debugging"
    const val WIDTH = "width"
    const val HEIGHT = "height"

    val DEBUGGING_DEF: String = FALSE
    const val WIDTH_DEF = "1040"
    const val HEIGHT_DEF = "486"

    @JvmOverloads
    operator fun get(key: String?, def: String? = "null"): String {
        return OPTIONS.getProperty(key, def)
    }

    fun getI(key: String?, def: Int): Int {
        return Options[key, def.toString()].toInt()
    }

    fun getI(key: String?): Int {
        return getI(key, 0)
    }

    fun getB(key: String?, def: Boolean): Boolean {
        return java.lang.Boolean.parseBoolean(Options[key, def.toString()])
    }

    fun getB(key: String?): Boolean {
        return getB(key, false)
    }

    operator fun set(k: String?, v: String?) {
        OPTIONS.setProperty(k, v)
        var r: Reader? = null
        try {
            FileWriter("options.properties").use { w ->
                r = FileReader("options.properties")
                OPTIONS.load(r)
                OPTIONS.store(w, null)
                OPTIONS.load(r)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (r != null) {
                    r!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    init {
        try {
            FileReader("options.properties").use { r -> OPTIONS.load(r) }
        } catch (e: IOException) {
            OPTIONS.putAll(
                of(
                    WIDTH, WIDTH_DEF,
                    DEBUGGING, FALSE,
                    HEIGHT, HEIGHT_DEF
                )
            )
            try {
                val w: Writer = FileWriter("options.properties")
                OPTIONS.store(w, null)
            } catch (ee: IOException) {
                ee.printStackTrace()
            }
        }
    }
}