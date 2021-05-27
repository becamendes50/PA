class JSONBoolean (private var boolean: Boolean) : JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    fun getBoolean() :Boolean {
        return boolean
    }

    override fun setValue(value :Any) {
        boolean = value as Boolean
    }

}