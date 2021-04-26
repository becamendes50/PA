import java.io.File
import java.io.PrintWriter

class JSONSerializer(private val file: File) : JSONVisitor{

    private val writer = PrintWriter(file)

    private var it = 0

    private var root = 0

    override fun visit(jsonObject: JSONObject) {
        writer.append("{ ")
        val map = jsonObject.getMap()
        if (it < 1) {
            root = map.size
        }
        map.forEach { key, value ->
            writer.append("\"" + key + "\": ")
            value.accept(this)
            writer.append("\n" )
            it++
        }
        writer.append("}")
        if (it > root-1) {
            writer.close()
        }
    }

    override fun visit(jsonArray: JSONArray) {
        writer.append("[ ")
        val array = jsonArray.getArray()
        array.forEachIndexed { index, value ->
            value.accept(this)
            writer.append("\n" )
        }
        writer.append("]")
    }

    override fun visit(jsonString: JSONString) {
        writer.append("\"" + jsonString.getString() + "\"")
    }

    override fun visit(jsonBoolean: JSONBoolean) {
        writer.append("" + jsonBoolean.getBoolean())
    }

    override fun visit(jsonNumber: JSONNumber) {
        writer.append("" + jsonNumber.getNumber())
    }




}