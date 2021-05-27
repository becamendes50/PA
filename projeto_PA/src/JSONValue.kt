abstract class JSONValue() {

    abstract fun accept(visitor: JSONVisitor)

    abstract fun setValue(value: Any)

}
