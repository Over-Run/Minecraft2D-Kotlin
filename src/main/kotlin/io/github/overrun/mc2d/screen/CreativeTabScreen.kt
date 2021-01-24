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

package io.github.overrun.mc2d.screen

import io.github.overrun.mc2d.Main
import io.github.overrun.mc2d.Player
import io.github.overrun.mc2d.block.Blocks
import io.github.overrun.mc2d.util.GlUtils
import io.github.overrun.mc2d.util.GlUtils.fillRect
import io.github.overrun.mc2d.util.GlfwUtils.HAND_CURSOR
import io.github.overrun.mc2d.util.GlfwUtils.isMousePress
import io.github.overrun.mc2d.util.GlfwUtils.setDefaultCursor
import io.github.overrun.mc2d.util.ImageReader.loadTexture
import io.github.overrun.mc2d.util.TextureDrawer.Companion.begin
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

/**
 * @author squid233
 * @since 2021/01/23
 */
object CreativeTabScreen {
    @JvmStatic
    private val SLOTS = ArrayList<Slot>(4)
    @JvmStatic
    var x = 0
    @JvmStatic
    var y = 0

    @JvmStatic
    fun init(width: Int, height: Int) {
        x = (width shr 1) - 195
        y = (height shr 1) + 136
        SLOTS.clear()
        for (i in 0..3) {
            val slot = Slot(x + 18 + i * 36, y - 36)
            slot.item = (i + 1).toByte()
            SLOTS.add(slot)
        }
    }

    @JvmStatic
    fun render(mouseX: Int, mouseY: Int, windowH: Int, player: Player) {
        var varMouseY = mouseY
        varMouseY = windowH - varMouseY
        begin(loadTexture("tab_items.png"))
            .color4f(1f, 1f, 1f, 1f)
            .tex2dVertex2d(0.0, 0.0, x.toDouble(), y.toDouble())
            .tex2dVertex2d(0.0, 1.0, x.toDouble(), (y - 272).toDouble())
            .tex2dVertex2d(1.0, 1.0, (x + 390).toDouble(), (y - 272).toDouble())
            .tex2dVertex2d(1.0, 0.0, (x + 390).toDouble(), y.toDouble())
            .end()
        GlUtils.drawText(x + 16, y, "Creative Tab")
        var showHand = false
        for (slot in SLOTS) {
            begin(loadTexture(Blocks.RAW_ID_BLOCKS.get(slot.item).toString() + ".png"))
                .color4f(1f, 1f, 1f, 1f)
                .tex2dVertex2d(0.0, 0.0, slot.x.toDouble(), slot.y.toDouble())
                .tex2dVertex2d(0.0, 1.0, slot.x.toDouble(), (slot.y - 32).toDouble())
                .tex2dVertex2d(1.0, 1.0, (slot.x + 32).toDouble(), (slot.y - 32).toDouble())
                .tex2dVertex2d(1.0, 0.0, (slot.x + 32).toDouble(), slot.y.toDouble())
                .end()
            if (mouseX >= slot.x && mouseX < slot.x + 32 && varMouseY <= slot.y && varMouseY > slot.y - 32) {
                showHand = true
                glDisable(GL_TEXTURE_2D)
                fillRect(
                    slot.x.toDouble(),
                    slot.y.toDouble(),
                    (slot.x + 32).toDouble(),
                    (slot.y - 32).toDouble(),
                    0x7fffffff,
                    true
                )
                glEnable(GL_TEXTURE_2D)
                if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                    player.handledBlock = slot.item
                    showHand = false
                    Main.openingGroup = false
                }
            }
        }
        if (showHand) {
            glfwSetCursor(glfwGetCurrentContext(), HAND_CURSOR)
        } else {
            setDefaultCursor()
        }
    }
}