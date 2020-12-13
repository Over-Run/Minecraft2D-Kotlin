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

package io.github.overrun.mc2d.input

import io.github.overrun.mc2d.screen.GameScreen
import io.github.overrun.mc2d.screen.Screens
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * @author squid233
 * @since 2020/09/14
 */
class KeyInput : KeyAdapter() {
    override fun keyPressed(e: KeyEvent) {
        pressing = e.keyCode
        execute(e, KeyBinding.PRESS)
    }

    override fun keyTyped(e: KeyEvent) {
        execute(e, KeyBinding.TYPED)
        Screens.openScreen.onKeyDown(e)
    }

    override fun keyReleased(e: KeyEvent) {
        if (pressing != 0) {
            execute(e, KeyBinding.RELEASE)
        }
        pressing = 0
    }

    private fun execute(e: KeyEvent, type: Int) {
        if (Screens.openScreen is GameScreen) KeyBinding.getKeyBinding(e.keyChar)?.let {
            if (it.type == type) it.onTyping().accept(it)
        }
    }

    companion object {
        private var pressing = 0
        fun getKeyDown(keyCode: Int): Boolean {
            return pressing == keyCode
        }
    }
}