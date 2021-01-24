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

package io.github.overrun.mc2d.util

import io.github.overrun.mc2d.util.ImageReader.loadTexture
import io.github.overrun.mc2d.util.TextureDrawer.Companion.begin
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL
import java.awt.Color
import java.nio.ByteBuffer

/**
 * @author squid233
 * @since 2021/01/08
 */
object GlUtils {
    @JvmStatic
    fun generateMipmap(
        target: Int,
        components: Int,
        width: Int,
        height: Int,
        format: Int,
        type: Int,
        pixels: ByteBuffer
    ) = if (GL.getCapabilities().glGenerateMipmap != NULL)
        glGenerateMipmap(target)
    else
        glTexImage2D(
            target, 0, components,
            width, height, 0,
            format, type, pixels)

    @JvmStatic
    fun drawRect(x1: Double, y1: Double, x2: Double, y2: Double, color: Int, alpha: Boolean) {
        glBegin(GL_LINE_STRIP)
        val c = Color(color, alpha)
        if (alpha) {
            glColor4f(
                c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f
            )
        } else {
            glColor3f(
                c.red / 255f, c.green / 255f, c.blue / 255f
            )
        }
        // Left top
        glVertex2d(x1, y1 - 1)
        // Left down
        glVertex2d(x1 + 1, y2)
        // Right down
        glVertex2d(x2 - 1, y2)
        // Right up
        glVertex2d(x2, y1 - 1)
        // Origin point
        glVertex2d(x1, y1)
        glEnd()
    }

    @JvmStatic
    fun fillRect(x1: Double, y1: Double, x2: Double, y2: Double, color: Int, alpha: Boolean) {
        glBegin(GL_QUADS)
        val c = Color(color, alpha)
        if (alpha) {
            glColor4f(
                c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f
            )
        } else {
            glColor3f(c.red / 255f, c.green / 255f, c.blue / 255f)
        }
        // Left top
        glVertex2d(x1, y1)
        // Left down
        glVertex2d(x1, y2)
        // Right down
        glVertex2d(x2, y2)
        // Right up
        glVertex2d(x2, y1)
        glEnd()
    }

    @JvmStatic
    fun drawText(x: Int, y: Int, text: String) {
        val drawer = begin(loadTexture("ascii.png"))
            .color4f(1f, 1f, 1f, 1f)
        val chars = text.toCharArray()
        for (i in chars.indices) {
            val texX = chars[i].toInt() * .0078125
            val resultX = (x + (i shl 4)).toDouble()
            drawer.tex2dVertex2d(texX, 0.0, resultX, y.toDouble())
                .tex2dVertex2d(texX, 1.0, resultX, (y - 32).toDouble())
                .tex2dVertex2d(texX + .0078125, 1.0, resultX + 16, (y - 32).toDouble())
                .tex2dVertex2d(texX + .0078125, 0.0, resultX + 16, y.toDouble())
        }
        drawer.end()
    }
}