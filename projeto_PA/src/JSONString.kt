class JSONString (private var string: String) : JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    fun getString() :String{
        return string
    }

    override fun setValue(value :Any) {
        string = value as String
    }

}