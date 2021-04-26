class JSONString (private val string: String) : JSONValue()  {

    override fun accept(visitor: JSONVisitor) {
        visitor.visit(this)
    }

    fun getString() :String{
        return string
    }

}