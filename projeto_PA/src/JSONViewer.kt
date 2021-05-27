import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem

class JSONViewer(val tree: Tree) : JSONVisitor {

    var hierarchy = true
    lateinit var saveNode :TreeItem

    override fun visit(jsonObject: JSONObject) {
        val node :TreeItem
        if (hierarchy == true){
            node = TreeItem(tree, SWT.NONE)
            hierarchy = false
        }
        else{
            node = TreeItem(saveNode, SWT.NONE)
        }
        node.text = "Object"
        node.data = jsonObject
        saveNode = node
        jsonObject.getMap().forEach { key, value ->
            value.accept(this)
            saveNode = node
        }
    }

    override fun visit(jsonArray: JSONArray) {
        val node :TreeItem
        if (hierarchy == true){
            node = TreeItem(tree, SWT.NONE)
            hierarchy = false
        }
        else{
            node = TreeItem(saveNode, SWT.NONE)
        }
        node.text = "Array"
        node.data = jsonArray
        saveNode = node
        jsonArray.getArray().forEachIndexed { index, value ->
            value.accept(this)
            saveNode = node
        }
    }

    override fun visit(jsonNumber: JSONNumber) {
        val node :TreeItem
        if (hierarchy == true){
            node = TreeItem(tree, SWT.NONE)
            hierarchy = false
        }
        else{
            node = TreeItem(saveNode, SWT.NONE)
        }
        node.text = jsonNumber.getNumber().toString()
        node.data = jsonNumber
        saveNode = node
    }

    override fun visit(jsonBoolean: JSONBoolean) {
        val node :TreeItem
        if (hierarchy == true){
            node = TreeItem(tree, SWT.NONE)
            hierarchy = false
        }
        else{
            node = TreeItem(saveNode, SWT.NONE)
        }
        node.text = jsonBoolean.getBoolean().toString()
        node.data = jsonBoolean
        saveNode = node
    }

    override fun visit(jsonString: JSONString) {
        val node :TreeItem
        if (hierarchy == true){
            node = TreeItem(tree, SWT.NONE)
            hierarchy = false
        }
        else{
            node = TreeItem(saveNode, SWT.NONE)
        }
        node.text = jsonString.getString()
        node.data = jsonString
        saveNode = node
    }

}