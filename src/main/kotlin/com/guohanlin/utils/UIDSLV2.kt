package com.guohanlin.utils

import com.guohanlin.ui.Icons
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.JBUI
import net.miginfocom.layout.CC
import java.awt.*
import java.awt.event.*
import java.net.URI
import javax.swing.*

/**
 * check and handle the parent Component and children Component
 */
fun checkAddView(parent: Any, vararg children: Component) {
    when (parent) {
        is AuxLayout -> {
            for (child in children) {
                parent.addComponent(child)
            }
        }
        is Container -> {
            for (child in children) {
                parent.add(child)
            }
        }
    }
}

/**
 * 注释：图片
 * 时间：2024/12/8 22:29
 * 作者：郭翰林
 */
fun Any.jImage(path: String): Icon {
    val imageIcon = Icons.load(path)
    checkAddView(this, JLabel(imageIcon))
    return imageIcon
}

/**
 * check and handle the parent Component and child Component with constraintsInParent
 */
fun checkAddView(parent: Any, child: Component, constraintsInParent: Any?) {
    when (parent) {
        is AuxLayout -> {
            parent.addComponent(child)
        }
        is Container -> {
            parent.add(child, constraintsInParent)
        }
    }
}

/**
 * generate a com.carzone.jRadioButton
 */
fun ButtonGroup.jRadioButton(
    text: String,
    selected: Boolean = false,
    actionListener: () -> Unit,
    init: JRadioButton.() -> Unit = {}
): JRadioButton {
    val view = JRadioButton(text, selected)
    view.init()
    view.addActionListener {
        actionListener.invoke()
    }
    this.add(view)
    return view
}


/**
 * generate a com.carzone.jVerticalLinearLayout  but return with jpanel
 */
fun Any.jVerticalLinearLayout(
    constraintsInParent: Any? = BorderLayout.CENTER,
    addToParent: Boolean = true,
    init: JVerticalLinearLayout.() -> Unit
): JPanel {

    val jVerticalLinearLayout = JVerticalLinearLayout()
    val jPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
        jVerticalLinearLayout.init()
        add(jVerticalLinearLayout)
    }
    if (addToParent) {
        checkAddView(this, jPanel, constraintsInParent)
    }
    return jPanel
}


/**
 * generate a com.carzone.jHorizontalLinearLayout but return with jpanel
 */
fun Any.jHorizontalLinearLayout(init: JHorizontalLinearLayout.() -> Unit): JPanel {
    val horizontalBox = JHorizontalLinearLayout()
    val jPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
        horizontalBox.init()
        add(horizontalBox)
    }
    checkAddView(this, jPanel)
    return jPanel
}

/**
 * 注释：消息提示扩展函数
 * 时间：2021/5/20 0020 20:50
 * 作者：郭翰林
 */
fun Any.showMessageTip(message: String) {
    Messages.showDialog(message, message("tip.title"), arrayOf("OK"), 0, null)
}


/**
 * generate a com.carzone.jButtonGroup
 */
fun Container.jButtonGroup(init: ButtonGroup.() -> Unit): ButtonGroup {
    val buttonGroup = ButtonGroup()
    buttonGroup.init()
    checkAddView(this, *(buttonGroup.elements.toList().toTypedArray()))
    return buttonGroup
}

/**
 * 注释：公用JComboBox
 * 时间：2021/5/20 0020 19:56
 * 作者：郭翰林
 */
fun <T> Any.jComboBox(items: Array<T>, addItemListener: (e: ItemEvent?) -> Unit): JComboBox<T> {
    val jComboBox = JComboBox(items)
    jComboBox.addItemListener { e -> addItemListener.invoke(e) }
    jComboBox.maximumSize = Dimension(300, 40)
    checkAddView(this, jComboBox)
    return jComboBox
}

/**
 * 注释：自定义Action按钮
 * 时间：2022/5/17 11:04 上午
 * 作者：郭翰林
 */
fun Any.jActionButton(
    action: AnAction,
    maximumSize: Dimension? = ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE
): ActionButton {
    val button = ActionButton(
        action,
        action.templatePresentation,
        ActionPlaces.UNKNOWN,
        ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE
    )
    button.maximumSize = maximumSize
    checkAddView(this, button)
    return button
}

/**
 * generate a JButton component
 */
fun Any.jButton(
    text: String = "",
    clickListener: () -> Unit,
    init: JButton.() -> Unit = {}
): JButton {
    val jButton = JButton(text)
    jButton.init()
    jButton.addActionListener(object : AbstractAction() {
        override fun actionPerformed(p0: ActionEvent?) {
            clickListener()
        }
    })
    checkAddView(this, jButton)
    return jButton
}


/**
 * generate a JLabel component
 */
fun Any.jLabel(text: String, textSize: Float = 13f, init: JLabel.() -> Unit = {}): JLabel {
    val jLabel = JLabel(text).apply {
        font = font.deriveFont(textSize)
    }
    jLabel.init()
    checkAddView(this, jLabel)
    return jLabel
}

/**
 * generate a specific JTextField component
 */
fun Any.jTextInput(
    initText: String = "",
    enabled: Boolean = true,
    maxSize: JBDimension = JBDimension(10000, 45),
    init: JTextField.() -> Unit = {}
): JTextField {
    val jTextInput = JBTextField().apply {
        text = initText
        maximumSize = maxSize
        isEnabled = enabled
    }
    jTextInput.init()
    checkAddView(this, jTextInput)
    return jTextInput
}


/**
 * generate a com.carzone.jCheckBox component
 */
fun Any.jCheckBox(
    text: String,
    initValue: Boolean = false,
    actionListener: (isSelected: Boolean) -> Unit,
    init: JBCheckBox.() -> Unit = {}
): JCheckBox {
    val jCheckBox = JBCheckBox(text, initValue)
    jCheckBox.addActionListener {
        actionListener.invoke(jCheckBox.isSelected)
    }
    jCheckBox.init()
    checkAddView(this, jCheckBox)
    return jCheckBox
}


/**
 * generate a scrollable component
 */
fun Any.jScrollPanel(
    size: JBDimension,
    constraintsInParent: Any = BorderLayout.CENTER,
    content: () -> Component
): JBScrollPane {

    val jScrollPanel = JBScrollPane(content()).apply {
        preferredSize = size
        border = null
    }
    checkAddView(this, jScrollPanel, constraintsInParent)
    return jScrollPanel
}


/**
 * generate a JSeparator component
 */
fun Any.jLine(): JSeparator {
    val jLine = JSeparator(SwingConstants.CENTER).apply {
        maximumSize = JBDimension(10000, 10)
        background = Color.GRAY
    }
    checkAddView(this, jLine)
    return jLine
}

/**
 * 注释：JTextPane DSL
 * 时间：2022/4/12 11:39 上午
 * 作者：郭翰林
 */
fun Any.jTextPane(size: JBDimension): JTextPane {
    val jTextPane = JTextPane().apply {
        preferredSize = size
    }
    checkAddView(this, jTextPane)
    return jTextPane
}


/**
 * generate multiple lines text input component
 */
fun Any.jTextAreaInput(
    initText: String = "",
    size: JBDimension = JBDimension(400, 50),
    enabled: Boolean = true,
    textLanguageType: LanguageFileType = PlainTextFileType.INSTANCE,
    project: Project,
    init: (textAreaInput: Document) -> Unit = {},
): JComponent {
    val editorFactory = EditorFactory.getInstance()
    val document = editorFactory.createDocument("").apply {
        setReadOnly(false)
    }
    val editor = editorFactory.createEditor(document, null, textLanguageType, false)
    editor.component.apply {
        isEnabled = enabled
        autoscrolls = true
        preferredSize = size
    }
    init(editor.document)
    WriteCommandAction.runWriteCommandAction(project) {
        editor.document.setText(initText)
    }
    checkAddView(this, editor.component)
    return editor.component
}


/**
 * generate a link component
 */
fun Any.jLink(
    text: String,
    linkURL: String? = null,
    linkURLColor: String = "#5597EB",
    maxSize: JBDimension? = null,
    onclick: () -> Unit = {}
): JLabel {
    val jLink =
        JLabel("<html><a href='$linkURL'><font color=\"$linkURLColor\">$text</font></a></html>").apply {
            if (maxSize != null) {
                maximumSize = maxSize
            }
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    linkURL?.let {
                        Desktop.getDesktop().browse(URI(it))
                    }
                    onclick()
                }

                override fun mouseEntered(e: MouseEvent?) {
                    cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                }

                override fun mouseExited(e: MouseEvent?) {
                    cursor = Cursor.getDefaultCursor()
                }
            })
        }

    checkAddView(this, jLink)
    return jLink
}


/**
 * generate a grid layout component
 */
fun Any.jGridLayout(rows: Int, columns: Int, init: JPanel.() -> Unit = {}): JPanel {
    val jPanel = JPanel().apply {
        layout = GridLayout(rows, columns, 10, 10)
    }
    jPanel.init()
    checkAddView(this, jPanel)
    return jPanel
}


/**
 * generate a icon component
 */
fun Any.jIcon(iconPath: String, init: JLabel.() -> Unit = {}): JLabel {
    val icon = IconLoader.getIcon(iconPath, Icons::class.java.classLoader)
    return JBLabel(icon).also { it.init() }
}

/**
 * generate a border layout which for easy adding inner views
 */
fun Any.jBorderLayout(init: SimpleBorderLayout.() -> Unit): JPanel {
    return SimpleBorderLayout().apply {
        init()
    }
}


/**
 * com.carzone.addFocusLostListener to JTextField, when focusLost invoke method
 */
fun JTextField.addFocusLostListener(listener: (e: FocusEvent?) -> Unit) {
    addFocusListener(object : FocusListener {
        override fun focusLost(e: FocusEvent?) {
            listener.invoke(e)
        }

        override fun focusGained(e: FocusEvent?) {

        }

    })
}


/**
 * the components in com.carzone.alignLeftComponent will be align Left
 *
 * for example：
 *
 * com.carzone.jVerticalLinearLayout{
 *    com.carzone.alignLeftComponent {
 *        com.carzone.jLabel("test")
 *    }
 *}
 */
fun JVerticalLinearLayout.alignLeftComponent(init: JVerticalLinearLayout.AlignLeftContainer.() -> Unit): JVerticalLinearLayout.AlignLeftContainer {
    return AlignLeftContainer().apply(init)
}


/**
 * auxiliary layout： help to add child in specific position
 */
interface AuxLayout {
    fun addComponent(comp: Component)
}

/**
 * com.carzone.JVerticalLinearLayout: Box with BoxLayout.Y_AXIS
 */
class JVerticalLinearLayout : Box(BoxLayout.Y_AXIS) {

    /**
     * Space height between lines
     */
    private val lineSpaceHeight = 10

    /**
     * fill the fixed space for linear layout
     */
    fun fixedSpace(spaceHeight: Int) {
        super.add(createVerticalStrut(JBUI.scale(spaceHeight)))
    }

    override fun add(comp: Component?): Component {
        fixedSpace(lineSpaceHeight)
        return super.add(comp)
    }

    /**
     * add component with align left style
     */
    inner class AlignLeftContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            val jPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.LINE_AXIS)
                componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
                val horizontalBox = Box(BoxLayout.X_AXIS)
                horizontalBox.add(comp)
                horizontalBox.add(createHorizontalGlue())
                add(horizontalBox)
            }
            add(jPanel)
        }
    }
}

fun fill(): CC = CC().grow().push()
fun fillX(): CC = CC().growX().pushX()
fun fillY(): CC = CC().growY().pushY()

fun wrap(): CC = CC().wrap()


/**
 * com.carzone.JHorizontalLinearLayout： Box with BoxLayout.X_AXIS
 */
class JHorizontalLinearLayout : Box(BoxLayout.X_AXIS) {
    /**
     * fill the remaining space for linear layout,like android empty space with weight value
     */
    fun fillSpace() {
        add(createHorizontalGlue())
    }

    /**
     * fill the fixed space for linear layout
     */
    fun fixedSpace(spaceWidth: Int) {
        add(createHorizontalStrut(JBUI.scale(spaceWidth)))
    }
}


fun SimpleBorderLayout.topContainer(init: SimpleBorderLayout.TopContainer.() -> Unit) =
    TopContainer().apply(init)

fun SimpleBorderLayout.bottomContainer(init: SimpleBorderLayout.BottomContainer.() -> Unit) =
    BottomContainer().apply(init)

fun SimpleBorderLayout.leftContainer(init: SimpleBorderLayout.LeftContainer.() -> Unit) =
    LeftContainer().apply(init)

fun SimpleBorderLayout.rightContainer(init: SimpleBorderLayout.RightContainer.() -> Unit) =
    RightContainer().apply(init)

fun SimpleBorderLayout.centerFillContainer(init: SimpleBorderLayout.CenterFillContainer.() -> Unit) =
    CenterFillContainer().apply(init)

/**
 * com.carzone.SimpleBorderLayout：JPanel with BorderLayout()
 */
class SimpleBorderLayout : JPanel(BorderLayout()) {
    var hasPutLeft = false
    var hasPutRight = false
    var hasPutTop = false
    var hasPutBottom = false
    var hasPutCenter = false

    fun putLeft(comp: Component) {
        if (hasPutLeft) {
            throw IllegalAccessError("Only Could put left one time")
        }
        add(comp, BorderLayout.WEST)
        hasPutLeft = true
    }

    fun putRight(comp: Component) {
        if (hasPutRight) {
            throw IllegalAccessError("Only Could put right one time")
        }
        add(comp, BorderLayout.EAST)
        hasPutRight = true
    }

    fun putTop(comp: Component) {
        if (hasPutTop) {
            throw IllegalAccessError("Only Could put top one time")
        }
        add(comp, BorderLayout.NORTH)
        hasPutTop = true
    }

    fun putBottom(comp: Component) {
        if (hasPutBottom) {
            throw IllegalAccessError("Only Could put bottom one time")
        }
        add(comp, BorderLayout.SOUTH)
        hasPutBottom = true
    }

    fun putCenterFill(comp: Component) {
        if (hasPutCenter) {
            throw IllegalAccessError("Only Could put center fill one time")
        }
        add(comp, BorderLayout.CENTER)
        hasPutCenter = true
    }

    inner class TopContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            putTop(comp)
        }
    }

    inner class BottomContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            putBottom(comp)
        }
    }

    inner class LeftContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            putLeft(comp)
        }
    }

    inner class RightContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            putRight(comp)
        }
    }

    inner class CenterFillContainer : AuxLayout {
        override fun addComponent(comp: Component) {
            putCenterFill(comp)
        }
    }

}



