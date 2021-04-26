class JSONObject(private val objectMap: MutableMap<String, JSONValue>) : JSONValue() {

    fun addValue(key: String, value: JSONValue) {
        objectMap.put(key, value)
    }

    fun removeValue(key: String) {
        objectMap.remove(key)
    }

    fun getMap() : MutableMap<String, JSONValue> {
        return objectMap
    }

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

}