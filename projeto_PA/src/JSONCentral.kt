import java.io.File

class JSONCentral {

    private val map1 = HashMap<String, JSONValue>()
    private var object1 = JSONObject(map1)

    fun getRootSize() : Int {
        return object1.getMap().size
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

    fun serializeJSON() {
        val file = File("root2")
        val serializer = JSONSerializer(file)
        object1.accept(serializer)
    }

}

fun main() {
    val jsonCentral = JSONCentral()
    jsonCentral.testValues()
    jsonCentral.serializeJSON()
}