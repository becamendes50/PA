import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class JSONConverter {

    fun convert(obj : Any) : JSONValue{

        val safe = when (obj) {
            is Short, is Int, is Long, is Float, is Double -> JSONNumber(obj as Number)

            is Boolean -> return JSONBoolean(obj)

            is String -> return JSONString(obj)

            is Collection<*> -> {
                var array1 = JSONArray(arrayListOf())
                for (o in obj) {
                    o?.let { convert(it) }?.let { array1.addValue(it) }
                }
                return array1
            }

            is Map<*,*> -> {
                var object1 = JSONObject(mutableMapOf())
                obj.forEach { key, value ->
                    value?.let { convert(it) }?.let { object1.addValue(key as String, it) }
                }
                return object1
            }

            else -> JSONString("")
        }

        return safe
    }

    fun converterManager(data: Any) : JSONObject{
        val jsonObject = JSONObject(mutableMapOf())
        val clazz = data::class
        clazz.declaredMemberProperties.forEach {
            if (!(it.hasAnnotation<PropertyBypass>())) {
                if (it.hasAnnotation<PropertyReIdentifier>()) {
                    it.findAnnotation<PropertyReIdentifier>()?.let { it1 -> it.getter.call(data)?.let {
                        it2 -> convert(it2) }?.let { it3 -> jsonObject.addValue(it1.name, it3) } }
                }
                else {
                    it.getter.call(data)?.let { it1 -> convert(it1) }?.let { it2 -> jsonObject.addValue(it.name, it2) }
                }
            }
        }
        return jsonObject
    }

}