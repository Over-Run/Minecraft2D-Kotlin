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

package io.github.overrun.mc2d.client

import io.github.overrun.mc2d.Minecraft2D
import io.github.overrun.mc2d.input.KeyInput
import io.github.overrun.mc2d.input.MouseInput
import io.github.overrun.mc2d.option.Options
import io.github.overrun.mc2d.screen.Screens
import io.github.overrun.mc2d.util.ImgUtil
import io.github.overrun.mc2d.util.Utils
import java.awt.Graphics
import java.awt.HeadlessException
import java.awt.Point
import javax.swing.JFrame

/**
 * @author squid233
 * @since 2020/09/14
 */
class Mc2dClient private constructor() : JFrame("Minecraft2D ${Minecraft2D.VERSION}") {
    override fun paint(g: Graphics) {
        val buf = createImage(width, height)
        val gg = buf.graphics
        Screens.openScreen.render(gg)
        g.drawImage(buf, 0, 0, null)
    }

    @Throws(HeadlessException::class)
    override fun getMousePosition(): Point {
        return Utils.compute(super.getMousePosition(), NP)
    }

    companion object {
        private const val serialVersionUID = 1L
        val NP = Point()
        var instance: Mc2dClient = Mc2dClient()
    }

    init {
        setSize(Options.getI(Options.WIDTH, 854), Options.getI(Options.HEIGHT, 480))
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        addKeyListener(KeyInput())
        addMouseListener(MouseInput())
        iconImage = ImgUtil.readImage("icon.png")
        val mem = Runtime.getRuntime().maxMemory() shr 20
        Minecraft2D.LOGGER.info("Max memory: ${if (mem >= 1024) "${(mem shr 10)} GB" else "$mem MB"}")
    }
}