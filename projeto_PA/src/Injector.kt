import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class InjectPlugIn

@Target(AnnotationTarget.PROPERTY)
annotation class InjectAction

class Injector {
    companion object {
        val map: MutableMap<String, KClass<*>> = mutableMapOf()

        val allActions = mutableListOf<KClass<*>>()

        init {
            val scanner = Scanner(File("di.properties"))
            while(scanner.hasNextLine()){
                val line = scanner.nextLine()
                val parts = line.split("=")
                if(parts[1].contains(";")) {
                    val act = parts[1].split(";")
                    act.forEach{ value ->
                        allActions.add(Class.forName(value).kotlin)
                    }
                }
                else {
                    map[parts[0]] = Class.forName(parts[1]).kotlin
                }
            }
            scanner.close()
        }

        fun <T:Any> create(type: KClass<T>) : T {
            val o: T = type.createInstance()
            type.declaredMemberProperties.forEach {
                if(it.hasAnnotation<InjectPlugIn>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    if (map.isNotEmpty())  {
                        val obj = map[key]!!.createInstance()
                        (it as KMutableProperty<*>).setter.call(o, obj)
                    }
                }
                else if(it.hasAnnotation<InjectAction>()) {
                    it.isAccessible = true
                    allActions.forEach { action ->
                        val obj = action!!.createInstance()
                        (it.getter.call(o) as MutableList<Action>).add(obj as Action)
                    }
                }
            }
            return o
        }
    }
}

fun main() {
    val w = Injector.create(Viewer::class)
    w.open()
}