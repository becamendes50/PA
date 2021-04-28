import java.io.File
import java.io.PrintWriter

class JSONSerializer(private val file: File) : JSONVisitor{

    var completeString :String = ""

    private val writer = PrintWriter(file)

    private var it = 0

    private var root = 0

    private var tabs = 0

    fun getWriter() : PrintWriter{
        return writer
    }

    override fun visit(jsonObject: JSONObject) {
        writer.append("{ \n\t")
        completeString += ("{ \n\t")
        tabs++
        var c = 1
        val map = jsonObject.getMap()
        if (it < 1) {
            root = map.size
        }
        map.forEach { key, value ->
            writer.append("\"" + key + "\": ")
            completeString += ("\"" + key + "\": ")
            value.accept(this)
            if (c < map.size) {
                writer.append(", \n\t")
                completeString += (", \n\t")
            }
            else {
                writer.append("\n" )
                completeString += "\n"
            }
            c++
            it++
        }
        writer.append("}")
        completeString += "}"
    }

    override fun visit(jsonArray: JSONArray) {
        writer.append("[ \n\t")
        completeString += "[ \n\t"
        var c = 1
        val array = jsonArray.getArray()
        array.forEachIndexed { index, value ->
            value.accept(this)
            if (c < array.size) {
                writer.append(", \n\t")
                completeString += ", \n\t"
            }
            else {
                writer.append("\n" )
                completeString += "\n"
            }
            c++
        }
        writer.append("]")
        completeString += "]"
    }

    override fun visit(jsonString: JSONString) {
        writer.append("\"" + jsonString.getString() + "\"")
        completeString += ("\"" + jsonString.getString() + "\"")
    }

    override fun visit(jsonBoolean: JSONBoolean) {
        writer.append("" + jsonBoolean.getBoolean())
        completeString += (jsonBoolean.getBoolean())
    }

    override fun visit(jsonNumber: JSONNumber) {
        writer.append("" + jsonNumber.getNumber())
        completeString += (jsonNumber.getNumber())
    }




}