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

package io.github.overrun.mc2d.screen

import io.github.overrun.mc2d.Minecraft2D
import io.github.overrun.mc2d.util.DrawHelper
import java.awt.Graphics
import java.awt.Image
import java.awt.Point

/**
 * @author squid233
 * @since 2020/12/02
 */
abstract class AbstractButtonWidget : ScreenWidget() {
    abstract val isEnable: Boolean
    abstract val usualTexture: Image
    abstract val width: Int
    abstract val height: Int
    open val prevPos: Point
        get() = Point(x, y)
    val isHover: Boolean
        get() = Minecraft2D.mouseX > prevPos.getX()
                && Minecraft2D.mouseX < prevPos.getX() + width
                && Minecraft2D.mouseY > prevPos.getY()
                && Minecraft2D.mouseY < prevPos.getY() + height
    open val hoverTexture: Image
        get() = usualTexture
    open val disableTexture: Image
        get() = usualTexture
    val texture: Image
        get() = if (isEnable) if (isHover) hoverTexture else usualTexture else disableTexture
    abstract val action: PressAction

    override fun render(g: Graphics) {
        DrawHelper.drawImage(g, texture, prevPos, width, height)
    }
}

typealias PressAction = (widget: AbstractButtonWidget) -> Unit