package com.noobnuby

import org.json.JSONObject
import java.awt.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import javax.swing.*


fun main() {

    //TODO 파일 다운
    //Default system
    val path = System.getProperty("user.dir")
    val ttf: InputStream? = Thread.currentThread().contextClassLoader.getResourceAsStream("Pretendard.ttf")
    val fontLoad: Font = Font.createFont(Font.TRUETYPE_FONT, ttf)
    val font = fontLoad.deriveFont(12f)


    val frame = JFrame("PaperMCAutoBuilder")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.size = Dimension(300, 450)
    frame.setResizable(false)
    frame.setLocationRelativeTo(null) //center frame
    frame.setLayout(null)

    //Create Main text
    val mainText = JLabel("PaperMCAutoBuilder")
    mainText.setBounds(86, -45, 128, 128)
    mainText.font = font
    frame.add(mainText)

    //Create main img
    val iconUrl = Thread.currentThread().contextClassLoader.getResource("PaperMC.png")
    val icon = ImageIcon(iconUrl)

    val imgLabel = JLabel(icon)
    imgLabel.setBounds(86, 50, 128, 128);
    frame.contentPane.add(imgLabel)

    //Create Radio Button
    val radioButtonWin = JRadioButton("Window",true)
    val radioButtonMac = JRadioButton("Mac")
    radioButtonWin.font = font
    radioButtonMac.font = font
    var isRadioWin = true
    var isRadioMac = false

    //Radio select event
    radioButtonWin.addActionListener {
        isRadioWin = true
        isRadioMac = false
    }
    radioButtonMac.addActionListener {
        isRadioMac = true
        isRadioWin = false
    }

    val radioGroup = ButtonGroup()
    radioGroup.add(radioButtonWin)
    radioGroup.add(radioButtonMac)

    radioButtonWin.setBounds(60, 200, 90, 50)
    radioButtonMac.setBounds(170, 200, 70, 50)

    frame.add(radioButtonWin)
    frame.add(radioButtonMac)

    radioGroup.elements

    //Create installButton
    val installButton = JButton("Install")
    installButton.setBounds(50, 300, 200, 40)
    installButton.font = font
    installButton.addActionListener {
        downloadFile(lastVersion(),path)
        settingEula(path)
        if(isRadioMac) {

        }
        else {

        }
    }
    frame.add(installButton)
    frame.isVisible = true
}

fun lastVersion(): String {
    val verObj = api("https://api.papermc.io/v2/projects/paper/") as JSONObject
    val versions = verObj.getJSONArray("versions")
    val version = versions.getString(versions.length() - 1)
    val buildObj = api("https://api.papermc.io/v2/projects/paper/versions/${version}") as JSONObject
    val builds = buildObj.getJSONArray("builds")
    val build = builds.getInt(builds.length() - 1)
    val lastVerURL = "https://api.papermc.io/v2/projects/paper/versions/${version}/builds/${build}/downloads/paper-${version}-${build}.jar"
    return lastVerURL
}

fun api(url:String): Any {
    try {
        val apiUrl = URL(url)
        val conn: HttpURLConnection = apiUrl.openConnection() as HttpURLConnection

        conn.setRequestMethod("GET") // http 메서드
        conn.setRequestProperty("Content-Type", "application/json") // header Content-Type 정보
        conn.setRequestProperty("auth", "myAuth") // header의 auth 정보
        conn.setDoOutput(true) // 서버로부터 받는 값이 있다면 true


        // 서버로부터 데이터 읽어오기
        val br = BufferedReader(InputStreamReader(conn.getInputStream()))
        val sb = StringBuilder()
        var line: String? = null

        while ((br.readLine().also { line = it }) != null) { // 읽을 수 있을 때 까지 반복
            sb.append(line)
        }

        val obj = JSONObject(sb.toString()) // json으로 변경
        return obj
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun settingFile(path:String,maxRam:String,isMac:Boolean) {

}

fun settingEula(path:String) {
    try {
        val eulaFile = File("eula.txt")
        eulaFile.createNewFile()

        val pw = PrintWriter(eulaFile)
        pw.print("eula=true")
        pw.close()
    }
    catch (e:Exception) {
        e.printStackTrace()
    }
}

fun downloadFile(url: String, path: String) {
    try {
        val rbc = Channels.newChannel(URL(url).openStream())
        val downloadFile = File(path, "server.jar")

        FileOutputStream(downloadFile).use { fos ->
            fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class RoundedButton : JButton {
    constructor() : super() {
        decorate()
    }

    constructor(text: String?) : super(text) {
        decorate()
    }

    constructor(action: Action?) : super(action) {
        decorate()
    }

    constructor(icon: Icon?) : super(icon) {
        decorate()
    }

    constructor(text: String?, icon: Icon?) : super(text, icon) {
        decorate()
    }

    protected fun decorate() {
        setBorderPainted(false)
        setOpaque(false)
    }

    override fun paintComponent(g: Graphics) {
        val c = Color(254, 254, 254) //배경색 결정
        val o = Color(0, 0, 0) //글자색 결정
        val width = width
        val height = height
        val graphics = g as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        if (getModel().isArmed) {
            graphics.color = c.darker()
        } else if (getModel().isRollover) {
            graphics.color = c.brighter()
        } else {
            graphics.color = c
        }
        graphics.fillRoundRect(0, 0, width, height, 15, 15)
        val fontMetrics = graphics.fontMetrics
        val stringBounds = fontMetrics.getStringBounds(text, graphics).getBounds()
        val textX = (width - stringBounds.width) / 2
        val textY = (height - stringBounds.height) / 2 + fontMetrics.ascent
        graphics.color = o
        graphics.font = font
        graphics.drawString(text, textX, textY)
        graphics.dispose()
        super.paintComponent(g)
    }
}