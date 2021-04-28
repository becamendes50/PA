package JSONTests

import JSONArray
import JSONBoolean
import JSONCentral
import JSONConverter
import JSONNumber
import JSONObject
import JSONSerializer
import JSONString
import JSONValue
import TestDataClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JSONUnit {

    fun testValues1() : JSONObject{

        val map1 = HashMap<String, JSONValue>()
        var object1 = JSONObject(map1)

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

        return object1
    }

    fun testValues2() : JSONObject{

        val map2 = HashMap<String, JSONValue>()
        var object2 = JSONObject(map2)

        val firstValue = JSONString("Goodbye")
        object2.addValue("test1", firstValue)

        val secondValue = JSONNumber(1234)
        object2.addValue("test2", secondValue)

        var array1 :ArrayList<JSONValue> = arrayListOf()
        var thirdValue = JSONArray(array1)
        object2.addValue("test3", thirdValue)

        return object2
    }

    @Test
    fun testSerialize() {
        val jsonCentral = JSONCentral()
        jsonCentral.serializeJSONTest(testValues1())
        val expected = "{ \n\t\"third\": \"Goodbye\", \n\t\"fifth\": 1234, \n\t\"fourth\": false, \n\t\"first\": { \n\t}, \n\t\"second\": [ \n\t\"Hello\", \n\ttrue\n]\n}"
        assertEquals(expected, jsonCentral.serializeJSONTest(testValues1()))
    }

    @Test
    fun testConverter() {
        val jsonCentral = JSONCentral()
        val jsonConverter = JSONConverter()
        val test1 :String = "Goodbye"
        val test2 :Int = 1234
        val test3 = arrayListOf<Any>()
        val dataClass = TestDataClass(test1, test2, test3)
        val dataConverted = jsonConverter.converterManager(dataClass)
        assertEquals(jsonCentral.serializeJSONTest(testValues2()), jsonCentral.serializeJSONTest(dataConverted))


    }


}