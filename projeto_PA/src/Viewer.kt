import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import java.awt.LayoutManager

fun main() {
    Viewer().open()
}

class Viewer() {

    @InjectPlugIn
    lateinit var plugin: PlugInSetup
    fun isPlugInInicialized() = ::plugin.isInitialized

    @InjectAction
    private val actions = mutableListOf<Action>()

    val jsonCentral: JSONCentral = JSONCentral()
    val shell: Shell = Shell(Display.getDefault())
    var tree: Tree
    val label1: Text
    val textBox: Text

    var array :ArrayList<TreeItem> = arrayListOf()

    init {
        shell.setSize(350,600)
        shell.text = "JSON Viewer"
        shell.layout = GridLayout(2, true)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)
        tree.layoutData = GridData(120, 200)

        label1 = Text(shell, SWT.BORDER or SWT.READ_ONLY)
        label1.layoutData = GridData(150, 212)

        textBox = Text(shell, SWT.BORDER)
        textBox.layoutData = GridData(133, 20)
        textBox.message = "Search"
    }

    fun serializeSelected(jsonValue: JSONValue) :String {
        return jsonCentral.serializeJSONText(jsonValue)
    }

    fun open() {
        jsonCentral.testValues()

        if (this::plugin.isInitialized) {
            tree = plugin.execute(this)
            array = plugin.arrayTree
        }
        else {
            jsonCentral.getRoot().accept(JSONViewer(tree))
            tree.expandAll()
        }

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                if (tree.selection.isNotEmpty()) {
                    label1.text = serializeSelected(tree.selection.first().data as JSONValue)
                }
            }
        })

        textBox.addModifyListener {
            array.forEach { treeItem ->
                if (!treeItem.isDisposed) {
                    if (treeItem.text.contains(textBox.text) && !textBox.text.isEmpty()) {
                        treeItem.setBackground(Color(255, 127, 0))
                    } else {
                        treeItem.setBackground(Color(255, 255, 255))
                    }
                }
            }
        }

        actions.forEach { action ->
            val button = Button(shell, SWT.NONE)
            button.setText(action.name)
            button.addListener(SWT.Selection) {
                    action.execute(this)
            }
        }

        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }

    fun Tree.expandAll() = traverse() { it.expanded = true }

    fun Tree.traverse(visitor: (TreeItem) -> Unit) {
        fun TreeItem.traverse() {
            visitor(this)
            items.forEach {
                array.add(it)
                it.traverse()
            }
        }
        items.forEach {
            array.add(it)
            it.traverse()
        }
    }
}


interface PlugInSetup {
    val arrayTree :ArrayList<TreeItem>
    fun execute(viewer: Viewer) :Tree
}

open class FirstPlugIn : PlugInSetup {

    var arrayNewTree :ArrayList<TreeItem> = arrayListOf()

    override val arrayTree: ArrayList<TreeItem>
        get() = arrayNewTree

    lateinit var newTree: Tree

    override fun execute(viewer: Viewer) :Tree {
        createNewTree(viewer).expandAllNewTree()
        arrayNewTree.forEach {
            if (it.data is JSONObject || it.data is JSONArray) {
                it.setImage(Image(Display.getDefault(), "/Users/beca/Downloads/projeto_PA/src/Assets/Folder-icon.png"))
            }
            else {
                it.setImage(Image(Display.getDefault(), "/Users/beca/Downloads/projeto_PA/src/Assets/Document-icon.png"))
            }
        }
        return newTree
    }

    fun createNewTree(viewer: Viewer) :Tree {
        newTree = Tree(viewer.shell, SWT.SINGLE or SWT.BORDER)
        newTree.layoutData = GridData(120, 200)
        viewer.jsonCentral.getRoot().accept(JSONViewer(newTree))
        return newTree
    }

    fun Tree.expandAllNewTree() = traverseNewTree() { it.expanded = true }

    fun Tree.traverseNewTree(visitor: (TreeItem) -> Unit) {
        fun TreeItem.traverseNewTree() {
            visitor(this)
            items.forEach {
                arrayNewTree.add(it)
                it.traverseNewTree()
            }
        }
        items.forEach {
            arrayNewTree.add(it)
            it.traverseNewTree()
        }
    }
}


interface Action {
    val name: String
    fun execute(viewer: Viewer)
}

class EditProperty : Action {

    lateinit var editTextBox :Text

    override val name: String
        get() = "Edit Property"

    override fun execute(viewer: Viewer) {
        if(viewer.tree.selection.isNotEmpty()) {
            var property = viewer.tree.selection.get(0)
            val editShell = Shell(Display.getDefault())
            editShell.setSize(180, 120)
            editShell.layout = GridLayout(1, true)

            val editLabel = Text(editShell, SWT.BORDER or SWT.READ_ONLY)
            editLabel.layoutData = GridData(150, 20)
            editLabel.text = "Current value: " + property.getText()

            editTextBox = Text(editShell, SWT.BORDER)
            editTextBox.layoutData = GridData(150, 20)
            editTextBox.message = "Insert new value"

            val editButton = Button(editShell, SWT.NONE)
            editButton.layoutData = GridData(150, 20)
            editButton.text = "Edit Property"

            editButton.addListener(SWT.Selection) {
                edit((property.data) as JSONValue)
                property.text = editTextBox.text
            }

            editShell.open()
            val display = Display.getDefault()
            while (!editShell.isDisposed) {
                if (!display.readAndDispatch()) display.sleep()
            }
        }
    }

    fun edit(jsonValue: JSONValue) {

        if (jsonValue is JSONString) {
            jsonValue.setValue(editTextBox.text)
        }
        if (jsonValue is JSONBoolean) {
            jsonValue.setValue((editTextBox.text).toBoolean())
        }
        if (jsonValue is JSONNumber) {
            jsonValue.setValue((editTextBox.text).toInt())
        }
    }
}

class CreateFile : Action {

    override val name: String
        get() = "Create File"

    override fun execute(viewer: Viewer) {
        if(viewer.tree.selection.isNotEmpty()) {
            val selected = viewer.tree.selection.get(0).data
            viewer.jsonCentral.serializeJSON(selected as JSONValue)
        }
    }
}

class AddProperty : Action {

    override val name: String
        get() = "Add Property"

    override fun execute(viewer: Viewer) {

        if(viewer.tree.selection.isNotEmpty()) {
            val type = viewer.tree.selection.get(0)
            if (type.data is JSONObject || type.data is JSONArray) {

                val addShell = Shell(Display.getDefault())
                addShell.setSize(180, 120)
                addShell.layout = GridLayout(1, true)

                val addTextBox = Text(addShell, SWT.BORDER)
                addTextBox.layoutData = GridData(150, 20)
                addTextBox.message = "Insert new value"

                val addButton = Button(addShell, SWT.NONE)
                addButton.layoutData = GridData(150, 20)
                addButton.text = "Add Property"

                addButton.addListener(SWT.Selection) {
                    var value = JSONString(addTextBox.text)

                    var newNode = type
                    var treeItem = TreeItem(newNode, SWT.NONE)
                    treeItem.data = value
                    treeItem.text = value.getString()
                    if (viewer.isPlugInInicialized()) {
                        treeItem.setImage(Image(Display.getDefault(), "/Users/beca/Downloads/projeto_PA/src/Assets/Document-icon.png"))
                    }
                }

                addShell.open()
                val display = Display.getDefault()
                while (!addShell.isDisposed) {
                    if (!display.readAndDispatch()) display.sleep()
                }
            }
        }
    }
}

class RemoveProperty : Action {

    override val name: String
        get() = "Remove Property"

    override fun execute(viewer: Viewer) {

        if(viewer.tree.selection.isNotEmpty()) {
            val type = viewer.tree.selection.get(0)
            if (type.data !is JSONObject && type.data !is JSONArray) {
                type.dispose()
            }
        }
    }
}