import java.io.File
import java.io.PrintWriter
import java.io.Serializable

class JSONCentral {

    private val map1 = HashMap<String, JSONValue>()
    private var object1 = JSONObject(map1)

    fun getRootSize() : Int {
        return object1.getMap().size
    }

    fun getRoot() : JSONValue{
        return object1
    }

    fun testValues() {
        val map2 = HashMap<String, JSONValue>()
        val firstValue = JSONObject(map2)
        object1.addValue("first", firstValue)

        var array1 :ArrayList<JSONValue> = arrayListOf()
        var secondValue = JSONArray(array1)
        val string1 = JSONString("Hello")
        val boolean1 = JSONBoolean(true)
        secondValue.addValue(string1)
        secondValue.addValue(boolean1)
        object1.addValue("second", secondValue)

        val thirdValue = JSONString("Goodbye")
        object1.addValue("third", thirdValue)

        val fourthValue = JSONBoolean(false)
        object1.addValue("fourth", fourthValue)

        val fifthValue = JSONNumber(1234)
        object1.addValue("fifth", fifthValue)
    }

    fun serializeJSON(obj: JSONValue) {
        val file = File(obj.toString())
        val serializer = JSONSerializer(file)
        obj.accept(serializer)
        serializer.getWriter().close()
    }

    fun serializeJSONText(obj: JSONValue) : String{
        val file = File("root")
        val serializer = JSONSerializer(file)
        obj.accept(serializer)
        serializer.getWriter().close()
        return serializer.completeString
    }

    fun searchJSON() {
        //val query :(JSONValue) -> Boolean = {v -> (v is JSONString)}
        //val query :(JSONValue) -> Boolean = {v -> (v is JSONNumber)}
        val query :(JSONValue) -> Boolean = {v -> (v is JSONObject && v.getMap().size > 0)}
        val searcher = JSONSearcher(query)
        object1.accept(searcher)
        searcher.returnQuery()
    }

    fun convertJSON(obj : Any) :JSONValue{
        val converter = JSONConverter()
        return converter.convert(obj)
    }

}

fun main() {
    val jsonCentral = JSONCentral()
    jsonCentral.testValues()
    jsonCentral.serializeJSON(jsonCentral.getRoot())

    //jsonCentral.searchJSON()

    /*val map1: MutableMap<String, String> = mutableMapOf()
    map1.put("bye1", "one")
    map1.put("bye2", "two")
    map1.put("bye3", "three")
    jsonCentral.serializeJSON(jsonCentral.convertJSON(setOf("hello", 12, true, map1)))*/

    /*val array1: ArrayList<Any> = arrayListOf()
    array1.add("bye1")
    array1.add("bye2")
    val conv = JSONConverter()
    val dataClass1 = TestDataClass("Hi", 14, array1)
    jsonCentral.serializeJSON(conv.converterManager(dataClass1))*/
}