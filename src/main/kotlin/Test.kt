import java.io.File

fun main() {
    val left = "./resources/good.xml"
    val right = "./resources/_good.xml"
    val out = "./out.txt";
    val ignoreListString = "IgnoredElement,ElementWithAttrToIgnore.attrToIgnore"

    val diff = compareXmlFromCli(left, right, ignoreListString, CompareOptions(verbose = false))
    println(diff)
    File(out).writeText(diff)
}