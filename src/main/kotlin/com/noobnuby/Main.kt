package com.noobnuby

import java.awt.*
import javax.swing.*

fun main(args: Array<String>) {

    //TODO 이미지
    val frame = JFrame("PaperMCAutoBuilder")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.size = Dimension(300, 500)
    //Create main img
    val icon = ImageIcon("resources/image.png")
    frame.iconImage = icon.image
    //Create installButton
    val installButton = JButton("Install")
    installButton.preferredSize = Dimension(10, 10)

    frame.add(installButton,BorderLayout.SOUTH)
    frame.isVisible = true
}