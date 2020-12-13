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

package io.github.overrun.mc2d.util

import io.github.overrun.mc2d.Minecraft2D
import org.intellij.lang.annotations.MagicConstant
import java.awt.Point

/**
 * <div style="font-family:jetbrains mono,consolas,monospace">
 * |-----------------|<br></br>
 * | U_L | U_M | U_R |<br></br>
 * | M_L | M_M | M_R |<br></br>
 * | D_L | D_M | D_R |<br></br>
 * |-----------------|</div>
 * @author squid233
 * @since 2020/11/24
 */
object Coordinator {
    const val U_L = 0
    const val U_M = 1
    const val U_R = 2
    const val M_L = 3
    const val M_M = 4
    const val M_R = 5
    const val D_L = 6
    const val D_M = 7
    const val D_R = 8
    const val CENTER = M_M
    fun transformation(
        x: Int, y: Int,
        @MagicConstant(valuesFromClass = Coordinator::class) type: Int
    ) = when (type) {
        U_L -> Point(x + 8, y + 30)
        U_M -> Point((Minecraft2D.width shr 1) + x, y + 30)
        U_R -> Point(Minecraft2D.width - 8 - x, y + 30)
        M_L -> Point(x + 8, (Minecraft2D.height - 22 shr 1) + y)
        M_M -> Point((Minecraft2D.width - 22 shr 1) + x, (Minecraft2D.height shr 1) + y)
        M_R -> Point(Minecraft2D.width - 22 - 8 - x, (Minecraft2D.height shr 1) + y)
        D_L -> Point(x + 8, Minecraft2D.height - 8 - y)
        D_M -> Point((Minecraft2D.width shr 1) + x, Minecraft2D.height - 8 - y)
        D_R -> Point(Minecraft2D.width - 8 - x, Minecraft2D.height - 8 - y)
        else -> Point()
    }

    fun isUp(type: Int): Boolean {
        return type in U_L..U_R
    }

    fun isLayerCenter(type: Int): Boolean {
        return type in M_L..M_R
    }

    fun isDown(type: Int): Boolean {
        return type in D_L..D_R
    }

    fun isLeft(type: Int): Boolean {
        return type == U_L || type == M_L || type == D_L
    }

    fun isCenter(type: Int): Boolean {
        return type == U_M || type == M_M || type == D_M
    }

    fun isRight(type: Int): Boolean {
        return type == U_R || type == M_R || type == D_R
    }
}