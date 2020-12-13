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

package io.github.overrun.mc2d.util.registry

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.overrun.mc2d.util.Identifier

/**
 * @author squid233
 * @since 2020/10/06
 */
class DefaultedRegistry<T>(private val defaultValue: T) : BaseRegistry<T>() {
    private val id2entry: BiMap<Identifier, T> = HashBiMap.create(255)
    private val rawId2entry: BiMap<Int, T> = HashBiMap.create(255)
    private var nextId = 0
    override fun register(id: Identifier, entry: T): T {
        if (nextId < rawId2entry.size) nextId = rawId2entry.size
        rawId2entry[nextId++] = entry
        id2entry[id] = entry
        return entry
    }

    override fun get(id: Identifier) = id2entry[id]!!

    operator fun get(rawId: Int) = if (rawId < 0 || rawId >= rawId2entry.size) defaultValue else rawId2entry[rawId]!!

    override fun getId(entry: T) = id2entry.inverse()[entry]!!

    fun getRawId(entry: T): Int {
        return rawId2entry.inverse()[entry]!!
    }

    override fun iterator() = rawId2entry.values.iterator()
}