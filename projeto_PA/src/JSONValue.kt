abstract class JSONValue() {

    abstract fun accept(visitor: JSONVisitor)

}
