import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.int

class CompareXmlCli : CliktCommand() {
    private val leftXml: String by option(help = "The left xml for comparison").required()
    private val rightXml: String by option(help = "The right xml for comparison").required()
    private val out: String by option(help = "The file path of the comparison result").default("", "if empty will just console output ")
    private val verbose: Boolean by option(help = "Full diff description").flag(default = false)
    private val ignoreList: String by option(help = "List of elements or attributes to ignore. \"el_A,el_B.attr_A,el_C\"").default(
        ""
    )

    override fun run() {
        echo(
            """
        Comparing XML
        - left: $leftXml
        - right: $rightXml
        - ignoreList: $ignoreList
        - verbose: $verbose            
        - out: $out            
        """.trimIndent()
        )

        val result = compareXmlFromCli(leftXml, rightXml, ignoreList, CompareOptions(verbose))
        println(result)
    }
}

fun main(args: Array<String>) = CompareXmlCli().main(args)
