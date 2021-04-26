class JSONNumber (private val number: Number): JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    fun getNumber() :Number {
        return number
    }

}