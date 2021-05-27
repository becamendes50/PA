class JSONNumber (private var number: Number): JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    override fun setValue(value :Any) {
        number = value as Number
    }

    fun getNumber() :Number {
        return number
    }

}