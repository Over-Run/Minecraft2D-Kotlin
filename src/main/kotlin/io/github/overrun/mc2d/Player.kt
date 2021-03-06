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

package io.github.overrun.mc2d

import io.github.overrun.mc2d.util.GlfwUtils.isKeyPress
import org.lwjgl.glfw.GLFW.*
import java.io.Serializable

/**
 * @author squid233
 * @since 2021/01/09
 */
class Player: Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @Transient
    var handledBlock: Byte = 1
    var x = .5
    var y = 6.0

    fun move() {
        if (isKeyPress(GLFW_KEY_A)
            || isKeyPress(GLFW_KEY_LEFT)
        ) {
            x -= .0625
        }
        if (isKeyPress(GLFW_KEY_D)
            || isKeyPress(GLFW_KEY_RIGHT)
        ) {
            x += .0625
        }
        if (isKeyPress(GLFW_KEY_SPACE)
            || isKeyPress(GLFW_KEY_W)
            || isKeyPress(GLFW_KEY_UP)
        ) {
            y += .0625
        }
        if (isKeyPress(GLFW_KEY_LEFT_SHIFT)
            || isKeyPress(GLFW_KEY_S)
            || isKeyPress(GLFW_KEY_DOWN)
        ) {
            y -= .0625
        }
    }
}