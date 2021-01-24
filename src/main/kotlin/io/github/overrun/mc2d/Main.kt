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

import io.github.overrun.mc2d.block.Blocks
import io.github.overrun.mc2d.level.World
import io.github.overrun.mc2d.option.Options
import io.github.overrun.mc2d.screen.CreativeTabScreen.init
import io.github.overrun.mc2d.screen.CreativeTabScreen.render
import io.github.overrun.mc2d.util.GlfwUtils.setDefaultCursor
import io.github.overrun.mc2d.util.ImageReader.loadTexture
import io.github.overrun.mc2d.util.ImageReader.withGlfwImg
import io.github.overrun.mc2d.util.TextureDrawer.Companion.begin
import io.github.overrun.mc2d.util.Utils.use
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.math.floor
import kotlin.math.max

/**
 * @author squid233
 * @since 2020/09/14
 */
class Main {
    companion object {
        const val VERSION = "0.2.0"
        @JvmStatic
        var openingGroup = false
        @JvmStatic
        private val logger = LogManager.getLogger()

        @JvmStatic
        fun main(args: Array<String>) {
            Options.init()
            Main().run()
        }
    }

    private val player = Player()
    private lateinit var world: World
    private var window: Long = 0
    private var width = 896
    private var height = 512
    private var mouseX = 0
    private var mouseY = 0

    private fun run() {
        logger.info("Loading for game Minecraft2D $VERSION")
        init()
        resize(width, height)
        glClearColor(.4f, .6f, .9f, .1f)
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            render()
            if (!openingGroup) {
                player.move()
            }
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
        logger.info("Stopping!")
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        window = glfwCreateWindow(width, height, "Minecraft2D $VERSION", NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }
        withGlfwImg(
            "icon.png"
        ) { imgs: GLFWImage.Buffer -> glfwSetWindowIcon(window, imgs) }
        glfwSetKeyCallback(
            window
        ) { window: Long, key: Int, _: Int, action: Int, _: Int ->
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_ESCAPE) {
                    if (openingGroup) {
                        openingGroup = false
                    } else {
                        world.save()
                        glfwSetWindowShouldClose(window, true)
                    }
                }
                if (key == Options[Options.KEY_CREATIVE_TAB, "E"].toUpperCase().toCharArray()[0].toInt()
                ) {
                    openingGroup = !openingGroup
                    if (!openingGroup) {
                        setDefaultCursor()
                    }
                }
                if (key == GLFW_KEY_ENTER) {
                    world.save()
                }
                if (key == GLFW_KEY_1) {
                    player.handledBlock = 1
                }
                if (key == GLFW_KEY_2) {
                    player.handledBlock = 2
                }
                if (key == GLFW_KEY_3) {
                    player.handledBlock = 3
                }
                if (key == GLFW_KEY_4) {
                    player.handledBlock = 4
                }
            }
        }
        glfwSetWindowCloseCallback(window) { world.save() }
        glfwSetWindowSizeCallback(
            window
        ) { _: Long, width: Int, height: Int -> resize(width, height) }
        glfwSetCursorPosCallback(window) { _: Long, x: Double, y: Double ->
            mouseX = floor(x).toInt()
            mouseY = floor(y).toInt()
        }
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            glfwGetWindowSize(window, pWidth, pHeight)
            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
            if (vidMode != null) {
                glfwSetWindowPos(
                    window,
                    vidMode.width() - pWidth[0] shr 1,
                    vidMode.height() - pHeight[0] shr 1
                )
            }
        }
        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        world = World(player, 64, 64)
        glfwSwapInterval(1)
        glfwShowWindow(window)
        setDefaultCursor()
    }

    private fun render() {
        world.render(mouseX, mouseY, width, height)
        if (openingGroup) {
            render(mouseX, mouseY, height, player)
        } else {
            glPushMatrix()
            glTranslatef(width.toFloat(), height.toFloat(), 0f)
            begin(
                loadTexture(
                    Blocks.RAW_ID_BLOCKS[player.handledBlock].toString() + ".png"
                )
            )
                .color4f(1f, 1f, 1f, 1f)
                .tex2dVertex2d(0.0, 0.0, -64.0, 0.0)
                .tex2dVertex2d(0.0, 1.0, -64.0, -64.0)
                .tex2dVertex2d(1.0, 1.0, 0.0, -64.0)
                .tex2dVertex2d(1.0, 0.0, 0.0, 0.0)
                .end()
            glPopMatrix()
        }
    }

    private fun resize(width: Int, height: Int) {
        val w = max(width, 1)
        val h = max(height, 1)
        init(w, h)
        this.width = w
        this.height = h
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, w.toDouble(), 0.0, h.toDouble(), 1.0, -1.0)
        glMatrixMode(GL_MODELVIEW)
        glViewport(0, 0, w, h)
    }
}