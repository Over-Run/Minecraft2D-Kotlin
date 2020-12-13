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
package io.github.overrun.mc2d.lang

import io.github.overrun.mc2d.option.Options
import io.github.overrun.mc2d.util.Utils
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author squid233
 * @since 2020/10/13
 */
object Language {
    private val K2V_EN_US: MutableMap<String, String> = Object2ObjectArrayMap(14)
    private val K2V_ZH_CN: MutableMap<String, String> = Object2ObjectArrayMap(14)
    val LANG = mapOf(
        "en_us" to K2V_EN_US,
        "zh_cn" to K2V_ZH_CN
    )

    fun load(namespace: String) {
        for (l in LANG.keys) {
            try {
                InputStreamReader(Utils.getResource("$namespace:lang/$l.lang"), StandardCharsets.UTF_8).use { r ->
                    val p = Properties(6)
                    p.load(r)
                    p.forEach { key: Any, value: Any -> LANG[l]!![key.toString()] = value.toString() }
                }
            } catch (ignored: IOException) {}
        }
    }

    operator fun get(locale: String = Options[Options.LANG, "en_us"], key: String, def: String = key) =
        LANG[locale]!!.getOrDefault(key, def)
}