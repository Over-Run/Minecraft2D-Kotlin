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
import io.github.overrun.mc2d.screen.Screens
import io.github.overrun.mc2d.text.IText
import io.github.overrun.mc2d.util.collect.Arrays.notContains
import java.awt.*
import java.awt.Font.TRUETYPE_FONT
import java.awt.Font.createFont
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment
import java.io.File
import java.io.IOException
import java.util.function.Consumer

/**
 * @author squid233
 * @since 2020/09/15
 */
object DrawHelper {
    var simsunb: Font? = null
    val SIMSUN = Font("新宋体", Font.PLAIN, 18)
    fun drawImage(g: Graphics, image: Image, point: Point) {
        g.drawImage(image, point.x, point.y, null)
    }

    fun drawImage(g: Graphics, image: Image, point: Point, width: Int, height: Int) {
        g.drawImage(image, point.x, point.y, width, height, null)
    }

    fun drawImage(g: Graphics, image: Image, x: Int, y: Int) {
        g.drawImage(image, x, y, null)
    }

    fun drawImage(g: Graphics, image: Image, x: Int, y: Int, width: Int, height: Int) {
        g.drawImage(image, x, y, width, height, null)
    }

    fun drawCenterImage(g: Graphics, image: Image, y: Int) {
        drawImage(g, image, Coordinator.transformation(-(image.getWidth(null) shr 1), y, Coordinator.U_M))
    }

    fun drawCenterImage(g: Graphics, image: Image, y: Int, width: Int, height: Int) {
        drawImage(g, image,
            Coordinator.transformation(-(image.getWidth(null) shr 1), y, Coordinator.U_M), width, height)
    }

    fun drawDefaultBackground(g: Graphics) {
        drawWithColor(g, Screens.BG_COLOR) { gg: Graphics ->
            gg.fillRect(0, 0, Minecraft2D.width, Minecraft2D.height)
        }
    }

    fun drawDirtBackground(g: Graphics) {
        var i = 0
        val h = Minecraft2D.height
        while (i < h) {
            var j = 0
            val w = Minecraft2D.width
            while (j < w) {
                drawImage(g, Images.OPTIONS_BACKGROUND, j, i)
                j += 16
            }
            i += 16
        }
    }

    fun drawCenteredText(g: Graphics, text: IText, y: Int, layout: Int = Coordinator.U_M) {
        drawText(g, text,
            Coordinator.transformation(
                -(text.getPrevWidth(g) shr 1), y, if (Coordinator.isCenter(layout)) layout else Coordinator.U_M)
        )
    }

    fun drawText(g: Graphics, text: IText, point: Point) {
        drawText(g, text, point.x, point.y)
    }

    fun drawText(g: Graphics, text: IText, x: Int, y: Int) {
        drawWithColor(g, Color.WHITE) { gg: Graphics ->
            gg.font = if (simsunb == null) SIMSUN else simsunb
            gg.drawString(text.asString(), x, y + text.getPrevHeight(gg))
        }
    }

    fun drawWithColor(g: Graphics, color: Color, consumer: Consumer<Graphics>) {
        val c = g.color
        g.color = color
        consumer.accept(g)
        g.color = c
    }

    fun drawRect(g: Graphics, color: Color, x: Int, y: Int, width: Int, height: Int, layout: Int) {
        drawWithColor(g, color) { gg: Graphics ->
            val p = Coordinator.transformation(x, y, layout)
            gg.drawRect(p.x, p.y, width, height)
        }
    }

    fun fillRect(g: Graphics, color: Color, x: Int, y: Int, width: Int, height: Int, layout: Int = Coordinator.U_L) {
        drawWithColor(g, color) { gg: Graphics ->
            val p = Coordinator.transformation(x, y, layout)
            gg.fillRect(p.x, p.y, width, height)
        }
    }

    fun getSimsunMetrics(g: Graphics): FontMetrics {
        return g.getFontMetrics(if (simsunb == null) SIMSUN else simsunb)
    }

    init {
        try {
            if (notContains(getLocalGraphicsEnvironment().availableFontFamilyNames, "新宋体")) {
                simsunb = createFont(TRUETYPE_FONT, File("simsun.ttc")).deriveFont(18f)
            }
        } catch (e: FontFormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}