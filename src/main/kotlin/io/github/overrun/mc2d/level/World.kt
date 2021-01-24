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

package io.github.overrun.mc2d.level

import io.github.overrun.mc2d.Main
import io.github.overrun.mc2d.Player
import io.github.overrun.mc2d.block.Block
import io.github.overrun.mc2d.block.Blocks.*
import io.github.overrun.mc2d.option.Options
import io.github.overrun.mc2d.option.Options.getB
import io.github.overrun.mc2d.util.GlUtils.drawRect
import io.github.overrun.mc2d.util.GlUtils.fillRect
import io.github.overrun.mc2d.util.GlfwUtils.isMousePress
import io.github.overrun.mc2d.util.ImageReader.loadTexture
import io.github.overrun.mc2d.util.TextureDrawer.Companion.begin
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT
import org.lwjgl.opengl.GL11.*
import java.io.*
import java.util.*

/**
 * @author squid233
 * @since 2021/01/09
 */
class World(val player: Player, @Transient val width: Int, @Transient val height: Int): Serializable {
    companion object {
        @JvmStatic
        private val logger = LogManager.getLogger()
        private const val serialVersionUID = 1L
    }

    private val blocks = ByteArray(width * height)

    init {
        Arrays.fill(blocks, AIR.rawId)
        // generate the terrain
        for (i in 0 until width) {
            setBlock(i, 0, BEDROCK)
        }
        for (i in 0..1) {
            for (j in 0 until width) {
                setBlock(j, i + 1, COBBLESTONE)
            }
        }
        for (i in 0..1) {
            for (j in 0 until width) {
                setBlock(j, i + 3, DIRT)
            }
        }
        for (i in 0 until width) {
            setBlock(i, 5, GRASS_BLOCK)
        }
        load()
    }

    fun setBlock(x: Int, y: Int, type: Byte) {
        blocks[x % width + y * width] = type
    }

    fun setBlock(x: Int, y: Int, block: Block) {
        setBlock(x, y, block.rawId)
    }

    fun getBlock(x: Int, y: Int): Block {
        return try {
            RAW_ID_BLOCKS.get(blocks[x % width + y * width])
        } catch (e: Exception) {
            AIR
        }
    }

    fun render(mouseX: Int, mouseY: Int, windowW: Int, windowH: Int) {
        var varMouseY = mouseY
        varMouseY = windowH - varMouseY
        for (i in 0 until height) {
            for (j in 0 until width) {
                val b = getBlock(j, i)
                val ltX: Double = (windowW shr 1) - 1 + (j * BLOCK_SIZE - player.x * BLOCK_SIZE)
                val ltY: Double = (windowH shr 1) - 1 + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE)
                val rdX: Double = (windowW shr 1) - 1 + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE)
                val rdY: Double = (windowH shr 1) - 1 + (i * BLOCK_SIZE - player.y * BLOCK_SIZE)
                if (b !== AIR) {
                    begin(loadTexture(BLOCKS.inverse()[b].toString() + ".png"))
                        .color4f(1f, 1f, 1f, 1f)
                        .tex2dVertex2d(
                            0.0, 1.0,
                            (windowW shr 1) - 1 + (j * BLOCK_SIZE - player.x * BLOCK_SIZE),
                            (windowH shr 1) - 1 + (i * BLOCK_SIZE - player.y * BLOCK_SIZE)
                        )
                        .tex2dVertex2d(1.0, 1.0, rdX, rdY)
                        .tex2dVertex2d(
                            1.0, 0.0,
                            (windowW shr 1) - 1 + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE),
                            (windowH shr 1) - 1 + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE)
                        )
                        .tex2dVertex2d(0.0, 0.0, ltX, ltY)
                        .end()
                }
                if (!Main.openingGroup
                    && mouseX >= ltX && mouseX < rdX && varMouseY <= ltY && varMouseY > rdY
                ) {
                    if (getB(
                            Options.BLOCK_HIGHLIGHT,
                            System.getProperty(
                                "mc2d.block.highlight",
                                "false"
                            )
                        )
                    ) {
                        glDisable(GL_TEXTURE_2D)
                        fillRect(ltX, ltY, rdX, rdY, 0x7fffffff, true)
                        glEnable(GL_TEXTURE_2D)
                    } else {
                        drawRect(ltX, ltY, rdX, rdY, 0, false)
                    }
                    if (getBlock(j, i) !== AIR
                        && isMousePress(GLFW_MOUSE_BUTTON_LEFT)
                    ) {
                        setBlock(j, i, AIR)
                    } else if (getBlock(j, i) === AIR
                        && isMousePress(GLFW_MOUSE_BUTTON_RIGHT)
                    ) {
                        setBlock(j, i, player.handledBlock)
                    }
                }
            }
        }
        glFinish()
    }

    fun load() {
        val file = File("level.dat")
        if (file.exists()) {
            try {
                FileInputStream(file).use { fis ->
                    ObjectInputStream(fis).use { ois ->
                        val world = ois.readObject() as World
                        System.arraycopy(world.blocks, 0, blocks, 0, blocks.size)
                        player.x = world.player.x
                        player.y = world.player.y
                    }
                }
            } catch (e: IOException) {
                logger.catching(e)
            } catch (e: ClassNotFoundException) {
                logger.catching(e)
            }
        }
    }

    fun save() {
        try {
            FileOutputStream("level.dat").use { os -> ObjectOutputStream(os).use { oos -> oos.writeObject(this) } }
        } catch (e: IOException) {
            logger.catching(e)
        }
    }
}