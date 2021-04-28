class JSONSearcher(private val query :(JSONValue) -> Boolean) :JSONVisitor {

    private var queryMap = mutableMapOf<String, JSONValue>()

    override fun visit(jsonArray: JSONArray) {
        if (query(jsonArray)) {
            queryMap.put(jsonArray.toString(), jsonArray)
        }
        jsonArray.getArray().forEachIndexed { index, value ->
            value.accept(this)
        }
    }

    override fun visit(jsonObject: JSONObject) {
        if (query(jsonObject)) {
            queryMap.put(jsonObject.toString(), jsonObject)
        }
        jsonObject.getMap().forEach { key, value ->
            value.accept(this)
        }
    }

    override fun visit(jsonNumber: JSONNumber) {
        if (query(jsonNumber)) {
            queryMap.put(jsonNumber.toString(), jsonNumber)
        }
    }

    override fun visit(jsonBoolean: JSONBoolean) {
        if (query(jsonBoolean)) {
            queryMap.put(jsonBoolean.toString(), jsonBoolean)
        }
    }

    override fun visit(jsonString: JSONString) {
        if (query(jsonString)) {
            queryMap.put(jsonString.toString(), jsonString)
        }
    }

    fun returnQuery() :MutableMap<String, JSONValue>{
        print("" + queryMap.values)
        return queryMap
    }

}