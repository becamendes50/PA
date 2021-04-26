interface JSONVisitor {

    fun visit(jsonArray: JSONArray)

    fun visit(jsonObject: JSONObject)

    fun visit(jsonNumber: JSONNumber)

    fun visit(jsonBoolean: JSONBoolean)

    fun visit(jsonString: JSONString)

}