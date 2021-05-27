class JSONArray (private val array: MutableList<JSONValue>) : JSONValue() {

    fun addValue(value: JSONValue) {
        array.add(value)
    }

    fun removeValue(value: JSONValue) {
        array.remove(value)
    }

    fun getArray() :MutableList<JSONValue> {
        return array
    }

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    override fun setValue(value :Any) {
    }

}