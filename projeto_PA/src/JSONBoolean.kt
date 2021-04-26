class JSONBoolean (private val boolean: Boolean) : JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    fun getBoolean() :Boolean {
        return boolean
    }

}